package com.hilifecare.di.modules;

import com.hilifecare.ui.plan.PlanDetailActivity;

import dagger.Module;

// android-hipster-needle-module-provides-import

@Module
public class PlanDetailModule extends ActivityModule {

    public PlanDetailModule(PlanDetailActivity activity) {
        super(activity);
    }

    // android-hipster-needle-module-provides-method

}