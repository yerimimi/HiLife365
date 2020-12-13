package com.hilifecare.ui.program;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hilifecare.R;
import com.hilifecare.model.Program;
import com.hilifecare.model.ReformProgram;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Administrator on 2017-02-06.
 */

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder>{
    Context mContext;
    private ArrayList<ReformProgram> rProgramArrayList;
    private ArrayList<Program> programArrayList;
    public ArrayList<String> sampleImage = new ArrayList<>();
    private PopupWindow popupWindow;
    private ImageView cancelButton;
    private HashMap<String, Integer> info_program = new HashMap<String, Integer>();
    private int init_flag;

    public ProgramAdapter(Context c, ArrayList<ReformProgram> rProgramList, ArrayList<Program> programArrayList) {
        this.mContext = c;
        this.rProgramArrayList = rProgramList;
        this.programArrayList = programArrayList;
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FBent%20Knee%20Push-up.PNG?alt=media&token=6ee61d9d-3eba-4763-ae28-b7bb63f83d4a");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FCorkscrew%20Push-up.PNG?alt=media&token=5651d908-790f-468f-8319-6fafc0400f47");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FStandard%20Push-up.PNG?alt=media&token=0d7a4394-aa9c-4290-9564-d863f7dca12a");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FDiamond%20Push-up.PNG?alt=media&token=04dce860-0e1e-4b2a-82e2-de000b011c5b");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FHindu%20Press-up.PNG?alt=media&token=ce2298ce-c75c-499d-8f5c-45b06add4d73");
        sampleImage.add("https://firebasestorage.googleapis.com/v0/b/hilifecare-a7c4e.appspot.com/o/images%2FWide%20Push-up.PNG?alt=media&token=557686d5-af3b-4cc6-aa5f-1a05a4167dca");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.program_name.setText(rProgramArrayList.get(position).getWeek() + " WEEK / DAY " + rProgramArrayList.get(position).getDay());
        holder.program_explain.setText(rProgramArrayList.get(position).getExplain());
        Glide.with(mContext)
                .load(sampleImage.get(position))
                .into(holder.program_image);
        holder.program_time.setText(String.valueOf(rProgramArrayList.get(position).getPlay_tiem()/60));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ExpandableListView construction_listview;
                    int min = 0;
                    int max = 0;
                    init_flag = 0;
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                    View layout = inflater.inflate(R.layout.activity_program_construction,
                            (ViewGroup) v.findViewById(R.id.program_construction_background));
                    popupWindow = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
                    popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);
                    popupWindow.setAnimationStyle(R.style.Animation);
                    popupWindow.update();

                    construction_listview = (ExpandableListView) layout.findViewById(R.id.construction_listview);
                    cancelButton = (ImageView) layout.findViewById(R.id.construction_cancel);

                    ArrayList<String> mGroupList = new ArrayList<String>();
                    ArrayList<ArrayList<HashMap<String, Integer>>> mChildList = new ArrayList<>();
                    ArrayList<HashMap<String, Integer>> mChildListContent = new ArrayList<>();

                    for(int i = 0; i <= position; i++) {
                        if(i > 0)
                            min += rProgramArrayList.get(i-1).getExercise_count();
                        max += rProgramArrayList.get(i).getExercise_count();
                    }

                    for(int i = min; i<max; i++){
                        if(Integer.parseInt(programArrayList.get(i).getStep()) == 0){
                            if(init_flag == 1) {
                                mChildList.add(mChildListContent);
                                mChildListContent = new ArrayList<>();
                            }
                            mGroupList.add(programArrayList.get(i).getType());
                            init_flag = 1;
                        }
                        info_program.put(programArrayList.get(i).getExercise_name(), programArrayList.get(i).getPlay_tiem());
                        mChildListContent.add(info_program);
                        info_program = new HashMap<>();
                    }

                    mChildList.add(mChildListContent);

                    construction_listview.setAdapter(new ProgramConstructionAdapter(layout.getContext(), mGroupList, mChildList));
                    cancelButton.setOnClickListener(view -> popupWindow.dismiss());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rProgramArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView program_image;
        public TextView program_explain;
        public TextView program_name;
        public TextView program_time;

        public ViewHolder(View itemView) {
            super(itemView);
            program_image = (ImageView)itemView.findViewById(R.id.program_image);
            program_explain = (TextView)itemView.findViewById(R.id.program_explain);
            program_name = (TextView)itemView.findViewById(R.id.program_name);
            program_time = (TextView)itemView.findViewById(R.id.program_time);
        }
    }
}
