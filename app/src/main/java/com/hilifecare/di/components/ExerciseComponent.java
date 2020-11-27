package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.ExerciseModule;
import com.hilifecare.ui.exercise.ExerciseActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {ExerciseModule.class})
public interface ExerciseComponent {

    void inject(ExerciseActivity activity);

    // android-hipster-needle-component-injection-method

}
