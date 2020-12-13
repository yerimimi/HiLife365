package com.hilifecare.ui.leveltest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hilifecare.R;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-01-24.
 */

public class LevelTestResultAdapter extends RecyclerView.Adapter<LevelTestResultAdapter.ViewHolder> {
    Context mContext;
    HashMap<String, String> levelTests;

    //TODO:
    public LevelTestResultAdapter(Context context, HashMap<String, String> objects) {
        mContext = context;
        this.levelTests = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_test_result_item,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //TODO:
        String name = (String) levelTests.keySet().toArray()[position];
        String value = levelTests.get(name);
        holder.name.setText(name);
        holder.value.setText(value);

        switch (position) {
            case 0:
                Glide.with(mContext).load(R.drawable.icon_test_pushup).into(holder.image);
                break;
            case 1:
                Glide.with(mContext).load(R.drawable.icon_test_squat).into(holder.image);
                break;
            case 2:
                Glide.with(mContext).load(R.drawable.icon_test_situp).into(holder.image);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return levelTests.keySet().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.leveltest_result_image)
        ImageView image;
        @Bind(R.id.leveltest_result_name)
        TextView name;
        @Bind(R.id.leveltest_result_count)
        TextView value;
        @Bind(R.id.leveltest_result_count_unit)
        TextView unit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
