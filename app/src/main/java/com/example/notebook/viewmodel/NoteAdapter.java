package com.example.notebook.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.MainActivity;
import com.example.notebook.NoteActivity;
import com.example.notebook.R;
import com.example.notebook.constants.NoteConstants;
import com.example.notebook.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Comparator<Note> mComparator = new Comparator<Note>(){
        @Override
        public int compare(Note o1, Note o2) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss", Locale.ENGLISH);
            Date date1 = null;
            try {
                date1 = format.parse(o1.getDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Date date2 = null;
            try {
                date2 = format.parse(o2.getDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return date2.compareTo(date1);
        }


    };

    // Store a member variable for the notes
    private List<Note> mNotesView;
    private List<Note> mNotesData;

    public List<Note> getmNotesData() {
        return mNotesData;
    }

    public List<Note> getNotesViewList() {
        return mNotesView;
    }

    public void setNotes(List<Note> mNotes) {
        mNotes.sort(mComparator);
        this.mNotesView = mNotes;
        mNotesData = new ArrayList<>(mNotes);
        notifyDataSetChanged();
    }
    public void addToNotes(Note note) {

        //mNotes.add(note);
        mNotesData.add(note);
        mNotesData.sort(mComparator);
        notifyDataSetChanged();
    }

    public void setNoteById(Note note) {
        /*mNotes = mNotes.stream().map((n) -> {
            if (n.getNoteId().equals(note.getNoteId())) {
                return note;
            } else {
                return n;
            }
        }).collect(Collectors.toList()); */
        mNotesData = mNotesData.stream().map((n) -> {
            if (n.getNoteId().equals(note.getNoteId())) {
                return note;
            } else {
                return n;
            }
        }).collect(Collectors.toList());
        mNotesData.sort(mComparator);
        notifyDataSetChanged();
    }

    private boolean deleteItem(int position) {
        Note noteToDelete = mNotesView.get(position);
        mNotesData.remove(noteToDelete);
        //mNotesData.remove(position);
        mNotesView.remove(position);
        // maybe sort
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mNotesView.size());

        ((MainActivity)appCompatActivity).savePrivately(appCompatActivity.getApplicationContext(), ((MainActivity) appCompatActivity).isSqlSave(),(ArrayList<Note>) mNotesData);
        //holder.itemView.setVisibility(View.GONE);
        return  true;
    }

    private AppCompatActivity appCompatActivity;
    // Pass in the contact array into the constructor
    public NoteAdapter(List<Note> notes, AppCompatActivity activity) {
        notes.sort(mComparator);
        mNotesView = notes;
        mNotesData = new ArrayList<>(notes);
        appCompatActivity = activity;
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
        Note note = mNotesView.get(position);

        // Set item views based on your views and data model
        TextView titleView = holder.title;
        titleView.setText(note.getTitle());

        TextView contentView = holder.content;
        contentView.setText(note.getContent());

        TextView dateView = holder.date;
        dateView.setText(note.getDate());
        /*
        Button button = holder.messageButton;
        button.setText(contact.isOnline() ? "Message" : "Offline");
        button.setEnabled(contact.isOnline());*/
    }

    // Returns the total count of mNotes in the list
    @Override
    public int getItemCount() {
        return mNotesView.size();
    }




    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;

        public TextView date;
        public TextView content;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.noteTitle);
            content = (TextView) itemView.findViewById(R.id.noteContent);
            date = (TextView) itemView.findViewById(R.id.noteDate);
        }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NoteActivity.class);
            Note currNote = mNotesView.get(getAdapterPosition());
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

    // method for filtering our recyclerview mNotes.
    public void filterList(String query) {
        mNotesView.clear();
        if(query.isEmpty()){
            mNotesView.addAll(mNotesData);
        } else{
            query = query.toLowerCase();
            for(Note item: mNotesData){
                if(item.getTitle().toLowerCase().contains(query) || item.getContent().toLowerCase().contains(query)){
                    mNotesView.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }



}
