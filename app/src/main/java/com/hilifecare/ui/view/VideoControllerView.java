package com.hilifecare.ui.view;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hilifecare.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Formatter;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class VideoControllerView extends FrameLayout {
    public static final String THEME_GREEN = "green";
    public static final String THEME_PURPLE = "purple";
    private static final int COUNT_DOWN = 5;

    private VideoControllerListener mControllerListener;
    private MediaPlayerControl mPlayer;
    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    @Bind(R.id.progress) SeekBar mProgress;
    @Nullable @Bind(R.id.time) TextView mEndTime;
    @Nullable @Bind(R.id.time_current) TextView mCurrentTime;
    private boolean             mShowing;
    private boolean             mDragging;
    private static final int    sDefaultTimeout = 3000;
    private static final int    FADE_OUT = 1;
    private static final int    SHOW_PROGRESS = 2;
    private boolean             mUseFastForward;
    private boolean             mFromXml;
    private boolean             mListenersSet;
    private View.OnClickListener mNextListener, mPrevListener;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;

    @Nullable @Bind(R.id.count_before_start)
    TextView mCount;
    @Nullable @Bind(R.id.ffwd)
    ImageButton mFfwdButton;
    @Nullable @Bind(R.id.rew)
    ImageButton mRewButton;
    @Nullable @Bind(R.id.video_play)
    ImageView mStartImage;
    @Nullable @Bind(R.id.video_start_container)
    FrameLayout mStartContainer;

    //portrait only
    @Nullable @Bind(R.id.video_pause_container)
    FrameLayout mPauseButton;
    @Nullable @Bind(R.id.pause_text)
    TextView mPauseText;
    @Nullable @Bind(R.id.video_full_screen)
    ImageView mFullscreenButton;

    //landscape only
    @Nullable @Bind(R.id.leveltest_name)
    TextView mVideoName;
    @Nullable @Bind(R.id.video_start_pause)
    ImageView mPauseAndPlayButton;
    @Nullable @Bind(R.id.next)
    ImageView mNextButton;
    @Nullable @Bind(R.id.prev)
    ImageView mPrevButton;
    @Nullable @Bind(R.id.video_small_screen)
    LinearLayout mSmallscreenButton;

//    private Handler mHandler = new MessageHandler(this);

    private boolean mAdding = false;
    private CountDownTimer countDownTimer;
    private CountDownTimer leftTimeCountTimer;
    private Handler timerHandler = new Handler();
    private String theme;

    private int exercise_time = 0;
    private int measure_sec;
    private int play_count = 1;
    private int count;

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = null;
        mContext = context;
        mUseFastForward = true;
        mFromXml = true;
    }

    public VideoControllerView(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        mUseFastForward = useFastForward;
    }

    public VideoControllerView(Context context, VideoControllerListener listener) {
        this(context, true);
        this.mControllerListener = listener;

        measure_sec = 20;
        play_count = 1;
        count = 0;
    }

    public VideoControllerView(Context context, int measure_sec, VideoControllerListener listener) {
        this(context, true);
        this.measure_sec = measure_sec;

        Timber.d("JHY Test VideoControllerView : " + play_count + " " + this.measure_sec);
        this.mControllerListener = listener;
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (mRoot != null)
            initControllerView(mRoot);
    }

    public void setMediaPlayer(MediaPlayerControl player, int positionState) {
        mPlayer = player;
        Timber.d("JHY Test setMediaPlayer : " + measure_sec + " " + mPlayer.getDuration()/1000 + " " + exercise_time);
        if(exercise_time == 0) {
            exercise_time = mPlayer.getDuration();
            exercise_time /= 1000;
            exercise_time *= 1000;
            exercise_time *= measure_sec/20;

            play_count = measure_sec / 20;
            count = 0;
        }
        if(positionState != 0){
            leftTimeCountTimer = new CountDownTimer(
                    ((exercise_time - positionState)/1000 + 1) * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    setProgress();
                }

                @Override
                public void onFinish() {

                }
            };
        }
        setProgress();
        updateStartPausePlay();
        updateFullScreen();
    }

    public void setMediaPlayer(MediaPlayerControl player, int positionState, int measure_sec) {
        mPlayer = player;
        Timber.d("JHY Test setMediaPlayer : " + measure_sec + " " + mPlayer.getDuration()/1000 + " " + exercise_time);
        if(exercise_time == 0) {
            exercise_time = mPlayer.getDuration();
            exercise_time /= 1000;
            exercise_time *= 1000;
            exercise_time *= measure_sec/20;

            play_count = measure_sec / 20;
            count = 0;
        }
        if(positionState != 0){
            leftTimeCountTimer = new CountDownTimer(
                    ((exercise_time - positionState)/1000 + 1) * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    setProgress();
                }

                @Override
                public void onFinish() {

                }
            };
        }
        setProgress();
        updateStartPausePlay();
        updateFullScreen();
    }

    public void setAnchorView(ViewGroup view, @Nullable String theme) {
        mAnchor = view;
        this.theme = theme;

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.view_video_controller, null);
        ButterKnife.bind(this, mRoot);
        initControllerView(mRoot);
        initTheme();

        return mRoot;
    }

    private void initTheme() {
        int color;
        if(THEME_GREEN.equals(theme)){
            if(mPauseAndPlayButton == null)
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_green));
            else
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_green_land));
            color = getResources().getColor(R.color.colorGreenBg);
            if(mStartImage != null) mStartImage.setImageResource(R.drawable.icon_play);
        }else if(THEME_PURPLE.equals(theme)){
            if(mPauseAndPlayButton == null)
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_purple));
            else
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_purple_land));
            color = getResources().getColor(R.color.colorPurpleBg);
            if(mStartImage != null) mStartImage.setImageResource(R.drawable.icon_play_purple);
        }else{
            color = getResources().getColor(R.color.colorPrimary);
            if(mStartImage != null) mStartImage.setImageResource(R.drawable.icon_play_purple);
        }
        if(mCount != null) mCount.setTextColor(color);
        if(mPauseText != null) mPauseText.setTextColor(color);
    }

    private void initControllerView(View v) {
        if (mFfwdButton != null && !mFromXml) {
            mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
        }

        if (mRewButton != null && !mFromXml) {
            mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
        }

        // By default these are hidden. They will be enabled when setPrevNextListeners() is called
        if (mNextButton != null && !mFromXml && !mListenersSet) {
            mNextButton.setVisibility(View.GONE);
        }
        if (mPrevButton != null && !mFromXml && !mListenersSet) {
            mPrevButton.setVisibility(View.GONE);
        }

        if (mProgress != null) {
            mProgress.setOnSeekBarChangeListener(mSeekListener);
            mProgress.getThumb().mutate().setAlpha(0);
            mProgress.setMax(1000);
        }

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }else if(mPauseAndPlayButton != null) {
                mPauseAndPlayButton.requestFocus();
            }

            FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
            );

            mAnchor.addView(this, tlp);
        }
        updateStartPausePlay();
        updateFullScreen();
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void setLeftTimeCountTimer() {
        leftTimeCountTimer = new CountDownTimer((exercise_time /1000 + 1) * 1000, 1000) {
            @Override
            public void onTick(long l) {
                setProgress();
            }

            @Override
            public void onFinish() {

            }
        };
        leftTimeCountTimer.start();
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        try{
            int position = mPlayer.getCurrentPosition();
            int duration = exercise_time;
            if (mProgress != null) {
                if (duration > 0) {
                    // use long to avoid overflow
                    long pos = 1000L * (position + (20000 * count)) / duration;
                    mProgress.setProgress( 1000 - ((int) pos));
                }
                int percent = mPlayer.getBufferPercentage()*(measure_sec/20);
                mProgress.setSecondaryProgress(percent * 10);
            }

            if (mEndTime != null)
                mEndTime.setText(stringForTime(duration));
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime(duration - position - (20000 * count)));

            Timber.d("JHY Test setProgress : " + mCurrentTime.getText() + " " + count + " " + play_count);

            return position;
        }catch (IllegalStateException e){
            Timber.e("wrong moment: player may not ready");
            return 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Timber.d("onTouchEvent is Pause");
        if (mPlayer != null && mPlayer.isPlaying()) doStartPauseResume();
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        Timber.d("onTrackballEvent");
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (mPlayer == null) {
//            return true;
//        }
//
//        int keyCode = event.getKeyCode();
//        final boolean uniqueDown = event.getRepeatCount() == 0
//                && event.getAction() == KeyEvent.ACTION_DOWN;
//        if (keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK
//                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
//                || keyCode == KeyEvent.KEYCODE_SPACE) {
//            if (uniqueDown) {
//                doStartPauseResume();
//                if (mPauseButton != null) {
//                    mPauseButton.requestFocus();
//                }
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
//            if (uniqueDown && !mPlayer.isPlaying()) {
//                mPlayer.start();
//                setStartPlayVisibility();
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
//                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
//            if (uniqueDown && mPlayer.isPlaying()) {
//                mPlayer.pause();
//                setPausePlayVisibility();
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
//                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
//                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
//            // don't show the controls for volume adjustment
//            return super.dispatchKeyEvent(event);
//        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
//            if (uniqueDown) {
//                hide();
//            }
//            return true;
//        }

        return super.dispatchKeyEvent(event);
    }

    @Nullable
    @OnClick({R.id.video_start_button,R.id.video_pause_container, R.id.video_start_pause})
    void start(){
        doStartPauseResume();
    }

    @Nullable
    @OnClick({R.id.video_full_screen, R.id.video_small_screen})
    void fullscreen(){
        doToggleFullscreen();
        updateFullScreen();
    }

    public void setStartPlayVisibility(int visibility) {
        if (mRoot == null || mPauseButton == null || mPlayer == null) {
            updateLandStartPausePlay();
            return;
        }

        mStartContainer.setVisibility(visibility);
//        mStartImage.setVisibility(visibility);
    }

    public void setPausePlayVisibility(int visibility) {
        if (mRoot == null || mPauseButton == null || mPlayer == null) {
            updateLandStartPausePlay();
            return;
        }

        mPauseButton.setVisibility(visibility);
    }

    public void updateStartPausePlay() {
        if (mRoot == null || mPauseButton == null || mPlayer == null) {
            updateLandStartPausePlay();
            return;
        }

        if (mPlayer.isPlaying()) {
            mPauseButton.setVisibility(View.INVISIBLE);
            mStartContainer.setVisibility(View.GONE);
            mStartImage.setVisibility(View.INVISIBLE);
        } else if(mPlayer.getCurrentPosition() == 0){
            mStartContainer.setVisibility(View.VISIBLE);
//            mStartImage.setVisibility(View.VISIBLE);
            mPauseButton.setVisibility(View.INVISIBLE);
        } else {
            mPauseButton.setVisibility(View.VISIBLE);
            mStartContainer.setVisibility(View.GONE);
            mStartImage.setVisibility(View.INVISIBLE);
        }
    }

    private void updateLandStartPausePlay() {
        if (mRoot == null || mPauseAndPlayButton == null || mPlayer == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mVideoName.setVisibility(View.VISIBLE);
            mSmallscreenButton.setVisibility(View.INVISIBLE);
            mStartContainer.setVisibility(View.GONE);
            //TODO:play image
            IconicsDrawable iconEmail = new IconicsDrawable(getContext())
                    .icon(GoogleMaterial.Icon.gmd_pause)
                    .color(getResources().getColor(R.color.colorPrimary))
                    .sizeDp(24);
            mPauseAndPlayButton.setImageDrawable(iconEmail);
        } else {
            mVideoName.setVisibility(View.INVISIBLE);
            mSmallscreenButton.setVisibility(View.VISIBLE);
            mStartContainer.setVisibility(View.VISIBLE);
            mPauseAndPlayButton.setImageResource(R.drawable.icon_fullscreen_play);
        }
    }

    public void updateFullScreen() {
        if (mRoot == null || mFullscreenButton == null || mPlayer == null) {
            return;
        }

        if (mPlayer.isFullScreen()) {
            mFullscreenButton.setVisibility(View.GONE);
//            mFullscreenButton.setImageResource(R.drawable.ic_media_fullscreen_shrink);
        }
        else {
            mFullscreenButton.setVisibility(View.VISIBLE);

//            mFullscreenButton.setImageResource(R.drawable.ic_media_fullscreen_stretch);
        }
    }

    private void doStartPauseResume() {
        if (mPlayer == null) {
            return;
        }

        if (!mPlayer.isPlaying()) {
            if (mPlayer.getCurrentPosition() == 0) {
                countDownTimer();
                return;
            } else {
                mPlayer.start();
                leftTimeCountTimer.start();
            }
        }else{
            mPlayer.pause();
            leftTimeCountTimer.cancel();
        }
        updateStartPausePlay();
    }

    private void countDownTimer() {
        mCount.setVisibility(View.VISIBLE);
        mStartContainer.setVisibility(View.GONE);
        if(mStartImage != null) mStartImage.setVisibility(View.INVISIBLE);
        countDownTimer = new CountDownTimer((COUNT_DOWN + 1) * 1000, 1000) {
            @Override
            public void onTick(long l) {
                mCount.setText(""+(l/1000));
            }

            @Override
            public void onFinish() {
                mCount.setVisibility(View.GONE);
                mPlayer.start();
                setLeftTimeCountTimer();
            }
        };
        countDownTimer.start();
    }

    private void doToggleFullscreen() {
        if (mPlayer == null) {
            return;
        }

        mPlayer.toggleFullScreen();
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
//            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
//            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (mPlayer == null) {
                return;
            }

            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = exercise_time;
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo( ((int)duration) - ((int) newposition));
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime( (int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updateStartPausePlay();
//            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
//            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mFfwdButton != null) {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null) {
            mRewButton.setEnabled(enabled);
        }
        if (mNextButton != null) {
            mNextButton.setEnabled(enabled && mNextListener != null);
        }
        if (mPrevButton != null) {
            mPrevButton.setEnabled(enabled && mPrevListener != null);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
//        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VideoControllerView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VideoControllerView.class.getName());
    }

    @Nullable
    @OnClick(R.id.rew)
    void rew(){
        if (mPlayer == null) {
            return;
        }

        int pos = mPlayer.getCurrentPosition();
        pos -= 5000; // milliseconds
        mPlayer.seekTo(pos);
        setProgress();

//        show(sDefaultTimeout);
    }

    @Nullable
    @OnClick(R.id.ffwd)
    void ffwd(){
        if (mPlayer == null) {
            return;
        }

        int pos = mPlayer.getCurrentPosition();
        pos += 15000; // milliseconds
        mPlayer.seekTo(pos);
        setProgress();

    }

    private void installPrevNextListeners() {
        if (mNextButton != null) {
            mNextButton.setOnClickListener(mNextListener);
            mNextButton.setEnabled(mNextListener != null);
        }

        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(mPrevListener);
            mPrevButton.setEnabled(mPrevListener != null);
        }
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {
        mNextListener = next;
        mPrevListener = prev;
        mListenersSet = true;

        if (mRoot != null) {
//            installPrevNextListeners();
            Timber.d("next button: "+mNextButton+", prev button: "+mPrevButton);
            if (mNextButton != null && !mFromXml) {
                mNextButton.setVisibility(View.VISIBLE);
            }
            if (mPrevButton != null && !mFromXml) {
                mPrevButton.setVisibility(View.VISIBLE);
            }
        }
        installPrevNextListeners();
    }

    public void showStartButton() {
        setStartPlayVisibility(View.VISIBLE);
    }

    public void reset() {
        leftTimeCountTimer.cancel();
        setProgress();
//        setLeftTimeCountTimer();
    }

    public void restart() {
        leftTimeCountTimer.cancel();
        leftTimeCountTimer.start();
        play_count--;
        count++;
    }

    public void reinit() {
        exercise_time = 0;
    }

    public void close() {
        if(leftTimeCountTimer != null) leftTimeCountTimer.cancel();
        if(countDownTimer != null) countDownTimer.cancel();
    }

//    private static class MessageHandler extends Handler {
//        private final WeakReference<VideoControllerView> mView;
//
//        MessageHandler(VideoControllerView view) {
//            mView = new WeakReference<VideoControllerView>(view);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            VideoControllerView view = mView.get();
//            if (view == null || view.mPlayer == null) {
//                return;
//            }
//
//            int pos;
//            switch (msg.what) {
//                case FADE_OUT:
//                    view.hide();
//                    break;
//                case SHOW_PROGRESS:
//                    pos = view.setProgress();
////                    if (!view.mDragging && view.mShowing && view.mPlayer.isPlaying()) {
//                    if (!view.mDragging && view.mPlayer.isPlaying()) {
//                        msg = obtainMessage(SHOW_PROGRESS);
//                        sendMessageDelayed(msg, 1000 - (pos % 1000));
//                    }
//                    break;
//            }
//        }
//    }

    public void setExercise_time(int exercise_time) {
        this.exercise_time = exercise_time;
        this.exercise_time *= 1000;
    }

    public void setSeekBarEnable(boolean flag){
        mProgress.setEnabled(flag);
    }
}