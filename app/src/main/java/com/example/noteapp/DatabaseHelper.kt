package com.example.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DatabaseHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "MyNotes"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Notes"

        // Define column names
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "Title"
        private const val COLUMN_CONTENT = "Content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreateTable = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(sqlCreateTable)
        Toast.makeText(context, "Database Created", Toast.LENGTH_LONG).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun insertNote(note: Note) {
        writableDatabase.use { db->
            val values = ContentValues().apply {
                put(COLUMN_TITLE, note.title)
                put(COLUMN_CONTENT, note.content)
            }

            db.insert(TABLE_NAME, null, values)
            Toast.makeText(context, "Note Saved", Toast.LENGTH_LONG).show()
        }
    }

    fun query(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = TABLE_NAME
        return qb.query(
            writableDatabase,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    fun delete(selection: String, selectionArgs: Array<String>){
        writableDatabase.use { db ->
            db.delete(TABLE_NAME, selection, selectionArgs)
        }
    }

    fun update(note: Note, selection: String, selectionArgs: Array<String>){
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }

        writableDatabase.use { db ->
            db.update(TABLE_NAME, values, selection, selectionArgs)
        }
    }
}