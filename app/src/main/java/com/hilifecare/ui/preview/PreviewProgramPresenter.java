package com.hilifecare.ui.preview;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.ui.base.BasePresenter;
import com.hilifecare.ui.exercise.ExerciseView;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class PreviewProgramPresenter extends BasePresenter<ExerciseView> {
    @Named("fake")
    @Inject
    FirebaseInteractor firebaseInteractor;

    @Inject
    public PreviewProgramPresenter() {

    }

}
