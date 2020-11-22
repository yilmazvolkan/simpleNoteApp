package com.yilmazvolkan.simplenoteapp.view

import android.view.View

data class NoteItemViewState(
    private var title: String = "",
    private var desc: String = "",
    private var imageURL: String = "", //TODO
    private var date: String = "",
    private var isEdited: Boolean
) {
    fun getIsEditedTextViewVisibility() = when (isEdited) {
        true -> View.VISIBLE
        false -> View.GONE
    }

    fun getTitle() = title

    fun setTitle(title: String) {
        this.title = title
    }

    fun getDesc() = desc

    fun setDesc(desc: String) {
        this.desc = desc
    }

    fun getDate() = date

    fun setDate(date: String) {
        this.date = date
    }
}