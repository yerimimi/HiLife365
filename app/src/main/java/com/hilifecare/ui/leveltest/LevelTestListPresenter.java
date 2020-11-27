package com.hilifecare.ui.leveltest;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FBCallback;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.model.LevelTest;
import com.hilifecare.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


@ActivityScope
public class LevelTestListPresenter extends BasePresenter<LevelTestListFragment> {

    @Named("real")
    @Inject
    FirebaseInteractor interactor;

    LevelTestListFragment fragment;

    String levelOfDifficulty;

    @Inject
    public LevelTestListPresenter(LevelTestListFragment fragment) {
        this.fragment = fragment;
    }

    public void initLevelTest(String levelOfDifficulty) {
        this.levelOfDifficulty = levelOfDifficulty;
        loadLevelTest(levelOfDifficulty);
    }

    public void loadLevelTest(String levelOfDifficulty) {
        unsubscribe();
        interactor.getLevelTest(levelOfDifficulty, new FBCallback<Observable<ArrayList<LevelTest>>>() {
            @Override
            public void onResultData(Observable<ArrayList<LevelTest>> data) {
                subscription = data.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<ArrayList<LevelTest>>() {
                            @Override
                            public void call(ArrayList<LevelTest> levelTests) {
                                fragment.getLevelTestList(levelTests);
                            }
                        });
            }
        });
    }

    public void setFragment(LevelTestListFragment fragment) {
        this.fragment = fragment;
    }
}

