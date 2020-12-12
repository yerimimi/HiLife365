package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.MyPageModule;
import com.hilifecare.di.modules.MyRecordModule;
import com.hilifecare.ui.mypage.MyPageActivity;
import com.hilifecare.ui.myrecord.CalorieActivity;
import com.hilifecare.ui.myrecord.HeartRateActivity;
import com.hilifecare.ui.myrecord.MyRecordActivity;
import com.hilifecare.ui.myrecord.StepCountActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {MyRecordModule.class})
public interface MyRecordComponent {

    void inject(MyRecordActivity activity);
    void inject(CalorieActivity activity);
    void inject(HeartRateActivity activity);
    void inject(StepCountActivity activity);

    // android-hipster-needle-component-injection-method

}
