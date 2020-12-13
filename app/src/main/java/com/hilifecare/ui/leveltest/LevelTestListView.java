package com.hilifecare.ui.leveltest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Logger;
import com.hilifecare.model.LevelTest;
import com.hilifecare.ui.base.PresenterView;

import java.util.ArrayList;

public interface LevelTestListView extends PresenterView {

    void getLevelTestList(ArrayList<LevelTest> levelTestHistory);
}
