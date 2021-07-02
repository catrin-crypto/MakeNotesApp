package com.example.makenotesapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ListFragment extends Fragment {
    private static final int MY_DEFAULT_DURATION = 1000;
    private static final String ARG_PARAM1 = "notes";
    public static final String NOTE_DATA = "NoteData";
    private Notes mNotes;
    private NoteData mCurrentNote;
    private boolean mIsLandscape;
    private NotesDataAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Navigation mNavigation;
    private Publisher mPublisher;
    private boolean mMoveToLastPosition;

    public ListFragment() {

    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
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
            showLandNoteEditor(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);

        initRecyclerView(recyclerView);
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        mNavigation = activity.getNavigation();
        mPublisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        mNavigation = null;
        mPublisher = null;
        super.onDetach();
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
//                mNotes.addNote("Заголовок ",
//                        "Описание ","Текст ");
//                mAdapter.notifyItemInserted(mNotes.getLength() - 1);
//                mRecyclerView.smoothScrollToPosition(mNotes.getLength() - 1);
                mNavigation.addFragment(EditorFragment.newInstance(), true);
                mPublisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(NoteData noteData) {
                        mNotes.addNote(noteData);
                        mAdapter.notifyItemInserted(mNotes.getLength() - 1);
                        mMoveToLastPosition = true;
                    }
                });
                return true;
            case R.id.action_clear:
                mNotes.clearNoteData();
                mAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_lines);
        //mNotes = new Notes(getResources()).init();
        initRecyclerView(mRecyclerView);
    }

    private void initRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new NotesDataAdapter(mNotes,this);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        if (mMoveToLastPosition){
            recyclerView.smoothScrollToPosition(mNotes.getLength() - 1);
            mMoveToLastPosition = false;
        }

        mAdapter.SetOnItemClickListener(new NotesDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentNote = mNotes.at(position);
                showNoteEditor(position);
            }
        });
    }

    private void showNoteEditor(int position) {
        if (mIsLandscape) {
            showLandNoteEditor(position);
        } else {
            showPortNoteEditor(position);
        }
    }

    private void showLandNoteEditor(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_DATA, mNotes.at(position));
        EditorFragment detail = new EditorFragment();
        detail.setArguments(bundle);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.edit_notes, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
        mPublisher.subscribe(new Observer() {
            @Override
            public void updateNoteData(NoteData noteData) {
                mNotes.updateNoteData(position, noteData);
                mAdapter.notifyItemChanged(position);
            }
        });
    }

    private void showPortNoteEditor(int position) {
        mNavigation.addFragment(EditorFragment.newInstance(mNotes.at(position)), true);
        mPublisher.subscribe(new Observer() {
            @Override
            public void updateNoteData(NoteData noteData) {
                mNotes.updateNoteData(position, noteData);
                mAdapter.notifyItemChanged(position);
            }
        });
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(NOTE_DATA, mNotes.at(position));
//        EditorFragment detail = new EditorFragment();
//        detail.setArguments(bundle);
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.first_frame_layout, detail);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.commit();

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_editor, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = mAdapter.getMenuPosition();

        switch(item.getItemId()) {
            case R.id.action_update:
                Toast.makeText(this.getContext(),
                        "Update", Toast.LENGTH_SHORT).show();
                mNavigation.addFragment(EditorFragment.newInstance(mNotes.at(position)), true);
                mPublisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(NoteData noteData) {
                        mNotes.updateNoteData(position, noteData);
                        mAdapter.notifyItemChanged(position);
                    }
                });

                return true;
            case R.id.action_delete:
                mNotes.deleteNoteData(position);
                mAdapter.notifyItemRemoved(position);

                Toast.makeText(this.getContext(),
                        "Delete", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

}