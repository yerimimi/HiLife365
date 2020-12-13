package com.hilifecare.ui.view;

/**
 * Created by imcreator on 2017. 5. 18..
 */

public interface MediaPlayerControl {
    void    start();
    void    pause();
    int     getDuration();
    int     getCurrentPosition();
    void    seekTo(int pos);
    boolean isPlaying();
    int     getBufferPercentage();
    boolean canPause();
    boolean canSeekBackward();
    boolean canSeekForward();
    boolean isFullScreen();
    void    toggleFullScreen();
}
