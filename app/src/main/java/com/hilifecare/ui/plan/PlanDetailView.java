package com.hilifecare.ui.plan;

import com.hilifecare.model.Plan;
import com.hilifecare.model.Program;
import com.hilifecare.model.ReformProgram;
import com.hilifecare.ui.base.PresenterView;

import java.util.ArrayList;

public interface PlanDetailView extends PresenterView {

    void updatePlanList(ArrayList<Plan> planArrayList);
    void getPlanDetailDataList(ArrayList<Program> data);
    void getPlanDetailData(ArrayList<Program> data);
    ArrayList<ReformProgram> reformProgram(ArrayList<Program> data);
}
