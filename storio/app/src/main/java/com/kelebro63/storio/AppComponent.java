package com.kelebro63.storio;

import android.support.annotation.NonNull;

import com.kelebro63.storio.databаse.DbModule;
import com.kelebro63.storio.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                DbModule.class
        }
)
public interface AppComponent {
    void inject(@NonNull MainActivity activity);

    //void inject(@NonNull SampleContentProvider sampleContentProvider);
}
