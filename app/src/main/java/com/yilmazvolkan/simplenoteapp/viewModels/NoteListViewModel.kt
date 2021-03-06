package com.yilmazvolkan.simplenoteapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yilmazvolkan.simplenoteapp.database.NotesDBHelper
import com.yilmazvolkan.simplenoteapp.view.NoteFragmentViewState
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NoteListViewModel(app: Application) : AndroidViewModel(app) {

    private val noteScreenViewStateLiveData =
        MutableLiveData<NoteFragmentViewState>().apply {
            value =
                NoteFragmentViewState(isAddClicked = false, isItemClicked = false, isEdited = false)
        }



    private var notesDBHelper = NotesDBHelper(app)

    fun getNoteItemSelectedViewStates() = notesDBHelper.readAllUsers()

    fun addEffectSelectedViewState(noteItemViewState: NoteItemViewState) {
        notesDBHelper.insertNote(noteItemViewState)
    }

    fun notifyItemUpdated(viewState: NoteItemViewState) {
        notesDBHelper.updateNote(viewState)
    }

    fun removeEffectSelectedViewState(id: String) {
        notesDBHelper.deleteNote(id)
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