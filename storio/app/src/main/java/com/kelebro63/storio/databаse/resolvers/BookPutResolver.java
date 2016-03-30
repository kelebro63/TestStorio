package com.kelebro63.storio.databаse.resolvers;

import android.content.ContentValues;
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

        ContentValues contentValuesBook = new ContentValues();
        contentValuesBook.put(BooksTable.COLUMN_ID, book.id());
        contentValuesBook.put(BooksTable.COLUMN_AUTHOR, book.author());
        contentValuesBook.put(BooksTable.COLUMN_NAME, book.title());
        contentValuesBook.put(BooksTable.COLUMN_READER, book.reader().id());

        ContentValues contentValuesReader = new ContentValues();
        contentValuesReader.put(ReadersTable.COLUMN_ID, book.reader().id());
        contentValuesReader.put(ReadersTable.COLUMN_NAME, book.reader().name());


        final PutResults<Object> putResults = storIOSQLite
                .put()
//                .contentValues(asList(contentValuesBook, contentValuesReader))
//                        .withPutResolver(contentValuesBook)
                .objects(asList(book, book.reader()))
                .prepare() // BTW: it will use transaction!
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<String>(2);

        affectedTables.add(BooksTable.TABLE);
        affectedTables.add(ReadersTable.TABLE);

        // Actually, it's not very clear what PutResult should we return here…
        // Because there is no table for this pair of tweet and user
        // So, let's just return Update Result
        return PutResult.newUpdateResult(putResults.numberOfUpdates(), affectedTables);
    }


}
