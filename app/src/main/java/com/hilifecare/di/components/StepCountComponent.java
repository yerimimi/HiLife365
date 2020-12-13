package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.MyRecordModule;
import com.hilifecare.di.modules.StepCountModule;
import com.hilifecare.ui.myrecord.CalorieActivity;
import com.hilifecare.ui.myrecord.HeartRateActivity;
import com.hilifecare.ui.myrecord.MyRecordActivity;
import com.hilifecare.ui.myrecord.StepCountActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {StepCountModule.class})
public interface StepCountComponent {

    void inject(StepCountActivity activity);

    // android-hipster-needle-component-injection-method

}
