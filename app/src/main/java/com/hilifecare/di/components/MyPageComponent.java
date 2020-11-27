package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.MyPageModule;
import com.hilifecare.ui.mypage.MyPageActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {MyPageModule.class})
public interface MyPageComponent {

    void inject(MyPageActivity activity);

    // android-hipster-needle-component-injection-method

}
