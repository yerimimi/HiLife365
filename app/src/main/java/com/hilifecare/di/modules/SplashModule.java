package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.ui.splash.SplashActivity;

// android-hipster-needle-module-provides-import

@Module
public class SplashModule extends ActivityModule {

    public SplashModule(SplashActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
