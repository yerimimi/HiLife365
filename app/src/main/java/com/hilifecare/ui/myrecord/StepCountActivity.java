package com.hilifecare.ui.myrecord;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerStepCountComponent;
import com.hilifecare.di.components.StepCountComponent;
import com.hilifecare.di.modules.StepCountModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.bluetooth.BluetoothLeService;
import com.hilifecare.ui.bluetooth.SampleGattAttributes;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.Stopwatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

import static com.hilifecare.R.id.distance_value;

@ActivityScope
public class StepCountActivity extends BaseActivity<StepCountPresenter> implements StepCountView, HasComponent<StepCountComponent> {

    @Inject
    StepCountPresenter stepCountPresenter;

    StepCountComponent stepCountComponent;

    ProgressDialog pDialog;

    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
//    @Bind(R.id.term_container)
//    RadioGroup termGroup;
    @Bind(R.id.step_count_graph)
    LineChart mChart;
    @Bind(R.id.step_value)
    TextView stepValue;
    @Bind(R.id.kcal_value)
    TextView kcalValue;
    @Bind(distance_value)
    TextView distanceValue;
    private Typeface mTfLight;

    Stopwatch stopwatch = new Stopwatch();


    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private ArrayList<String> mValues = new ArrayList<>();

    private String sync_time = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    private int time_stack = 0;

    protected void injectModule() {
        stepCountComponent = DaggerStepCountComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .stepCountModule(new StepCountModule(this))
                .build();
        stepCountComponent.inject(this);
        
    }
    
    public PresenterFactory<StepCountPresenter> getPresenterFactory() {
        return () -> stepCountPresenter;
    }

    public void init() {
        if(getmDeviceAddress() == null) {
            Toast.makeText(this, "블루투스가 연결되어 있지 않습니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        toolbar.setName(getTitle().toString());
        toolbar.setShadowVisibility(View.VISIBLE);
        toolbar.setToolbarRightVisibility(View.INVISIBLE);

        initChart();

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

//        initTermGroup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(getmDeviceAddress());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getmDeviceAddress() != null) {
            unregisterReceiver(mGattUpdateReceiver);
            unbindService(mServiceConnection);
            mBluetoothLeService = null;
            mChart = null;
        }
    }

//    private void initTermGroup() {
//        termGroup.check(R.id.term_day);
//        termGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                switch (checkedId){
//                    case R.id.term_day:
//                        //TODO: 기간별 데이터 교체
//                        break;
//                    case R.id.term_month:
//                        break;
//                    case R.id.term_year:
//                        break;
//                }
//            }
//        });
//    }

    private void initChart() {
        mTfLight = Typeface.createFromAsset(getAssets(), "fonts/Aero-Matics-Display-Regular.ttf");

//        mChart.setOnChartGestureListener(this);
//        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setTypeface(mTfLight);
        mChart.getXAxis().setTextSize(16f);
        mChart.getXAxis().setTextColor(getResources().getColor(R.color.colorText));
        mChart.getXAxis().setLabelCount(5);
        mChart.setExtraBottomOffset(10f);
        mChart.getAxisLeft().setTypeface(mTfLight);
        mChart.getAxisLeft().setTextSize(16f);
        mChart.getAxisLeft().setTextColor(getResources().getColor(R.color.colorText));
        mChart.getAxisLeft().setLabelCount(8);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        // no description text
        mChart.getDescription().setEnabled(false);
    }

    @OnClick(R.id.toolbar_left)
    void goBack(){
        finish();
    }

    public void setChart(ArrayList<Entry> values){
        LineDataSet set1;

//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//        } else {
//             create a dataset and give it a type
        set1 = new LineDataSet(values, "StepCount");

        set1.setDrawIcons(false);

        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setColor(ContextCompat.getColor(this, R.color.colorOrangeBt));
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(false);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

//            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//            dataSets.add(set1); // add the datasets
//
//            // create a data object with the datasets
//            LineData data = new LineData(dataSets);
        mValues.add(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(mValues));
        LineData data = new LineData(set1);
        // set data
        mChart.setData(data);
        mChart.invalidate();
//        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> mValues = new ArrayList<>();

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues.get((int) value);
        }
    }

    public void setStepValue(String value){
        try {
            stepValue.setText(value.substring(0, 4));
        } catch (Exception e) {
            stepValue.setText(value);
        }
    }

    public void setKcalValue(String value){
        try {
            kcalValue.setText(value.substring(0, 4));
        } catch (Exception e) {
            kcalValue.setText(value);
        }
    }

    public void setDistanceValue(String value){
        try {
            distanceValue.setText(value.substring(0, 5));
        } catch (Exception e) {
            distanceValue.setText(value);
        }
    }

    protected int getLayoutResource() {
        return R.layout.activity_step_count;
    }

    @Override
    public StepCountComponent getComponent() {
        return stepCountComponent;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(getmDeviceAddress());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mBluetoothLeService.connect(getmDeviceAddress());
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String sync_flag = intent.getStringExtra(BluetoothLeService.SYNC_DATA);
                String hr_data = intent.getStringExtra(BluetoothLeService.HR_DATA);
                String spo2_data = intent.getStringExtra(BluetoothLeService.SPO2_DATA);
                String speed_data = intent.getStringExtra(BluetoothLeService.SPEED_DATA);
                String calories_data = convertCalories(Double.parseDouble(speed_data)*3);
                String distance_data = convertDistance(Double.parseDouble(speed_data)*3);
                String step_data = convertStep(Double.parseDouble(distance_data));
                if (sync_flag.equals("1")) {    //sync 전송
                    //firebase 전송
                    String time = intent.getStringExtra(BluetoothLeService.TIME_DATA);

                    stepCountPresenter.insertStepCount(convertTime(Integer.parseInt(time)), Integer.parseInt(hr_data), Integer.parseInt(spo2_data), Integer.parseInt(speed_data));
                } else {                //실시간 전송
                    displayData(Integer.parseInt(speed_data)*3, Double.parseDouble(distance_data), Double.parseDouble(calories_data), Double.parseDouble(step_data),
                            Integer.parseInt(hr_data), Integer.parseInt(spo2_data));
                    time_stack = 0;
                    sync_time = getTime();
                }
            }
        }
    };

    @Override
    public String getTime() {
        return new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }

    @Override
    public String convertTime(int time_stack2) {
        String convert_time;
        String convert_day = sync_time.substring(0,10);
        time_stack++;

        convert_time = sync_time.substring(11,19);
        int hh = Integer.parseInt(convert_time.substring(0,2));
        int mm = Integer.parseInt(convert_time.substring(3,5));
        int ss = Integer.parseInt(convert_time.substring(6,8));

        for(int i =0; i < time_stack; i++) {
            ss = ss-(i*30);
            if(ss < 0)
            {
                ss = 60+ss;
                mm--;
                if(mm < 0)
                {
                    mm = 60+mm;
                    hh--;
                }
            }
        }

        return convert_day + " " + String.valueOf(hh) + ":" + String.valueOf(mm) + ":" + String.valueOf(ss);
    }

    private void displayData(int speed_value, double distance_value, double calories_value, double step_value, int hr_value, int spo2_value) {
        stepCountPresenter.setData(this, distance_value, calories_value, step_value);
        stepCountPresenter.insertStepCount(getTime(), hr_value, spo2_value, speed_value);
    }

    @Override
    public String convertCalories(double speed_data) {
        if(0 < speed_data && speed_data < 5)
            return String.valueOf(3.6);
        else if(speed_data > 8)
            return String.valueOf(8.1);
        else return "0";
    }

    @Override
    public String convertDistance(double speed_data) {
        return String.valueOf(speed_data/1.1);
    }

    @Override
    public String convertStep(double distance_data) {
        return String.valueOf(distance_data/0.629);
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
        setGattCharacteristic();
    }

    public void setGattCharacteristic() {
        if (mGattCharacteristics != null) {
            final BluetoothGattCharacteristic characteristic =
                    mGattCharacteristics.get(2).get(0);
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected void onStart() {
        stopwatch.printLog("StepCountActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.reset(); // 현재 화면이 없어질 때
    }
}
