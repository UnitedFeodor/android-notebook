package com.example.notebook.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.NoteActivity;
import com.example.notebook.R;
import com.example.notebook.constants.NoteConstants;
import com.example.notebook.model.Note;

import java.util.List;
import java.util.stream.Collectors;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {


    // Store a member variable for the notes
    private List<Note> mNotes;

    public List<Note> getNotesList() {
        return mNotes;
    }

    public void setNotes(List<Note> mNotes) {
        this.mNotes = mNotes;
    }
    public void addToNotes(Note note) {
        mNotes.add(note);
    }

    public void setNoteById(Note note) {
        mNotes = mNotes.stream().map((n) -> {
            if (n.getNoteId().equals(note.getNoteId())) {
                return note;
            } else {
                return n;
            }
        }).collect(Collectors.toList());

    }

    // Pass in the contact array into the constructor
    public NoteAdapter(List<Note> notes) {
        mNotes = notes;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.note_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // map onClick activity
        holder.itemView.setOnClickListener(holder::onClick);

        holder.itemView.setOnLongClickListener(holder::onLongClick);

        // Get the data model based on position
        Note note = mNotes.get(position);

        // Set item views based on your views and data model
        TextView titleView = holder.title;
        titleView.setText(note.getTitle());

        TextView contentView = holder.content;
        contentView.setText(note.getContent());
        /*
        Button button = holder.messageButton;
        button.setText(contact.isOnline() ? "Message" : "Offline");
        button.setEnabled(contact.isOnline());*/
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    private boolean deleteItem(int position) {
        mNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mNotes.size());
        //holder.itemView.setVisibility(View.GONE);
        return  true;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public TextView content;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.noteTitle);
            content = (TextView) itemView.findViewById(R.id.noteContent);
        }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NoteActivity.class);
            Note currNote = mNotes.get(getAdapterPosition());
            /*
            intent.putExtra(NoteConstants.NOTE_TITLE,currNote.getTitle());
            intent.putExtra(NoteConstants.NOTE_CONTENT,currNote.getContent());
            intent.putExtra(NoteConstants.NOTE_ID,currNote.getNoteId());

             */

            //intent.putExtra(NoteConstants.CURRENT_NOTE,Note());
            intent.putExtra(NoteConstants.CURRENT_NOTE,currNote);
            ((Activity)view.getContext()).startActivityForResult(intent, NoteConstants.REQUEST_CODE_ADD_TO_LIST);

            //view.getContext().startActivity(intent);


        }



        @Override
        public boolean onLongClick(View view) {
            return deleteItem(getAdapterPosition());

        }
    }



}
