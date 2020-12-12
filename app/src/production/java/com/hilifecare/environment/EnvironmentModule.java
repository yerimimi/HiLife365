package com.hilifecare.environment;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.hilifecare.BuildConfig;
import com.hilifecare.application.App;
import com.hilifecare.di.ForApplication;
import com.hilifecare.network.OkHttpInterceptors;
import com.hilifecare.network.OkHttpNetworkInterceptors;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class EnvironmentModule {

    static final int DISK_CACHE_SIZE = (int) 1_000_000L;

    private final App app;

    public EnvironmentModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(@ForApplication Context app,
                                     @OkHttpInterceptors @NonNull List<Interceptor> interceptors,
                                     @OkHttpNetworkInterceptors @NonNull List<Interceptor> networkInterceptors) {

        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        final OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        for (Interceptor interceptor : interceptors) {
            okHttpBuilder.addInterceptor(interceptor);
        }

        for (Interceptor networkInterceptor : networkInterceptors) {
            okHttpBuilder.addNetworkInterceptor(networkInterceptor);
        }

        okHttpBuilder.cache(cache);
        okHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);

        return okHttpBuilder.build();

    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(@NonNull OkHttpClient okHttpClient, @NonNull Gson gson) {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT_LOCAL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return restAdapter;
    }

    @Provides
    @Singleton
    public MixpanelAPI provideMixpanelApi(@ForApplication Application application) {
    	   MixpanelAPI mixpanel = MixpanelAPI.getInstance(application, "token");
    	   return mixpanel;
    }

}
