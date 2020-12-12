package com.hilifecare.di.modules;

import com.hilifecare.ui.mypage.MyPageActivity;
import com.hilifecare.ui.setting.SettingActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class SettingModule extends ActivityModule {

    public SettingModule(SettingActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
