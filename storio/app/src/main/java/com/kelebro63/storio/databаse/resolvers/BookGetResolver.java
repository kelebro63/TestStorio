package com.kelebro63.storio.databаse.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.kelebro63.storio.databаse.Entities.Book;
import com.kelebro63.storio.databаse.Entities.Reader;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

/**
 * Created by Kelebro63 on 30.03.2016.
 */
public class BookGetResolver extends DefaultGetResolver<Book> {
    @NonNull
    @Override
    public Book mapFromCursor(Cursor cursor) {
        Book object = Book.newBook(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("author")), Reader.newReader((long) 125454545, ""));
        return object;
    }
}
