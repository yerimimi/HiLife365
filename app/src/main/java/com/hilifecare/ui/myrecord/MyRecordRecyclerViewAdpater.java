package com.hilifecare.ui.myrecord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.ui.leveltest.LevelTestAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by imcreator on 2017. 6. 21..
 */

public class MyRecordRecyclerViewAdpater extends RecyclerView.Adapter<MyRecordRecyclerViewAdpater.ViewHolder> {
    String[] bodyNames;
    String[] bodyEnglishNames;
    int[] bodyImages = {R.drawable.icon_pectoralis, R.drawable.icon_human_back, R.drawable.icon_thigh,
            R.drawable.icon_calf, R.drawable.icon_brachialis, R.drawable.icon_forearm,
            R.drawable.icon_abdominal};
    public MyRecordRecyclerViewAdpater(Context context) {
        bodyNames = context.getResources().getStringArray(R.array.body_part);
        bodyEnglishNames = context.getResources().getStringArray(R.array.body_part_en);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_record_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageResource(bodyImages[position]);
        holder.name.setText(bodyNames[position]);
        holder.nameEn.setText(bodyEnglishNames[position]);
    }

    @Override
    public int getItemCount() {
        return bodyNames.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.leveltest_result_image)
        ImageView image;
        @Bind(R.id.leveltest_result_name_en)
        TextView nameEn;
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
