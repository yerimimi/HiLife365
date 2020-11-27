package com.hilifecare.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilifecare.di.HasComponent;
import com.hilifecare.model.UserInfo;

import butterknife.ButterKnife;
import nucleus.view.NucleusSupportFragment;

public abstract class BaseFragment<P extends BasePresenter> extends NucleusSupportFragment<P> {
    private static UserInfo userInfo;

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    public void init() {
        Log.d("HL3", "Fragment Open");
    }

    @CallSuper
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("HL3 : ", "new Fragment");
        inject();
        setPresenterFactory(getPresenterFactory());
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();
        
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this); 
        super.onDestroyView();
        
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }
    public UserInfo getUserInfo() { return this.userInfo; }

    protected abstract void inject();

    protected abstract int getLayoutResource();

    

}
