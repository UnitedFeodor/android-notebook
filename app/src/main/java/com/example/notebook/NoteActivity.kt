package com.example.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class NoteActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }


    }
}