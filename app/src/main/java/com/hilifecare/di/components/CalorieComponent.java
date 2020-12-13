package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.CalorieModule;
import com.hilifecare.di.modules.MyRecordModule;
import com.hilifecare.ui.myrecord.CalorieActivity;
import com.hilifecare.ui.myrecord.HeartRateActivity;
import com.hilifecare.ui.myrecord.MyRecordActivity;
import com.hilifecare.ui.myrecord.StepCountActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {CalorieModule.class})
public interface CalorieComponent {

    void inject(CalorieActivity activity);
    // android-hipster-needle-component-injection-method

}
