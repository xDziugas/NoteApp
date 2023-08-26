package com.example.noteapp

import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class AddNotesActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        etTitle = findViewById(R.id.et_addNotesTitle)
        etContent = findViewById(R.id.et_addNotesContent)
    }

    fun btnAdd(view: View){

        val dbManager = DatabaseHelper(this)

        val note = Note(
            id = 1,
            title = etTitle.text.toString(),
            content = etContent.text.toString()
        )

        if(note.title != "" && note.content != "") {
            dbManager.insertNote(note)
        }

        finish()
    }
}