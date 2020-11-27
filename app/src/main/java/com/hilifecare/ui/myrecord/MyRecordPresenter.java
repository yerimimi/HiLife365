package com.hilifecare.ui.myrecord;

import com.github.mikephil.charting.data.RadarEntry;
import com.hilifecare.application.Constants;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


@ActivityScope
public class MyRecordPresenter extends BasePresenter<MyRecordView> {
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    HashMap<String, String> exerciseFirebaseIdMap = new HashMap<String, String>();

    @Inject
    public MyRecordPresenter() {

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

    public void setData(MyRecordActivity activity) {
        float mult = 80;
        float min = 20;
        int cnt = 8;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mult) + min;
            entries1.add(new RadarEntry(val1));
        }
        activity.setChart(entries1);
    }
}
