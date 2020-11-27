package com.hilifecare.di.components;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.MyRecordDetailModule;
import com.hilifecare.di.modules.StepCountModule;
import com.hilifecare.ui.myrecord.MyRecordDetailActivity;
import com.hilifecare.ui.myrecord.StepCountActivity;

import dagger.Component;

// android-hipster-needle-component-injection-import


@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {MyRecordDetailModule.class})
public interface MyRecordDetailComponent {

    void inject(MyRecordDetailActivity activity);

    // android-hipster-needle-component-injection-method

}
