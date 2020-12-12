package com.hilifecare.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hilifecare.R;

import java.util.ArrayList;

/**
 * Created by imcreator on 2017. 5. 24..
 */

public class PhaseMaker {
    public static ArrayList<TextView> makeTextView(Context context, int num){
        ArrayList<TextView> textViews = new ArrayList<>();
        Typeface aeroMatics = Typeface.createFromAsset(context.getAssets(), "fonts/Aero-Matics-Display-Regular.ttf");
        for (int i = 0; i < num; i++) {

            TextView phase = new TextView(context);
            phase.setTypeface(aeroMatics);
            phase.setText(Integer.toString(i + 1));
            phase.setTextSize(18f);
            phase.setPadding(0, 0, 20, 0);
            phase.setId(i + 1);
            textViews.add(phase);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textViews.get(i).setLayoutParams(lp);
        }
        return  textViews;
    }
}
