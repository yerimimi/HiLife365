package com.hilifecare.ui.plan;

import com.hilifecare.model.Plan;
import com.hilifecare.ui.base.PresenterView;

import java.util.ArrayList;

/**
 * Created by imcreator on 2017. 5. 9..
 */

public interface PlanView extends PresenterView{
    void updateRecommendPlan(Plan recommendPlan);
    void updatePlanList(ArrayList<Plan> planArrayList);
}
