package com.yilmazvolkan.simplenoteapp.database

import android.provider.BaseColumns

object DBContract {

    /* Inner class that defines the table contents */
    class NoteEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "notes"
            const val COLUMN_NOTE_ID = "noteid"
            const val COLUMN_TITLE = "title"
            const val COLUMN_DESC = "desc"
            const val COLUMN_URL = "url"
            const val COLUMN_CREATED_DATE = "date"
            const val COLUMN_IS_EDITED = "isedited"
        }
    }
}