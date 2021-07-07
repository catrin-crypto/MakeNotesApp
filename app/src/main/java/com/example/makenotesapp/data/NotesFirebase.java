package com.example.makenotesapp.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotesFirebase implements INotes {
    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "[NotesSource]";

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private CollectionReference mCollection = mStore.collection(NOTES_COLLECTION);

    private List<NoteData> mNotesData = new ArrayList<NoteData>();

    public NotesFirebase init(final NotesFirebaseResponse notesFirebaseResponse) {
        mCollection.orderBy(NotesDataMapping.Fields.CREATION_DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mNotesData = new ArrayList<NoteData>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                NoteData cardData = NotesDataMapping.toNoteData(id, doc);
                                mNotesData.add(cardData);
                            }
                            Log.d(TAG, "success " + mNotesData.size() + " qnt");
                            notesFirebaseResponse.initialized(NotesFirebase.this);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;
    }

    @Override
    public NoteData at(int i) {
        return mNotesData.get(i);
    }

    @Override
    public int getLength() {
        if (mNotesData == null) return 0;
        return mNotesData.size();
    }

    @Override
    public void addNoteData(NoteData noteData) {
        mCollection.add(NotesDataMapping.toDocument(noteData)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                noteData.setId(documentReference.getId());
             }
        });
        mNotesData.add(noteData);

    }

    @Override
    public void updateNoteData(int position, NoteData noteData) {
        String id = noteData.getId();
         if (mCollection.document(id).set(NotesDataMapping.toDocument(noteData)).isSuccessful())
            mNotesData.set(position, noteData);
    }

    @Override
    public void deleteNoteData(int position) {
        if (mCollection.document(mNotesData.get(position).getId()).delete().isSuccessful())
            mNotesData.remove(position);
    }

    @Override
    public void clearNoteData() {
        for (NoteData noteData1: mNotesData) {
            mCollection.document(noteData1.getId()).delete();
        }
        mNotesData = new ArrayList<>();
    }

    @Override
    public List<NoteData> getNotesList(){
        return mNotesData;
    }
}
