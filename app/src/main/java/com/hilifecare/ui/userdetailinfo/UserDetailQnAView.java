package com.hilifecare.ui.userdetailinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hilifecare.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by imcreator on 2017. 6. 13..
 */

public class UserDetailQnAView extends FrameLayout {
    private QnACallback callback;

    public UserDetailQnAView(@NonNull Context context, QnACallback callback) {
        super(context);
        this.callback = callback;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.user_detail_qna, this, false);
        ButterKnife.bind(this,layout);
        addView(layout);
    }

    @OnClick(R.id.cancel)
    void cancel(){
        callback.onCancel();
    }

    @OnClick(R.id.user_detail_qna_prev)
    void prev(){
        //TODO: if prev question exists
    }

    @OnClick(R.id.user_detail_qna_next)
    void next() {
        //TODO: if question is answered
    }

    public interface QnACallback{
        void onCancel();
    }
}
