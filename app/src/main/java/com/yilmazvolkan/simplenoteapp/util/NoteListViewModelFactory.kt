package com.yilmazvolkan.simplenoteapp.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yilmazvolkan.simplenoteapp.viewModels.NoteListViewModel

class NoteListViewModelFactory(val app: Application) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (AndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            return NoteListViewModel(app) as T
        }
        return super.create(modelClass)
    }
}