package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.DeviceControlModule;
import com.hilifecare.ui.bluetooth.DeviceControlActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {DeviceControlModule.class})
public interface DeviceControlComponent {

    void inject(DeviceControlActivity activity);

    // android-hipster-needle-component-injection-method

}
