package com.example.notebook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.SearchView.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.constants.NoteConstants
import com.example.notebook.model.Note
import com.example.notebook.viewmodel.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.Locale.filter
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var menuImage: ImageView
    private lateinit var rvNotes: RecyclerView
    private lateinit var searchView: SearchView

    private lateinit var noteAdapter: NoteAdapter

    lateinit var notes: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuImage = findViewById(R.id.menuImage)
        menuImage.setOnClickListener {
            val popupMenu = PopupMenu(this,menuImage)
            popupMenu.menuInflater.inflate(R.menu.menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_memory ->
                        Toast.makeText(
                            this@MainActivity,
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                    R.id.menu_sqlite ->
                        Toast.makeText(
                            this@MainActivity,
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()

                }
                true
            }
            popupMenu.show()

        }

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra(NoteConstants.CURRENT_NOTE,Note());
            startActivityForResult(intent, NoteConstants.REQUEST_CODE_ADD_TO_LIST)
            //startActivity(intent)
        }




        // Lookup the recyclerview in activity layout
        rvNotes = findViewById<View>(R.id.recyclerView) as RecyclerView
        // Initialize notes
        notes = Note.createNotesList(3)
        //notes = ArrayList<Note>() //TODO load from memory and db
        // Create adapter passing in the sample user data
        noteAdapter = NoteAdapter(notes)
        // Attach the adapter to the recyclerview to populate items
        rvNotes.adapter = noteAdapter
        // Set layout manager to position the items
        rvNotes.layoutManager = LinearLayoutManager(this)
        // That's all!


        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(msg: String): Boolean {
                (rvNotes.adapter as NoteAdapter).filterList(msg)
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                (rvNotes.adapter as NoteAdapter).filterList(msg)
                return false
            }


        })

    }
/*
    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Note> = ArrayList()

        // running a for loop to compare elements.
        for (item in notes) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.title.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault())) ||
                item.content.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            noteAdapter.filterList(filteredlist)
        }
    }*/

    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NoteConstants.REQUEST_CODE_ADD_TO_LIST && resultCode == RESULT_OK) {
            val passedItem: Note? = data?.extras!![NoteConstants.CURRENT_NOTE] as Note?
            val noteAdapter = (rvNotes.adapter as NoteAdapter)
            val sameId = noteAdapter.notesList.stream().filter{ it.noteId.equals(passedItem?.noteId)}.count()
            if (sameId == 0L) { // new note added
                noteAdapter.addToNotes(passedItem);
                noteAdapter.notifyItemRangeChanged(noteAdapter.notesList.size,noteAdapter.notesList.size)
            } else {
                noteAdapter.setNoteById(passedItem);
                noteAdapter.notifyDataSetChanged();
            }
            (rvNotes.adapter as NoteAdapter).filterList(searchView.query.toString())
            // deal with the item yourself TODO save
        }
    }

}