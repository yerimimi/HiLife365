package com.hilifecare.environment;

import android.app.Application;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.hilifecare.BuildConfig;
import com.hilifecare.di.ForApplication;
import com.hilifecare.util.logging.CrashReportingTree;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class EnvironmentConfiguration {

    @Inject
    @ForApplication
    Application app;

    @Inject
    public EnvironmentConfiguration() {
    }

    public void configure() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        Schedulers.io().createWorker().schedule(() -> Stetho.initializeWithDefaults(app));
        if (BuildConfig.DEBUG) {
           Timber.plant(new Timber.DebugTree());
        } else {
           Timber.plant(new CrashReportingTree());
        }
    }

}
