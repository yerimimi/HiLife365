package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.UserDetailInfoModule;
import com.hilifecare.ui.userdetailinfo.UserDetailInfoActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {UserDetailInfoModule.class})
public interface UserDetailInfoComponent {

    void inject(UserDetailInfoActivity activity);

    // android-hipster-needle-component-injection-method

}
