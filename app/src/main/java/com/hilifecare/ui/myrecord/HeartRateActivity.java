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
import android.util.Log;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hilifecare.R;
import com.hilifecare.application.App;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.di.HasComponent;
import com.hilifecare.di.components.DaggerHeartRateComponent;
import com.hilifecare.di.components.HeartRateComponent;
import com.hilifecare.di.modules.HeartRateModule;
import com.hilifecare.ui.base.BaseActivity;
import com.hilifecare.ui.bluetooth.BluetoothLeService;
import com.hilifecare.ui.bluetooth.SampleGattAttributes;
import com.hilifecare.ui.view.CustomDialog;
import com.hilifecare.ui.view.CustomToolbar;
import com.hilifecare.util.logging.HrStopwatch;
import com.hilifecare.util.logging.ResponseStopwatch;
import com.hilifecare.util.logging.ScreenStopwatch;
import com.hilifecare.util.logging.VisualStopwatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import nucleus.factory.PresenterFactory;

@ActivityScope
public class HeartRateActivity extends BaseActivity<HeartRatePresenter> implements HeartRateView,
        HasComponent<HeartRateComponent>, OnChartValueSelectedListener {

    @Inject
    HeartRatePresenter heartRatePresenter;

    HeartRateComponent heartRateComponent;

    ProgressDialog pDialog_syncHR;
    ProgressDialog pDialog_restingHR;

    @Bind(R.id.toolbar)
    CustomToolbar toolbar;
    //    @Bind(R.id.term_container)
//    RadioGroup termGroup;
    @Bind(R.id.heart_rate_graph)
    LineChart mChart;
    @Bind(R.id.min)
    TextView minValue;
    @Bind(R.id.max)
    TextView maxValue;
    @Bind(R.id.avg)
    TextView avgValue;
    @Bind(R.id.exercise_start)
    TextView exercise_startnend;
    private Typeface mTfLight;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private ArrayList<String> mValues = new ArrayList<>();

    private String sync_time = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    private int time_stack = 0;

    private Timer timer;
    private ArrayList<Integer> restingHRs = new ArrayList<>();
    private int count;
    private int time_tick;
    private int hr = 0;
    private int restingHR = 0;
    private CustomDialog customDialog;

    protected void injectModule() {
        heartRateComponent = DaggerHeartRateComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .heartRateModule(new HeartRateModule(this))
                .build();
        heartRateComponent.inject(this);

    }

    public PresenterFactory<HeartRatePresenter> getPresenterFactory() {
        return () -> heartRatePresenter;
    }

    public void init() {
        if (getmDeviceAddress() == null) {
            Toast.makeText(this, "블루투스가 연결되어 있지 않습니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        toolbar.setName(getTitle().toString());
        toolbar.setShadowVisibility(View.VISIBLE);
        toolbar.setToolbarRightVisibility(View.INVISIBLE);

        initChart();
        pDialog_syncHR = new ProgressDialog(this);
        pDialog_restingHR = new ProgressDialog(this);

        timer = new Timer();

//        initRadioGroup();

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        Log.d("HL3", "BLE 연결 시작");
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
        if (getmDeviceAddress() != null) {
            unregisterReceiver(mGattUpdateReceiver);
            unbindService(mServiceConnection);
            mBluetoothLeService = null;
            mChart = null;
        }
    }

    //    private void initRadioGroup() {
//        termGroup.check(R.id.term_day);
//        termGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            switch (checkedId){
//                case R.id.term_day:
//                    //TODO: 기간별 데이터 교체
//                    heartRatePresenter.setData(this, data);
//                    break;
//                case R.id.term_month:
//                    break;
//                case R.id.term_year:
//                    break;
//            }
//        });
//    }

    private void initChart() {
        mTfLight = Typeface.createFromAsset(getAssets(), "fonts/Aero-Matics-Display-Regular.ttf");

        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setTypeface(mTfLight);
        mChart.getXAxis().setTextSize(16f);
        mChart.getXAxis().setLabelCount(5);
        mChart.getXAxis().setTextColor(getResources().getColor(R.color.colorText));
        mChart.setExtraBottomOffset(10f);
        mChart.getAxisLeft().setTypeface(mTfLight);
        mChart.getAxisLeft().setTextSize(16f);
        mChart.getAxisLeft().setTextColor(getResources().getColor(R.color.colorText));
        mChart.getAxisLeft().setLabelCount(8);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(false);

//        YAxis yAxis = mChart.getAxisLeft();
//        yAxis.setAxisMinimum(40f);
//        yAxis.setAxisMaximum(180f);
//        yAxis.setLabelCount(8, true);
    }

    @OnClick(R.id.toolbar_left)
    void goBack() {
        finish();
    }

    @OnClick(R.id.exercise_start)
    void setExerciseMode() {
        if (exercise_startnend.getText().equals("운동시작")) {
            count = 0;
            customDialog = new CustomDialog(this, CustomDialog.TYPE_TWOBUTTON)
                    .setMessage("운동모드를 시작 하시겠습니까?")
                    .setOkClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setExerciseModeStartListener();
                            pDialog_restingHR.setMessage("평상 심박수 측정중");
                            pDialog_restingHR.show();
                        }
                    });

            customDialog.showDialog();
        } else {
            count = 0;
            time_tick = 0;
            customDialog = new CustomDialog(this, CustomDialog.TYPE_TWOBUTTON)
                    .setMessage("운동모드를 종료 하시겠습니까?")
                    .setOkText("종료")
                    .setOkClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setExerciseModeFinishListener();
                        }
                    });

            customDialog.showDialog();
        }
    }

    private void setExerciseModeStartListener() {
        customDialog.dismiss();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!pDialog_restingHR.isShowing())
                            timer.cancel();
                        if (count < 60) {
                            restingHRs.add(count++, hr);
                            Log.d("JHY test hr", String.valueOf(hr));
                        } else {
                            int sumRestingHRs = 0;
                            for (int i = 0; i < count; i++) {
                                sumRestingHRs += restingHRs.get(i);
                            }
                            restingHR = sumRestingHRs / count;
                            Log.d("JHY test aver hr", String.valueOf(restingHR));
                            exercise_startnend.setText("운동중");
                            exercise_startnend.setTextColor(Color.GREEN);
                            heartRatePresenter.insertHeartRate(getTime(), -1, -1, "-1", "-1", "-1");
                            if (pDialog_restingHR.isShowing())
                                pDialog_restingHR.dismiss();
                            timer.cancel();
                        }
                    }
                });
            }
        }, 500, 1000);
    }

    private void setExerciseModeFinishListener() {
        exercise_startnend.setText("운동시작");
        exercise_startnend.setTextColor(Color.GRAY);
        heartRatePresenter.insertHeartRate(getTime(), -2, -2, "-2", "-2", "-2");

        customDialog.dismiss();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (restingHR >= hr) {
                            count++;
                            Log.d("JHY test hr", String.valueOf(hr));
                            if (count >= 5) {
                                //평상심박수 회복!
                                //회복한 시간을 출력해줘야함 (time_tick+1)*5 + 0.5 초
                                setExerciseModeResultListener(time_tick);
                                timer.cancel();
                            } else {
                                time_tick++;
                            }
                        } else {
                            //평상심박수보다 높을때
                            time_tick++;
                        }
                    }
                });
            }
        }, 500, 1000);
    }

    private void setExerciseModeResultListener(int time_tick) {
        double recover_time = time_tick + 0.5;
        customDialog = new CustomDialog(this, CustomDialog.TYPE_ONEBUTTON)
                .setMessage("당신의 평상시 심박수 회복시간은 \n" + recover_time + "초 입니다.")
                .setOkText("확 인")
                .setOkClickListener(v -> customDialog.dismiss());
        customDialog.showDialog();
    }

    protected int getLayoutResource() {
        return R.layout.activity_heart_rate;
    }

    @Override
    public HeartRateComponent getComponent() {
        return heartRateComponent;
    }

    public void setChart(ArrayList<Entry> hr, ArrayList<Entry> spo2, int max_Evalue) {
        LineDataSet set1;
        LineDataSet set2;

//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//        } else {
//             create a dataset and give it a type
        set1 = new LineDataSet(hr, "HR");

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

        if (set1.getEntryCount() == max_Evalue) {
            set1.removeFirst();
            for (Entry entry : set1.getValues())
                entry.setX(entry.getX() - 1);
        }

        set2 = new LineDataSet(spo2, "SPO2");
        set2.setDrawIcons(false);

        set2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set2.setDrawCircles(false);
        set2.setDrawValues(false);
        set2.setColor(ContextCompat.getColor(this, R.color.colorBlueBt));
        set2.setCircleColor(Color.BLACK);
        set2.setLineWidth(2f);
        set2.setCircleRadius(3f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(false);
        set2.setFormLineWidth(1f);
        set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set2.setFormSize(15.f);

        if (set2.getEntryCount() == max_Evalue) {
            set2.removeFirst();
            for (Entry entry : set2.getValues())
                entry.setX(entry.getX() - 1);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets
//
//            // create a data object with the datasets
//            LineData data = new LineData(dataSets);
        mValues.add(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(mValues));
        LineData data = new LineData(dataSets);

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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void setPointValues(int min, int max, int avg) {
        minValue.setText(String.valueOf(min));
        maxValue.setText(String.valueOf(max));
        avgValue.setText(String.valueOf(avg));
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
                Log.d("HL3", "BLE 연결 완료");
                HrStopwatch.getInstance().printElapsedTimeLog("HrConnected");
                //TODO 생체부착형 센서 접속속도 (심박)
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
                String calories_data = convertCalories(Double.parseDouble(speed_data) * 3);
                String distance_data = convertDistance(Double.parseDouble(speed_data) * 3);
                String step_data = convertStep(Double.parseDouble(distance_data));
                if (sync_flag.equals("1")) {    //sync 전송
                    //firebase 전송
                    Log.d("HL3", "동기화 데이터 전송");
                    if (!pDialog_syncHR.isShowing()) {
                        pDialog_syncHR.setMessage("동기화중....");
                        pDialog_syncHR.show();
                    }
                    String time = intent.getStringExtra(BluetoothLeService.TIME_DATA);

                    heartRatePresenter.insertHeartRate(convertTime(Integer.parseInt(time)),
                            Integer.parseInt(hr_data),
                            Integer.parseInt(spo2_data),
                            calories_data,
                            distance_data,
                            step_data);
                } else {                //실시간 전송
                    if (pDialog_syncHR.isShowing())
                        pDialog_syncHR.dismiss();
                    Log.d("HL3", "HR 측정 - " + hr_data);
                    displayData(hr_data, spo2_data, distance_data, calories_data, step_data);
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
        String convert_day = sync_time.substring(0, 10);
        time_stack++;

        convert_time = sync_time.substring(11, 19);
        int hh = Integer.parseInt(convert_time.substring(0, 2));
        int mm = Integer.parseInt(convert_time.substring(3, 5));
        int ss = Integer.parseInt(convert_time.substring(6, 8));

        for (int i = 0; i < time_stack; i++) {
            ss = ss - (i * 30);
            if (ss < 0) {
                ss = 60 + ss;
                mm--;
                if (mm < 0) {
                    mm = 60 + mm;
                    hh--;
                }
            }
        }

        return convert_day + " " + String.valueOf(hh) + ":" + String.valueOf(mm) + ":" + String.valueOf(ss);
    }

    @Override
    public String convertCalories(double speed_data) {
        if (0 < speed_data && speed_data < 5)
            return String.valueOf(3.6);
        else if (speed_data > 8)
            return String.valueOf(8.1);
        else return "0";
    }

    @Override
    public String convertDistance(double speed_data) {
        return String.valueOf(speed_data / 1.1);
    }

    @Override
    public String convertStep(double distance_data) {
        return String.valueOf(distance_data / 0.629);
    }

    private void displayData(String hr_data, String spo2_data, String distance_data, String calories_data, String step_data) {
        if (90 < Integer.valueOf(hr_data) && 200 > Integer.valueOf(hr_data)) {
            if (Integer.valueOf(hr_data) > (Integer.valueOf(avgValue.getText().toString()) * 1.4)) {
                CustomDialog notify = new CustomDialog(this, CustomDialog.TYPE_ONEBUTTON);
                if (Integer.valueOf(hr_data) > 120 || Integer.valueOf(hr_data) > (Integer.valueOf(avgValue.getText().toString()) * 1.6)) {
                    //위험메시지 출력
                    notify.setMessage("위험! 위험!\n심박수가 " + Integer.valueOf(hr_data) + " 입니다.  \n즉시 운동을 그만두세요!");
                    notify.setOkText("확인");
                    notify.setOkClickListener(v -> notify.dismiss());
                    notify.showDialog();
                } else {
                    //경고메시지 출력
                    notify.setMessage("경고! 경고!\n심박수가 " + Integer.valueOf(hr_data) + " 입니다.");
                    notify.setOkText("확인");
                    notify.setOkClickListener(v -> notify.dismiss());
                    notify.showDialog();
                }
            }
            Log.d("HL3", "HR 측정");
            heartRatePresenter.setData(this, hr_data, spo2_data);
            heartRatePresenter.insertHeartRate(getTime(),
                    Integer.parseInt(hr_data),
                    Integer.parseInt(spo2_data),
                    distance_data,
                    calories_data,
                    step_data);
            VisualStopwatch.getInstance().printElapsedTimeLog("HrDisplay");
            ResponseStopwatch.getInstance().printElapsedTimeLog("HrResponse");
        } else if (60 < Integer.valueOf(hr_data) && 200 > Integer.valueOf(hr_data)) {
            Log.d("HL3", "HR 측정");
            hr = Integer.valueOf(hr_data);
            heartRatePresenter.setData(this, hr_data, spo2_data);
            heartRatePresenter.insertHeartRate(getTime(),
                    Integer.parseInt(hr_data),
                    Integer.parseInt(spo2_data),
                    distance_data,
                    calories_data,
                    step_data);
            VisualStopwatch.getInstance().printElapsedTimeLog("HrDisplay");
            ResponseStopwatch.getInstance().printElapsedTimeLog("HrResponse");
        }
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
        ScreenStopwatch.getInstance().printElapsedTimeLog("HeartRateActivity"); // 다른 화면이 나타날 때
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset(); // 현재 화면이 없어질 때
    }
}
