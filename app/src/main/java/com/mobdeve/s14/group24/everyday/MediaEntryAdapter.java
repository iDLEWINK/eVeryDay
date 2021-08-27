package com.mobdeve.s14.group24.everyday;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaEntryAdapter extends RecyclerView.Adapter<MediaEntryViewHolder> {

    public static final String KEY_DATE = "KEY_DATE";
    public static final String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";
    public static final String KEY_CAPTION = "";
    public static final String KEY_LIKES = "";

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

        mediaEntryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewMediaEntryActivity.class);

                MediaEntry mediaEntry = mediaEntries.get(mediaEntryViewHolder.getBindingAdapterPosition());

                intent.putExtra(Keys.KEY_ID.name(), mediaEntry.getId());
                intent.putExtra(Keys.KEY_DATE.name(), mediaEntry.getDate().toStringFull());
                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), mediaEntry.getImagePath());
                intent.putExtra(Keys.KEY_CAPTION.name(), mediaEntry.getCaption());
                intent.putExtra(Keys.KEY_MOOD.name(), mediaEntry.getMood());

                v.getContext().startActivity(intent);
            }
        });

        return mediaEntryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MediaEntryViewHolder holder, int position) {
        MediaEntry mediaEntry = mediaEntries.get(position);

        holder.setTvDate(mediaEntry.getDate().toStringNoYear());
        holder.setIvImage(MediaStore.Images.Media.getBitmap(MainActivity.getContentResolver(), mediaEntry.getImagePath()));
        holder.setIvMood(mediaEntry.getMood());
    }

    @Override
    public int getItemCount() {
        return mediaEntries.size();
    }
}
