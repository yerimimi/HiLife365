package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.ui.leveltest.LevelTestActivity;
import com.hilifecare.ui.leveltest.LevelTestListFragment;
import com.hilifecare.ui.leveltest.LevelTestPlayingActivity;

// android-hipster-needle-module-provides-import

@Module
public class LevelTestModule extends ActivityModule {

    public LevelTestModule(LevelTestActivity activity) {
        super(activity);
    }

    @ActivityScope
    @Provides
    LevelTestListFragment provideLevelTestListFragment() {
        return new LevelTestListFragment();
    }

    @ActivityScope
    @Provides
    LevelTestPlayingActivity provideLevelTestPlayingFragment() {
        return new LevelTestPlayingActivity();
    }
    // android-hipster-needle-module-provides-method

}
