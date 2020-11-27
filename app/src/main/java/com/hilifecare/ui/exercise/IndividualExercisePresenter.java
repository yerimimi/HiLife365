package com.hilifecare.ui.exercise;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;

/**
 * Created by imcreator on 2017. 5. 9..
 */

@ActivityScope
class IndividualExercisePresenter extends BasePresenter<IndividualExerciseView>{
    FirebaseInteractor interactor;

//    @Inject
//    public IndividualExercisePresenter(@Named("real") FirebaseInteractor interactor){
//        this.interactor = interactor;
//    }


    public IndividualExercisePresenter() {
    }
}
