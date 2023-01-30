package com.example.notebook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Note implements Serializable {


    private String noteId = UUID.randomUUID().toString();
    private String title = "";
    private String content = "";

    public Note() {
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

    public static ArrayList<Note> createNotesList(int numNotes) {
        ArrayList<Note> notesList = new ArrayList<Note>();

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
