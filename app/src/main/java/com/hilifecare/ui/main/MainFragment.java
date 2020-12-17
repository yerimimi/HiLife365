package com.hilifecare.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hilifecare.R;
import com.hilifecare.di.components.MainComponent;
import com.hilifecare.model.UserInfo;
import com.hilifecare.ui.base.BaseFragment;
import com.hilifecare.ui.base.EmptyPresenter;
import com.hilifecare.ui.leveltest.LevelTestActivity;
import com.hilifecare.util.logging.ScreenStopwatch;

import butterknife.OnClick;

public class MainFragment extends BaseFragment<EmptyPresenter> {

    @Override
    protected void inject() {
        if(getComponent(MainComponent.class) != null)
            getComponent(MainComponent.class).inject(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    public void init() {
        try {
            Bundle bundle = getArguments();
            UserInfo userInfo = (UserInfo) bundle.getSerializable("userinfo");
            setUserInfo(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @OnClick({R.id.leveltest_button, R.id.program_button})
    @OnClick(R.id.leveltest_button)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.leveltest_button:
                Intent i = new Intent(getContext(), LevelTestActivity.class);
                startActivity(i);
                break;
//            case R.id.program_button:
//                break;
        }
    }

    @Override
    public void onResume() {
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause(){

        ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());
        super.onPause();
    }
}
