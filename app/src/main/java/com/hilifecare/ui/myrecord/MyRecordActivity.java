package com.hilifecare.ui.myrecord;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerMyRecordComponent;
import com.hilifecare.di.components.MyRecordComponent;
import com.hilifecare.di.modules.MyRecordModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.ScreenStopwatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;
import timber.log.Timber;

@ActivityScope
public class MyRecordActivity extends BaseActivity<MyRecordPresenter> implements MyRecordView, HasComponent<MyRecordComponent> {

    @Inject
    MyRecordPresenter myRecordPresenter;

    MyRecordComponent myRecordComponent;

    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    @Bind(R.id.my_record_date)
    TextView dateView;
    @Bind(R.id.my_record_level)
    ImageView userLevel;
    @Bind(R.id.my_record_percentage)
    TextView percentage;
    @Bind(R.id.rader_graph)
    RadarChart mChart;
    @Bind(R.id.my_record_list)
    RecyclerView recyclerView;
    private MyRecordRecyclerViewAdpater adapter;
    private Typeface mTfLight;
    private Calendar calendar;

    protected void injectModule() {
        myRecordComponent = DaggerMyRecordComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .myRecordModule(new MyRecordModule(this))
                .build();
        myRecordComponent.inject(this);
        
    }
    
    public PresenterFactory<MyRecordPresenter> getPresenterFactory() {
        return () -> myRecordPresenter;
    }

    public void init() {
        toolbar.setName(getTitle().toString());
        toolbar.setShadowVisibility(View.VISIBLE);
        toolbar.setToolbarRightVisibility(View.INVISIBLE);

        initChart();
        myRecordPresenter.setData(this);

        adapter = new MyRecordRecyclerViewAdpater(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //TODO: 날짜 설정
        setDate(new Date());
    }

    private void initChart() {
        mTfLight = Typeface.createFromAsset(getAssets(), "fonts/Aero-Matics-Display-Regular.ttf");

        mChart.getDescription().setEnabled(false);

        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.LTGRAY);
        mChart.setWebAlpha(100);
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Timber.d("angle for point: "+mChart.getAngleForPoint(me.getX(),me.getY()));
                int index = mChart.getIndexForAngle(mChart.getAngleForPoint(me.getX(),me.getY()));
                Timber.d( "index of angle: "+index);
                Toast.makeText(MyRecordActivity.this, mChart.getXAxis().getFormattedLabel(index),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyRecordActivity.this, MyRecordDetailActivity.class);
                intent.putExtra("title", mChart.getXAxis().getFormattedLabel(index));
                startActivity(intent);
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextColor(getResources().getColor(R.color.colorText));
        xAxis.setTextSize(13f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"심폐지구력", "유연성", "근력", "평형성", "민첩성",
                    "유산소", "조작력", "순발력"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
//        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(13f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        mChart.getLegend().setEnabled(false);
    }

    public void setChart(ArrayList<RadarEntry> entries) {
        RadarDataSet set1 = new RadarDataSet(entries, "This Week");
        set1.setColor(getResources().getColor(R.color.colorTurquoise));
        set1.setFillColor(Color.TRANSPARENT);
        set1.setDrawFilled(false);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.invalidate();
    }

    public void setDate(Date date){
        if(calendar == null)
            calendar = Calendar.getInstance();
        calendar.setTime(date);
        try{
            dateView.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
        }catch (Exception e){
            Timber.e(e.getMessage());
        }

    }

    public void setLevel(int level){
        switch (level){
            case 1:
                userLevel.setImageResource(R.drawable.icon_level1);
                break;
            case 2:
                userLevel.setImageResource(R.drawable.icon_level2);
                break;
            case 3:
                userLevel.setImageResource(R.drawable.icon_level3);
                break;
            case 4:
                userLevel.setImageResource(R.drawable.icon_level4);
                break;
            case 5:
                userLevel.setImageResource(R.drawable.icon_level5);
                break;
        }
    }

    public void setPercentage(int percent){
        percentage.setText(percent+"%");
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    @OnClick(R.id.my_record_date)
    void changeDate() {
        //TODO: get current date, set current date, get changed date, set date
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year,monthOfYear,dayOfMonth);
                        setDate(calendar.getTime());
                    }
                },calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    protected int getLayoutResource() {
        return R.layout.activity_my_record;
    }

    @Override
    public MyRecordComponent getComponent() {
        return myRecordComponent;
    }

    @Override
    protected void onStart() {
        ScreenStopwatch.getInstance().printElapsedTimeLog("MyRecordActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset(); // 현재 화면이 없어질 때
    }
}
