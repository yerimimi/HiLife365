package com.hilifecare.ui.leveltest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerLevelTestResultComponent;
import com.hilifecare.di.components.LevelTestResultComponent;
import com.hilifecare.di.modules.LevelTestResultModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.main.MainActivity;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class LevelTestResultActivity extends BaseActivity<LevelTestResultPresenter> implements LevelTestResultView, HasComponent<LevelTestResultComponent> {

    @Inject
    LevelTestResultPresenter levelTestResultPresenter;

    LevelTestResultComponent levelTestResultComponent;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.toolbar_right)
    ImageView toolbarRight;
    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    @Bind(R.id.result_level)
    ImageView resultLevelImage;
    @Bind(R.id.leveltest_result_level)
    TextView resultLevel;

    ProgressDialog pDialog;

    private HashMap<String, String> userTestResultMap = new HashMap<>();
    private PopupWindow popupWindow;

    protected void injectModule() {
        levelTestResultComponent = DaggerLevelTestResultComponent.builder().applicationComponent(App.get(this).getComponent()).levelTestResultModule(new LevelTestResultModule(this)).build();
        levelTestResultComponent.inject(this);
        
    }
    
    public PresenterFactory<LevelTestResultPresenter> getPresenterFactory() {
        return () -> levelTestResultPresenter;
    }

    public void init() {
        Intent intent = getIntent();
        userTestResultMap = (HashMap<String, String>)intent.getSerializableExtra("userLevelTestInfo");
        toolbarRight.setVisibility(View.INVISIBLE);
        toolbarName.setText(getTitle());
        toolbar.setShadowVisibility(View.VISIBLE);


        getPresenter().insertLevelTestResult(userTestResultMap, getTime());
    }

    @Override
    public String getTime() {
        //TODO: java.lang.IllegalArgumentException: Unknown pattern character 'Y'
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(System.currentTimeMillis()));
    }

    @Override
    public void setLevel(String level) {
        resultLevel.setText(level);
        switch (level.toLowerCase()){
            case "excellent":
                resultLevelImage.setImageResource(R.drawable.icon_level1);
                break;
            case "good":
                resultLevelImage.setImageResource(R.drawable.icon_level2);
                break;
            case "fair":
                resultLevelImage.setImageResource(R.drawable.icon_level3);
                break;
            case "poor":
                resultLevelImage.setImageResource(R.drawable.icon_level4);
                break;
            case "very poor":
                resultLevelImage.setImageResource(R.drawable.icon_level5);
                break;
        }
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.share_facebook_button)
    void shareToFacebook(){
        //TODO: share facebook
    }

    @OnClick(R.id.share_insta_button)
    void shareToInstagram() {
        //TODO: share instagram
    }

    @OnClick(R.id.leveltest_result_more)
    void showResultDetail(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.view_level_test_result,
                (ViewGroup) findViewById(R.id.result_list_container));
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
//        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.update();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.level_test_result_list);
        LevelTestResultAdapter levelTestResultAdapter = new LevelTestResultAdapter(this, userTestResultMap);
        recyclerView.setAdapter(levelTestResultAdapter);
    }

    protected int getLayoutResource() {
        return R.layout.activity_level_test_result;
    }

    @Override
    public LevelTestResultComponent getComponent() {
        return levelTestResultComponent;
    }

    @Override
    protected void onResume() {
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());

    }
}
