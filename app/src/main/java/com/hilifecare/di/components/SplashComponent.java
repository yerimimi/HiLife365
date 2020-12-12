package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.SplashModule;
import com.hilifecare.ui.splash.SplashActivity;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {SplashModule.class})
public interface SplashComponent {

    void inject(SplashActivity activity);

    // android-hipster-needle-component-injection-method

}
