package com.hilifecare.ui.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.hilifecare.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ListViewActivity extends AppCompatActivity {

    private ListView listview;
    private MyAdapter myAdapter;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listview = (ListView) findViewById(R.id.listView1);
        button = (Button) findViewById(R.id.createDataButton);
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);

        button.setOnClickListener(view -> {
            myAdapter.setName(Arrays.asList("aa", "bb"));
            Log.d("ListViewTest", "size : " + myAdapter.getCount());
            myAdapter.notifyDataSetChanged();
        });

    }
}
