package com.hilifecare.ui.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilifecare.R;
import com.hyoil.hipreslogic.info.HiExerciseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-02-21.
 */

public class PlanAddAdapter extends BaseAdapter {
    private List<HiExerciseInfo> eiList = null;


    public PlanAddAdapter(List<HiExerciseInfo> objects) {
        eiList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder;

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (v == null) {
            v = inflater.inflate(R.layout.plan_add_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_childCount = (TextView) v.findViewById(R.id.plan_add_count);
            viewHolder.tv_childName = (TextView) v.findViewById(R.id.plan_add_name);
            viewHolder.tv_childExTime = (TextView) v.findViewById(R.id.plan_add_exercise_time);
            viewHolder.tv_childRsTime = (TextView) v.findViewById(R.id.plan_add_rest_time);
            viewHolder.tv_childSetCount = (TextView) v.findViewById(R.id.plan_add_set_count);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int count = position + 1;
        viewHolder.tv_childCount.setText(Integer.toString(count));
        viewHolder.tv_childExTime.setText("1");
        viewHolder.tv_childRsTime.setText("2");
        viewHolder.tv_childSetCount.setText("3");
        return v;
    }

    public int getCount() {
        return eiList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public HiExerciseInfo getItem(int position) {
        return eiList.get(position);
    }

    class ViewHolder {
        public TextView tv_childCount;
        public TextView tv_childName;
        public TextView tv_childExTime;
        public TextView tv_childRsTime;
        public TextView tv_childSetCount;
    }

}
