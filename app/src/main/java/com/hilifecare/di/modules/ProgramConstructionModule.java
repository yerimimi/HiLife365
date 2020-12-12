package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.ui.program.ProgramConstructionActivity;

// android-hipster-needle-module-provides-import

@Module
public class ProgramConstructionModule extends ActivityModule {

    public ProgramConstructionModule(ProgramConstructionActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
