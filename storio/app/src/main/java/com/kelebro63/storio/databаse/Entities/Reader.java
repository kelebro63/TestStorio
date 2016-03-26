package com.kelebro63.storio.databаse.Entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kelebro63.storio.databаse.tables.ReadersTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Bistrov Alexey on 24.03.2016.
 */
@StorIOSQLiteType(table = ReadersTable.TABLE)
public class Reader {
    /**
     * If object was not inserted into db, id will be null
     */
    @Nullable
    @StorIOSQLiteColumn(name = ReadersTable.COLUMN_ID, key = true)
    Long id;;

    @NonNull
    @StorIOSQLiteColumn(name = ReadersTable.COLUMN_NAME)
    String name;

    // leave default constructor for AutoGenerated code!
    Reader() {
    }

    private Reader(@Nullable Long id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public static Reader newReader(@Nullable Long id, @NonNull String name) {
        return new Reader(id, name);
    }

    @NonNull
    public static Reader newReader(@NonNull String name) {
        return new Reader(null, name);
    }

    @Nullable
    public Long id() {
        return id;
    }

    @NonNull
    public String name() {
        return name;
    }

    // BTW, you can use AutoValue/AutoParcel to get immutability and code generation for free
    // Check our tests, we have examples!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader book = (Reader) o;

        if (id != null ? !id.equals(book.id) : book.id != null) return false;
        return name.equals(book.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
