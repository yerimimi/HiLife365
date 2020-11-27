package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.ui.exercise.ExerciseActivity;

// android-hipster-needle-module-provides-import

@Module
public class ExerciseModule extends ActivityModule {

    public ExerciseModule(ExerciseActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
