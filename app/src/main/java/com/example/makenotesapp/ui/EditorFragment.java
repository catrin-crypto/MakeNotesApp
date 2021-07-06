package com.example.makenotesapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.makenotesapp.MainActivity;
import com.example.makenotesapp.data.NoteData;
import com.example.makenotesapp.observer.Publisher;
import com.example.makenotesapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

public class EditorFragment extends Fragment {

    public static final String TAG = "EditorFragment";
    public static final String ARG_NOTE_DATA = "NoteData";
    private NoteData mNoteData;
    private Publisher publisher;
    private TextInputEditText title;
    private TextInputEditText description;
    private TextInputEditText text;
    private DatePicker datePicker;

    public static EditorFragment getInstance(FragmentManager fragmentManager,NoteData noteData) {
        Fragment fr = fragmentManager.findFragmentByTag(TAG);
        EditorFragment fragment;
        if (fr == null){
            fragment = new EditorFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_NOTE_DATA, noteData);
            fragment.setArguments(args);

        } else {
            fragment = (EditorFragment) fr;
            fragment.setData(noteData);
        }
        return fragment;
    }

    public static EditorFragment getInstance() {
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
        mNoteData = collectNoteData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(mNoteData);
    }

    private NoteData collectNoteData() {
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();
        String text = this.text.getText().toString();
        Date date = getDateFromDatePicker();
        NoteData answer;
        if (mNoteData != null){
            answer = new NoteData(title,description,date,text);
            answer.setId(mNoteData.getId());
            return answer;
        }else {
            return new NoteData(title, description, date, text);
        }
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

    public void setData(NoteData noteData){
        mNoteData = noteData;
    }
}
