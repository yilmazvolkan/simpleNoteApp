package com.yilmazvolkan.simplenoteapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yilmazvolkan.simplenoteapp.view.NoteFragmentViewState
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState

class NoteListViewModel(app: Application) : AndroidViewModel(app) {

    private val noteScreenViewStateLiveData =
        MutableLiveData<NoteFragmentViewState>().apply { value = NoteFragmentViewState(isAddClicked = false) }

    private val myNotesList = arrayListOf<NoteItemViewState>(
        NoteItemViewState("1", "d", "", "date", false),
        NoteItemViewState("2", "d", "", "date", true),
        NoteItemViewState("3", "d", "", "date", false),
        NoteItemViewState("4", "d", "", "date", false),
        NoteItemViewState("5", "d", "", "date", false),
    )

    fun getEffectSelectedViewStates(): ArrayList<NoteItemViewState> =
        myNotesList

    fun addEffectSelectedViewState(noteItemViewState: NoteItemViewState){
        myNotesList.add(noteItemViewState)
    }

    fun getNoteScreenViewStateLiveData(): LiveData<NoteFragmentViewState> =
        noteScreenViewStateLiveData

    fun notifyNoteScreenViewStateLiveData(isAddClicked: Boolean) {
        noteScreenViewStateLiveData.value = NoteFragmentViewState(isAddClicked = isAddClicked)
    }

}