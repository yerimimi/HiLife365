package com.hilifecare.di.modules;

import dagger.Module;
import dagger.Provides;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.ui.base.BaseActivity;

@ActivityScope
@Module
public class ActivityModule {

    protected BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    protected BaseActivity activity() {
        return activity;
    }
}
