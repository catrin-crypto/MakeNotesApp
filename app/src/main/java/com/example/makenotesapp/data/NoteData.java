package com.example.makenotesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

public class NoteData implements Parcelable {

    private String id;
    private String header;
    private String description;
    private Date creationDate;
    private String text;

    public NoteData(String header, String description, String text) {
        this.header = header;
        this.description = description;
        this.creationDate = Calendar.getInstance().getTime();
        this.text = text;
    }

    public NoteData(String header, String description, Date creationDate, String text) {
        this.header = header;
        this.description = description;
        this.creationDate = creationDate;
        this.text = text;
    }

    protected NoteData(Parcel in) {
        header = in.readString();
        description = in.readString();
        creationDate = new Date(in.readLong());
        text = in.readString();

    }

    public static final Creator<NoteData> CREATOR = new Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel in) {
            return new NoteData(in);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(header);
        dest.writeString(description);
        dest.writeLong(creationDate.getTime());
        dest.writeString(text);
    }
}
