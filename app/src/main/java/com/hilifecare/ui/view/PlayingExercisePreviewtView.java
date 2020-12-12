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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.application.Constants;
import com.hilifecare.model.PreviewProgram;
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

public class PlayingExercisePreviewtView extends FrameLayout
    implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayerControl{
    //Playing_exercise_preview_view
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

    //sub start
    @Bind(R.id.sub_start)
    RelativeLayout subStartLayout;
    @Bind(R.id.preivew_substart_next_button)
    LinearLayout subStartNextButton;

    //complete
    @Bind(R.id.preview_end)
    RelativeLayout endLayout;
    @Bind(R.id.complete_text)
    TextView complete_text;
    @Bind(R.id.preview_end_next_button)
    LinearLayout endNextButton;

    ArrayList<TextView> textViews = new ArrayList<TextView>();
    ArrayList<PreviewProgram> previewProgramArrayList;

    int now_flag = 0;

    long startTime = 0L;
    private Handler timerHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    OnPlayingPopUpViewCallback callback;
    private VideoControllerView controller;
    private MediaPlayer player;
    private int positionState;
    private int color;
    private int defaultColor;

    public PlayingExercisePreviewtView(@NonNull Context context) {
        super(context);
    }

    public PlayingExercisePreviewtView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayingExercisePreviewtView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(ArrayList<PreviewProgram> previewProgramArrayList, OnPlayingPopUpViewCallback callback) {
        this.callback = callback;
        this.previewProgramArrayList = previewProgramArrayList;
        color = getResources().getColor(R.color.colorGreenBg);
        defaultColor = getResources().getColor(R.color.colorText);
        LayoutInflater infalter = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        view = infalter.inflate(R.layout.playing_exercise_preview_view, this, false);
        addView(view);
        ButterKnife.bind(this, view);

        initVideoController(0);

        textViews = PhaseMaker.makeTextView(view.getContext(), previewProgramArrayList.size());
        for(TextView tv: textViews) container.addView(tv);

        startExercise(Constants.STATE_EXERCISE_BEGIN);
    }

    private void initVideoController(int orientation) {
        SurfaceHolder videoHolder = exercise_video.getHolder();
        videoHolder.addCallback(this);

        controller = new VideoControllerView(getContext(),
                new VideoControllerListener() {
                });
        controller.setAnchorView(exercise_ing_view, VideoControllerView.THEME_GREEN);//(FrameLayout) findViewById(R.id.videoSurfaceContainer));
        controller.setPrevNextListeners(v -> next(), v -> prev());
    }

    private void startExercise(int state) {
        resetStages();
        textViews.get(now_flag).setTextColor(color);

        switch (state) {
            case Constants.STATE_EXERCISE_BEGIN:
                if(now_flag != 0){
                    endLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_out));
                    endLayout.setVisibility(View.INVISIBLE);
                }
                if(subStartLayout != null) {
                    subStartLayout.setVisibility(View.VISIBLE);
                    exercise_ing_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_in));
                    exercise_ing_view.setVisibility(View.VISIBLE);
                }
                setExercise();
                break;
            case Constants.STATE_EXERCISE_ING:
                if (startTime == 0L) {
                    startTime = SystemClock.uptimeMillis();
                    timerHandler.postDelayed(updateTimerThread, 0);
                }
                break;
            case Constants.STATE_EXERCISE_NEXT:
                if(now_flag < previewProgramArrayList.size() - 1){
                    player.stop();
                    now_flag++;
                    startExercise(Constants.STATE_EXERCISE_BEGIN);
                }
                break;
            case Constants.STATE_EXERCISE_PREV:
                if(now_flag == 0) return;
                player.stop();
                now_flag--;
                startExercise(Constants.STATE_EXERCISE_BEGIN);
                break;
            case Constants.STATE_EXERCISE_STOP:
                timeSwapBuff += timeInMilliseconds;
                timerHandler.removeCallbacks(updateTimerThread);
                break;
            case Constants.STATE_EXERCISE_RESTART:
                startTime = SystemClock.uptimeMillis();
                timerHandler.postDelayed(updateTimerThread, 0);
                break;
            case Constants.STATE_EXERCISE_END:
                timerHandler.removeCallbacks(updateTimerThread);
                callback.onStateExerciseEnd(null);
                break;
        }
    }

    private void resetStages() {
        for(TextView textView: textViews)
            textView.setTextColor(defaultColor);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            total_time.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
            timerHandler.postDelayed(this, 0);
        }
    };

    @OnClick(R.id.preview_end_next_button)
    void endNext(){
        now_flag++;
        endLayout.setVisibility(View.INVISIBLE);
        if (now_flag >= previewProgramArrayList.size()) {
            startExercise(Constants.STATE_EXERCISE_END);
        } else {
            startExercise(Constants.STATE_EXERCISE_BEGIN);
        }
    }

    @Nullable
    @OnClick(R.id.preivew_substart_next_button)
    void subStartNext(){
        subStartLayout.setVisibility(View.INVISIBLE);
        setExercise();
    }

    private void setExercise() {
        setPlayer();

        if(now_flag < previewProgramArrayList.size()) {
            callback.onSetExerciseTitle(previewProgramArrayList.get(now_flag).getExercise_name());
        }
        //TODO: set next image
        //this.next_exercise_image.setImageResource(next_exercise_image);
        if(now_flag == 0) {
            //TODO: first exercise image input
            this.prev_exercise_name.setText("");
            this.prev.setText("FIRST EXERCISE");
        } else {
            this.prev_exercise_name.setText(previewProgramArrayList.get(now_flag - 1).getExercise_name());
            this.prev.setText("PREV");
        }
        try {
            this.next_exercise_name.setText(previewProgramArrayList.get(now_flag + 1).getExercise_name());
            this.next.setText("NEXT");
        } catch (Exception e) {
            this.next_exercise_name.setText("");
            this.next.setText("LAST EXERCISE");
        }
        controller.showStartButton();
    }

    private void setPlayer() {
        try {
            if(player == null){
                player = new MediaPlayer();
                player.setOnCompletionListener(mp -> {
                    try {
                        if (player.getDuration() - player.getCurrentPosition() != 0) return;
                        exercise_ing_view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_out));
                        endLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_slide_in));
                        endLayout.setVisibility(View.VISIBLE);
                        complete_text.setText(previewProgramArrayList.get(now_flag).getExercise_name() + " 동작을 완료하였습니다.");
                    }catch (Exception e){
                        Timber.e(e.getMessage());
                    }
                });
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnPreparedListener(this);
            }else{
                player.reset();
            }

            player.setDataSource(getContext(), Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
            player.prepareAsync();
            controller.reset();
//            player.setDataSource(getContext(), Uri.parse("https://www.youtube.com/watch?v=ELrWQ9BXly0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @OnClick(R.id.next_container)
    void next(){
        startExercise(Constants.STATE_EXERCISE_NEXT);
    }

    @Nullable
    @OnClick(R.id.prev_container)
    void prev() {
        startExercise(Constants.STATE_EXERCISE_PREV);
    }

    public void close(){
        SharedPreferences preferences = getContext().getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        player.pause();
        editor.putInt("player_position", player.getCurrentPosition());
        editor.commit();
        controller.close();
        timerHandler.removeCallbacks(updateTimerThread);
        player.stop();
        player.release();
    }

    public void setPositionState(int positionState){
        this.positionState = positionState;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Timber.d("onPrepared");
        player.seekTo(positionState);
        controller.setMediaPlayer(this, positionState, 0);
        positionState = 0;
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    public void start() {
        if(player.getCurrentPosition() == 0){
            startExercise(Constants.STATE_EXERCISE_ING);
        }else{
            startExercise(Constants.STATE_EXERCISE_RESTART);
        }
        subStartLayout.setVisibility(View.INVISIBLE);
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
        startExercise(Constants.STATE_EXERCISE_STOP);
    }

    @Override
    public int getDuration() {
        return player.getDuration();
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

    public interface OnPlayingPopUpViewCallback{
        void onStateExerciseEnd(HashMap<String, String> userTestResultMap);
        void onSetExerciseTitle(String title);
        void onToggleOrientation();
    }

    public void setCallback(OnPlayingPopUpViewCallback callback){
        this.callback = callback;
    }
}
