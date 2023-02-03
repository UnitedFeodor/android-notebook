package com.example.notebook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.SearchView.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.constants.NoteConstants
import com.example.notebook.dao.NoteDAO
import com.example.notebook.database.DatabaseClient
import com.example.notebook.database.NoteDatabase
import com.example.notebook.model.Note
import com.example.notebook.viewmodel.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var menuImage: ImageView
    private lateinit var rvNotes: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var saveType: TextView

    private lateinit var noteAdapter: NoteAdapter

    lateinit var notes: ArrayList<Note>


    public var isSqlSave = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveType = findViewById(R.id.saveMethod)

        menuImage = findViewById(R.id.menuImage) //TODO save choice
        menuImage.setOnClickListener {
            val popupMenu = PopupMenu(this,menuImage)
            popupMenu.menuInflater.inflate(R.menu.menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                var isChanged = false;
                when (item.itemId) {
                    R.id.menu_memory -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Save method : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        saveType.setText(item.title)
                        if(isSqlSave == true) {
                            isChanged = true
                        }
                        isSqlSave = false;
                    }
                    R.id.menu_sqlite -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Save method : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        saveType.setText(item.title)
                        if(isSqlSave == false) {
                            isChanged = true
                        }
                        isSqlSave = true;
                    }
                }
                if(isChanged) {
                    savePrivately(
                        applicationContext,
                        !isSqlSave,
                        noteAdapter.getmNotesData() as ArrayList<Note>
                    )
                    val notesLoaded = loadPrivately(applicationContext, isSqlSave)
                    (rvNotes.adapter as NoteAdapter).setNotes(notesLoaded)
                    searchView.setQuery("",true)
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
        notes = loadPrivately(applicationContext,isSqlSave)
        //notes = Note.createNotesList(3)
        //notes = ArrayList<Note>() //TODO load from memory and db
        // Create adapter passing in the sample user data
        noteAdapter = NoteAdapter(notes,this)
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

    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NoteConstants.REQUEST_CODE_ADD_TO_LIST && resultCode == RESULT_OK) {
            val passedItem: Note? = data?.extras!![NoteConstants.CURRENT_NOTE] as Note?
            val noteAdapter = (rvNotes.adapter as NoteAdapter)
            val sameId = noteAdapter.notesViewList.stream().filter{ it.noteId.equals(passedItem?.noteId)}.count()
            if (sameId == 0L) { // new note added
                noteAdapter.addToNotes(passedItem);
                noteAdapter.notifyItemRangeChanged(noteAdapter.notesViewList.size,noteAdapter.notesViewList.size)
            } else {
                noteAdapter.setNoteById(passedItem);
                noteAdapter.notifyDataSetChanged();
            }
            noteAdapter.filterList(searchView.query.toString())
            this.noteAdapter = noteAdapter
            // deal with the item yourself TODO save
            savePrivately(applicationContext,isSqlSave,noteAdapter.getmNotesData() as ArrayList<Note>)

        }
    }

    private val DATA_FILE_NAME = "data.notes"
    private val DIR_NAME = "notesApp"

    fun savePrivately(context: Context,isSql: Boolean, dataList: kotlin.collections.ArrayList<Note>) {
        if(!isSql) {
            // TODO form memory only list

            val fos = openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(dataList)
            oos.close()
        } else { //TODO sql

            val noteDB: NoteDatabase = NoteDatabase.getInstance(applicationContext)
            val noteDAO: NoteDAO = noteDB.noteDao()
/*
            val coro= GlobalScope
            coro.launch {
                //Looper.prepare()
                //Toast.makeText(applicationContext,"before insert",Toast.LENGTH_SHORT)
                //Looper.loop()
                noteDB.runInTransaction{

                    for (note in dataList) {
                        noteDAO.insert(note)
                    }
                }
            } */
            //var isInserted = false
            //while(!isInserted) {
                GlobalScope.launch {
                    noteDB.runInTransaction {
                        println("Task from runInTransaction save scope")
                        /*for (note in dataList) {
                            noteDAO.insert(note)
                        }*/
                        noteDAO.nukeTable()
                        noteDAO.insertAllNotes(dataList)
                    }
                    //isInserted = true
                }
            //}
        }
    }

    fun loadPrivately(context: Context,isSql: Boolean): kotlin.collections.ArrayList<Note> {
        if (!isSql) {
            try {
                val fis = openFileInput(DATA_FILE_NAME)
                val ois = ObjectInputStream(fis)
                try {
                    val notes: ArrayList<Note> = ois.readObject() as ArrayList<Note>
                    return notes
                } catch (e: Exception) {
                    e.printStackTrace()
                    val notes = ArrayList<Note>()
                    return notes
                } finally {
                    fis.close()
                    ois.close()
                }

            } catch (e: FileNotFoundException) {
                // Create directory into internal memory;
                //val mydir: File = context.getDir(DIR_NAME, Context.MODE_PRIVATE)
                // Get a file myfile within the dir mydir.
                //val fileWithinMyDir = File(mydir, DATA_FILE_NAME)
                e.printStackTrace()
                val fos = openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE)
                val oos = ObjectOutputStream(fos)
                oos.writeObject(ArrayList<Note>())
                oos.close()
                return ArrayList<Note>()
            }

        } else { //TODO sql
            val noteDB: NoteDatabase = NoteDatabase.getInstance(applicationContext)
            val noteDAO: NoteDAO = noteDB.noteDao()
            var notes: ArrayList<Note>? = null
            /*
            val coro= GlobalScope
            coro.launch {
                //Looper.prepare()
                //Toast.makeText(applicationContext,"before getAll",Toast.LENGTH_SHORT)
                //Looper.loop()
                noteDB.runInTransaction{
                    notes = noteDAO.all as ArrayList<Note>
                }
            }*/
            while (notes == null) {
                GlobalScope.launch {
                    noteDB.runInTransaction {
                        println("Task from runInTransaction load scope")
                        notes = noteDAO.all as ArrayList<Note>
                    }
                }
            }
            if (notes == null) {
                return kotlin.collections.ArrayList<Note>()
            } else {
                return notes as ArrayList<Note>
            }

        }
    }


    override fun onDestroy() {
        val noteAdapter = (rvNotes.adapter as NoteAdapter)
        savePrivately(applicationContext,isSqlSave,noteAdapter.getmNotesData() as ArrayList<Note>)
        super.onDestroy()
    }
}