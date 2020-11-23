package com.yilmazvolkan.simplenoteapp.view

import android.view.View

data class NoteFragmentViewState(
    val isAddClicked: Boolean,
    val isItemClicked: Boolean
) {

    fun getListStateVisibility() =
        if (isAddClicked) View.GONE else if (isItemClicked) View.GONE else View.VISIBLE

    // When item is clicked
    fun getShowStateVisibility() =
        if (isAddClicked) View.GONE else if (isItemClicked) View.VISIBLE else View.GONE

    fun getAddStateVisibility() =
        if (isAddClicked) View.VISIBLE else if (isItemClicked) View.GONE else View.GONE

    fun getEditStateVisibility() =
        if (isAddClicked) View.VISIBLE else if (isItemClicked) View.VISIBLE else View.GONE
}