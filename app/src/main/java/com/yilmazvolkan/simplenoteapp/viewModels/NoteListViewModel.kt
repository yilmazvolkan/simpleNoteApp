package com.yilmazvolkan.simplenoteapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yilmazvolkan.simplenoteapp.database.NotesDBHelper
import com.yilmazvolkan.simplenoteapp.view.NoteFragmentViewState
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState

class NoteListViewModel(app: Application) : AndroidViewModel(app) {

    private val noteScreenViewStateLiveData =
        MutableLiveData<NoteFragmentViewState>().apply {
            value =
                NoteFragmentViewState(isAddClicked = false, isItemClicked = false, isEdited = false)
        }

    private val myNotesList = arrayListOf<NoteItemViewState>()

    private var notesDBHelper = NotesDBHelper(app)


    fun getEffectSelectedViewStates(): ArrayList<NoteItemViewState> {
        return notesDBHelper.readAllUsers()
    }

    fun addEffectSelectedViewState(noteItemViewState: NoteItemViewState) {
        notesDBHelper.insertNote(noteItemViewState)
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