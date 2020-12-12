package com.hilifecare.ui.myrecord;

import com.github.mikephil.charting.data.Entry;
import com.hilifecare.R;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class StepCountPresenter extends BasePresenter<MyRecordView> {
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    HashMap<String, String> exerciseFirebaseIdMap = new HashMap<String, String>();

    ArrayList<Entry> values = new ArrayList<Entry>();
    String pre_time = null;
    double total_distance = 0;
    double total_calories = 0;
    double total_step = 0;
    int i = 0;

    @Inject
    public StepCountPresenter() {

    }

    public void setData(StepCountActivity activity, double distance_value, double calories_value, double step_value) {
        String time = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
//        int count = 24;
//        int range = 150;
//        ArrayList<Entry> values = new ArrayList<Entry>();
//
//        for (int i = 0; i < count; i+=3) {
//            float val = (float) (Math.random() * range) + 3;
//            values.add(new Entry(i, val, activity.getResources().getDrawable(R.drawable.bullet)));
//        }

        total_distance += distance_value;
        total_calories += calories_value;
        total_step += step_value;

        if(!time.equals(pre_time)) {
            values.add(new Entry(i++, (int)total_step, activity.getResources().getDrawable(R.drawable.bullet)));
            activity.setChart(values);
            activity.setDistanceValue(String.valueOf(total_distance));
            activity.setKcalValue(String.valueOf(total_calories));
            activity.setStepValue(String.valueOf(total_step));

            pre_time = time;

            total_step = 0;
        }
    }

    public void insertStepCount(String time, int hr_value, int spo2_value, int speed_value) {
        unsubscribe();
        firebaseInteractor.insertSmartBandData("hr", time, hr_value);
        firebaseInteractor.insertSmartBandData("spo2", time, spo2_value);
        firebaseInteractor.insertSmartBandData("speed", time, speed_value);
    }
}
