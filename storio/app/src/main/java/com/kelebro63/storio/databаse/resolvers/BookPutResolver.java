package com.kelebro63.storio.databаse.resolvers;

import android.support.annotation.NonNull;

import com.kelebro63.storio.databаse.Entities.Book;
import com.kelebro63.storio.databаse.tables.BooksTable;
import com.kelebro63.storio.databаse.tables.ReadersTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

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
        // We can even reuse StorIO methods

        final PutResults<Object> putResults = storIOSQLite
                .put()
                .objects(asList(book, book.reader()))
                .prepare()
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<String>(2);

        affectedTables.add(BooksTable.TABLE);
        affectedTables.add(ReadersTable.TABLE);

        return PutResult.newUpdateResult(putResults.numberOfUpdates(), affectedTables);
    }}
