package com.hilifecare.di.modules;

import com.hilifecare.ui.exercise.ExerciseActivity;
import com.hilifecare.ui.preview.PreviewProgramActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class PreviewProgramModule extends ActivityModule {

    public PreviewProgramModule(PreviewProgramActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
