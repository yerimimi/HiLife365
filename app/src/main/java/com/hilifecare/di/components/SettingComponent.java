package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.MyPageModule;
import com.hilifecare.di.modules.SettingModule;
import com.hilifecare.ui.mypage.MyPageActivity;
import com.hilifecare.ui.setting.SettingActivity;
import com.hilifecare.ui.setting.SettingPresenter;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {SettingModule.class})
public interface SettingComponent {

    void inject(SettingActivity activity);

    // android-hipster-needle-component-injection-method

}
