package com.yilmazvolkan.simplenoteapp.view

import android.view.View

data class NoteFragmentViewState(
    val isAddClicked: Boolean
) {

    fun getListViewsVisibility() = if (isAddClicked) View.GONE else View.VISIBLE
}