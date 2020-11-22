package com.yilmazvolkan.simplenoteapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState

class NoteListViewModel(app: Application) : AndroidViewModel(app) {

    private val myEffectViewStates = arrayListOf<NoteItemViewState>(
        NoteItemViewState("1", "d", "", "date", false),
        NoteItemViewState("2", "d", "", "date", true),
        NoteItemViewState("3", "d", "", "date", false),
        NoteItemViewState("4", "d", "", "date", false),
        NoteItemViewState("5", "d", "", "date", false),
    )

    fun getEffectSelectedViewStates(): ArrayList<NoteItemViewState> =
        myEffectViewStates
}