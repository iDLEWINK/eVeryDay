package com.mobdeve.s14.group24.everyday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaEntryAdapter extends RecyclerView.Adapter<MediaEntryViewHolder> {

    private ArrayList<MediaEntry> mediaEntries;

    public MediaEntryAdapter (ArrayList<MediaEntry> mediaEntries) {
        this.mediaEntries = mediaEntries;
    }

    @NonNull
    @NotNull
    @Override
    public MediaEntryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_media, parent, false);

        MediaEntryViewHolder mediaEntryViewHolder = new MediaEntryViewHolder(view);
        return mediaEntryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MediaEntryViewHolder holder, int position) {
        MediaEntry mediaEntry = mediaEntries.get(position);


        holder.setTvDate(mediaEntry.getDate().toStringNoYear());
        holder.setIvImage(mediaEntry.getImagePath());
        holder.setIvMoodRating(mediaEntry.getMoodRating());
    }

    @Override
    public int getItemCount() {
        return this.mediaEntries.size();
    }
}
