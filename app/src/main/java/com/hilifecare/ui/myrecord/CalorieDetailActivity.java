package com.hilifecare.ui.myrecord;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hilifecare.R;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalorieDetailActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    @Bind(R.id.calorie_detail_day_of_week)
    TextView dayOfWeek;
    @Bind(R.id.calorie_graph)
    BarChart mChart;
    private Typeface mTfLight;

    @CallSuper
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_detail);
        ButterKnife.bind(this);

        toolbar.setName(getTitle().toString());
        toolbar.setShadowVisibility(View.VISIBLE);
        toolbar.setToolbarRightVisibility(View.INVISIBLE);

        String item = getIntent().getStringExtra("item");
        if(item != null) dayOfWeek.setText(item);

        initChart();
        //TODO: 더미 데이터 교체
        setData(6, 4000);
    }

    private void initChart() {
        mTfLight = Typeface.createFromAsset(getAssets(), "fonts/Aero-Matics-Display-Regular.ttf");

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        mChart.getLegend().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        mChart.setExtraTopOffset(10f);

        IAxisValueFormatter xAxisFormatter = new DayOfWeekAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(getResources().getColor(R.color.colorText));
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextSize(16f);
        leftAxis.setTextColor(getResources().getColor(R.color.colorText));
        leftAxis.setLabelCount(5);//, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            if (Math.random() * 100 < 25) {
                yVals1.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.bullet)));
            } else {
                yVals1.add(new BarEntry(i, val));
            }
        }

        setChart(yVals1);
    }

    private void setChart(ArrayList<BarEntry> yVals1) {
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                set1.setColors(getResources().getColor(R.color.colorText, null));
            }else{
                set1.setColors(getResources().getColor(R.color.colorText));
            }
            set1.setHighLightColor(getResources().getColor(R.color.colorTopaz));
            set1.setHighLightAlpha(255);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setDrawValues(false);

            mChart.setData(data);
        }
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    @Override
    protected void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("CalorieDetailActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset(); // 현재 화면이 없어질 때
    }
}
