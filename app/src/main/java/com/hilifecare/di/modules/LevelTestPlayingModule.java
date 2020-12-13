package com.hilifecare.di.modules;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.ui.leveltest.LevelTestActivity;
import com.hilifecare.ui.leveltest.LevelTestListFragment;
import com.hilifecare.ui.leveltest.LevelTestPlayingActivity;

import dagger.Module;
import dagger.Provides;

// android-hipster-needle-module-provides-import

@Module
public class LevelTestPlayingModule extends ActivityModule {

    public LevelTestPlayingModule(LevelTestPlayingActivity activity) {
        super(activity);
    }

}
