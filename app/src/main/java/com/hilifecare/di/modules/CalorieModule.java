package com.hilifecare.di.modules;

import com.hilifecare.ui.myrecord.CalorieActivity;
import com.hilifecare.ui.myrecord.MyRecordActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class CalorieModule extends ActivityModule {

    public CalorieModule(CalorieActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
