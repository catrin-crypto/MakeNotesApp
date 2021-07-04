package com.example.makenotesapp.ui;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.makenotesapp.MainActivity;
import com.example.makenotesapp.Navigation;
import com.example.makenotesapp.data.INotes;
import com.example.makenotesapp.data.NoteData;
import com.example.makenotesapp.data.Notes;
import com.example.makenotesapp.data.NotesFirebase;
import com.example.makenotesapp.data.NotesFirebaseResponse;
import com.example.makenotesapp.observer.Observer;
import com.example.makenotesapp.observer.Publisher;
import com.example.makenotesapp.R;

public class ListFragment extends Fragment {
    private static final int MY_DEFAULT_DURATION = 1000;
    private static final String ARG_PARAM1 = "notes";
    public static final String NOTE_DATA = "NoteData";
    public static final String TAG = "ListFragment";
    private INotes mNotes;
    private NoteData mCurrentNote;
    private boolean mIsLandscape;
    private NotesDataAdapter mAdapter;
    private Navigation mNavigation;
    private Publisher mPublisher;
    private boolean mMoveToFirstPosition;

    public ListFragment() {

    }

    public static ListFragment getInstance(FragmentManager fragmentManager,Bundle args) {
        Fragment fr = fragmentManager.findFragmentByTag(TAG);
        ListFragment fragment;
        if (fr == null){
            fragment = new ListFragment();
        } else fragment = (ListFragment) fr;
        if (args == null) args = new Bundle();
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
            if (savedInstanceState != null) {
                mNotes = savedInstanceState.getParcelable(ARG_PARAM1);
            }
        }
        if (mNotes == null) {
            mNotes = new Notes();
            mNotes.addNoteData(
                    new NoteData("First Note",
                            "first description",
                            "Very First Text written in first note"));
            mNotes.addNoteData(
                    new NoteData("Second Note",
                            "second description",
                            "Very Second Text written in second note"));
        }

        if (savedInstanceState != null) {
            mCurrentNote = savedInstanceState.getParcelable(NOTE_DATA);
        }

        if (mCurrentNote == null){
            mCurrentNote = mNotes.at(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        mIsLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (mIsLandscape) {
            showLandNoteEditor(0);
        }
        initRecyclerView(recyclerView);
        setHasOptionsMenu(true);
        mNotes = new NotesFirebase().init(new NotesFirebaseResponse() {
            @Override
            public void initialized(INotes notesData) {
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setDataSource(mNotes);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }


    private void initRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new NotesDataAdapter(this);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        if (mMoveToFirstPosition && mNotes.getLength() > 0){
            recyclerView.scrollToPosition(0);
            mMoveToFirstPosition = false;
        }

        mAdapter.SetOnItemClickListener(new NotesDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentNote = mNotes.at(position);
                showNoteEditor(position);
                mPublisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(NoteData noteData) {
                        mNotes.updateNoteData(position, noteData);
                        mAdapter.notifyItemChanged(position);
                    }
                });
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

        NoteData noteData = null;
        if (position > -1)
           noteData = mNotes.at(position);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        EditorFragment editorFragment = EditorFragment.getInstance(fragmentManager,noteData);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.edit_notes, editorFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortNoteEditor(int position) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        NoteData noteData = null;
        if (position > -1)
            noteData = mNotes.at(position);
        mNavigation.addFragmentToFirstFrame(
                EditorFragment.getInstance(fragmentManager,noteData),
                true,EditorFragment.TAG);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_editor, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId){
        switch (menuItemId){
            case R.id.action_add:
                showNoteEditor(-1);
                mPublisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(NoteData noteData) {
                        mNotes.addNoteData(noteData);
                        mAdapter.notifyItemInserted(mNotes.getLength() - 1);
                        mMoveToFirstPosition = true;
                    }
                });
                return true;
            case R.id.action_update:
                final int updatePosition = mAdapter.getMenuPosition();
                showNoteEditor(updatePosition);
                mPublisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(NoteData noteData) {
                        mNotes.updateNoteData(updatePosition, noteData);
                        mAdapter.notifyItemChanged(updatePosition);
                    }
                });
                return true;
            case R.id.action_delete:
                int deletePosition = mAdapter.getMenuPosition();
                mNotes.deleteNoteData(deletePosition);
                mAdapter.notifyItemRemoved(deletePosition);
                return true;
            case R.id.action_clear:
                mNotes.clearNoteData();
                mAdapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        Notes notes = new Notes();
        notes.replaceNotesData(mNotes.getNotesList());
        savedInstanceState.putParcelable(ARG_PARAM1,notes);
    }

}