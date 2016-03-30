package com.kelebro63.storio.databаse.resolvers;

import android.support.annotation.NonNull;

import com.kelebro63.storio.databаse.Entities.Book;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import java.lang.Override;

/**
 * Generated resolver for Delete Operation
 */
public class BookDeleteResolver extends DefaultDeleteResolver<Book> {
    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    protected DeleteQuery mapToDeleteQuery(@NonNull Book object) {
        return DeleteQuery.builder()
                .table("books")
                .where("_id = ?")
                .whereArgs(object.id())
                .build();
    }
}
