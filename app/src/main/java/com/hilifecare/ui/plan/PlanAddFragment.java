package com.hilifecare.ui.plan;

import android.support.v4.app.Fragment;

import com.hilifecare.R;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.components.MainComponent;
import com.hilifecare.ui.base.BaseFragment;
import com.hilifecare.ui.main.MainActivity;
import com.hilifecare.util.logging.ScreenStopwatch;
import javax.inject.Inject;

@ActivityScope
public class PlanAddFragment extends BaseFragment<PlanPresenter>{

    @Inject PlanPresenter planPresenter;

    public PlanAddFragment(){
    }
    protected int getLayoutResource() {
        return R.layout.fragment_plan_add;
    }


    @Override
    public void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("PlanAddFragment");
        super.onStart();
    }

    @Override
    public void onPause(){
        ScreenStopwatch.getInstance().reset();
        super.onPause();
    }

    @Override
    protected void inject() {
        getComponent(MainComponent.class).inject(this);
    };
}
