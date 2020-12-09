package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.ui.leveltest.LevelTestResultActivity;

// android-hipster-needle-module-provides-import

@Module
public class LevelTestResultModule extends ActivityModule {

    public LevelTestResultModule(LevelTestResultActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
