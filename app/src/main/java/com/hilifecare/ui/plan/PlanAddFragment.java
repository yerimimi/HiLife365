package com.hilifecare.ui.plan;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.hilifecare.R;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.components.MainComponent;
import com.hilifecare.model.Plan;
import com.hilifecare.ui.base.BaseFragment;
import com.hilifecare.ui.main.MainActivity;
import com.hilifecare.util.logging.ScreenStopwatch;
import com.hyoil.hipreslogic.constants.HiPresLogicConstants;
import com.hyoil.hipreslogic.filter.HiExerciseFilter;
import com.hyoil.hipreslogic.filter.HiExerciseFilterInput;
import com.hyoil.hipreslogic.info.HiExerciseInfo;
import com.hyoil.hipreslogic.info.HiUserInfo;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

@ActivityScope
public class PlanAddFragment extends BaseFragment<PlanPresenter>{

    HiExerciseFilterInput hiExerciseFilterInput;
    HiPresLogicConstants hiPresLogicConstants;
    HiExerciseInfo hiExerciseInfo;
    HiUserInfo hiUserInfo = new HiUserInfo();

    @Bind(R.id.user_plan_add_listview)
    ListView user_plan_add_listview;
    @Bind(R.id.plan_add_button)
    Button plan_add_button;
    @Bind(R.id.exercise_place_gym_checkbox)
    CheckBox exercise_place_gym_checkbox;
    @Bind(R.id.exercise_place_home_checkbox)
    CheckBox exercise_place_home_checkbox;
    @Bind(R.id.exercise_place_inside_checkbox)
    CheckBox exercise_place_inside_checkbox;
    @Bind(R.id.exercise_place_outside_checkbox)
    CheckBox exercise_place_outside_checkbox;
    @Bind(R.id.sleeping_time_edittext)
    EditText sleeping_time_edittext;

    public PlanAddFragment(){
    }

    public void insertHiUserInfo(){
        hiUserInfo.getSleepingMinutes();
        if(exercise_place_gym_checkbox.isChecked())
            hiExerciseFilterInput.isPlaceGym();
    }


    @Override
    protected void inject() {
        getComponent(MainComponent.class).inject(this);
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
}
