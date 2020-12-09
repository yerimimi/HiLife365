package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.LevelTestModule;
import com.hilifecare.ui.leveltest.LevelTestActivity;
import com.hilifecare.ui.leveltest.LevelTestListFragment;
import com.hilifecare.ui.leveltest.LevelTestPlayingActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {LevelTestModule.class})
public interface LevelTestComponent {

    void inject(LevelTestActivity activity);
    void inject(LevelTestListFragment fragment);

    // android-hipster-needle-component-injection-method

}
