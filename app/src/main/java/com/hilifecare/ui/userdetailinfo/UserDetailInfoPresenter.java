package com.hilifecare.ui.userdetailinfo;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.model.UserInfo;
import com.hilifecare.ui.base.BasePresenter;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class UserDetailInfoPresenter extends BasePresenter<UserDetailInfoView> {
    private static final int INIT_USER_INFO = 1;
    private static final int SAVE_USER_INFO = 2;
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    @Inject
    public UserDetailInfoPresenter() {

    }

    public void insertUserDetailInfo(UserInfo userinfo) {
        unsubscribe();
        firebaseInteractor.insertUserDatailInfo(userinfo);
    }
}
