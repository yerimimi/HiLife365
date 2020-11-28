package com.hilifecare.ui.leveltest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerLevelTestComponent;
import com.hilifecare.di.components.LevelTestComponent;
import com.hilifecare.di.modules.LevelTestModule;
import com.hilifecare.model.LevelTest;
import com.hilifecare.ui.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;
import com.hilifecare.util.logging.Stopwatch;


@ActivityScope
public class
LevelTestActivity extends BaseActivity<LevelTestPresenter> implements LevelTestView, HasComponent<LevelTestComponent>,
LevelTestListFragment.OnFragmentInteractionListener{
    public static final int LEVEL_TEST_LIST = 1;
    public static final int LEVEL_TEST_PLAYING = 2;
    @Inject
    LevelTestListFragment levelTestListFragment;
    @Inject
    LevelTestPlayingActivity levelTestPlayingFragment;

    @Inject
    LevelTestPresenter levelTestPresenter;

    LevelTestComponent levelTestComponent;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.toolbar_right)
    ImageView toolbarRight;

    Stopwatch stopwatch = new Stopwatch();


    public static Context mContext;
    private Fragment fragment;

    protected void injectModule() {
        levelTestComponent = DaggerLevelTestComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .levelTestModule(new LevelTestModule(this))
                .build();
        levelTestComponent.inject(this);
    }

    public PresenterFactory<LevelTestPresenter> getPresenterFactory() {
        return () -> levelTestPresenter;
    }

    @Override
    public void init() {
        toolbarRight.setVisibility(View.GONE);
        mContext = this;
        levelTestListFragment = new LevelTestListFragment();
        setFragment(levelTestListFragment, "레벨 테스트");
        goFragment(fragment);
    }

    protected int getLayoutResource() {
        return R.layout.activity_level_test;
    }

    @Override
    public LevelTestComponent getComponent() {
        if(levelTestComponent == null){
            levelTestComponent = DaggerLevelTestComponent.builder()
                    .applicationComponent(App.get(this).getComponent())
                    .levelTestModule(new LevelTestModule(this))
                    .build();
        }
        return levelTestComponent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick(R.id.toolbar_left)
    void onBack(){
        stopwatch.reset();
        onBackPressed();
        stopwatch.printLog("toolbar_left");
    }

    @Override
    public void onFragmentInteraction(int where, Object test) {
        if(where == LEVEL_TEST_PLAYING) {
            Intent intent = new Intent(this, LevelTestPlayingActivity.class);
            intent.putExtra("LevelTestData", (ArrayList<LevelTest>)test);
            startActivity(intent);
        }
    }

    public void setFragment(Fragment fragment, String title) {
        this.fragment = fragment;
        this.toolbarName.setText(title);
    }

    public void goFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    public void setTitle(String title){
        this.toolbarName.setText(title);
    }

}
