package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.LevelTestResultModule;
import com.hilifecare.ui.leveltest.LevelTestResultActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {LevelTestResultModule.class})
public interface LevelTestResultComponent {

    void inject(LevelTestResultActivity activity);

    // android-hipster-needle-component-injection-method

}
