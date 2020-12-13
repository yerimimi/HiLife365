package com.hilifecare.di.modules;

import com.hilifecare.ui.myrecord.MyRecordActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class MyRecordModule extends ActivityModule {

    public MyRecordModule(MyRecordActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
