package com.hilifecare.ui.myrecord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilifecare.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by imcreator on 2017. 6. 21..
 */

public class CalorieRecyclerViewAdpater extends RecyclerView.Adapter<CalorieRecyclerViewAdpater.ViewHolder> {
    String[] dummyDayOfWeek = {"오늘", "수", "화", "월"};
    String[] dummyCalories = {"1,548", "1,500", "1,248", "966"};
    CalorieAdapterListener listener;
    public CalorieRecyclerViewAdpater(Context context, CalorieAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calorie_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.amount.setText(dummyCalories[position]);
        holder.dayOfWeek.setText(dummyDayOfWeek[position]);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(holder.dayOfWeek.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dummyDayOfWeek.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        @Bind(R.id.calorie_amount)
        TextView amount;
        @Bind(R.id.calorie_day_of_week)
        TextView dayOfWeek;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    interface CalorieAdapterListener{
        void onItemSelected(Object item);
    }

}
