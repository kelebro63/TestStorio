package com.kelebro63.storio;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Bistrov Alexey on 24.03.2016.
 */
public class App extends Application {

    @Nullable
    private volatile AppComponent appComponent;

    @NonNull
    public static App get(@NonNull Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @NonNull
    public AppComponent appComponent() {
        if (appComponent == null) {
            synchronized (App.class) {
                if (appComponent == null) {
                    appComponent = createAppComponent();
                }
            }
        }

        //noinspection ConstantConditions
        return appComponent;
    }
    @NonNull
    private AppComponent createAppComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .build();
    }

}
