package com.example.makenotesapp;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteDataFragment extends Fragment {
    private static final String NOTES_LIST_TRANSACTION = "NotesListTrans";
    private static final String NOTES = "Notes";
    public static final String NOTE_DATA = "NoteData";
    private Notes mNotes;
    private NoteData mCurrentNote;
    private boolean mIsLandscape;

    public NoteDataFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notes Single Parameter.
     * @return A new instance of fragment NoteDataOutput.
     */
    public static NoteDataFragment newInstance(Notes notes) {
        NoteDataFragment fragment = new NoteDataFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTES, notes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotes = getArguments().getParcelable(NOTES);
        }
        if (mNotes == null) {
            //TODO: Make user data load here
            mNotes = new Notes();
            mNotes.addNote("First Note", "first description", "Very First Text written in first note");
            mNotes.addNote("Second Note", "second description", "Very Second Text written in second note");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_data_output, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            mCurrentNote = savedInstanceState.getParcelable(NOTE_DATA);
        } else {
            mCurrentNote = mNotes.at(0);
        }

        if (mIsLandscape) {
            showLandNoteEditor(mCurrentNote);
        }
    }

    private void showNoteEditor(NoteData currentNote) {
        if (mIsLandscape) {
            showLandNoteEditor(currentNote);
        } else {
            showPortNoteEditor(currentNote);
        }
    }

    private void showLandNoteEditor(NoteData currentNote) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_DATA, currentNote);
        NoteEditorFragment detail = new NoteEditorFragment();
        detail.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.edit_notes, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortNoteEditor(NoteData currentNote) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_DATA, currentNote);
        NoteEditorFragment detail = new NoteEditorFragment();
        detail.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.addToBackStack(NOTES_LIST_TRANSACTION);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.first_frame_layout, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), MainActivity2NoteText.class);
//        intent.putExtra(NOTE_DATA, currentNote);
//        startActivity(intent);
    }

    private void initList(View view) {
        LinearLayout commonLayout = view.findViewById(R.id.commonLayout);
        for (int i = 0; i < mNotes.getLength(); i++) {
            NoteData note = mNotes.at(i);

            LinearLayout layoutView = new LinearLayout(view.getContext());

            layoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentNote = note;
                    showNoteEditor(note);
                }
            });

            layoutView.setOrientation(LinearLayout.HORIZONTAL);
            commonLayout.addView(layoutView);
            TextView tvHeader = new TextView(getContext());
            tvHeader.setText(note.getHeader());
            tvHeader.setTextSize(15);
            layoutView.addView(tvHeader);
            TextView tvDescription = new TextView(getContext());
            tvDescription.setText(note.getDescription());
            tvDescription.setTextSize(15);
            layoutView.addView(tvDescription);
            TextView tvDate = new TextView(getContext());
            tvDate.setText(note.getCreationDate().toString());
            tvDate.setTextSize(15);
            layoutView.addView(tvDate);

        }
    }

}