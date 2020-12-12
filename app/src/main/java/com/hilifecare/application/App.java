package com.hilifecare.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.github.johnkil.print.PrintConfig;
import com.hilifecare.R;
import com.hilifecare.di.ForApplication;
import com.hilifecare.di.components.ApplicationComponent;
import com.hilifecare.environment.EnvironmentConfiguration;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;
import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {

    private ApplicationComponent graph;

    private RefWatcher refWatcher;

    @ForApplication
    @Inject
    Context context;

    @Inject
    EnvironmentConfiguration environmentConfiguration;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

//        LeakCanary.install(this);
//        refWatcher = LeakCanary.install(this);

        
        AndroidThreeTen.init(this); 
        PrintConfig.initDefault(getAssets(), "fonts/MaterialIcons-Regular.ttf");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/NotoSansKR-Regular-Hestia.otf").setFontAttrId(R.attr.fontPath).build());

        graph = createComponent();

        environmentConfiguration.configure();

    }

    public RefWatcher getRefWatcher() {
        if (refWatcher == null) {
            refWatcher = LeakCanary.install(this);
        }
        return refWatcher;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (graph == null) {
            createComponent();
        }
        return graph;
    }

    private ApplicationComponent createComponent() {
        graph = ApplicationComponent.Initializer.init(this);
        graph.inject(this);
        return graph;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    public void recreateComponents() {
        graph = ApplicationComponent.Initializer.init(this);
        graph.inject(this);
        environmentConfiguration.configure();
    }


}
