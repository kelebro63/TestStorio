package com.kelebro63.storio.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kelebro63.storio.App;
import com.kelebro63.storio.R;
import com.kelebro63.storio.databаse.Entities.Book;
import com.kelebro63.storio.databаse.Entities.Reader;
import com.kelebro63.storio.databаse.tables.BooksTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class MainActivity extends AppCompatActivity {

    @Inject
    StorIOSQLite storIOSQLite;

    @Bind(R.id.button)
    Button btnAddBooks;
    @Bind(R.id.button2)
    Button btnClearBooks;
    @Bind(R.id.tweets_recycler_view)
    RecyclerView recyclerView;

    private BooksAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
        reloadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.get(this).appComponent().inject(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        adapter = new BooksAdapter();
        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button)
    public void click() {
        Toast.makeText(MainActivity.this, "click", Toast.LENGTH_LONG).show();
        final List<Book> books = new ArrayList<Book>();

        Reader reader = Reader.newReader("test Reader");

        books.add(Book.newBook("artem_zin", "Checkout StorIO — modern API for SQLiteDatabase & ContentResolver", reader));
        books.add(Book.newBook("HackerNews", "It's revolution! Dolphins can write news on HackerNews with our new app!", reader));
        books.add(Book.newBook("AndroidDevReddit", "Awesome library — StorIO", reader));
        books.add(Book.newBook("Facebook", "Facebook community in Twitter is more popular than Facebook community in Facebook and Instagram!", reader));
        books.add(Book.newBook("Google", "Android be together not the same: AOSP, AOSP + Google Apps, Samsung Android", reader));
        books.add(Book.newBook("Reddit", "Now we can send funny gifs directly into your brain via Oculus Rift app!", reader));
        books.add(Book.newBook("ElonMusk", "Tesla Model S OTA update with Android Auto 5.2, fixes for memory leaks", reader));
        books.add(Book.newBook("AndroidWeekly", "Special issue #1: StorIO — forget about SQLiteDatabase, ContentResolver APIs, ORMs suck!", reader));
        books.add(Book.newBook("Apple", "Yosemite update: fixes for Wifi issues, yosemite-wifi-patch#142", reader));

        // Looks/reads nice, isn't it?
        storIOSQLite
                .put()
                .objects(books)
                .prepare()
                .asRxObservable()
                .observeOn(mainThread()) // Remember, all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(new Observer<PutResults<Book>>() {
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "ошибка добавления данных", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PutResults<Book> putResults) {
                        // After successful Put Operation our subscriber in reloadData() will receive update!
                    }

                    @Override
                    public void onCompleted() {
                        // no impl required
                    }
                });
    }

    @OnClick(R.id.button2)
    public void clear() {
        storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table("books")
                        .build())
                .prepare()
                .executeAsBlocking();

    }


    private void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    void reloadData() {
        final Subscription subscription = storIOSQLite
                .get()
                .listOfObjects(Book.class)
                .withQuery(BooksTable.QUERY_ALL)
                .prepare()
                .asRxObservable() // it will be subscribed to changes in tweets table!
                .delay(1, SECONDS) // for better User Experience :) Actually, StorIO is so fast that we need to delay emissions (it's a joke, or not)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Book>>() {
                    @Override
                    public void call(List<Book> books) {
                        // Remember: subscriber will automatically receive updates
                        // Of tables from Query (tweets table in our case)
                        // This makes your code really Reactive and nice!

                        // We guarantee, that list of objects will never be null (also we use @NonNull/@Nullable)
                        // So you just need to check if it's empty or not
                        if (books.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.setBooks(books);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // In cases when you are not sure that query will be successful
                        // You can prevent crash of the application via error handler

                    }
                });
    }
}
