package com.hilifecare.ui.mypage;

import com.google.firebase.auth.FirebaseAuth;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class MyPagePresenter extends BasePresenter<MyPageView> {
    private static final int INIT_PROFILE = 1;
    private static final int RESET_PLAN = 2;
    private static final int CHANGE_PLAN = 3;
    private static final int SIGN_OUT = 4;

    FirebaseInteractor interactor;

    @Inject
    public MyPagePresenter(@Named("real") FirebaseInteractor interactor) {
        this.interactor = interactor;
    }


    public void setUserImage() {

    }

    public void setUserName() {

    }

    public void setUserLevel() {
        //TODO: set user level
    }

    public void resetPlan() {
        //onResetPlanComplete
    }

    public void changeProfile(){
        //changeProfile
    }

    public void signOut(){
        //onSignOutSuccess
        FirebaseAuth.getInstance().signOut();
    }
}
