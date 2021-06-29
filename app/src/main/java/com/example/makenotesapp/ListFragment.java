package com.example.makenotesapp;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListFragment extends Fragment {

    private static final String ARG_PARAM1 = "notes";
    public static final String NOTE_DATA = "NoteData";

    private Notes mNotes;
    private NoteData mCurrentNote;
    private boolean mIsLandscape;


    public ListFragment() {

    }

    public static ListFragment newInstance(Notes notes) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, notes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotes = getArguments().getParcelable(ARG_PARAM1);
        }
        if (mNotes == null) {

            mNotes = new Notes();
            mNotes.addNote("First Note", "first description", "Very First Text written in first note");
            mNotes.addNote("Second Note", "second description", "Very Second Text written in second note");
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);

        initRecyclerView(recyclerView);
        return view;

    }

    private void initRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        NotesDataAdapter adapter = new NotesDataAdapter(mNotes);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);
        adapter.SetOnItemClickListener(new NotesDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentNote = mNotes.at(position);
                showNoteEditor(mCurrentNote);
            }
        });
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
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.first_frame_layout, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

}