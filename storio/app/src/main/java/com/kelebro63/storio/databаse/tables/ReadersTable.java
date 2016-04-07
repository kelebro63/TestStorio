package com.kelebro63.storio.databаse.tables;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by Bistrov Alexey on 24.03.2016.
 */
public class ReadersTable {
    @NonNull
    public static final String TABLE = "readers";

    @NonNull
    public static final String COLUMN_ID = "_id";

    @NonNull
    public static final String COLUMN_NAME = "name";

    @NonNull
    public static final String COLUMN_BOOK_ID = "book_id";

    // Yep, with StorIO you can safely store queries as objects and reuse them, they are immutable
    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    // This is just class with Meta Data, we don't need instances
    private ReadersTable() {
        throw new IllegalStateException("No instances please");
    }

    // Better than static final field -> allows VM to unload useless String
    // Because you need this string only once per application life on the device
    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_BOOK_ID + " INTEGER , "
                + COLUMN_NAME + " TEXT NOT NULL"
                + ");";
    }
}
