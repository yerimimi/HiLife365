package com.hilifecare.ui.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hilifecare.R;
import com.hilifecare.model.Plan;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-02-21.
 */

public class PlanAdapter extends ArrayAdapter<Plan> {

    TextView plan_name;
    TextView plan_explain;
    Context mContext;

    private LayoutInflater mInflater;

    public PlanAdapter(Context context, ArrayList<Plan> objects) {
        super(context, 0, objects);
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = null;

        if(v == null) {
            view = mInflater.inflate(R.layout.plan_item, null);
        } else {
            view = v;
        }

        final Plan plan = this.getItem(position);

        plan_name = (TextView)view.findViewById(R.id.plan_name);
        plan_explain = (TextView)view.findViewById(R.id.plan_explain);

        plan_name.setText(plan.getName());
        plan_explain.setText(plan.getExplain());

        return view;
    }
}
