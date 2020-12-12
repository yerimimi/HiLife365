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
public class HeartRatePresenter extends BasePresenter<MyRecordView> {
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    private static final int MAX_VALUE = 150;
    int min = MAX_VALUE;
    int max = 0;
    int avg;
    ArrayList<Entry> hr_values = new ArrayList<Entry>();
    ArrayList<Entry> spo2_values = new ArrayList<Entry>();
    int sum = 0;
    String pre_time = null;

    HashMap<String, String> exerciseFirebaseIdMap = new HashMap<String, String>();

    int e1 = 499;
    int max_Evalue = 300;
    int i = 0;

    @Inject
    public HeartRatePresenter() {
//        for(int i=0; i<max_Evalue; i++) {
//            hr_values.add(new Entry(i,0));
//            spo2_values.add(new Entry(i,0));
//        }
    }

    public void setData(HeartRateActivity activity, String hr, String spo2) {
        int int_spo2;
        String time = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));

//        if(e1 > 0 && e1 < max_Evalue) {
//            hr_values.add(new Entry(e1, Integer.valueOf(hr), activity.getResources().getDrawable(R.drawable.bullet)));
//            spo2_values.add(new Entry(e1--, Integer.valueOf(spo2), activity.getResources().getDrawable(R.drawable.bullet)));
//        } else if(e1 == 0)
//            e1 = max_Evalue;
//        else {
//            hr_values.add(new Entry(e1, Integer.valueOf(hr), activity.getResources().getDrawable(R.drawable.bullet)));
//            spo2_values.add(new Entry(e1++, Integer.valueOf(spo2), activity.getResources().getDrawable(R.drawable.bullet)));
//        }
        int_spo2 = Integer.valueOf(spo2);
        if(int_spo2 <= 90)
            int_spo2 = 98;
        hr_values.add(new Entry(i, Integer.valueOf(hr), activity.getResources().getDrawable(R.drawable.bullet)));
        spo2_values.add(new Entry(i++, int_spo2, activity.getResources().getDrawable(R.drawable.bullet)));

        if (Integer.valueOf(hr) < min) min = Integer.valueOf(hr);
        if (Integer.valueOf(hr) > max) max = Integer.valueOf(hr);
        activity.setChart(hr_values, spo2_values, max_Evalue);
        activity.setPointValues(min, max, int_spo2);
        pre_time = time;
    }

    public void insertHeartRate(String time, int hr_value, int spo2_value, String distance_value, String calories_value, String step_value) {
        unsubscribe();
        firebaseInteractor.insertSmartBandData("hr", time, hr_value);
        firebaseInteractor.insertSmartBandData("spo2", time, spo2_value);
        firebaseInteractor.insertSmartBandData("distance", time, distance_value);
        firebaseInteractor.insertSmartBandData("calories", time, calories_value);
        firebaseInteractor.insertSmartBandData("step", time, step_value);
    }
}
