package com.hilifecare.di.modules;

import com.hilifecare.ui.bluetooth.DeviceControlActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class DeviceControlModule extends ActivityModule {

    public DeviceControlModule(DeviceControlActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}
