package com.hilifecare.ui.exercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilifecare.R;
import com.hilifecare.util.logging.ScreenStopwatch;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-02-20.
 */

public class ExerciseProgramFragment extends Fragment{

    public ExerciseProgramFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_exercise, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

//    @OnClick(R.id.myrecord_heart_rate)
//    void goHeartRate(){
//
//    }
//
//    @OnClick(R.id.myrecord_step_count)
//    void goStepCount() {}
//
//    @OnClick(R.id.myrecord_calorie_consumption)
//    void goCalorieConsumption(){
//
//    }
//
//    @OnClick(R.id.myrecord_level_test_result)
//    void goLevelTestResult() {}

    @Override
    public void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("ExerciseProgramFragment");
        super.onStart();
    }

    @Override
    public void onPause(){
        ScreenStopwatch.getInstance().reset();
        super.onPause();
    }

}
