package com.example.makenotesapp.data;

import java.util.List;

public interface INotes {
    NoteData at(int i);

    List<NoteData> getNotesList();

    int getLength();

    void addNoteData(NoteData noteData);

    void updateNoteData(int position, NoteData noteData);

    void deleteNoteData(int position);

    void clearNoteData();
}
