package com.example.makenotesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Notes implements Parcelable {
    private ArrayList<NoteData> notes;

    protected Notes(Parcel in) {
        notes = in.createTypedArrayList(NoteData.CREATOR);
    }

    public Notes(){
        notes = new ArrayList<>();
    };

    public void addNote(String header,String description,String text){
        notes.add(new NoteData(header,description,text));
    }

    public NoteData at(int i){
        return notes.get(i);
    }


    public int getLength(){
        return notes.size();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(notes);
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
}
