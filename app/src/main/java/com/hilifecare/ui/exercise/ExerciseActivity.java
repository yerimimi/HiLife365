package com.hilifecare.ui.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerExerciseComponent;
import com.hilifecare.di.components.ExerciseComponent;
import com.hilifecare.di.modules.ExerciseModule;
import com.hilifecare.model.Exercise;
import com.hilifecare.model.Program;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.ui.view.PlayingExerciseView;
import com.hilifecare.util.logging.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

import static com.hilifecare.R.id.toolbar2;

@ActivityScope
public class ExerciseActivity extends BaseActivity<ExercisePresenter> implements ExerciseView, HasComponent<ExerciseComponent> {
//    @Inject
//    ExercisePresenter exercisePresenter;

    ExerciseComponent exerciseComponent;

    @Bind(R.id.playing_exercise_view_background)
    LinearLayout layout;
    @Bind(toolbar2)
    CustomToolbar toolbar;

    PlayingExerciseView playingExerciseView;

    ArrayList<Program> programArrayList;
    Exercise exercise;
    int flag = 0;

    private int positionState;
    private long timerState;

    Stopwatch stopwatch = new Stopwatch();


//    public PresenterFactory<ExercisePresenter> getPresenterFactory() {
//        return () -> exercisePresenter;
//    }


    public ExerciseActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        programArrayList = (ArrayList<Program>) intent.getSerializableExtra("programinfo");
        exercise = (Exercise) intent.getSerializableExtra("exerciseinfo");

        if(programArrayList == null)
            flag = 1;
        else
            flag = 0;
        onWindowFocusChanged(true);
        toolbar.setToolbarRightVisibility(View.INVISIBLE);
        playingExerciseView = new PlayingExerciseView(this);
        if(flag == 0) {
            playingExerciseView.initExercise(programArrayList, new PlayingExerciseView.OnPlayingExerciseViewCallback() {
                @Override
                public void onStateExerciseEnd(HashMap<String, String> userTestResultMap) {

                }

                @Override
                public void onSetExerciseTitle(String title) {
                    toolbar.setName(title);
                }

                @Override
                public void toggleFullScreen() {
                    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
//                    playingExerciseView.pause();
                    editor.putInt("player_position", playingExerciseView.getCurrentPosition());
                    editor.putLong("timer_position", playingExerciseView.getTimerPosition());
                    editor.commit();
                    if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        ExerciseActivity.super.setTheme(R.style.ExerciseStyle);
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        ExerciseActivity.super.setTheme(R.style.AppTheme_FullScreen);
                    }
                }
            });
        } else {
            playingExerciseView.initExercise(exercise, new PlayingExerciseView.OnPlayingExerciseViewCallback() {
                @Override
                public void onStateExerciseEnd(HashMap<String, String> userTestResultMap) {

                }

                @Override
                public void onSetExerciseTitle(String title) {
                    toolbar.setName(title);
                }

                @Override
                public void toggleFullScreen() {
                    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
//                    playingExerciseView.pause();
                    editor.putInt("player_position", playingExerciseView.getCurrentPosition());
                    editor.putLong("timer_position", playingExerciseView.getTimerPosition());
                    editor.commit();
                    if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        ExerciseActivity.super.setTheme(R.style.ExerciseStyle);
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        ExerciseActivity.super.setTheme(R.style.AppTheme_FullScreen);
                    }
                }
            });
        }
        playingExerciseView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }

        layout.addView(playingExerciseView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        positionState = preferences.getInt("player_position", 0);
        timerState = preferences.getLong("timer_position", 0);
        Timber.d("timer: "+timerState);
        if(playingExerciseView != null){
            playingExerciseView.setPositionState(positionState);
            playingExerciseView.setTimerState(timerState);
        }
        editor.remove("player_position");
        editor.remove("timer_position");
        editor.commit();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus) initExercise();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(playingExerciseView != null) playingExerciseView.close();
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        playingExerciseView.close();
        finish();
    }

    protected int getLayoutResource() {
        return R.layout.activity_exercise;
    }

    @Override
    public ExerciseComponent getComponent() {
        return exerciseComponent;
    }

    @Override
    protected void injectModule() {
        exerciseComponent = DaggerExerciseComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .exerciseModule(new ExerciseModule(this))
                .build();
        exerciseComponent.inject(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playingExerciseView.close();
    }

    @Override
    protected void onStart() {
        stopwatch.printLog("ExerciseActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.reset(); // 현재 화면이 없어질 때
    }
}
