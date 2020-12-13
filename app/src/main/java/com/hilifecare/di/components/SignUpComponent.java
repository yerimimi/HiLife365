package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.SignUpModule;
import com.hilifecare.ui.signup.SignUpActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {SignUpModule.class})
public interface SignUpComponent {

    void inject(SignUpActivity activity);

    // android-hipster-needle-component-injection-method

}
