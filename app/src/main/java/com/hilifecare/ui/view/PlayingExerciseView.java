package com.hilifecare.ui.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.Constants;
import com.hilifecare.model.Exercise;
import com.hilifecare.model.Program;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by imcreator on 2017. 5. 2..
 */

public class PlayingExerciseView extends FrameLayout
    implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayerControl{
    private final int ORIENTATION_LANDSCAPE = 1;
    private final int ORIENTATION_PORTRATE = 0;
    View view;

    //changing
    @Bind(R.id.exercise_ing_view)
    FrameLayout exercise_ing_view;
    @Bind(R.id.exercise_video)
    SurfaceView exercise_video;

    //description
    @Bind(R.id.exercise_title)
    TextView exercise_title;
    @Bind(R.id.exercise_explain)
    TextView exercise_explain;
    @Bind(R.id.program_time)
    TextView program_time;

//    //sub start
//    @Bind(R.id.sub_start)
//    RelativeLayout subStartLayout;
//    @Bind(R.id.preivew_substart_next_button)
//    LinearLayout subStartNextButton;
//
//    //complete
//    @Bind(R.id.preview_end)
//    RelativeLayout endLayout;
//    @Bind(R.id.complete_text)
//    TextView complete_text;
//    @Bind(R.id.preview_end_next_button)
//    LinearLayout endNextButton;

    ArrayList<Program> programArrayList;

    int now_flag = 0;
    int orientation;

    long startTime = 0L;
    private Handler timerHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    OnPlayingExerciseViewCallback callback;
    private MediaPlayer player;
    private VideoControllerView controller;
    private int positionState;
    private int color;
    private int defaultColor;
    private boolean isStartTimeRestored = true;
    int duration;

    ArrayList<String> sampleVideo = new ArrayList<>();

    public PlayingExerciseView(@NonNull Context context) {
        super(context);
    }

    public PlayingExerciseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayingExerciseView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initExercise(ArrayList<Program> programArrayList, OnPlayingExerciseViewCallback callback) {
        this.callback = callback;
        this.programArrayList = programArrayList;
        LayoutInflater infalter = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        defaultColor = getResources().getColor(R.color.colorText);
        view = infalter.inflate(R.layout.playing_exercise_view, this, false);
        color = getResources().getColor(R.color.colorPrimary);
        addView(view);
        ButterKnife.bind(this, view);

        exercise_title.setText(programArrayList.get(0).getExercise_name());
        exercise_explain.setText(programArrayList.get(0).getSimple_explain());

        sampleVideo.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/videos%2FBent%20Knee%20Push-up.mp4?alt=media&token=2916d37b-8eb9-4650-a2b3-038c6ba5fad5");
        sampleVideo.add("");

        initVideoController(ORIENTATION_PORTRATE);

        startExercise(Constants.STATE_EXERCISE_BEGIN);
    }

    public void initExercise(Exercise exercise, OnPlayingExerciseViewCallback callback) {
        this.callback = callback;
        LayoutInflater infalter = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        defaultColor = getResources().getColor(R.color.colorText);
        view = infalter.inflate(R.layout.playing_exercise_view, this, false);
        color = getResources().getColor(R.color.colorPrimary);
        addView(view);
        ButterKnife.bind(this, view);

        callback.onSetExerciseTitle(exercise.getName());
        exercise_title.setText(exercise.getName());
        exercise_explain.setText(exercise.getSimple_explain());

        sampleVideo.add(exercise.getVideo());
        sampleVideo.add("");

        initVideoController(ORIENTATION_PORTRATE);

        startExercise(Constants.STATE_EXERCISE_BEGIN);
    }

    private void initVideoController(int orientation) {
        SurfaceHolder videoHolder = exercise_video.getHolder();
        videoHolder.addCallback(this);
        this.orientation = orientation;

//        player = new MediaPlayer();
//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                exercise_ing_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_out));
//                now_flag++;
//            }
//        });
//        controller = new VideoControllerView(getContext(),
//                new VideoControllerListener() {});
//        controller.setAnchorView(exercise_ing_view, null);
//        controller.setPrevNextListeners(
//                v -> Toast.makeText(getContext(), "next video", Toast.LENGTH_SHORT).show(),
//                v -> Toast.makeText(getContext(), "prev video", Toast.LENGTH_SHORT).show());
//
//        try {
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            player.setDataSource(getContext(), Uri.parse(sampleVideo.get(orientation)));
//            player.setOnPreparedListener(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        controller = new VideoControllerView(getContext(),
                new VideoControllerListener() {});
        controller.setAnchorView(exercise_ing_view, null);
//        controller.setPrevNextListeners(v -> {
//            Toast.makeText(getContext(), "next video", Toast.LENGTH_SHORT).show();
//        }, v -> {
//            Toast.makeText(getContext(), "prev video", Toast.LENGTH_SHORT).show();
//        });
    }

    private void startExercise(int state) {
        switch (state) {
            case Constants.STATE_EXERCISE_BEGIN:
                setExercise();
                break;
            case Constants.STATE_EXERCISE_END:
                break;
            case Constants.STATE_EXERCISE_STOP:
                stopTimer();
                break;
            case Constants.STATE_EXERCISE_ING:
                controller.setSeekBarEnable(true);
                timerHandler.postDelayed(updateTimerThread, 0);
                break;
            case Constants.STATE_EXERCISE_RESTART:
                if(!isStartTimeRestored) startTime = SystemClock.uptimeMillis();
                isStartTimeRestored = false;
                timerHandler.postDelayed(updateTimerThread, 0);
                break;
        }
    }

    private void stopTimer() {
        timeSwapBuff += timeInMilliseconds;
        startTime = 0;
        timerHandler.removeCallbacks(updateTimerThread);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            setTime();
            timerHandler.postDelayed(this, 0);
        }
    };

    private void setTime(){
        if(startTime == 0)
            startTime = SystemClock.uptimeMillis();
        timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
        updatedTime = timeSwapBuff + timeInMilliseconds;
    }

    public void setPositionState(int positionState){
        this.positionState = positionState;
    }
    public void setTimerState(long timerState) {
        this.timeSwapBuff = timerState;
        updatedTime = timeSwapBuff + timeInMilliseconds;
        if(updatedTime != 0){
            isStartTimeRestored = true;
        }
    }

    private void setExercise() {
        setPlayer();
        controller.showStartButton();
    }

    private void setPlayer() {
        try {
            if(player == null){
                player = new MediaPlayer();
                player.setOnCompletionListener(mp -> {
                    try{
                        if(duration - player.getCurrentPosition() > 1000 )
                            return;
                        exercise_ing_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_out));
                        exercise_ing_view.setVisibility(View.INVISIBLE);
                    }catch (Exception e){
                        Timber.e(e.getMessage());
                    }
                });
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnPreparedListener(this);
            }else{
                player.reset();
            }https:

            player.setDataSource(getContext(), Uri.parse(sampleVideo.get(orientation)));
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.exercise_video)
    void exerciseVideoClick() {}

    public void close(){
        if(player.isPlaying())
            player.stop();
        player.reset();
        controller.close();
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        Timber.d("onPrepared");
        duration = player.getDuration();
        player.seekTo(positionState);
        controller.setMediaPlayer(this, positionState);
        controller.setSeekBarEnable(false);
        positionState = 0;
        //(FrameLayout) findViewById(R.id.videoSurfaceContainer));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Timber.d("surfaceCreated");
        player.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SharedPreferences preferences = getContext().getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        player.pause();
        editor.putInt("player_position", player.getCurrentPosition());
        editor.commit();
    }

    @Override
    public void start() {
        if(player.getCurrentPosition() == 0){
            startExercise(Constants.STATE_EXERCISE_ING);
        }else{
            startExercise(Constants.STATE_EXERCISE_RESTART);
        }
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {
        callback.toggleFullScreen();
    }

    public long getTimerPosition() {
        return updatedTime;
//        return startTime;
    }

    public interface OnPlayingExerciseViewCallback {
        void onStateExerciseEnd(HashMap<String, String> userTestResultMap);
        void onSetExerciseTitle(String title);
        void toggleFullScreen();
    }

    public void setCallback(OnPlayingExerciseViewCallback callback){
        this.callback = callback;
    }
}
