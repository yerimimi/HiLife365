package com.hilifecare.di.modules;

import com.hilifecare.ui.myrecord.MyRecordActivity;
import com.hilifecare.ui.myrecord.StepCountActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class StepCountModule extends ActivityModule {

    public StepCountModule(StepCountActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
