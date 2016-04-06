package com.kelebro63.storio.databаse.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.kelebro63.storio.databаse.Entities.Book;
import com.kelebro63.storio.databаse.Entities.Reader;
import com.kelebro63.storio.databаse.tables.BooksTable;
import com.kelebro63.storio.databаse.tables.ReadersTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

/**
 * Created by Kelebro63 on 30.03.2016.
 */
public class BookGetResolver extends DefaultGetResolver<Book> {
    @NonNull
    @Override
    public Book mapFromCursor(Cursor cursor) {

        final Reader reader = Reader.newReader(
                cursor.getLong(cursor.getColumnIndexOrThrow(ReadersTable.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(ReadersTable.COLUMN_NAME))
        );

        final Book book = Book.newBook(
                cursor.getLong(cursor.getColumnIndexOrThrow(BooksTable.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(BooksTable.COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndexOrThrow(BooksTable.COLUMN_TITLE)),
                reader);




        return book;
    }
}

