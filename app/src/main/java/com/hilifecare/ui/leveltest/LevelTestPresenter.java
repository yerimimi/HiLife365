package com.hilifecare.ui.leveltest;

import android.app.ProgressDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.model.LevelTest;
import com.hilifecare.ui.base.BasePresenter;

import javax.inject.Inject;
import javax.inject.Named;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


@ActivityScope
public class LevelTestPresenter extends BasePresenter<LevelTestView> {
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    @Inject
    public LevelTestPresenter() {

    }

}
