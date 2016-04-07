package com.kelebro63.storio.databаse.resolvers;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.kelebro63.storio.databаse.Entities.Book;
import com.kelebro63.storio.databаse.tables.BooksTable;
import com.kelebro63.storio.databаse.tables.ReadersTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Created by Kelebro63 on 26.03.2016.
 */
public class BookPutResolver extends PutResolver<Book> {

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Book book) {

        ContentValues contentValuesBook = new ContentValues();
        contentValuesBook.put(BooksTable.COLUMN_ID, book.id());
        contentValuesBook.put(BooksTable.COLUMN_AUTHOR, book.author());
        contentValuesBook.put(BooksTable.COLUMN_TITLE, book.title());
        contentValuesBook.put(BooksTable.TABLE, "");

        ContentValues contentValuesReader = new ContentValues();
        contentValuesReader.put(ReadersTable.COLUMN_ID, book.reader().id());
        contentValuesReader.put(ReadersTable.COLUMN_NAME, book.reader().name());
        contentValuesReader.put(ReadersTable.COLUMN_BOOK_ID, book.id());
        contentValuesReader.put(ReadersTable.TABLE, "");

        final PutResults<ContentValues> putResults = storIOSQLite
                .put()
                .contentValues(asList(contentValuesBook, contentValuesReader)).withPutResolver(new DefaultPutResolver<ContentValues>() {
                    @NonNull
                    @Override
                    protected InsertQuery mapToInsertQuery(@NonNull ContentValues object) {
                        if (object.containsKey(BooksTable.TABLE)) {
                            return InsertQuery.builder()
                                    .table(BooksTable.TABLE)
                                    .build();
                        } else {
                            return InsertQuery.builder()
                                    .table(ReadersTable.TABLE)
                                    .build();
                        }
                    }

                    @NonNull
                    @Override
                    protected UpdateQuery mapToUpdateQuery(@NonNull ContentValues object) {
                        if (object.containsKey(BooksTable.TABLE)) {
                            return UpdateQuery.builder()
                                    .table(BooksTable.TABLE)
                                    .where("_id = ?")
                                    .whereArgs(object.get(BooksTable.COLUMN_ID))
                                    .build();
                        } else {
                            return UpdateQuery.builder()
                                    .table(ReadersTable.TABLE)
                                    .where("_id = ?")
                                    .whereArgs(object.get(ReadersTable.COLUMN_ID))
                                    .build();
                        }

                    }

                    @NonNull
                    @Override
                    protected ContentValues mapToContentValues(@NonNull ContentValues object) {
                        if (object.containsKey(BooksTable.TABLE)) {
                            ContentValues contentValues = new ContentValues(3);
                            contentValues.put(BooksTable.COLUMN_ID, object.getAsString(BooksTable.COLUMN_ID));
                            contentValues.put(BooksTable.COLUMN_AUTHOR,object.getAsString(BooksTable.COLUMN_AUTHOR));
                            contentValues.put(BooksTable.COLUMN_TITLE,object.getAsString(BooksTable.COLUMN_TITLE));
                            return contentValues;
                        } else {
                            ContentValues contentValuesReader = new ContentValues();
                            contentValuesReader.put(ReadersTable.COLUMN_ID, object.getAsString(ReadersTable.COLUMN_ID));
                            contentValuesReader.put(ReadersTable.COLUMN_NAME, object.getAsString(ReadersTable.COLUMN_NAME));
                            contentValuesReader.put(ReadersTable.COLUMN_BOOK_ID, object.getAsString(ReadersTable.COLUMN_BOOK_ID));
                            return contentValuesReader;
                        }
                    }
                }).prepare().executeAsBlocking();

        final Set<String> affectedTables = new HashSet<String>(2);

        affectedTables.add(BooksTable.TABLE);
        affectedTables.add(ReadersTable.TABLE);

        return PutResult.newUpdateResult(putResults.numberOfUpdates(), affectedTables);
    }}
