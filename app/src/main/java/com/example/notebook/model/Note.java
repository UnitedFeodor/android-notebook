package com.example.notebook.model;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Note implements Serializable {

    @PrimaryKey
    @NonNull
    private String noteId = UUID.randomUUID().toString();

    private String title = "";
    private String content = "";

    private String date = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date());

    public Note() {
        noteId = UUID.randomUUID().toString();
        title = "";
        content = "";
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return noteId.equals(note.noteId) && title.equals(note.title) && content.equals(note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, title, content);
    }

    public String getNoteId() {
        return noteId;
    }
    public void setNoteId(String id) {
        this.noteId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String newDate) {
        this.date = newDate;
    }
    public void setDate(Date newDate) {
        this.date = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(newDate);
    }

    public static ArrayList<Note> createNotesList(int numNotes) {
        ArrayList<Note> notesList = new ArrayList<>();

        for (int i = 1; i <= numNotes; i++) {
            StringBuilder content = new StringBuilder();
            for(int j = 1; j <= i; j++) {
                content.append("content");
            }
            notesList.add(new Note("TITLE " + i, content.toString() + i));
        }

        return notesList;
    }
}
