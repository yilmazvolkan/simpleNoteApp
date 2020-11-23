package com.yilmazvolkan.simplenoteapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yilmazvolkan.simplenoteapp.view.NoteFragmentViewState
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState

class NoteListViewModel(app: Application) : AndroidViewModel(app) {

    private val noteScreenViewStateLiveData =
        MutableLiveData<NoteFragmentViewState>().apply {
            value =
                NoteFragmentViewState(isAddClicked = false, isItemClicked = false, isEdited = false)
        }

    private val myNotesList = arrayListOf<NoteItemViewState>(
        NoteItemViewState("id1", "1", "d", "", "date", false),
        NoteItemViewState("id2", "2", "d", "", "date", true),
        NoteItemViewState("id3", "3", "d", "", "date", false),
        NoteItemViewState("id4", "4", "d", "", "date", false),
        NoteItemViewState("id5", "5", "d", "", "date", false),
    )

    fun getEffectSelectedViewStates(): ArrayList<NoteItemViewState> =
        myNotesList

    fun addEffectSelectedViewState(noteItemViewState: NoteItemViewState) {
        myNotesList.add(noteItemViewState)
    }

    fun notifyItemUpdated(index: Int, viewState: NoteItemViewState){
        myNotesList[index].setTitle(viewState.getTitle())
        myNotesList[index].setDesc(viewState.getDesc())
        myNotesList[index].setImageURL(viewState.getImageURL())
        myNotesList[index].setIsEdited(viewState.getIsEdited())
    }

    fun removeEffectSelectedViewState(index: Int) {
        myNotesList.removeAt(index)
    }

    fun getNoteScreenViewStateLiveData(): LiveData<NoteFragmentViewState> =
        noteScreenViewStateLiveData

    fun notifyNoteScreenViewStateLiveData(
        isAddClicked: Boolean,
        isItemClicked: Boolean,
        isEdited: Boolean
    ) {
        noteScreenViewStateLiveData.value = NoteFragmentViewState(
            isAddClicked = isAddClicked,
            isItemClicked = isItemClicked,
            isEdited = isEdited
        )
    }

}