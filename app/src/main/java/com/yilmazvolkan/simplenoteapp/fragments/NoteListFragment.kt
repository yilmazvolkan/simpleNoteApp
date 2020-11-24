package com.yilmazvolkan.simplenoteapp.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yilmazvolkan.simplenoteapp.R
import com.yilmazvolkan.simplenoteapp.adapters.NoteListAdapter
import com.yilmazvolkan.simplenoteapp.databinding.FragmentNoteListBinding
import com.yilmazvolkan.simplenoteapp.util.NoteListViewModelFactory
import com.yilmazvolkan.simplenoteapp.util.inflate
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState
import com.yilmazvolkan.simplenoteapp.viewModels.NoteListViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


class NoteListFragment : Fragment() {

    private val binding: FragmentNoteListBinding by inflate(R.layout.fragment_note_list)

    private lateinit var noteListViewModel: NoteListViewModel

    private val noteListAdapter = NoteListAdapter()

    private var selectedIndex = -1

    private var onBackButtonClicked: (() -> Unit)? = null

    private var isItemClicked = false
    private var isEditClicked = false

    private var compositeDisposable = CompositeDisposable()

    fun setOnBackButtonClicked(onBackButtonClicked: (() -> Unit)?) {
        this.onBackButtonClicked = onBackButtonClicked
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (binding.recyclerViewEffects.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        binding.recyclerViewEffects.setHasFixedSize(true)

        binding.recyclerViewEffects.adapter = noteListAdapter

        binding.textViewTitle.movementMethod = ScrollingMovementMethod()
        binding.textViewDesc.movementMethod = ScrollingMovementMethod()
        binding.textViewUrl.movementMethod = ScrollingMovementMethod()
        binding.editTextDesc.movementMethod = ScrollingMovementMethod()

        initializeViewModel()
        initializeViewListeners()
        observeNoteListViewModel()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                when {
                    isEditClicked -> {
                        isEditClicked = false
                        editBackPressedHelper()
                    }
                    isItemClicked -> {
                        isItemClicked = false
                        addBackPressedHelper()
                    }
                    else -> {
                        onBackButtonClicked?.invoke()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, // LifecycleOwner
            callback
        )
    }

    private fun initializeViewModel() {
        activity?.let {
            noteListViewModel = ViewModelProvider(
                this,
                NoteListViewModelFactory(it.application)
            ).get(NoteListViewModel::class.java)
        }
    }

    private fun initializeViewListeners() {
        binding.imageViewBack.setOnClickListener {
            when {
                isEditClicked -> {
                    isEditClicked = false
                    editBackPressedHelper()
                }
                isItemClicked -> {
                    isItemClicked = false
                    addBackPressedHelper()
                }
                else -> {
                    onBackButtonClicked?.invoke()
                }
            }
        }

        binding.imageViewAddNote.setOnClickListener {
            clearTextViews()
            noteListViewModel.notifyNoteScreenViewStateLiveData(
                isAddClicked = true,
                isItemClicked = false,
                isEdited = false
            )
        }

        binding.buttonAdd.setOnClickListener {
            if (isValid()) {
                val c = Calendar.getInstance()

                val noteItemViewState = NoteItemViewState(
                    id = System.currentTimeMillis().toString(),
                    title = binding.editTextTitle.text.toString(),
                    desc = binding.editTextDesc.text.toString(),
                    imageURL = binding.editTextUrl.text.toString(),
                    date = "${c.get(Calendar.DAY_OF_MONTH)}/${c.get(Calendar.MONTH)}/${
                        c.get(
                            Calendar.YEAR
                        )
                    }",
                    isEdited = false
                )

                noteListViewModel.addEffectSelectedViewState(noteItemViewState)
                noteListAdapter.addEffectsDetail(noteItemViewState)

                clearFocus()
                noteListViewModel.notifyNoteScreenViewStateLiveData(
                    isAddClicked = false,
                    isItemClicked = false,
                    isEdited = false
                )
            }
        }
        binding.buttonEdit.setOnClickListener {
            if (selectedIndex >= 0) {
                isEditClicked = true
                fillTextViews(selectedIndex)
                noteListViewModel.notifyNoteScreenViewStateLiveData(
                    isAddClicked = false,
                    isItemClicked = false,
                    isEdited = true
                )
            }
        }

        binding.buttonSave.setOnClickListener {
            if (isValid()) {
                isEditClicked = false

                compositeDisposable.add(
                    noteListViewModel.getNoteItemSelectedViewStates()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val note = it[selectedIndex]

                            note.setTitle(binding.editTextTitle.text.toString())
                            note.setDesc(binding.editTextDesc.text.toString())
                            note.setImageURL(binding.editTextUrl.text.toString())
                            note.setIsEdited(true)

                            noteListViewModel.notifyItemUpdated(note)
                            noteListAdapter.notifyItemUpdated(selectedIndex, note)
                        }, { /*error*/ })
                )

                clearFocus()
                noteListViewModel.notifyNoteScreenViewStateLiveData(
                    isAddClicked = false,
                    isItemClicked = false,
                    isEdited = false
                )
            }
        }

        binding.buttonDelete.setOnClickListener {
            if (selectedIndex >= 0) {
                isItemClicked = false

                compositeDisposable.add(
                    noteListViewModel.getNoteItemSelectedViewStates()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val id = it[selectedIndex].getID()
                            noteListViewModel.removeEffectSelectedViewState(id)
                        }, { /*error*/ })
                )

                noteListAdapter.deleteEffectsDetail(selectedIndex)
                noteListViewModel.notifyNoteScreenViewStateLiveData(
                    isAddClicked = false,
                    isItemClicked = false,
                    isEdited = false
                )
            }
        }

        noteListAdapter.setItemClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClicked(selectedPosition: Int) {
                if (selectedPosition == -1) {
                    return
                }
                isItemClicked = true
                selectedIndex = selectedPosition
                fillTextViews(selectedPosition)
                noteListViewModel.notifyNoteScreenViewStateLiveData(
                    isAddClicked = false,
                    isItemClicked = true,
                    isEdited = false
                )
            }
        })
    }

    private fun observeNoteListViewModel() = with(noteListViewModel) {
        getNoteScreenViewStateLiveData().observe(viewLifecycleOwner) {
            binding.noteFragmentViewState = it
            binding.executePendingBindings()
        }
        compositeDisposable.add(
            noteListViewModel.getNoteItemSelectedViewStates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    noteListAdapter.setEffectsDetailList(it)
                }, { /*error*/ })
        )
    }

    private fun isValid(): Boolean {
        var isValid = true
        if (binding.editTextTitle.text.toString().isEmpty()) {
            binding.inputLayoutTitle.isErrorEnabled = true
            binding.inputLayoutTitle.error = "Tittle cannot be empty!"
            isValid = false
        } else {
            binding.inputLayoutTitle.isErrorEnabled = false
        }
        if (binding.editTextDesc.text.toString().isEmpty()) {
            binding.inputLayoutDesc.isErrorEnabled = true
            binding.inputLayoutDesc.error = "Description cannot be empty!"
            isValid = false
        } else {
            binding.inputLayoutDesc.isErrorEnabled = false
        }
        return isValid
    }

    private fun fillTextViews(index: Int) {
        compositeDisposable.add(
            noteListViewModel.getNoteItemSelectedViewStates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val note = it[index]

                    binding.editTextTitle.setText(note.getTitle())
                    binding.editTextDesc.setText(note.getDesc())
                    binding.editTextUrl.setText(note.getImageURL())

                    binding.textViewTitle.text = note.getTitle()
                    binding.textViewDesc.text = note.getDesc()
                    binding.textViewUrl.text = note.getImageURL()
                }, { /*error*/ })
        )
    }

    private fun clearTextViews() {
        binding.editTextTitle.setText("")
        binding.editTextDesc.setText("")
        binding.editTextUrl.setText("")
    }

    private fun clearFocus() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun editBackPressedHelper() {
        binding.inputLayoutTitle.isErrorEnabled = false
        binding.inputLayoutDesc.isErrorEnabled = false

        clearFocus()
        clearTextViews()
        noteListViewModel.notifyNoteScreenViewStateLiveData(
            isAddClicked = false,
            isItemClicked = true,
            isEdited = false
        )
    }

    private fun addBackPressedHelper() {
        binding.inputLayoutTitle.isErrorEnabled = false
        binding.inputLayoutDesc.isErrorEnabled = false

        clearFocus()
        clearTextViews()
        noteListViewModel.notifyNoteScreenViewStateLiveData(
            isAddClicked = false,
            isItemClicked = false,
            isEdited = false
        )
    }

    companion object {
        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }
}