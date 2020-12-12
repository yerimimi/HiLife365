package com.hilifecare.ui.emg.le.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.hilifecare.ui.emg.le.attr.EmgGattAttr.*;
import com.hilifecare.ui.emg.EmgActivity;
import com.hilifecare.ui.emg.le.entity.Emg;
import com.hilifecare.util.logging.EmgStopwatch;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.hilifecare.ui.emg.le.attr.EmgGattAttr.*;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class EmgService extends Service {
    private final static String TAG = EmgActivity.class.getSimpleName();
    private final static List<String> filterDeviceName = Arrays.asList("HiLife", "(c)HYOIL");
    private boolean bioHealthcoreServiceState = false;

    private Handler handler;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothDevice bluetoothDevice;
    private BluetoothGattDescriptor descriptor;
    private BluetoothLeScanner bleScanner;
    private String bluetoothDeviceAddress;
    private final Set<BluetoothDevice> scannedDevices = new HashSet<>();
    private Set<BluetoothDevice> bondedDevices = new HashSet<>();

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "Connected to GATT server.");
                intentAction = ACTION_GATT_CONNECTED;
                broadcastUpdate(intentAction);

                // Attempts to discover services after successful connection.
                Log.d(TAG, "Attempting to start service discovery:" + bluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Disconnected from GATT server.");
                intentAction = ACTION_GATT_DISCONNECTED;
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.d(TAG, "mtu changed : " + mtu);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered : " + status);
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.d(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic,
                                          int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(String action, String deviceName, String deviceAddress) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA_DEVICE_NAME, deviceName);
        intent.putExtra(EXTRA_DATA_DEVICE_ADDRESS, deviceAddress);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String action, Emg data) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_BEACON_DATA, data);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String action,
                                 BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        if (UART_TX_CHAR_UUID.equals(characteristic.getUuid())) {
            Log.d(TAG, "BIO_HEALTH_CORE_MEASUREMENT_CHAR_UUID Broadcast");

            byte[] raw = characteristic.getValue();
            final StringBuilder stringBuilder = new StringBuilder(raw.length);
            for (byte byteChar : raw)
                stringBuilder.append(String.format("%02X ", byteChar));
            Log.d(TAG, "raw(hex) :  " + stringBuilder.toString());
            intent.putExtra(EXTRA_EMG_DATA, raw);

        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public EmgService getService() {
            return EmgService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (initialize()) {
            Log.d(TAG, "initialize Success");
        } else {
            Log.d(TAG, "initialize Fail");
        }
        handler = new Handler(Looper.getMainLooper());
        bondedDevices = bluetoothAdapter.getBondedDevices();
        scannedDevices.addAll(bondedDevices);
        scanLeDevice(true);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        Log.d(TAG, "onUnbind : disconnect(), close() Call");
        scanLeDevice(false);
        close();
        bluetoothGatt = null;
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestory()");
        super.onDestroy();
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (!Objects.requireNonNull(bluetoothAdapter).isEnabled()) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        bleScanner = bluetoothAdapter.getBluetoothLeScanner();
        if (bleScanner == null) {
            Log.e(TAG, "Unable to obtain a LeScanner.");
            return false;
        }

        Log.d(TAG, "Set bluetoothManager, enable to obtain a BluetoothAdapter.");
        if ((bluetoothManager != null) && (bluetoothAdapter != null) && (bleScanner != null)) {
            Log.d(TAG, bluetoothManager.toString() + ", " + bluetoothAdapter.toString() + ", " + bleScanner.toString());
        }
        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (address == null) {
            Log.d(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        if (!bioHealthcoreServiceState) {
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            if (bluetoothDevice == null) {
                Log.d(TAG, "Device not found.  Unable to connect.");
                return false;
            }
        }

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        if (!bioHealthcoreServiceState) {
            bluetoothGatt = bluetoothDevice.connectGatt(this, true, bluetoothGattCallback);
            bioHealthcoreServiceState = true;
            Log.d(TAG, "connected address  : " + address);
        }
        bluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            Log.d(TAG, "disconnect() : bluetoothAdapter or bluetoothGatt not initialized");
            return;
        }
        Log.d(TAG, "disconnect() call");
        bluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (bluetoothGatt == null) {
            Log.d(TAG, "close() : bluetoothGatt not initialized");
            return;
        }
        bluetoothGatt.close();
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            Log.d(TAG, "readCharacteristic() : bluetoothAdapter or bluetoothGatt not initialized");
            return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            Log.d(TAG, "setCharacteristicNotification() : bluetoothAdapter or bluetoothGatt not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        // This is specific to Heart Rate Measurement.
        if (UART_TX_CHAR_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UART_TX_CCCD_UUID);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * setting Indication on characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicIndication(BluetoothGattCharacteristic characteristic,
                                            boolean enabled) {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            Log.d(TAG, "setCharacteristicIndication() : bluetoothAdapter or bluetoothGatt not initialized");
            return;
        }
        boolean res = bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (res) {
            if (UART_TX_CHAR_UUID.equals(characteristic.getUuid())) {
                Log.d(TAG, "Indication Write Start");
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UART_TX_CCCD_UUID);
                Log.d(TAG, "descriptor UUID  is : " + descriptor.getUuid().toString());
                final byte[] ENABLE_INDICATION_VALUE = {0x02, 0x00};
                res = descriptor.setValue(ENABLE_INDICATION_VALUE);
                if (res) {
                    handler.postDelayed(() -> {
                        Log.d(TAG, "descriptor.setValue() return True");
                        boolean res1 = bluetoothGatt.writeDescriptor(descriptor);
                        Log.d(TAG, "bluetoothGatt.writeDescriptor() res 1 is : " + res1);
                    }, 1000);

                }
            }
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (bluetoothGatt == null) return null;
        List<BluetoothGattService> services = bluetoothGatt.getServices();
        Log.d(TAG, "services len is " + services.size());
        return services;
    }


    public BluetoothGattService getGattService(UUID uuid) {
        if (bluetoothGatt == null) return null;
        return bluetoothGatt.getService(uuid);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            if (bleScanner != null) {
                Log.d(TAG, "LeScanStart()");
                bleScanner.startScan(mScanCallback);
            } else {
                Log.e(TAG, "Unable to obtain a LeScanner.");
            }
        } else {
            if (bleScanner != null) {
                Log.d(TAG, "LeScanStop()");
                bleScanner.stopScan(mScanCallback);
            } else {
                Log.e(TAG, "Unable to obtain a LeScanner.");
            }
        }
    }

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            processResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                processResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "onScanFailed() call " + errorCode);
        }

        private void processResult(final ScanResult result) {
            ScanRecord scanRecord = result.getScanRecord();
            if(scanRecord != null) {
                byte[] raws = result.getScanRecord().getBytes();
                ArrayList<Integer> unsignedRaw = new ArrayList<>();
                int idx = 0;
                for (byte raw:raws) {
                    int res = raw < 0 ? raw+256 : raw;
                    unsignedRaw.add(idx, (res));
                    idx++;
                }
                if(unsignedRaw.size() != 0) {
                    if(unsignedRaw.get(5) == 0x28 && unsignedRaw.get(6) == 0x63
                        && unsignedRaw.get(7) == 0x29 && unsignedRaw.get(8) == 0x48
                        && unsignedRaw.get(9) == 0x59 && unsignedRaw.get(10) == 0x4f
                        && unsignedRaw.get(11) == 0x49 && unsignedRaw.get(12) == 0x4c) {
                        int index = unsignedRaw.get(17);
                        int w = unsignedRaw.get(18) + unsignedRaw.get(19) * 256;
                        int x = unsignedRaw.get(20) + unsignedRaw.get(21) * 256;
                        int y = unsignedRaw.get(22) + unsignedRaw.get(23) * 256;
                        int z = unsignedRaw.get(24) + unsignedRaw.get(25) * 256;
                        int emg = unsignedRaw.get(26) + unsignedRaw.get(27) * 256;
                        Emg data = new Emg(index, w, x, y, z, emg);
                        broadcastUpdate(ACTION_BEACON_DATA, data);
                    }
                }
            }
            BluetoothDevice device = result.getDevice();
            scannedDevices.add(device);
            if ((device.getName() != null) && !device.getName().isEmpty()) {
                for (String filterDeviceName : getFilterDeviceName()) {
                    if (device.getName().contains(filterDeviceName)) {
                                if (device.getName().contains(filterDeviceName)) {
                                    broadcastUpdate(ACTION_FIND_DEVICE, device.getName(), device.getAddress());
                                    EmgStopwatch.getInstance().reset();
                        }
                    }
                }
            }
        }
        public List<String> getFilterDeviceName() {
            return filterDeviceName;
        }
    };
}