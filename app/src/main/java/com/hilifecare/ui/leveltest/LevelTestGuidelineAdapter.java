package com.hilifecare.ui.leveltest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.model.LevelTest;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-06-19.
 */

public class LevelTestGuidelineAdapter extends RecyclerView.Adapter<LevelTestGuidelineAdapter.ViewHolder> {
    Context context;
    ArrayList<LevelTest> levelTests;
    View men_guideline;
    View women_guideline;

    public LevelTestGuidelineAdapter(Context context, ArrayList<LevelTest> objects) {
        this.context = context;
        this.levelTests = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_test_guideline_item, parent, false);

        men_guideline = v.findViewById(R.id.men_guideline_table);
        women_guideline = v.findViewById(R.id.women_guideline_table);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.levelTest_name.setText(levelTests.get(position).getName());
        holder.m_excellent.setText(levelTests.get(position).getGuideline().get(4).toString()+"~");
        holder.m_good.setText("~"+levelTests.get(position).getGuideline().get(3).toString());
        holder.m_fair.setText("~"+levelTests.get(position).getGuideline().get(2).toString());
        holder.m_pool.setText("~"+levelTests.get(position).getGuideline().get(1).toString());
        holder.m_very_pool.setText("~"+levelTests.get(position).getGuideline().get(0).toString());

        holder.w_excellent.setText(levelTests.get(position).getGuideline().get(9).toString()+"~");
        holder.w_good.setText("~"+levelTests.get(position).getGuideline().get(8).toString());
        holder.w_fair.setText("~"+levelTests.get(position).getGuideline().get(7).toString());
        holder.w_pool.setText("~"+levelTests.get(position).getGuideline().get(6).toString());
        holder.w_very_pool.setText("~"+levelTests.get(position).getGuideline().get(5).toString());
    }

    @Override
    public int getItemCount() {
        return levelTests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.leveltest_name)
        TextView levelTest_name;

        TextView m_excellent;
        TextView m_good;
        TextView m_fair;
        TextView m_pool;
        TextView m_very_pool;

        TextView w_excellent;
        TextView w_good;
        TextView w_fair;
        TextView w_pool;
        TextView w_very_pool;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            m_excellent = (TextView)itemView.findViewById(R.id.men_guideline_table).findViewById(R.id.excellent);
            m_good = (TextView)itemView.findViewById(R.id.men_guideline_table).findViewById(R.id.good);
            m_fair = (TextView)itemView.findViewById(R.id.men_guideline_table).findViewById(R.id.fair);
            m_pool = (TextView)itemView.findViewById(R.id.men_guideline_table).findViewById(R.id.pool);
            m_very_pool = (TextView)itemView.findViewById(R.id.men_guideline_table).findViewById(R.id.very_pool);

            w_excellent = (TextView)itemView.findViewById(R.id.women_guideline_table).findViewById(R.id.excellent);
            w_good = (TextView)itemView.findViewById(R.id.women_guideline_table).findViewById(R.id.good);
            w_fair = (TextView)itemView.findViewById(R.id.women_guideline_table).findViewById(R.id.fair);
            w_pool = (TextView)itemView.findViewById(R.id.women_guideline_table).findViewById(R.id.pool);
            w_very_pool = (TextView)itemView.findViewById(R.id.women_guideline_table).findViewById(R.id.very_pool);
        }
    }
}
