package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.ui.signup.SignUpActivity;

// android-hipster-needle-module-provides-import

@Module
public class SignUpModule extends ActivityModule {

    public SignUpModule(SignUpActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
