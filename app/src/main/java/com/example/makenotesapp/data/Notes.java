package com.example.makenotesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Notes implements Parcelable, INotes {
    private List<NoteData> mNotesData;

    protected Notes(Parcel in) {
        mNotesData = in.createTypedArrayList(NoteData.CREATOR);
    }

    public Notes() {
        mNotesData = new ArrayList<>();
    }



    public void addNoteData(String header, String description, String text) {
        mNotesData.add(new NoteData(header, description, text));
    }

    @Override
    public NoteData at(int i) {
        return mNotesData.get(i);
    }

    @Override
    public int getLength() {
        return mNotesData.size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mNotesData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    @Override
    public void addNoteData(NoteData noteData) {
        mNotesData.add(noteData);  }

    @Override
    public void updateNoteData(int position, NoteData noteData) {
        mNotesData.set(position, noteData);
    }

    @Override
    public void deleteNoteData(int position) {
        mNotesData.remove(position);
    }

    @Override
    public void clearNoteData() {
        mNotesData.clear();
    }

    @Override
    public List<NoteData> getNotesList(){
        return mNotesData;
    }

    public void replaceNotesData(List<NoteData> notesData){
        mNotesData = notesData;
    }
}
