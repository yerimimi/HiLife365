package com.hilifecare.ui.program;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hilifecare.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017-02-08.
 */

public class ProgramConstructionAdapter extends BaseExpandableListAdapter {
    private ArrayList<String> groupList = null;
    private ArrayList<ArrayList<HashMap<String, Integer>>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private Context mContext;
    public ArrayList<String> sampleImage = new ArrayList<>();

    public ProgramConstructionAdapter(Context c, ArrayList<String> groupList, ArrayList<ArrayList<HashMap<String, Integer>>> childList) {
        super();
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FBent%20Knee%20Push-up.PNG?alt=media&token=6ee61d9d-3eba-4763-ae28-b7bb63f83d4a");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FCorkscrew%20Push-up.PNG?alt=media&token=5651d908-790f-468f-8319-6fafc0400f47");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FStandard%20Push-up.PNG?alt=media&token=0d7a4394-aa9c-4290-9564-d863f7dca12a");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FDiamond%20Push-up.PNG?alt=media&token=04dce860-0e1e-4b2a-82e2-de000b011c5b");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FHindu%20Press-up.PNG?alt=media&token=ce2298ce-c75c-499d-8f5c-45b06add4d73");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FWide%20Push-up.PNG?alt=media&token=557686d5-af3b-4cc6-aa5f-1a05a4167dca");
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return null;
    }

    public Map.Entry<String, Integer> customGetChild(int groupPosition, int childPosition) {
        Set<Map.Entry<String, Integer>> set = childList.get(groupPosition).get(childPosition).entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = set.iterator();
        return iterator.next();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.exercise_list_parent, parent, false);
            viewHolder.tv_groupName = (TextView)v.findViewById(R.id.exercise_list_parent);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_groupName.setText(getGroup(groupPosition));
        //expand child
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;

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

        int imageCount = 0;
        if(groupPosition == 0)
            imageCount = groupPosition+childPosition;
        else if(groupPosition == 1)
            imageCount = groupPosition*2+childPosition;
        else
            imageCount = groupPosition*3-1;

        Glide.with(mContext)
                .load(sampleImage.get(imageCount))
                .into(viewHolder.iv_image);
        viewHolder.tv_childName.setText(customGetChild(groupPosition, childPosition).getKey());
        viewHolder.tv_childTime.setText(String.valueOf(customGetChild(groupPosition, childPosition).getValue()));
        viewHolder.tv_childCount.setText(String.valueOf(childPosition+1));
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder{
        public ImageView iv_image;
        public TextView tv_groupName;
        public TextView tv_childName;
        public TextView tv_childTime;
        public TextView tv_childCount;
    }

}
