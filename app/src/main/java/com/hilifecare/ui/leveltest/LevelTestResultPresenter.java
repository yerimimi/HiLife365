package com.hilifecare.ui.leveltest;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class LevelTestResultPresenter extends BasePresenter<LevelTestResultView> {
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    @Inject
    public LevelTestResultPresenter() {

    }

    public void insertLevelTestResult(HashMap<String, String> userTestResultMap, String time) {
        unsubscribe();
        firebaseInteractor.insetLevelTestResult(userTestResultMap, time);
    }
}