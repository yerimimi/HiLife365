package com.hilifecare.ui.view;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hilifecare.R;
import com.hilifecare.application.Constants;
import com.hilifecare.model.LevelTest;
import com.hilifecare.util.PhaseMaker;

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

public class PlayingLevelTestView extends FrameLayout
    implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayerControl{
    //Playing_Leveltest_view
    View view;
    @Bind(R.id.linearLayout2)
    LinearLayout container;

    @Bind(R.id.exercise_ing_view)
    FrameLayout exercise_ing_view;
    @Bind(R.id.exercise_video)
    SurfaceView exercise_video;

    //portrait only
    @Bind(R.id.next_exercise_image)
    ImageView next_exercise_image;
    @Bind(R.id.next_exercise_name)
    TextView next_exercise_name;
    @Nullable @Bind(R.id.next)
    TextView next;
    @Nullable @Bind(R.id.prev_exercise_image)
    ImageView prev_exercise_image;
    @Nullable @Bind(R.id.prev_exercise_name)
    TextView prev_exercise_name;
    @Nullable @Bind(R.id.prev)
    TextView prev;
    @Bind(R.id.total_time)
    TextView total_time;

    //leveltest end view
    @Bind(R.id.exercise_end_view)
    FrameLayout exercise_end_view;
    @Bind(R.id.complete_text)
    TextView complete_text;
    @Nullable
    @Bind(R.id.exercise_end_image)
    ImageView exercise_end_image;
    @Bind(R.id.exercise_count)
    EditText exercise_count;
    @Bind(R.id.exercise_count_unit)
    TextView count_unit;

    //leveltest description view
//    @Nullable
//    @Bind(R.id.exercise_description_view)
//    RelativeLayout exercise_description_view;
    @Nullable
    @Bind(R.id.description_title)
    TextView description_title;
    @Nullable
    @Bind(R.id.description_content) TextView description_content;

    ArrayList<TextView> textViews = new ArrayList<TextView>();
    ArrayList<LevelTest> levelTestArrayList = new ArrayList<LevelTest>();

    int now_flag = 0;
    int orientation;

    long startTime = 0L;
    private Handler timerHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int play_count = 0;
    int duration;

    String levelOfDifficulty;
    ProgressDialog pDialog;
    private HashMap<String, String> userTestResultMap = new HashMap<>();

    OnPlayingPopUpViewCallback callback;
    private VideoControllerView controller;
    private MediaPlayer player;
    private int positionState;
    private int color;
    private int defaultColor;
    private boolean isStartTimeRestored = false;

    public PlayingLevelTestView(@NonNull Context context) {
        super(context);
    }

    public PlayingLevelTestView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayingLevelTestView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(ArrayList<LevelTest> levelTestArrayList, int now_flag, OnPlayingPopUpViewCallback callback) {
        this.levelTestArrayList = levelTestArrayList;
        this.callback = callback;
        this.now_flag = now_flag;
        color = getResources().getColor(R.color.colorPrimary);
        defaultColor = getResources().getColor(R.color.colorText);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.playing_leveltest_view, this, false);
        addView(view);
        ButterKnife.bind(this, view);

        initVideoController(0);

        textViews = PhaseMaker.makeTextView(view.getContext(), levelTestArrayList.size());
        for(TextView tv: textViews) container.addView(tv);

        startExercise(Constants.STATE_EXERCISE_BEGIN);
    }

    private void initVideoController(int orientation) {
        SurfaceHolder videoHolder = exercise_video.getHolder();
        videoHolder.addCallback(this);

        orientation = this.orientation;

        controller = new VideoControllerView(getContext(),
                levelTestArrayList.get(now_flag).getMeasure_secs(),
                new VideoControllerListener() {});
        controller.setAnchorView(exercise_ing_view, null);
        controller.setPrevNextListeners(v -> {
            Toast.makeText(getContext(), "next video", Toast.LENGTH_SHORT).show();
            next();
        }, v -> {
            Toast.makeText(getContext(), "prev video", Toast.LENGTH_SHORT).show();
            prev();
        });
//        controller.setEnabled(true);
    }

    private void startExercise(int state) {
        resetStages();
        if(textViews.size() > now_flag)
            textViews.get(now_flag).setTextColor(color);

        switch (state) {
            case Constants.STATE_EXERCISE_BEGIN:
                setExercise();
                if(now_flag != 0){
                    exercise_end_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_out));
                    exercise_end_view.setVisibility(View.INVISIBLE);
                    exercise_ing_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_in));
                    exercise_ing_view.setVisibility(View.VISIBLE);
                }
//                if(exercise_description_view != null) {
//                    exercise_description_view.setVisibility(View.VISIBLE);
//                    exercise_ing_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_in));
//                    exercise_ing_view.setVisibility(View.VISIBLE);
//                }
                break;
            case Constants.STATE_EXERCISE_ING:
                controller.setSeekBarEnable(true);
                timerHandler.postDelayed(updateTimerThread, 0);
                break;
            case Constants.STATE_EXERCISE_NEXT:
                if(now_flag < levelTestArrayList.size() - 1){
//                    if(player.isPlaying()) stopTimer();
//                    if(!player.isPlaying()) startExercise(Constants.STATE_EXERCISE_RESTART);
                    now_flag++;
                    callback.nextFlag();
                    startExercise(Constants.STATE_EXERCISE_BEGIN);
                }
                break;
            case Constants.STATE_EXERCISE_PREV:
                if(now_flag == 0) return;
//                if(player.isPlaying()) stopTimer();
//                if(!player.isPlaying()) startExercise(Constants.STATE_EXERCISE_RESTART);
                now_flag--;
                callback.prevFlag();
                startExercise(Constants.STATE_EXERCISE_BEGIN);
                break;
            case Constants.STATE_EXERCISE_STOP:
                stopTimer();
                break;
            case Constants.STATE_EXERCISE_RESTART:
                if(!isStartTimeRestored) startTime = SystemClock.uptimeMillis();
                isStartTimeRestored = false;
                timerHandler.postDelayed(updateTimerThread, 0);
                break;
            case Constants.STATE_EXERCISE_END:
                timerHandler.removeCallbacks(updateTimerThread);
                callback.onStateExerciseEnd(userTestResultMap);
                break;
        }
    }

    private void stopTimer() {
        timeSwapBuff += timeInMilliseconds;
        startTime = 0;
        timerHandler.removeCallbacks(updateTimerThread);
    }

    private void resetStages() {
        for(TextView textView: textViews)
            textView.setTextColor(defaultColor);
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
        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        total_time.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
    }

    public void setPositionState(int positionState){
        this.positionState = positionState;
    }
    public void setTimerState(long timerState) {
        this.timeSwapBuff = timerState;
        updatedTime = timeSwapBuff + timeInMilliseconds;
        if(updatedTime != 0){
            isStartTimeRestored = true;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            total_time.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
        }
    }

    @OnClick(R.id.next_button)
    void showNextTest(){
        now_flag++;
        callback.nextFlag();
        userTestResultMap.put(levelTestArrayList.get(now_flag - 1).getName(), exercise_count.getText().toString());
        if (now_flag >= levelTestArrayList.size()) {
            startExercise(Constants.STATE_EXERCISE_END);
        } else {
            startExercise(Constants.STATE_EXERCISE_BEGIN);
        }
    }

//    @Nullable
//    @OnClick(R.id.description_next_button)
//    void startTest(){
//        if(exercise_description_view.getVisibility() == View.INVISIBLE) return;
//        exercise_description_view.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.view_slide_out));
//        exercise_description_view.setVisibility(View.INVISIBLE);
//    }

    private void setExercise() {
        setPlayer();
        exercise_count.setText(null);
        exercise_count.setHint("00");

        if(now_flag < levelTestArrayList.size()) {
            callback.onSetExerciseTitle(levelTestArrayList.get(now_flag).getName());
        }
        controller.showStartButton();

        if(prev_exercise_name == null || prev == null) return;
        if(now_flag == 0) {
            this.prev_exercise_name.setText("");
            this.prev.setText("FIRST EXERCISE");
            this.prev_exercise_image.setVisibility(INVISIBLE);
        } else {
            this.prev_exercise_image.setVisibility(VISIBLE);
            this.prev_exercise_name.setText(levelTestArrayList.get(now_flag - 1).getName());
            this.prev.setText("PREV");
            Glide.with(getContext()).load(levelTestArrayList.get(now_flag - 1).getImage()).into(this.prev_exercise_image);
        }
        if(now_flag+1 >= levelTestArrayList.size()) {
            this.next_exercise_name.setText("");
            this.next.setText("LAST EXERCISE");
            this.next_exercise_image.setVisibility(INVISIBLE);
        } else {
            this.next_exercise_image.setVisibility(VISIBLE);
            this.next_exercise_name.setText(levelTestArrayList.get(now_flag + 1).getName());
            this.next.setText("NEXT");
            Glide.with(getContext()).load(levelTestArrayList.get(now_flag + 1).getImage()).into(this.next_exercise_image);
        }
    }

    private void setPlayer() {
        try {
            if(player == null){
                player = new MediaPlayer();
                player.setOnCompletionListener(mp -> {
                    try{
//                        if(player.getDuration() - player.getCurrentPosition() > 1000 )
//                            return;
//                        player.release();
                        if(play_count > 1) {
                            play_count--;

                            player.stop();
                            player.prepare();
                            controller.reset();
                            player.start();
                            controller.restart();
                        } else {
                            exercise_ing_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_out));
                            exercise_ing_view.setVisibility(View.INVISIBLE);
                            exercise_end_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_in));
                            exercise_end_view.setVisibility(View.VISIBLE);
                            complete_text.setText(levelTestArrayList.get(now_flag).getName() + " 동작을 완료하였습니다.");
                            controller.reinit();
                        }
                    }catch (Exception e){
                        Timber.e(e.getMessage());
                    }
                });
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnPreparedListener(this);
            }else{
                player.reset();
            }https:


            player.setDataSource(getContext(), Uri.parse(levelTestArrayList.get(now_flag).getRepeat_video()));
            play_count = levelTestArrayList.get(now_flag).getMeasure_secs() / 20;
            player.prepareAsync();
//            if(now_flag != 0) controller.reset();
//            player.setDataSource(getContext(), Uri.parse("https://www.youtube.com/watch?v=ELrWQ9BXly0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @OnClick(R.id.next_container)
    void next(){
        startExercise(Constants.STATE_EXERCISE_NEXT);
        controller.reinit();
    }

    @Nullable
    @OnClick(R.id.prev_container)
    void prev() {
        startExercise(Constants.STATE_EXERCISE_PREV);
        controller.reinit();
    }

    public void close(){
        SharedPreferences preferences = getContext().getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        player.pause();
        editor.putInt("player_position", player.getCurrentPosition());
        editor.putLong("timer_position", startTime);
        editor.commit();
        controller.close();
        timerHandler.removeCallbacks(updateTimerThread);
        player.stop();
        player.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Timber.d("onPrepared");
        duration = player.getDuration();
        player.seekTo(positionState);
//        controller.setExercise_time(levelTestArrayList.get(now_flag).getMeasure_secs());
        controller.setMediaPlayer(this, positionState, levelTestArrayList.get(now_flag).getMeasure_secs());
        controller.setSeekBarEnable(false);
        positionState = 0;
//        controller.reset();
        Timber.d("duration: "+ duration);
        Timber.d("current position: "+player.getCurrentPosition());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Timber.d("surfaceCreated");
        player.setDisplay(holder);
//        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void start() {
        if(player.getCurrentPosition() == 0){
            startExercise(Constants.STATE_EXERCISE_ING);
        }else{
            startExercise(Constants.STATE_EXERCISE_RESTART);
        }
//        exercise_description_view.setVisibility(View.INVISIBLE);
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
        startExercise(Constants.STATE_EXERCISE_STOP);
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
        callback.onToggleOrientation();
    }

    public long getTimerPosition() {
        return updatedTime;
//        return startTime;
    }

    public interface OnPlayingPopUpViewCallback{
        void onStateExerciseEnd(HashMap<String, String> userTestResultMap);
        void onSetExerciseTitle(String title);
        void onToggleOrientation();
        void nextFlag();
        void prevFlag();
    }

    public void setCallback(OnPlayingPopUpViewCallback callback){
        this.callback = callback;
    }
}
