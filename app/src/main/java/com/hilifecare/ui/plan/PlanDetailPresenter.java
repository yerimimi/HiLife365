package com.hilifecare.ui.plan;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FBCallback;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.model.Plan;
import com.hilifecare.model.Program;
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
public class PlanDetailPresenter extends BasePresenter<PlanDetailView> {
    PlanDetailActivity activity;
    FirebaseInteractor interactor;

    @Inject
    public PlanDetailPresenter(@Named("real") FirebaseInteractor interactor) {
//        this.activity = activity;
        this.interactor = interactor;
    }

    public void initPlan(PlanDetailActivity activity) {
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

    public void setProgramList(Plan plan, PlanDetailActivity activity) {
        unsubscribe();
        interactor.getProgramList(plan, new FBCallback<Observable<ArrayList<Program>>>() {
            @Override
            public void onResultData(Observable<ArrayList<Program>> data) {
                subscription = data.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ArrayList<Program>>() {
                            @Override
                            public void call(ArrayList<Program> Programs) {
                                activity.getPlanDetailDataList(Programs);
                            }
                        });
            }
        });
    }

    public void setProgram(Plan plan, PlanDetailActivity activity) {
        unsubscribe();
        interactor.getProgram(plan, new FBCallback<Observable<ArrayList<Program>>>() {
            @Override
            public void onResultData(Observable<ArrayList<Program>> data) {
                subscription = data.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ArrayList<Program>>() {
                            @Override
                            public void call(ArrayList<Program> Programs) {
                                activity.getPlanDetailData(Programs);
                            }
                        });
            }
        });
    }
}
