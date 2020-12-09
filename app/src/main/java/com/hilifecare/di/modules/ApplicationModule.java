package com.hilifecare.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.hilifecare.application.App;
import com.hilifecare.di.ForApplication;
import com.hilifecare.domain.executors.JobExecutor;
import com.hilifecare.domain.executors.ThreadExecutor;
import com.hilifecare.domain.interactors.firebase.FakeFirebaseInteractorImpl;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractorImpl;
import com.hilifecare.domain.repository.firebase.FirebaseLocalRepository;
import com.hilifecare.domain.repository.firebase.FirebaseRemoteRepository;
import com.hilifecare.domain.repository.firebase.FirebaseRepository;
import com.hilifecare.domain.repository.firebase.FirebaseRepositoryImpl;
import com.hilifecare.storage.Storage;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
// android-hipster-needle-module-provides-import

@Module
public class ApplicationModule {

    public static final String MAIN_THREAD_HANDLER = "main_thread_handler";

    protected App application;

    public ApplicationModule(App application) {
        this.application = application;
    }

    @ForApplication
    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @ForApplication
    @Provides
    @Singleton
    public App provideApp() {
        return application;
    }

    @ForApplication
    @Provides
    @Singleton
    public Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    @Singleton
    Storage provideStorage(Gson gson, SharedPreferences preferences) {
        return new Storage(preferences, gson);
    }

    @Provides
    @Singleton
    EventBus provideBus() {
        return EventBus.getDefault();
    }

    @Provides @NonNull @Named(MAIN_THREAD_HANDLER) @Singleton
    public Handler provideMainThreadHandler() {
        return new Handler(Looper.getMainLooper());
    }




    @Provides
    @Named("real")
    @Singleton
    FirebaseInteractor provideFirebaseInteractor(FirebaseRepository firebaseRepository , ThreadExecutor executor) {
        return new FirebaseInteractorImpl(firebaseRepository,executor);
    }

    @Provides
    @Named("fake")
    @Singleton
    FirebaseInteractor provideFakeFirebaseInteractor() {
        return new FakeFirebaseInteractorImpl();
    }

    @Provides
    @Singleton
    FirebaseRepository provideFirebaseRepository(Retrofit retrofit) {
        return new FirebaseRepositoryImpl(new FirebaseRemoteRepository(retrofit), new FirebaseLocalRepository());
    }
    // android-hipster-needle-module-provides-method

}
