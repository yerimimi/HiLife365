package com.hilifecare.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hilifecare.R;

import timber.log.Timber;

/**
 * Created by imcreator on 2017. 4. 13..
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    TextView required;
    private Drawable uncompleteButton = getResources().getDrawable(R.drawable.icon_check_uncheck);
    private Drawable completeButton = getResources().getDrawable(R.drawable.icon_check_checked);

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        completeButton = this.getCompoundDrawables()[2];

        required = new TextView(getContext());
        required.setText("*");
        required.setTextColor(getResources().getColor(R.color.colorPrimary));

        handleClearButton();

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.d("text changed");
                CustomEditText.this.handleClearButton();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    void handleClearButton() {
        if (this.getText().toString().equals(""))
        {
            // add the clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], uncompleteButton, this.getCompoundDrawables()[3]);
            this.drawableStateChanged();
            Timber.d("hide complete button");
        }
        else
        {

            //remove clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], completeButton, this.getCompoundDrawables()[3]);
            this.drawableStateChanged();
            Timber.d("show complete button");
        }
    }
}
