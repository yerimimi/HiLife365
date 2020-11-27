package com.hilifecare.ui.exercise;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hilifecare.R;
import com.hilifecare.model.Exercise;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by imcreator on 2017. 6. 22..
 */

public class IndividualExcerciseAdapter extends RecyclerView.Adapter<IndividualExcerciseAdapter.ViewHolder> {
    ArrayList<Exercise> exercises;
    IndividualExerciseAdapterListener listener;
    Context mContext;

    public IndividualExcerciseAdapter(Context mContext, ArrayList<Exercise> exercises, IndividualExerciseAdapterListener listener) {
        this.mContext = mContext;
        this.exercises = exercises;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_program_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String place = "";
        holder.mItem = exercises.get(position);
        holder.exerciseName.setText(holder.mItem.getName());
        holder.level.setText("난이도 : " + holder.mItem.getLevel() + "단계");
        holder.number.setText(String.valueOf(position+1));
        holder.category_value.setText(holder.mItem.getCategory_value());
        //TODO: set image
        Glide
                .with(mContext)
                .load(holder.mItem.getImage())
                .into(holder.image);
        holder.mView.setOnClickListener(v -> listener.onExerciseClicked(holder.mItem));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ExerciseActivity.class);
                i.putExtra("exerciseinfo", holder.mItem);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.exercise_list_child_count)
        TextView number;
        @Bind(R.id.exercise_list_child_name)
        TextView exerciseName;
        @Bind(R.id.exercise_list_child_level)
        TextView level;
        @Bind(R.id.exercise_list_child_category_value)
        TextView category_value;
        @Bind(R.id.exercise_list_child_image)
        ImageView image;
        public Exercise mItem;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }
    }

    interface IndividualExerciseAdapterListener {
        void onExerciseClicked(Exercise exercise);
    }
}
