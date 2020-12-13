package com.hilifecare.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilifecare.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by imcreator on 2017. 4. 17..
 */

public class CustomDialog{
    public static final int TYPE_ONEBUTTON = 1;
    public static final int TYPE_TWOBUTTON = 2;
    @IntDef({TYPE_TWOBUTTON, TYPE_ONEBUTTON})
    @Retention(RetentionPolicy.SOURCE)
    @interface SharingState {}

    AlertDialog dialog;
    View.OnClickListener cancelListener;
    View.OnClickListener okListener;

    @Bind(R.id.dialog_message)
    TextView message;
    @Bind(R.id.dialog_button_ok)
    Button ok;
    @Bind(R.id.cancel)
    ImageView cancel;
    Button cancel2;

    public CustomDialog(Context context, @SharingState int type) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView;
        if(type == TYPE_ONEBUTTON){
            dialogView = inflater.inflate(R.layout.dialog_confirm, null);
        }else{
            dialogView = inflater.inflate(R.layout.dialog_confirm_two, null);
            cancel2 = (Button)dialogView.findViewById(R.id.dialog_button_cancel);
        }

        dialogBuilder.setView(dialogView);
        ButterKnife.bind(this, dialogView);

        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancel.setOnClickListener(v -> dialog.dismiss());
        if(cancel2 != null) cancel2.setOnClickListener(v -> dialog.dismiss());
    }

    public CustomDialog setMessage(String message) {
        this.message.setText(message);
        return this;
    }

    public CustomDialog setCancelClickListener(View.OnClickListener listener){
        cancelListener = listener;
        cancel.setOnClickListener(cancelListener);
        if(cancel2 != null) cancel2.setOnClickListener(cancelListener);
        return this;
    }

    public CustomDialog setOkText(String text){
        ok.setText(text);
        return this;
    }

    public CustomDialog setOkClickListener(View.OnClickListener listener){
        okListener = listener;
        ok.setOnClickListener(okListener);
        return this;
    }

    public void showDialog(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public AlertDialog getCustomDialog(){
        return dialog;
    }
}
