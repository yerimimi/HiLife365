package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.LevelTestModule;
import com.hilifecare.di.modules.LevelTestPlayingModule;
import com.hilifecare.ui.leveltest.LevelTestActivity;
import com.hilifecare.ui.leveltest.LevelTestListFragment;
import com.hilifecare.ui.leveltest.LevelTestPlayingActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {LevelTestPlayingModule.class})
public interface LevelTestPlayingComponent {

    void inject(LevelTestPlayingActivity fragment);

    // android-hipster-needle-component-injection-method

}
