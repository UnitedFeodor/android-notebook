package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.model.Note
import com.example.notebook.viewmodel.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

     private lateinit var fab: FloatingActionButton
     private lateinit var menuImage: ImageView

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
            startActivity(intent)
        }

        // Lookup the recyclerview in activity layout
        val rvContacts = findViewById<View>(R.id.recyclerView) as RecyclerView
        // Initialize notes
        notes = Note.createNotesList(20)
        // Create adapter passing in the sample user data
        val adapter = NoteAdapter(notes)
        // Attach the adapter to the recyclerview to populate items
        rvContacts.adapter = adapter
        // Set layout manager to position the items
        rvContacts.layoutManager = LinearLayoutManager(this)
        // That's all!

    }

}