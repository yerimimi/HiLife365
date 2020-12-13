package com.hilifecare.ui.bluetooth;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Administrator on 2017-07-12.
 */

@ActivityScope
public class DeviceControlPresenter extends BasePresenter<DeviceControlView>{
    @Named("real")
    @Inject
    FirebaseInteractor firebaseInteractor;

    @Inject
    public DeviceControlPresenter() {

    }

    public void insertSmartBandAddress(String mDeviceAddress) {
        unsubscribe();
        firebaseInteractor.insertSmartBandAddress(mDeviceAddress);
    }
}
