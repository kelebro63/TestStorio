package com.kelebro63.storio.databаse.Entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kelebro63.storio.databаse.tables.BooksTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Bistrov Alexey on 24.03.2016.
 */
@StorIOSQLiteType(table = BooksTable.TABLE)
public class Book {
    /**
     * If object was not inserted into db, id will be null
     */
    @Nullable
    @StorIOSQLiteColumn(name = BooksTable.COLUMN_ID, key = true)
    Long id;

    @NonNull
    @StorIOSQLiteColumn(name = BooksTable.COLUMN_AUTHOR)
    String author;

    @NonNull
    @StorIOSQLiteColumn(name = BooksTable.COLUMN_TITLE)
    String title;

//    @NonNull
//    @StorIOSQLiteColumn(name = BooksTable.COLUMN_TITLE)
    Reader reader;

    // leave default constructor for AutoGenerated code!
    Book() {
    }

    private Book(@Nullable Long id, @NonNull String author, @NonNull String title, @NonNull  Reader reader) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.reader = reader;
    }

    @NonNull
    public static Book newBook(@Nullable Long id, @NonNull String author, @NonNull String title, @NonNull  Reader reader) {
        return new Book(id, author, title, reader);
    }

    @NonNull
    public static Book newBook(@NonNull String author, @NonNull String title, @NonNull  Reader reader) {
        return new Book(null, author, title, reader);
    }

    @Nullable
    public Long id() {
        return id;
    }

    @NonNull
    public String author() {
        return author;
    }

    @NonNull
    public String title() {
        return title;
    }

    @NonNull
    public Reader reader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    // BTW, you can use AutoValue/AutoParcel to get immutability and code generation for free
    // Check our tests, we have examples!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != null ? !id.equals(book.id) : book.id != null) return false;
        if (!author.equals(book.author)) return false;
        return title.equals(book.title);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + author.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", name='" + title + '\'' +
                ", reader='" + reader() + '\'' +
                '}';
    }
}
