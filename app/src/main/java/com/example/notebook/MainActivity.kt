package com.example.notebook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.constants.NoteConstants
import com.example.notebook.model.Note
import com.example.notebook.viewmodel.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var menuImage: ImageView
    private lateinit var rvNotes: RecyclerView

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
        //notes = Note.createNotesList(1)
        notes = ArrayList<Note>()
        // Create adapter passing in the sample user data
        noteAdapter = NoteAdapter(notes)
        // Attach the adapter to the recyclerview to populate items
        rvNotes.adapter = noteAdapter
        // Set layout manager to position the items
        rvNotes.layoutManager = LinearLayoutManager(this)
        // That's all!


    }

    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NoteConstants.REQUEST_CODE_ADD_TO_LIST && resultCode == RESULT_OK) {
            val passedItem: Note? = data?.extras!![NoteConstants.CURRENT_NOTE] as Note?
            val sameId = noteAdapter.notesList.stream().filter{ it.noteId.equals(passedItem?.noteId)}.count()
            if (sameId == 0L) { // new note added
                noteAdapter.addToNotes(passedItem);
                noteAdapter.notifyItemRangeChanged(noteAdapter.notesList.size,noteAdapter.notesList.size)
            } else {
                noteAdapter.setNoteById(passedItem);
                noteAdapter.notifyDataSetChanged();
            }

            // deal with the item yourself TODO save
        }
    }

}