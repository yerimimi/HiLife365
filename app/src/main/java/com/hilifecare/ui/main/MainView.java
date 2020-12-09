package com.hilifecare.ui.main;

import android.support.v4.app.Fragment;

import com.hilifecare.model.Exercise;
import com.hilifecare.model.Plan;
import com.hilifecare.ui.base.PresenterView;

import java.util.ArrayList;

public interface MainView extends PresenterView {
    void setUserName(String name);
    void updatePlanList(ArrayList<Plan> planArrayList);
    void setSmartBandAddress(String mDeviceAddress);
    void setIndividualExercise(ArrayList<Exercise> exerciseArrayList, Fragment fragment);
}
