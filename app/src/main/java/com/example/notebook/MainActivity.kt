package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

     private lateinit var fab: FloatingActionButton
     private lateinit var menuImage: ImageView

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

    }
}