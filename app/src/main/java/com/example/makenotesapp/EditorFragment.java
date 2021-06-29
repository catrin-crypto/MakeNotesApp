package com.example.makenotesapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

public class EditorFragment extends Fragment {

    private static final String ARG_NOTE_DATA = "NoteData";
    private NoteData mNoteData;
    private Publisher publisher;
    private TextInputEditText title;
    private TextInputEditText description;
    private TextInputEditText text;
    private DatePicker datePicker;

    public static EditorFragment newInstance(NoteData noteData) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE_DATA, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditorFragment newInstance() {
        EditorFragment fragment = new EditorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNoteData = getArguments().getParcelable(ARG_NOTE_DATA);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor_fragment, container, false);
        initView(view);
        if (mNoteData != null) {
            populateView();
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mNoteData = collectCardData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(mNoteData);
    }

    private NoteData collectCardData() {
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();
        String text = this.text.getText().toString();
        Date date = getDateFromDatePicker();

        return new NoteData(title, description, date, text);
    }

    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }

    private void initView(View view) {
        title = view.findViewById(R.id.inputTitle);
        description = view.findViewById(R.id.inputDescription);
        datePicker = view.findViewById(R.id.inputDate);
        text = view.findViewById(R.id.inputText);
    }

    private void populateView() {
        title.setText(mNoteData.getHeader());
        description.setText(mNoteData.getDescription());
        text.setText(mNoteData.getText());
        initDatePicker(mNoteData.getCreationDate());
    }

    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }
}
