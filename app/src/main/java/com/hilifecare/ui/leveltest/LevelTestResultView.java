package com.hilifecare.ui.leveltest;

import com.hilifecare.ui.base.PresenterView;

public interface LevelTestResultView extends PresenterView {
    String getTime();

    void setLevel(String level);
}
