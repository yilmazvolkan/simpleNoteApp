package com.yilmazvolkan.simplenoteapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import java.util.*


class NoteListFragment : Fragment() {

    private val binding: FragmentNoteListBinding by inflate(R.layout.fragment_note_list)

    private lateinit var noteListViewModel: NoteListViewModel

    private val noteListAdapter = NoteListAdapter()

    private var selectedIndex = -1

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

        initializeViewModel()
        initializeViewListeners()
        observeNoteListViewModel()
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
            noteListViewModel.notifyNoteScreenViewStateLiveData(
                isAddClicked = false,
                isItemClicked = false,
                isEdited = false
            )
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

                val note = noteListViewModel.getEffectSelectedViewStates()[selectedIndex]

                note.setTitle(binding.editTextTitle.text.toString())
                note.setDesc(binding.editTextDesc.text.toString())
                note.setImageURL(binding.editTextUrl.text.toString())
                note.setIsEdited(true)

                noteListViewModel.notifyItemUpdated(note)
                noteListAdapter.notifyItemUpdated(selectedIndex, note)

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
                val id = noteListViewModel.getEffectSelectedViewStates()[selectedIndex].getID()
                noteListViewModel.getEffectSelectedViewStates().removeAt(selectedIndex)
                noteListViewModel.removeEffectSelectedViewState(id)

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
                selectedIndex = selectedPosition
                fillTextViews(selectedPosition)
                noteListViewModel.notifyNoteScreenViewStateLiveData(
                    isAddClicked = false,
                    isItemClicked = true,
                    isEdited = false
                )

                //noteListViewModel.getEffectSelectedViewStates()
                // noteListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun observeNoteListViewModel() = with(noteListViewModel) {
        getNoteScreenViewStateLiveData().observe(viewLifecycleOwner) {
            binding.noteFragmentViewState = it
            binding.executePendingBindings()
        }
        noteListAdapter.setEffectsDetailList(getEffectSelectedViewStates())
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
        val note = noteListViewModel.getEffectSelectedViewStates()[index]
        binding.editTextTitle.setText(note.getTitle())
        binding.editTextDesc.setText(note.getDesc())
        binding.editTextUrl.setText(note.getImageURL())

        binding.textViewTitle.text = note.getTitle()
        binding.textViewDesc.text = note.getDesc()
        binding.textViewUrl.text = note.getImageURL()
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

    companion object {
        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }
}