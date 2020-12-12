package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.ui.mypage.MyPageActivity;

// android-hipster-needle-module-provides-import

@Module
public class MyPageModule extends ActivityModule {

    public MyPageModule(MyPageActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
