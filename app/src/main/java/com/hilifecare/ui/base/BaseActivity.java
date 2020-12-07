package com.hilifecare.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hilifecare.model.UserInfo;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusAppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity<P extends Presenter> extends NucleusAppCompatActivity<P> {
    private static String mDeviceAddress = null;
    private static UserInfo userInfo;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        injectModule();
        ButterKnife.bind(this);
        setPresenterFactory(getPresenterFactory());
        Fabric.with(this, new Crashlytics());
//        LeakCanary.install(getApplication());
        init();
    }

    protected void init() {
    }

    protected abstract void injectModule();

    protected abstract int getLayoutResource();

    @CallSuper
    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
//        RefWatcher refWatcher = App.get(this).getRefWatcher();
//        refWatcher.watch(this);
    }

    @CallSuper
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public void setUserInfo(UserInfo userInfo) {this.userInfo = userInfo; }
    public UserInfo getUserInfo() { return this.userInfo; }
    public FirebaseUser getFirebaseUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    public void setmDeviceAddress(String mDeviceAddress) { this.mDeviceAddress = mDeviceAddress; }
    public String getmDeviceAddress() { return mDeviceAddress; }

    /*@Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset(); // 현재 화면이 없어질 때
    }*/
}
