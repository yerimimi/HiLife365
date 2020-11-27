package com.hilifecare.ui.program;

import com.hilifecare.di.ActivityScope;
import com.hilifecare.domain.interactors.firebase.FirebaseInteractor;
import com.hilifecare.model.Program;
import com.hilifecare.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;


@ActivityScope
public class ProgramPresenter extends BasePresenter<ProgramView> {
    ProgramActivity activity;
    FirebaseInteractor interactor;
    ArrayList<Program> program;
    @Inject
    public ProgramPresenter(ProgramActivity activity,
                            @Named("real") FirebaseInteractor interactor,
                            ArrayList<Program> program) {
        this.activity = activity;
        this.interactor = interactor;
        this.program = program;
    }

    public void initView(){
        activity.initView(program);
    }

    public void setProgram(){
        activity.startProgram(program);

    }

    public void setConstructionPopUp() {
        activity.showConstructionPopUp(program);
    }
}
