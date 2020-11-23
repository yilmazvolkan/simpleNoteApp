package com.yilmazvolkan.simplenoteapp.database

import android.provider.BaseColumns

object DBContract {

    /* Inner class that defines the table contents */
    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "notes"
            val COLUMN_NOTE_ID = "noteid"
            val COLUMN_TITLE = "title"
            val COLUMN_DESC = "desc"
            val COLUMN_URL = "url"
            val COLUMN_CREATED_DATE = "date"
            val COLUMN_IS_EDITED = "isedited"
        }
    }
}