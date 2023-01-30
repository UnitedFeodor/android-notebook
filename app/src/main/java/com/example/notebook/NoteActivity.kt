package com.example.notebook

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.notebook.constants.NoteConstants


class NoteActivity : AppCompatActivity() {


    private lateinit var noteContentEdit: EditText
    private lateinit var noteTitleEdit: EditText
    private lateinit var backButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)


        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        noteTitleEdit = findViewById(R.id.editTitle)
        noteContentEdit = findViewById(R.id.editNote)

        val usedIntent = this.intent
        val noteTitleString = usedIntent.getStringExtra(NoteConstants.NOTE_TITLE);
        if(noteTitleString != null) {
            noteTitleEdit.setText(noteTitleString)
        }

        val noteContentString = usedIntent.getStringExtra(NoteConstants.NOTE_CONTENT);
        if(noteContentString != null) {
            noteContentEdit.setText(noteContentString)
        }
    }
}