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
import com.kelebro63.storio.databаse.tables.ReadersTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
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

        books.add(Book.newBook((long) 1, "author_1", "title_1", reader));
        books.add(Book.newBook((long) 2, "author_2", "title_2", reader));
        books.add(Book.newBook((long) 3, "author_3", "title_3", reader));


        // Looks/reads nice, isn't i t?
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
                        reloadData();
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    @OnClick(R.id.button2)
    public void clear() {
        storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(BooksTable.TABLE).table(ReadersTable.TABLE)
                        .build())
                .prepare()
                .asRxObservable() // it will be subscribed to changes in tweets table!
                .delay(1, SECONDS) // for better User Experience :) Actually, StorIO is so fast that we need to delay emissions (it's a joke, or not)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteResult>() {
                    @Override
                    public void onCompleted() {
                        reloadData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DeleteResult deleteResult) {

                    }
                });

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
