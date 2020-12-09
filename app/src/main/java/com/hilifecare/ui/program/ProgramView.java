package com.hilifecare.ui.program;

import com.hilifecare.model.Program;
import com.hilifecare.ui.base.PresenterView;

import java.util.ArrayList;

public interface ProgramView extends PresenterView {
    void showConstructionPopUp(ArrayList<Program> programArrayList);
    void startProgram(ArrayList<Program> programArrayList);
    void initView(ArrayList<Program> program);
}
