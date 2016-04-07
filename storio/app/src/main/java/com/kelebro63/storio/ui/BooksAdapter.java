package com.kelebro63.storio.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelebro63.storio.R;
import com.kelebro63.storio.databаse.Entities.Book;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bistrov Alexey on 25.03.2016.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder>{

    private List<Book> books;

    public void setBooks(@Nullable List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(BooksAdapter.ViewHolder holder, int position) {
        final Book book = books.get(position);

        holder.authorTextView.setText("@" + book.author());
        holder.contentTextView.setText(book.title());
        holder.readerTextView.setText(book.reader().name());
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_author)
        TextView authorTextView;

        @Bind(R.id.tv_title)
        TextView contentTextView;

        @Bind(R.id.tv_reader)
        TextView readerTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
