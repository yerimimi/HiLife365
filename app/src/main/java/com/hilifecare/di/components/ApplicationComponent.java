package com.hilifecare.di.components;

import android.content.Context;

import com.google.gson.Gson;
import com.hilifecare.application.App;
import com.hilifecare.di.ForApplication;
import com.hilifecare.di.modules.AndroidModule;
import com.hilifecare.di.modules.ApplicationModule;
import com.hilifecare.domain.executors.ThreadExecutor;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.domain.repository.firebase.FirebaseRepository;
import com.hilifecare.environment.EnvironmentModule;
import com.hilifecare.network.OkHttpInterceptorsModule;
import com.hilifecare.storage.Storage;
import com.hilifecare.util.gson.GsonModule;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
// android-hipster-needle-component-injection-import

@Singleton
@Component(modules = {
        ApplicationModule.class,
        AndroidModule.class,
        GsonModule.class,
        OkHttpInterceptorsModule.class,
        EnvironmentModule.class}
)
public interface ApplicationComponent {

    ThreadExecutor provideThreadExecutor();

    Storage provideStorage();

    Retrofit provideRetrofit();

    @ForApplication
    Context provideContext();

    Gson provideGson();

    EventBus provideEventBus();

    void inject(App app);

    @Named("real")
    FirebaseInteractor provideFirebaseInteractor();
    @Named("fake")
    FirebaseInteractor provideFakeFirebaseInteractor();
    FirebaseRepository provideFirebaseRepository();
    // android-hipster-needle-component-injection-method

    final class Initializer {
          public static ApplicationComponent init(App app) {
              return DaggerApplicationComponent.builder()
                            .androidModule(new AndroidModule())
                            .gsonModule(new GsonModule())
                            .applicationModule(new ApplicationModule(app))
                            .environmentModule(new EnvironmentModule(app))
                            .okHttpInterceptorsModule(new OkHttpInterceptorsModule())
                            .build();
          }
    }

}
