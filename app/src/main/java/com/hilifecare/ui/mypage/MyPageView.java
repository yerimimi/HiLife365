package com.hilifecare.ui.mypage;

import android.net.Uri;

import com.hilifecare.ui.base.PresenterView;

public interface MyPageView extends PresenterView {
    void setUserInfo();
    void setUserImage(Uri url);
    void setUserName(String name);
    void setUserLevel(int level);
    void onResetPlanComplete();
    void onSignOutSuccess();
}
