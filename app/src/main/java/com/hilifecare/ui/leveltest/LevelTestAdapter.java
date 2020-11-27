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
import com.hilifecare.model.LevelTest;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-01-24.
 */

public class LevelTestAdapter extends RecyclerView.Adapter<LevelTestAdapter.ViewHolder> {
    Context mContext;
    ArrayList<LevelTest> levelTests;


    public LevelTestAdapter(Context context, ArrayList<LevelTest> objects) {
        mContext = context;
        this.levelTests = objects;
    }

//    private OnClickListener program_image_click_listener =
//            new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.naver.com"));
//                    mContext.startActivity(i);
//                }
//            };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_test_item,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(levelTests.get(position).getName());
        holder.desc.setText(levelTests.get(position).getDescriptions());
        Glide
                .with(mContext)
                .load(levelTests.get(position).getImage())
                .into(holder.preview);

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
        return levelTests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.test_image)
        ImageView image;
        @Bind(R.id.test_name)
        TextView name;
        @Bind(R.id.test_desc)
        TextView desc;
        @Bind(R.id.test_preview)
        ImageView preview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
