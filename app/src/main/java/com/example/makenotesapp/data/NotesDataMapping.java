package com.example.makenotesapp.data;


import com.google.firebase.Timestamp;

        import java.util.HashMap;
        import java.util.Map;

public class NotesDataMapping {

    public static class Fields{
        public final static String HEADER = "header";
        public final static String DESCRIPTION = "description";
        public final static String CREATION_DATE = "creationDate";
        public final static String TEXT = "text";
    }

    public static NoteData toNoteData(String id, Map<String, Object> doc) {
        Timestamp timeStamp = (Timestamp)doc.get(Fields.CREATION_DATE);
        NoteData answer = new NoteData((String) doc.get(Fields.HEADER),
                (String) doc.get(Fields.DESCRIPTION),
                timeStamp.toDate(),
                (String) doc.get(Fields.TEXT));
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(NoteData noteData){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.HEADER, noteData.getHeader());
        answer.put(Fields.DESCRIPTION, noteData.getDescription());
        answer.put(Fields.CREATION_DATE, noteData.getCreationDate());
        return answer;
    }
}