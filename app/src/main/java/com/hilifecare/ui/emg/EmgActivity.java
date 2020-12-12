package com.hilifecare.ui.emg;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hilifecare.R;
import com.hilifecare.di.ActivityScope;
import com.hilifecare.ui.emg.le.entity.Emg;
import com.hilifecare.ui.emg.le.service.EmgService;
import com.hilifecare.ui.emg.retrofit2.OneM2M;
import com.hilifecare.ui.emg.retrofit2.entity.OneM2MCin;
import com.hilifecare.ui.emg.retrofit2.entity.OneM2MCinContents;
import com.hilifecare.ui.emg.retrofit2.entity.OneM2MCinLatestRoot;
import com.hilifecare.util.logging.EmgStopwatch;
import com.hilifecare.util.logging.ScreenStopwatch;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hilifecare.ui.emg.le.attr.EmgGattAttr.*;

@ActivityScope
public class EmgActivity extends AppCompatActivity {

    private EmgService emgService;
    private String tag = getClass().getSimpleName();
    BluetoothGattService mEmgService;
    BluetoothGattCharacteristic mEmgChar;
    TextView emgDataTextView;
    Button getLatestemgDataButton;
    TextView latestEmgDataTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emg);
        emgDataTextView = (TextView) findViewById(R.id.emgDataTextView);
        getLatestemgDataButton = (Button) findViewById(R.id.getLatestemgDataButton);
        latestEmgDataTextView =  (TextView) findViewById(R.id.latestEmgDataTextView);
        getLatestemgDataButton.setOnClickListener( view -> {
            OneM2M oneM2M = new OneM2M();
            oneM2M.setRetrieveCInCallback(
                    new Callback<OneM2MCinLatestRoot>() {
                        @Override
                        public void onResponse(Call<OneM2MCinLatestRoot> call, Response<OneM2MCinLatestRoot> response) {
                            if (response.isSuccessful()) {
                                Log.d(tag, "retrieveCinLatest() Result : " + response.body() + " / " + response.code());
                                latestEmgDataTextView.setText(response.body().m2mCin.con);
                            } else {
                                Log.d(tag, "retrieveCinLatest() Result " + response.code() + " / " + response.toString());
                            }
                        }
                        @Override
                        public void onFailure(Call<OneM2MCinLatestRoot> call, Throwable t) {
                            Log.d(tag, "retrieveCinLatest() onFailure " + t.getMessage());
                        }
                    }
            );
            oneM2M.retrieveCinLatest("931faa7028b3185445096a43057149f6", "6fe5e3d7f88940f6a09ad7d2c532d023" , "data");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent gattServiceIntent = new Intent(this, EmgService.class);
        boolean res = bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i(tag, "bindService res : " + res);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        ScreenStopwatch.getInstance().reset();
        Log.i(tag, "onPause():: call");
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            emgService = ((EmgService.LocalBinder) service).getService();
            Log.i(tag, "TemperatureService::onServiceConnected() Success !! " + EmgService.class.getName());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(tag, "TemperatureService::onServiceDisconnected() Success !! " + EmgService.class.getName());
            emgService.disconnect();
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(tag, "BroadcastReceiver: onReceive()");
            if (ACTION_GATT_CONNECTED.equals(action))
                Log.d(tag, "BroadcastReceiver: ACTION_GATT_CONNECTED Complete !! ");
            else if (ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(tag, "BroadcastReceiver: ACTION_GATT_SERVICES_DISCOVERED Complete !! ");
                EmgStopwatch.getInstance().printElapsedTimeLog("EmgConnected");
                mEmgService = emgService.getGattService(UART_SERVICE_UUID);
                Log.d(tag, "mEmgService is : " + mEmgService.getUuid().toString());
                mEmgChar = mEmgService.getCharacteristic(UART_TX_CHAR_UUID);
                Log.d(tag, "mEmgChar is : " + mEmgChar.getUuid().toString());
                BluetoothGattDescriptor mEmgDescriptor = mEmgChar.getDescriptor(UART_TX_CCCD_UUID);
                Log.d(tag, "mEmgDesc : " + mEmgDescriptor.getUuid().toString());
                emgService.setCharacteristicNotification(mEmgChar, true);
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(tag, "BroadcastReceiver: ACTION_DATA_AVAILABLE New Data!!! ");
                byte[] raws = intent.getByteArrayExtra(EXTRA_EMG_DATA);
                ArrayList<Integer> unsignedRaw = new ArrayList<>();
                int idx = 0;
                for (byte raw:raws) {
                    int res = raw < 0 ? raw+256 : raw;
                    unsignedRaw.add(idx, res);
                    idx++;
                }
                if(unsignedRaw.size() != 0) {
                    int index = unsignedRaw.get(0);
                    int count = unsignedRaw.get(4) + unsignedRaw.get(3)*256 + unsignedRaw.get(2)*258*256 + unsignedRaw.get(1)*256*256*256;
                    int w = unsignedRaw.get(6) + unsignedRaw.get(5)*256;
                    int x = unsignedRaw.get(8) + unsignedRaw.get(7)*256;
                    int y = unsignedRaw.get(10) + unsignedRaw.get(9)*256;
                    int z = unsignedRaw.get(12) + unsignedRaw.get(11)*256;
                    int emg = unsignedRaw.get(14) + unsignedRaw.get(13)*256;
                }
            } else if (ACTION_BEACON_DATA.equals(action)) {
                Log.d(tag, "[ACTION_BEACON_DATA]");
                Emg data = (Emg) intent.getSerializableExtra(EXTRA_BEACON_DATA);
                OneM2MCin cin = new OneM2MCin(
                        new OneM2MCinContents(
                                "application/json",
                                data.toString()
                        )
                );

                OneM2M oneM2M = new OneM2M();
                oneM2M.setCreateCInCallback(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d(tag, "createCin() Result : " + response.body() + " / " + response.code());
                                } else {
                                    Log.d(tag, "createCin() Result " + response.code() + " / " + response.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d(tag, "createCin() onFailure " + t.getMessage());
                            }
                        }
                );
                oneM2M.createCin("931faa7028b3185445096a43057149f6", "6fe5e3d7f88940f6a09ad7d2c532d023" , "data", cin);
                emgDataTextView.setText(data.toString());
            } else if (ACTION_FIND_DEVICE.equals(action)) {
                Log.d(tag, "[ACTION_FIND_DEVICE]");
                String deviceMacAddress = intent.getStringExtra(EXTRA_DATA_DEVICE_ADDRESS);
                boolean res = emgService.connect(deviceMacAddress);
                Log.d(tag, "[ACTION_FIND_DEVICE] deviceMacAddress : " + deviceMacAddress + "res : " + res);
                if (res) {
                    String mConnectedDeviceName = intent.getStringExtra(EXTRA_DATA_DEVICE_NAME);
                    Log.d(tag, "[ACTION_FIND_DEVICE] mConnectedDeviceName : " + mConnectedDeviceName);
                }
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        intentFilter.addAction(ACTION_BEACON_DATA);
        intentFilter.addAction(ACTION_FIND_DEVICE);
        return intentFilter;
    }

    protected void onStart(){
        super.onStart();
        ScreenStopwatch.getInstance().printElapsedTimeLog("EmgActivity");
    }
}