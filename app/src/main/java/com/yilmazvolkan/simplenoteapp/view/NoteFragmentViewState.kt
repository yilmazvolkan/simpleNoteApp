package com.yilmazvolkan.simplenoteapp.view

import android.view.View

data class NoteFragmentViewState(
    val isAddClicked: Boolean,
    val isItemClicked: Boolean,
    val isEdited: Boolean
) {

    fun getListStateVisibility() =
        if (isAddClicked) View.GONE else if (isItemClicked) View.GONE else if (isEdited) View.GONE else View.VISIBLE

    fun getListNotStateVisibility() =
        if (isAddClicked) View.VISIBLE else if (isItemClicked) View.VISIBLE else if (isEdited) View.VISIBLE else View.GONE

    // When item is clicked
    fun getShowStateVisibility() =
        if (isAddClicked) View.GONE else if (isItemClicked) View.VISIBLE else if (isEdited) View.GONE else View.GONE

    fun getAddStateVisibility() =
        if (isAddClicked) View.VISIBLE else if (isItemClicked) View.GONE else if (isEdited) View.VISIBLE else View.GONE

    fun getEditStateSaveBtnVisibility() =
        if (isAddClicked) View.GONE else if (isItemClicked) View.GONE else if (isEdited) View.VISIBLE else View.GONE

    fun getEditStateAddBtnVisibility() =
        if (isAddClicked) View.VISIBLE else if (isItemClicked) View.GONE else if (isEdited) View.GONE else View.GONE
}