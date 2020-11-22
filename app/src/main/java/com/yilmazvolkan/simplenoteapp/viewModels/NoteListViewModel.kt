package com.yilmazvolkan.simplenoteapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState

class NoteListViewModel(app: Application) : AndroidViewModel(app) {

    private val myEffectViewStates = arrayListOf<NoteItemViewState>()

    fun getEffectSelectedViewStates(): ArrayList<NoteItemViewState> =
        myEffectViewStates
}