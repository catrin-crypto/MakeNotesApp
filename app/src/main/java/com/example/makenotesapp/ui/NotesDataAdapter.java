package com.example.makenotesapp.ui;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makenotesapp.data.INotes;
import com.example.makenotesapp.data.NoteData;
import com.example.makenotesapp.data.Notes;
import com.example.makenotesapp.R;

import java.text.SimpleDateFormat;

public class NotesDataAdapter
        extends RecyclerView.Adapter<NotesDataAdapter.ViewHolder> {
    private INotes notesSource;
    private OnItemClickListener itemClickListener;
    private final Fragment mFragment;
    private int mMenuPosition;

    public int getMenuPosition() {
        return mMenuPosition;
    }

    public NotesDataAdapter(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void setDataSource(INotes dataSource){
        this.notesSource = dataSource;
        notifyDataSetChanged();
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
            registerContextMenu(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onLongClick(View v) {
                    mMenuPosition = getLayoutPosition();
                    itemView.showContextMenu(10, 10);
                    return true;
                }
            });
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (mFragment != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mMenuPosition = getLayoutPosition();
                        return false;
                    }
                });

                mFragment.registerForContextMenu(itemView);
            }
        }

        public void setData(NoteData cardData) {
            titleView.setText(cardData.getHeader());
            descriptionView.setText(cardData.getDescription());
            dateView.setText(new SimpleDateFormat("dd-MM-yy").format(cardData.getCreationDate()));
        }
    }
}


