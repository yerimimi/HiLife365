package com.hilifecare.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by imcreator on 2017. 6. 20..
 */

public class HumanBodyView extends View {
    private TextPaint countPaint;

    public HumanBodyView(Context context) {
        super(context);
        init();
    }

    public HumanBodyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        countPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        countPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        countPaint.setTextSize(18f * getResources().getDisplayMetrics().scaledDensity);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
    }
}
