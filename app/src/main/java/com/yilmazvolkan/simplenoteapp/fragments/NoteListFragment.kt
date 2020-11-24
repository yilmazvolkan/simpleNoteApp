package com.yilmazvolkan.simplenoteapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yilmazvolkan.simplenoteapp.R
import com.yilmazvolkan.simplenoteapp.adapters.NoteListAdapter
import com.yilmazvolkan.simplenoteapp.databinding.FragmentNoteListBinding
import com.yilmazvolkan.simplenoteapp.util.NoteListViewModelFactory
import com.yilmazvolkan.simplenoteapp.util.inflate
import com.yilmazvolkan.simplenoteapp.viewModels.NoteListViewModel

class NoteListFragment : Fragment() {

    private val binding: FragmentNoteListBinding by inflate(R.layout.fragment_note_list)

    private lateinit var noteListViewModel: NoteListViewModel

    private val noteListAdapter = NoteListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.fabAddNote.setOnClickListener {

        }

        binding.buttonAdd.setOnClickListener {
            if (isValid()){

            }
        }
    }

    private fun observeNoteListViewModel() = with(noteListViewModel) {
        noteListAdapter.setEffectsDetailList(getEffectSelectedViewStates())
    }

    private fun isValid(): Boolean{
        var isValid = true
        if (binding.editTextTitle.text.toString().isEmpty()){
            binding.inputLayoutTitle.isErrorEnabled = true
            binding.inputLayoutTitle.error = "Tittle cannot be empty!"
            isValid = false
        }
        else{
            binding.inputLayoutTitle.isErrorEnabled = false
        }
        if (binding.editTextDesc.text.toString().isEmpty()){
            binding.inputLayoutDesc.isErrorEnabled = true
            binding.inputLayoutDesc.error = "Description cannot be empty!"
            isValid = false
        }
        else{
            binding.inputLayoutDesc.isErrorEnabled = false
        }
        return isValid
    }

    companion object {
        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }
}