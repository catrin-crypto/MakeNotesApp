package com.example.makenotesapp.observer;

import com.example.makenotesapp.data.NoteData;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void disactivate(){
        observers.clear();
    }

    public void notifySingle(NoteData noteData) {
        for (Observer observer : observers) {
            observer.updateNoteData(noteData);
        }
    }

}
