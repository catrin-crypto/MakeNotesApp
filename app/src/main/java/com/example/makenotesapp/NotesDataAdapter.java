package com.example.makenotesapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesDataAdapter
        extends RecyclerView.Adapter<NotesDataAdapter.ViewHolder> {
    private Notes notesSource;
    private OnItemClickListener itemClickListener;

    public NotesDataAdapter(Notes notesSource) {
        this.notesSource = notesSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesDataAdapter.ViewHolder viewHolder, int position) {
        NoteData noteData = notesSource.at(position);
        viewHolder.titleView.setText(noteData.getHeader());
        viewHolder.descriptionView.setText(noteData.getDescription());
        viewHolder.dateView.setText(noteData.getCreationDate().toString());
    }

    @Override
    public int getItemCount() {
        return notesSource.getLength();
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView, descriptionView, dateView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
            descriptionView = (TextView) itemView.findViewById(R.id.description);
            dateView = (TextView) itemView.findViewById(R.id.creation_date);
            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }

    }

}
