package com.hilifecare.ui.myrecord;

import com.hilifecare.ui.base.PresenterView;

public interface StepCountView extends PresenterView {
    String getTime();
    String convertTime(int time_stack);
    String convertCalories(double speed_data);
    String convertDistance(double speed_data);
    String convertStep(double distance_data);
}
