package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.LoginModule;
import com.hilifecare.ui.login.LoginActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {LoginModule.class})
public interface LoginComponent {

    void inject(LoginActivity activity);

    // android-hipster-needle-component-injection-method

}
