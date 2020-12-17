package com.hilifecare.ui.myrecord;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.CalorieComponent;
import com.hilifecare.di.components.DaggerCalorieComponent;
import com.hilifecare.di.modules.CalorieModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class CalorieActivity extends BaseActivity<CaloriePresenter> implements CalorieView, HasComponent<CalorieComponent> {

    @Inject
    CaloriePresenter caloriePresenter;

    CalorieComponent calorieComponent;

    ProgressDialog pDialog;

    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    @Bind(R.id.calorie_graph)
    BarChart mChart;
    @Bind(R.id.calorie_list)
    RecyclerView recyclerView;
    CalorieRecyclerViewAdpater recyclerViewAdpater;
    private Typeface mTfLight;

    protected void injectModule() {
        calorieComponent = DaggerCalorieComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .calorieModule(new CalorieModule(this))
                .build();
        calorieComponent.inject(this);
        
    }
    
    public PresenterFactory<CaloriePresenter> getPresenterFactory() {
        return () -> caloriePresenter;
    }

    public void init() {
        toolbar.setName(getTitle().toString());
        toolbar.setShadowVisibility(View.VISIBLE);
        toolbar.setToolbarRightVisibility(View.INVISIBLE);

        initChart();
        caloriePresenter.setData(this);

        //
        recyclerViewAdpater = new CalorieRecyclerViewAdpater(this, item -> {
            Intent intent = new Intent(CalorieActivity.this, CalorieDetailActivity.class);
            intent.putExtra("item", (String) item);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdpater);
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

    public void setChart(ArrayList<BarEntry> yVals1) {
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
//            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
//            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    protected int getLayoutResource() {
        return R.layout.activity_calorie;
    }

    @Override
    public CalorieComponent getComponent() {
        return calorieComponent;
    }

    @Override
    protected void onResume() {
        ScreenStopwatch.getInstance().printElapsedTimeLog(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().printResetTimeLog(getClass().getSimpleName());
    }
}
