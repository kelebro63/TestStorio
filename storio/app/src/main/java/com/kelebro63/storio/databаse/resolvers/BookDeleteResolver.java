package com.kelebro63.storio.databаse.resolvers;

import android.support.annotation.NonNull;

import com.kelebro63.storio.databаse.Entities.Book;
import com.kelebro63.storio.databаse.tables.BooksTable;
import com.kelebro63.storio.databаse.tables.ReadersTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Generated resolver for Delete Operation
 */
public class BookDeleteResolver extends DeleteResolver<Book> {
    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull Book book) {
        storIOSQLite
                .delete()
                .objects(asList(book, book.reader()))
                .prepare() // BTW: it will use transaction!
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<String>(2);

        affectedTables.add(BooksTable.TABLE);
        affectedTables.add(ReadersTable.TABLE);

        return DeleteResult.newInstance(2, affectedTables);
    }
}
