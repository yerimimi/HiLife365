package com.hilifecare.ui.exercise;

import com.hilifecare.model.Exercise;
import com.hilifecare.ui.base.PresenterView;

import java.util.ArrayList;

/**
 * Created by imcreator on 2017. 5. 9..
 */

public interface IndividualExerciseView extends PresenterView{
    void updateIndividualExcerciseList(ArrayList<Exercise> exerciseArrayList);
}
