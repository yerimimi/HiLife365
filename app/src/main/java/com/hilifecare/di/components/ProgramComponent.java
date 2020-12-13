package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.ProgramModule;
import com.hilifecare.ui.program.ProgramActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {ProgramModule.class})
public interface ProgramComponent {

    void inject(ProgramActivity activity);

    // android-hipster-needle-component-injection-method

}
