package com.hilifecare.ui.myrecord;

import com.github.mikephil.charting.data.BarEntry;
import com.hilifecare.R;
import com.hilifecare.application.Constants;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class CaloriePresenter extends BasePresenter<MyRecordView> {
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    HashMap<String, String> exerciseFirebaseIdMap = new HashMap<String, String>();

    @Inject
    public CaloriePresenter() {

    }

    public void insertLevelTestResult() {

        fGet("user_level_test_history", null, Constants.INSERT_LEVEL_TEST_RESULT);
    }

    public void fGet(String databaseName, String uuid, int state) {
        switch (state) {
//            case Constants.INSERT_LEVEL_TEST_RESULT:
//                restartableLatestCache(0, () ->
//                                firebaseInteractor.fGet(databaseName, uuid)
//                                        .subscribeOn(Schedulers.newThread())
//                                        .observeOn(AndroidSchedulers.mainThread()),
//                        MyRecordView::insertLevelTestResult);
//                start(0);
//                break;
        }
    }

    public void setData(CalorieActivity activity) {
        float start = 1f;
        int count = 6;
        int range = 4000;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, val, activity.getResources().getDrawable(R.drawable.bullet)));
            } else {
                yVals1.add(new BarEntry(i, val));
            }
        }
        activity.setChart(yVals1);
    }
}
