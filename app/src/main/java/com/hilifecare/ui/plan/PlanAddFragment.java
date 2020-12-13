package com.hilifecare.ui.plan;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.github.mikephil.charting.highlight.Highlight;
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
import com.hyoil.hipreslogic.filter.HiExerciseFilterInput2;
import com.hyoil.hipreslogic.info.HiExerciseBodyPart;
import com.hyoil.hipreslogic.info.HiExerciseInfo;
import com.hyoil.hipreslogic.info.HiLevelTestInfo;
import com.hyoil.hipreslogic.info.HiUserInfo;
import com.hyoil.hipreslogic.intensity.HiExerciseIntensityAdjuster;
import com.hyoil.hipreslogic.suggester.HiExerciseSuggester;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

@ActivityScope
public class PlanAddFragment extends BaseFragment<PlanPresenter> {

    HiExerciseFilterInput hiExerciseFilterInput = new HiExerciseFilterInput();
    HiExerciseFilter hiExerciseFilter = new HiExerciseFilter();
    HiExerciseInfo hiExerciseInfo = new HiExerciseInfo();
    HiExerciseIntensityAdjuster hiExerciseIntensityAdjuster = new HiExerciseIntensityAdjuster();
    HiPresLogicConstants hiPresLogicConstants = new HiPresLogicConstants();
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

    public PlanAddFragment() {
    }

    @OnClick({R.id.plan_add_button})
    void planAdd() {
        sleeping_time_edittext.getText().toString();
        hiExerciseFilterInput.placeOutdoor = exercise_place_outside_checkbox.isChecked();
        hiExerciseFilterInput.placeGym = exercise_place_gym_checkbox.isChecked();
        hiExerciseFilterInput.placeIndoor = exercise_place_inside_checkbox.isChecked();
        hiExerciseFilterInput.placeHome = exercise_place_home_checkbox.isChecked();
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
    public void onPause() {
        ScreenStopwatch.getInstance().reset();
        super.onPause();
    }
}
