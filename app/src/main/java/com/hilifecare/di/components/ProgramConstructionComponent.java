package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.ProgramConstructionModule;
import com.hilifecare.ui.program.ProgramConstructionActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {ProgramConstructionModule.class})
public interface ProgramConstructionComponent {

    void inject(ProgramConstructionActivity activity);

    // android-hipster-needle-component-injection-method

}
