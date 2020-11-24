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
    fun deleteNote(id: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.NoteEntry.COLUMN_NOTE_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id)
        // Issue SQL statement.
        db.delete(DBContract.NoteEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun insertNote(note: NoteItemViewState): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.NoteEntry.COLUMN_NOTE_ID, note.getID())
        values.put(DBContract.NoteEntry.COLUMN_TITLE, note.getTitle())
        values.put(DBContract.NoteEntry.COLUMN_DESC, note.getDesc())
        values.put(DBContract.NoteEntry.COLUMN_URL, note.getImageURL())
        values.put(DBContract.NoteEntry.COLUMN_CREATED_DATE, note.getDate())
        values.put(DBContract.NoteEntry.COLUMN_IS_EDITED, note.getIsEdited())
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.NoteEntry.TABLE_NAME, null, values)

        return true
    }


    fun readAllUsers(): ArrayList<NoteItemViewState> {
        val notes = ArrayList<NoteItemViewState>()
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("select * from " + DBContract.NoteEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.isAfterLast.not()) {
                val id =
                    cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_NOTE_ID))
                val title =
                    cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_TITLE))
                val desc = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_DESC))
                val url = cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_URL))
                val date =
                    cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_CREATED_DATE))
                val isEdited =
                    cursor.getString(cursor.getColumnIndex(DBContract.NoteEntry.COLUMN_IS_EDITED))

                notes.add(NoteItemViewState(id, title, desc, url, date, (isEdited == "1")))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return notes
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "SimpleNoteApplication.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.NoteEntry.TABLE_NAME + " (" +
                    DBContract.NoteEntry.COLUMN_NOTE_ID + " TEXT PRIMARY KEY," +
                    DBContract.NoteEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                    DBContract.NoteEntry.COLUMN_DESC + " TEXT NOT NULL," +
                    DBContract.NoteEntry.COLUMN_URL + " TEXT," +
                    DBContract.NoteEntry.COLUMN_CREATED_DATE + " TEXT NOT NULL," +
                    DBContract.NoteEntry.COLUMN_IS_EDITED + " INTEGER DEFAULT 0)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.NoteEntry.TABLE_NAME
    }

}