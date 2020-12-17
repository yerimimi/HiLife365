package com.hilifecare.ui.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilifecare.R;

import java.util.List;
import java.util.zip.Inflater;

public class MyAdapter extends BaseAdapter {

    List<String> name;

    public MyAdapter() {
    }

    @Override
    public int getCount() {
        if(name != null) {
            return name.size();
        }

        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {
        if(name != null) {
            return name.get(position);
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.list_view_item, parent, false);
            }
        }

        TextView nameTextView = (TextView)convertView.findViewById(R.id.name);

        nameTextView.setText(name.get(position));

        return convertView;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }


}
