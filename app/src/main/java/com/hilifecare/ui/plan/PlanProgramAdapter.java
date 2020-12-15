package com.hilifecare.ui.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hilifecare.R;
import com.hilifecare.model.Plan;
import com.hyoil.hipreslogic.info.HiExerciseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-02-21.
 */

public class PlanProgramAdapter extends ArrayAdapter<HiExerciseInfo> {
    private List<HiExerciseInfo> eiList = null;

    Context mContext;
    private ViewHolder viewHolder = null;

    private LayoutInflater inflater;
    public ArrayList<String> sampleImage = new ArrayList<>();

    public PlanProgramAdapter(Context context, List<HiExerciseInfo> objects) {
        super(context, 0, objects);
        mContext = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        eiList = objects;
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FBent%20Knee%20Push-up.PNG?alt=media&token=6ee61d9d-3eba-4763-ae28-b7bb63f83d4a");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FCorkscrew%20Push-up.PNG?alt=media&token=5651d908-790f-468f-8319-6fafc0400f47");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FStandard%20Push-up.PNG?alt=media&token=0d7a4394-aa9c-4290-9564-d863f7dca12a");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FDiamond%20Push-up.PNG?alt=media&token=04dce860-0e1e-4b2a-82e2-de000b011c5b");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FHindu%20Press-up.PNG?alt=media&token=ce2298ce-c75c-499d-8f5c-45b06add4d73");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FWide%20Push-up.PNG?alt=media&token=557686d5-af3b-4cc6-aa5f-1a05a4167dca");

    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.exercise_list_child, null);
            viewHolder.tv_childName = (TextView) v.findViewById(R.id.exercise_list_child_name);
            viewHolder.iv_image = (ImageView) v.findViewById(R.id.exercise_list_child_image);
            viewHolder.tv_childTime = (TextView) v.findViewById(R.id.exercise_list_child_time);
            viewHolder.tv_childCount = (TextView) v.findViewById(R.id.exercise_list_child_count);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

//        int imageCount = 0;
//        if(groupPosition == 0)
//            imageCount = groupPosition+childPosition;
//        else if(groupPosition == 1)
//            imageCount = groupPosition*2+childPosition;
//        else
//            imageCount = groupPosition*3-1;
//
//        Glide.with(mContext)
//                .load(sampleImage.get(imageCount))
//                .into(viewHolder.iv_image);
        viewHolder.tv_childName.setText(eiList.get(position).getName());
//        viewHolder.tv_childTime.setText(String.valueOf(customGetChild(groupPosition, childPosition).getValue()));
//        viewHolder.tv_childCount.setText(String.valueOf(childPosition+1));
        return v;
    }

    class ViewHolder{
        public ImageView iv_image;
        public TextView tv_childName;
        public TextView tv_childTime;
        public TextView tv_childCount;
    }

}
