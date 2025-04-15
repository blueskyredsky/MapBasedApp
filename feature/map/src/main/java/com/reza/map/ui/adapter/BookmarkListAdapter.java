package com.reza.map.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reza.map.data.model.BookmarkView;
import com.reza.map.databinding.BookmarkItemBinding;

import java.util.List;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.ViewHolder> {

    private List<BookmarkView> bookmarkData;
    private final OnBookmarkClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public BookmarkItemBinding binding;

        public ViewHolder(BookmarkItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public BookmarkListAdapter(List<BookmarkView> bookmarkData, OnBookmarkClickListener listener) {
        this.bookmarkData = bookmarkData;
        this.listener = listener;
    }

    public void setBookmarkData(List<BookmarkView> bookmarks) {
        this.bookmarkData = bookmarks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        BookmarkItemBinding binding = BookmarkItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (bookmarkData != null) {
            BookmarkView bookmarkViewData = bookmarkData.get(position);
            holder.binding.bookmarkNameTextView.setText(bookmarkViewData.getTitle());
            holder.binding.bookmarkIcon.setImageResource(bookmarkViewData.getCategoryResourceId());

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookmarkClicked(bookmarkViewData);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookmarkData == null ? 0 : bookmarkData.size();
    }
}
