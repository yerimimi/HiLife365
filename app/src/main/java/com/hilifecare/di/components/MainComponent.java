package com.hilifecare.di.components;

import dagger.Component;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.modules.MainModule;
import com.hilifecare.ui.exercise.IndividualExerciseFragment;
import com.hilifecare.ui.main.MainActivity;
import com.hilifecare.ui.main.MainFragment;
import com.hilifecare.ui.plan.PlanFragment;

@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity activity);
    void inject(MainFragment fragment);
    void inject(PlanFragment fragment);
    void inject(IndividualExerciseFragment fragment);

}
