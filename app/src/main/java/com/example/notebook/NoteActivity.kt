package com.example.notebook

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.notebook.constants.NoteConstants
import com.example.notebook.model.Note
import java.text.SimpleDateFormat
import java.util.*


class NoteActivity : AppCompatActivity() {

    private var currentNote: Note? = null

    // After API 23 the permission request for accessing external storage is changed
    // Before API 23 permission request is asked by the user during installation of app
    // After API 23 permission request is asked at runtime
    private val EXTERNAL_STORAGE_PERMISSION_CODE = 23


    private lateinit var noteContentEdit: EditText
    private lateinit var noteTitleEdit: EditText
    private lateinit var noteDateView: TextView
    private lateinit var backButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)


        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            //savePrivately()
            finish()
        }

        noteTitleEdit = findViewById(R.id.editTitle)
        noteContentEdit = findViewById(R.id.editNote)
        noteDateView = findViewById(R.id.viewDate)


        val usedIntent = this.intent
        currentNote = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            usedIntent.getSerializableExtra(NoteConstants.CURRENT_NOTE, Note::class.java)
        } else {
            usedIntent.getSerializableExtra(NoteConstants.CURRENT_NOTE) as Note
        }
        noteTitleEdit.setText(currentNote!!.title)
        noteContentEdit.setText(currentNote!!.content)
        noteDateView.setText(currentNote!!.date)


        /*
        val noteTitleString = usedIntent.getStringExtra(NoteConstants.NOTE_TITLE);
        if(noteTitleString != null) {
            noteTitleEdit.setText(noteTitleString)
        }

        val noteContentString = usedIntent.getStringExtra(NoteConstants.NOTE_CONTENT);
        if(noteContentString != null) {
            noteContentEdit.setText(noteContentString)
        }*/
    }

    /*
    fun savePublicly(view: View?) {
        // Requesting Permission to access External Storage
        ActivityCompat.requestPermissions(
            this, arrayOf(READ_EXTERNAL_STORAGE),
            EXTERNAL_STORAGE_PERMISSION_CODE
        )
        val editTitleData: String = noteTitleEdit.text.toString()
        val editContentData: String = noteContentEdit.text.toString()

        // getExternalStoragePublicDirectory() represents root of external storage, we are using DOWNLOADS
        // We can use following directories: MUSIC, PODCASTS, ALARMS, RINGTONES, NOTIFICATIONS, PICTURES, MOVIES
        val folder: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Storing the data in file with name as notesData.txt

        val file = File(folder, "notesData.txt")
        writeTextData(file, editTextData)
        editText.setText("")
    }

    fun savePrivately(view: View?) {
        val editTitleData: String = noteTitleEdit.text.toString()
        val editContentData: String = noteContentEdit.text.toString()

        // Creating folder with name NotesApp
        val folder: File? = getExternalFilesDir("NotesApp")

        // Creating file with name notes.txt
        val file = File(folder, "notes.txt")
        writeTextData(file, editTextData)
        editText.setText("")
    }

    // writeTextData() method save the data into the file in byte format
    // It also toast a message "Done/filepath_where_the_file_is_saved"
    private fun writeTextData(file: File, data: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray())
            Toast.makeText(this, "Done" + file.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace() //TODO exception???
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    */
    override fun finish() {
        currentNote!!.title = noteTitleEdit.text.toString()
        currentNote!!.content = noteContentEdit.text.toString()
        currentNote!!.date = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(Date())
        //currentNote.noteId =

        val returnIntent = Intent()
        returnIntent.putExtra(NoteConstants.CURRENT_NOTE, currentNote)
        // setResult(RESULT_OK);
        setResult(RESULT_OK, returnIntent)
        //By not passing the intent in the result, the calling activity will get null data.
        super.finish()
    }


}