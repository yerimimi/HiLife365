package com.hilifecare.di.modules;

import com.hilifecare.ui.myrecord.MyRecordDetailActivity;
import com.hilifecare.ui.myrecord.StepCountActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class MyRecordDetailModule extends ActivityModule {

    public MyRecordDetailModule(MyRecordDetailActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
