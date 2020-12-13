package com.hilifecare.ui.plan;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FBCallback;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.model.Plan;
import com.hilifecare.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by imcreator on 2017. 5. 9..
 */

@ActivityScope
class PlanPresenter extends BasePresenter<PlanView>{
    FirebaseInteractor interactor;

    @Inject
    public PlanPresenter(@Named("real") FirebaseInteractor interactor){
        this.interactor = interactor;
    }

    public void initPlan(PlanFragment fragment) {
        unsubscribe();
        interactor.getPlanList(new FBCallback<Observable<ArrayList<Plan>>>() {
            @Override
            public void onResultData(Observable<ArrayList<Plan>> data) {
                subscription = data
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(planArrayList -> {
                            Timber.d("get plan list success");
                            fragment.updatePlanList(planArrayList);
                        });
            }
        });

//        restartableLatestCache(0, () -> interactor.getPlanList()
//                        .observeOn(Schedulers.io())
//                        .subscribeOn(AndroidSchedulers.mainThread()),
//                (planView, planArrayList) -> {
//                    Timber.d("get plan list success");
//                    planView.updatePlanList(planArrayList);
//                },
//                new Action2<PlanView, Throwable>() {
//                    @Override
//                    public void call(PlanView planView, Throwable throwable) {
//                        Timber.e(throwable.getMessage());
//                    }
//                });
//        start(0);
    }

    public void initRecommendPlan(PlanFragment fragment) {
        unsubscribe();
        interactor.getRecommendPlan(new FBCallback<Observable<Plan>>() {
            @Override
            public void onResultData(Observable<Plan> data) {
                subscription = data
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(plan -> {
                            Timber.d("get plan list success");
                            fragment.updateRecommendPlan(plan);
                        });
            }
        });
    }
}
