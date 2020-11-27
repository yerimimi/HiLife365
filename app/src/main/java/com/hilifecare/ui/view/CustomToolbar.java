package com.hilifecare.ui.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilifecare.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by imcreator on 2017. 5. 12..
 */

public class CustomToolbar extends FrameLayout {
    private String theme;
    View view;
    OnClickListener rightListener;
    OnClickListener leftListener;
    int rightVisibility;
    int leftVisibility;
    TextView toolbarName;
    ImageView toolbarRight;
    ImageView toolbarLeft;
    View shadow;

    public CustomToolbar(@NonNull Context context) {
        super(context);
        theme = "blue";
        initView(theme);
    }

    public CustomToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        theme = context.obtainStyledAttributes(attrs, R.styleable.CustomViewColor)
                .getString(R.styleable.CustomViewColor_type);
        initView(theme);
    }

    public CustomToolbar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        theme = context.obtainStyledAttributes(attrs, R.styleable.CustomViewColor)
                .getString(R.styleable.CustomViewColor_type);
        initView(theme);
    }

    public void initView(String theme){
        this.theme = theme;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        if(view != null) removeView(view); view = null;
        if("green".equals(theme)){
            view = inflater.inflate(R.layout.toolbar_green, this, false);
        }else if("purple".equals(theme)){
            view = inflater.inflate(R.layout.toolbar_purple, this, false);
        }else if("orange".equals(theme)){
            view = inflater.inflate(R.layout.toolbar_orange, this, false);
        }else if("solid_green".equals(theme)){
            view = inflater.inflate(R.layout.toolbar_setting, this, false);
        }else if("solid_blue".equals(theme)){
            view = inflater.inflate(R.layout.toolbar_signup, this, false);
        }else if("yellowgreen".equals(theme)){
            view = inflater.inflate(R.layout.toolbar_yellowgreen, this, false);
        }else if("pink".equals(theme)){
            view = inflater.inflate(R.layout.toolbar_pink, this, false);
        } else{
            view = inflater.inflate(R.layout.toolbar, this, false);
        }
        addView(view);
        toolbarName = (TextView) view.findViewById(R.id.toolbar_name);
        toolbarLeft = (ImageView) view.findViewById(R.id.toolbar_left);
        toolbarRight = (ImageView) view.findViewById(R.id.toolbar_right);
        shadow = view.findViewById(R.id.toolbar_shadow);
        toolbarLeft.setOnClickListener(leftListener);
        toolbarRight.setOnClickListener(rightListener);
        toolbarLeft.setVisibility(leftVisibility);
        toolbarRight.setVisibility(rightVisibility);
    }

    public void setName(String name){
        toolbarName.setText(name);
    }

    public void setToolbarRight(OnClickListener listener){
        rightListener = listener;
        toolbarRight.setOnClickListener(listener);
    }

    public void setToolbarLeft(OnClickListener listener){
        leftListener = listener;
        toolbarLeft.setOnClickListener(listener);
    }

    public void setToolbarRightVisibility(int visibility){
        rightVisibility = visibility;
        toolbarRight.setVisibility(visibility);
    }

    public void setToolbarLeftVisibility(int visibility){
        leftVisibility = visibility;
        toolbarLeft.setVisibility(visibility);
    }

    public void setShadowVisibility(int visibility){
        if(shadow != null) shadow.setVisibility(visibility);
    }
}
