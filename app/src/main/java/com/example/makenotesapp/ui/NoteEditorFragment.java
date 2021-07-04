package com.example.makenotesapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.makenotesapp.data.NoteData;
import com.example.makenotesapp.R;

public class NoteEditorFragment extends Fragment {

    private static final String NOTE_DATA = "NoteData";
    private NoteData mNoteData;

    public NoteEditorFragment() {
    }

    public NoteEditorFragment(NoteData noteData) {
        mNoteData = noteData;
    }

    public static NoteEditorFragment newInstance(String noteData) {
        NoteEditorFragment fragment = new NoteEditorFragment();
        Bundle args = new Bundle();
        args.putString(NOTE_DATA, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (mNoteData == null) {
                mNoteData = getArguments().getParcelable(NOTE_DATA);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mNoteData != null) {
            initList(view);
        }
    }

    private void initList(View view) {
        LinearLayout editorLayout = view.findViewById(R.id.editorLayout);
        LinearLayout layoutView = new LinearLayout(view.getContext());
        layoutView.setOrientation(LinearLayout.HORIZONTAL);
        editorLayout.addView(layoutView);
        EditText etHeader = new EditText(getContext());
        etHeader.setText(mNoteData.getHeader());
        etHeader.setTextSize(15);
        layoutView.addView(etHeader);
        EditText etDescription = new EditText(getContext());
        etDescription.setText(mNoteData.getDescription());
        etDescription.setTextSize(15);
        layoutView.addView(etDescription);
        EditText etDate = new EditText(getContext());
        etDate.setText(mNoteData.getCreationDate().toString());
        etDate.setTextSize(15);
        layoutView.addView(etDate);
        EditText noteText = new EditText(getContext());
        noteText.setText(mNoteData.getText());
        editorLayout.addView(noteText);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_editor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(getContext(), "Action is out of service", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}