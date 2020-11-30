package com.hilifecare.ui.leveltest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerLevelTestPlayingComponent;
import com.hilifecare.di.components.LevelTestPlayingComponent;
import com.hilifecare.di.modules.LevelTestPlayingModule;
import com.hilifecare.model.LevelTest;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.view.CustomDialog;
import com.hilifecare.ui.view.PlayingLevelTestView;
import com.hilifecare.util.logging.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

@ActivityScope
public class LevelTestPlayingActivity extends BaseActivity<LevelTestPlayingPresenter>
implements LevelTestPlayingView,HasComponent<LevelTestPlayingComponent>{
    private OnFragmentInteractionListener mListener;

    @Bind(R.id.toolbar_name)
    TextView toolbarName;
    @Bind(R.id.toolbar_right)
    ImageView toolbarRight;
    @Bind(R.id.playing)
    LinearLayout layout;
    PlayingLevelTestView playingView;
    ArrayList<LevelTest> levelTestArrayList;

    Stopwatch stopwatch = new Stopwatch();


    LevelTestPlayingComponent levelTestPlayingComponent;
    private int positionState;
    private long timerState;

    private static int now_flag = 0;

    public LevelTestPlayingActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        levelTestArrayList = (ArrayList<LevelTest>)intent.getSerializableExtra("LevelTestData");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        positionState = preferences.getInt("player_position", 0);
        timerState = preferences.getLong("timer_position", 0);
        Timber.d("timer: "+timerState);
        if(playingView != null){
            playingView.setPositionState(positionState);
            playingView.setTimerState(timerState);
        }
        editor.remove("player_position");
        editor.remove("timer_position");
        editor.commit();
    }

    @Override
    public void init() {
        super.init();
        toolbarName.setText("레벨 테스트");
        toolbarRight.setVisibility(View.GONE);
        initLevelTest();
    }

    @Override
    protected void injectModule() {
        levelTestPlayingComponent = DaggerLevelTestPlayingComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .levelTestPlayingModule(new LevelTestPlayingModule(this))
                .build();
        levelTestPlayingComponent.inject(this);
    }

    private void initLevelTest(){
        if (playingView == null) {
            playingView = new PlayingLevelTestView(this);
        }
        playingView.initView(levelTestArrayList, now_flag,
                new PlayingLevelTestView.OnPlayingPopUpViewCallback() {
                    @Override
                    public void onStateExerciseEnd(HashMap<String, String> userTestResultMap) {
                        Intent i = new Intent(LevelTestPlayingActivity.this, LevelTestResultActivity.class);
                        i.putExtra("userLevelTestInfo", userTestResultMap);
                        startActivity(i);
                    }

                    @Override
                    public void onSetExerciseTitle(String title) {
                        toolbarName.setText(title);
                    }

                    @Override
                    public void onToggleOrientation() {
//                        setFlag(now_flag);
                        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
//                        playingView.pause();
                        editor.putInt("player_position", playingView.getCurrentPosition());
                        editor.putLong("timer_position", playingView.getTimerPosition());
                        editor.commit();
                        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            LevelTestPlayingActivity.super.setTheme(R.style.LevelTestStyle);
                        }else{
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            LevelTestPlayingActivity.super.setTheme(R.style.AppTheme_FullScreen);
                        }
                    }

                    @Override
                    public void nextFlag() {
                        now_flag++;
//                        setFlag(now_flag);
                    }

                    @Override
                    public void prevFlag() {
                        now_flag--;
//                        setFlag(now_flag);
                    }
                });
        playingView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        layout.addView(playingView);
    }

    public void setFlag(int now_flag) {
        this.now_flag = now_flag;
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        CustomDialog dialog = new CustomDialog(this,CustomDialog.TYPE_TWOBUTTON);
        dialog.setMessage(getString(R.string.leveltest_stop_message))
                .setOkText("종료")
                .setOkClickListener(v -> finish()).showDialog();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_level_test_playing;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
        playingView.close();
    }

    @Override
    public LevelTestPlayingComponent getComponent() {
        return levelTestPlayingComponent;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    @Override
    protected void onStart() {
        stopwatch.printLog("LevelTestPlayingActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.reset(); // 현재 화면이 없어질 때
    }
}
