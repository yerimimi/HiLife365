package com.hilifecare.ui.myrecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilifecare.R;
import com.hilifecare.ui.emg.EmgActivity;
import com.hilifecare.util.logging.ScreenStopwatch;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017-02-20.
 */

public class MyRecordFragment extends Fragment{

    public MyRecordFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_record, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.myrecord_heart_rate)
    void goHeartRate(){
        startActivity(new Intent(getContext(), HeartRateActivity.class));
    }

//    @OnClick(R.id.myrecord_step_count)
//    void goStepCount()  {
//        startActivity(new Intent(getContext(), StepCountActivity.class));
//    }
//
//    @OnClick(R.id.myrecord_calorie_consumption)
//    void goCalorieConsumption(){
//        startActivity(new Intent(getContext(), CalorieActivity.class));
//    }
//
    @OnClick(R.id.myrecord_level_test_result)
    void goLevelTestResult() {
        startActivity(new Intent(getContext(), MyRecordActivity.class));
    }

    @OnClick({R.id.myrecord_emg})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myrecord_emg:
            Intent j = new Intent(getContext(), EmgActivity.class);
            startActivity(j);
            break;
        }
    }


    @Override
    public void onResume(){
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());
    }

}
