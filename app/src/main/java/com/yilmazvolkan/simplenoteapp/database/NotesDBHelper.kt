package com.yilmazvolkan.simplenoteapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState

class NotesDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertNote(note: NoteItemViewState): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_NOTE_ID, note.getID())
        values.put(DBContract.UserEntry.COLUMN_TITLE, note.getTitle())
        values.put(DBContract.UserEntry.COLUMN_DESC, note.getDesc())
        values.put(DBContract.UserEntry.COLUMN_URL, note.getImageURL())
        values.put(DBContract.UserEntry.COLUMN_CREATED_DATE, note.getDate())
        values.put(DBContract.UserEntry.COLUMN_IS_EDITED, note.getIsEdited())
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.UserEntry.TABLE_NAME, null, values)

        return true
    }


    fun readAllUsers(): ArrayList<NoteItemViewState> {
        val notes = ArrayList<NoteItemViewState>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id =
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NOTE_ID))
                val title =
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_TITLE))
                val desc = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_DESC))
                val url = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_URL))
                val date =
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_CREATED_DATE))
                val isEdited =
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_IS_EDITED))

                notes.add(NoteItemViewState(id, title, desc, url, date, (isEdited == "1")))
                cursor.moveToNext()
            }
        }
        return notes
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "SimpleNoteApplication.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                    DBContract.UserEntry.COLUMN_NOTE_ID + " TEXT PRIMARY KEY," +
                    DBContract.UserEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                    DBContract.UserEntry.COLUMN_DESC + " TEXT NOT NULL," +
                    DBContract.UserEntry.COLUMN_URL + " TEXT," +
                    DBContract.UserEntry.COLUMN_CREATED_DATE + " TEXT NOT NULL," +
                    DBContract.UserEntry.COLUMN_IS_EDITED + " INTEGER DEFAULT 0)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME
    }

}