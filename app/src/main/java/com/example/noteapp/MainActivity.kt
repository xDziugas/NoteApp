package com.example.noteapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var lvNotes: ListView
    var listNotes: ArrayList<Note> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lvNotes = findViewById(R.id.lv_noteList)

        //Load from database
        loadQuery("%")


    }

    private fun loadQuery(title: String) {
        val dbManager = DatabaseHelper(this)
        val projection = arrayOf("id", "Title", "Content")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projection, "Title like ?", selectionArgs, "Title")

        listNotes.clear()

        if (cursor.moveToNext()) {
            do {
                val idIndex = cursor.getColumnIndex("id")
                val titleIndex = cursor.getColumnIndex("Title")
                val contentIndex = cursor.getColumnIndex("Content")

                if (idIndex >= 0 && titleIndex >= 0 && contentIndex >= 0) {
                    val id = cursor.getInt(idIndex)
                    val colTitle = cursor.getString(titleIndex)
                    val content = cursor.getString(contentIndex)

                    val note = Note(id, colTitle, content)
                    listNotes.add(note)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()

        val myNotesAdapter = NotesAdapter(listNotes)
        lvNotes.adapter = myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv = menu.findItem(R.id.menu_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {

            }

            R.id.menu_addNote -> {
                val intent = Intent(this, AddNotesActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class NotesAdapter(var listNotesAdapter: ArrayList<Note>) : BaseAdapter() {

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return listNotesAdapter[position].id!!.toLong()  //position.toLong()
        }

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val viewHolder: ViewHolder
            var myView = convertView

            if (myView == null) {
                myView = layoutInflater.inflate(R.layout.note_ticket, null)
                viewHolder = ViewHolder(myView)
                myView.tag = viewHolder
            } else {
                viewHolder = myView.tag as ViewHolder
            }

            val note = listNotesAdapter[position]
            viewHolder.titleTextView.text = note.title
            viewHolder.contentTextView.text = note.content

            return myView
        }

        private inner class ViewHolder(view: View) {
            val titleTextView: TextView = view.findViewById(R.id.tv_title)
            val contentTextView: TextView = view.findViewById(R.id.tv_content)
        }

    }

}