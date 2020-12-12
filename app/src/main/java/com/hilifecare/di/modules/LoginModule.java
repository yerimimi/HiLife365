package com.hilifecare.di.modules;

import dagger.Module;

import com.hilifecare.ui.login.LoginActivity;

// android-hipster-needle-module-provides-import

@Module
public class LoginModule extends ActivityModule {

    public LoginModule(LoginActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
