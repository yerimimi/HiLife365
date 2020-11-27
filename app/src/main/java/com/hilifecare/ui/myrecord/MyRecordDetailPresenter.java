package com.hilifecare.ui.myrecord;

import com.hilifecare.application.Constants;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class MyRecordDetailPresenter extends BasePresenter<MyRecordView> {
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    HashMap<String, String> exerciseFirebaseIdMap = new HashMap<String, String>();

    @Inject
    public MyRecordDetailPresenter() {

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
}
