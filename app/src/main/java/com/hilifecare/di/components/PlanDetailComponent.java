package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.PlanDetailModule;
import com.hilifecare.ui.plan.PlanDetailActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {PlanDetailModule.class})
public interface PlanDetailComponent {

    void inject(PlanDetailActivity activity);

    // android-hipster-needle-component-injection-method

}
