package com.hilifecare.ui.main;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FBCallback;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.model.Exercise;
import com.hilifecare.model.Plan;
import com.hilifecare.model.UserInfo;
import com.hilifecare.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@ActivityScope
public class MainPresenter extends BasePresenter<MainView> {
    FirebaseInteractor interactor;

    @Inject
    public MainPresenter(@Named("real") FirebaseInteractor interactor){
        this.interactor = interactor;
    }

    public void initUserProfile(){
        //TODO: init user image and name
    }

    public void initPlan(MainActivity activity) {
        unsubscribe();
        interactor.getPlanList(new FBCallback<Observable<ArrayList<Plan>>>() {
            @Override
            public void onResultData(Observable<ArrayList<Plan>> data) {
                subscription = data
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(planArrayList -> {
                            Timber.d("get plan list success");
                            activity.updatePlanList(planArrayList);
                        });
            }
        });
    }

    public void getSmartBandAddress(MainActivity activity, ProgressDialog progressDialog){
        unsubscribe();
        interactor.getSmartBandAddress(new FBCallback<Observable<String>>() {
            @Override
            public void onResultData(Observable<String> data) {
                subscription = data
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(mDeviceAddress -> {
                            Timber.d("get SmartBand Address");
                            activity.setSmartBandAddress(mDeviceAddress);
                            progressDialog.dismiss();
                        });
            }
        });

    }

    public void getExercise(MainActivity activity, ProgressDialog progressDialog, Fragment fragment){
        unsubscribe();
        interactor.getExerciseList(new FBCallback<Observable<ArrayList<Exercise>>>() {
            @Override
            public void onResultData(Observable<ArrayList<Exercise>> data) {
                subscription = data
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(exercises -> {
                            activity.setIndividualExercise(exercises, fragment);
                           progressDialog.dismiss();
                        });
            }
        });
    }

    public void setUserInfo(MainActivity activity) {
        unsubscribe();
        interactor.getUserInfo(new FBCallback<Observable<UserInfo>>() {
            @Override
            public void onResultData(Observable<UserInfo> data) {
                subscription = data.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<UserInfo>() {
                            @Override
                            public void call(UserInfo userInfo) {
                                activity.setUserinfo(userInfo);
                            }
                        });
            }
        });
    }
}
