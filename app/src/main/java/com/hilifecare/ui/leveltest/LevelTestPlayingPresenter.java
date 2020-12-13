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
public class LevelTestPlayingPresenter extends BasePresenter<LevelTestPlayingView> {

    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    @Inject
    public LevelTestPlayingPresenter() {

    }

    public void initLevelTest() {
//        fGet("level_test", null);
    }

//    public void fGet(String databaseName, String uuid) {
//        restartableLatestCache(0, () ->
//            firebaseInteractor.fGet(databaseName, uuid)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread()),
//            LevelTestPlayingView::getLevelTestData);
//        start(0);
//    }

    public void setLevelTest(LevelTestAdapter levelTestAdapter, DatabaseReference levelTestHistory, String levelOfDifficulty, ProgressDialog pDialog) {
        Query query = levelTestHistory.child(levelOfDifficulty);
        query.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ltSnapshot : dataSnapshot.getChildren()) {
                        try {
                            String name = (String) ltSnapshot.child("exercise_name").getValue();
                            String descriptions = (String) ltSnapshot.child("descriptions").getValue();
                            int measure_sec = Integer.parseInt(ltSnapshot.child("measure_secs").getValue().toString());
//                            levelTestAdapter.add(new LevelTest(name, descriptions, measure_sec));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    pDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Timber.e("set level test: "+databaseError.getMessage());
                }
            }
        );
    }
}
