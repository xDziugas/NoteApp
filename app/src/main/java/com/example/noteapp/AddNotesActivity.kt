package com.example.noteapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddNotesActivity : AppCompatActivity() {

    private val etTitle: EditText by lazy { findViewById(R.id.et_addNotesTitle) }
    private val etContent: EditText by lazy { findViewById(R.id.et_addNotesContent) }
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        val bundle = intent.extras

        bundle?.apply {
            id = getInt("id", 0)
            if (id != 0) {
                etTitle.setText(getString("Title"))
                etContent.setText(getString("Content"))
            }
        }
    }

    fun btnAdd(view: View){
        saveNote()
    }

    private fun saveNote() {
        val dbManager = DatabaseHelper(this)

        val note = Note(
            id = 1,
            title = etTitle.text.toString(),
            content = etContent.text.toString()
        )

        if (id == 0) {
            if (note.title!!.isNotBlank() && note.content!!.isNotBlank()) {
                dbManager.insertNote(note)
            }
        } else {
            dbManager.update(
                note = note,
                selection = "id=?",
                selectionArgs = arrayOf(id.toString())
            )
        }

        finish()
    }
}