package com.hilifecare.di.modules;

import com.hilifecare.ui.myrecord.HeartRateActivity;
import com.hilifecare.ui.myrecord.MyRecordActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class HeartRateModule extends ActivityModule {

    public HeartRateModule(HeartRateActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
