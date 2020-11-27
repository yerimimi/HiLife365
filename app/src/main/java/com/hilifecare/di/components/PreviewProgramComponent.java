package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.ExerciseModule;
import com.hilifecare.di.modules.PreviewProgramModule;
import com.hilifecare.ui.exercise.ExerciseActivity;
import com.hilifecare.ui.preview.PreviewProgramActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {PreviewProgramModule.class})
public interface PreviewProgramComponent {

    void inject(PreviewProgramActivity activity);

    // android-hipster-needle-component-injection-method

}
