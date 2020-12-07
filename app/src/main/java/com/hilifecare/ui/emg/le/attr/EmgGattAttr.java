package com.hilifecare.ui.emg.le.attr;

import java.util.UUID;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class EmgGattAttr {
    public static final Integer SCAN_PERIOD = 3600000;
    public static final Integer STATE_DISCONNECTED = 0;
    public static final Integer STATE_CONNECTING = 1;
    public static final Integer STATE_CONNECTED = 2;

    public static final String EXTRA_EMG_DATA = "com.cb.sdk.le.attr.EXTRA_EMG_DATA";
    public static final String ACTION_GATT_CONNECTED = "com.cb.sdk.le.attr.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.cb.sdk.le.attr.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.cb.sdk.le.attr.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_DATA_AVAILABLE = "com.cb.sdk.le.attr.ACTION_DATA_AVAILABLE";
    public static final String ACTION_BEACON_DATA = "com.cb.sdk.le.attr.ACTION_BEACON_DATA";
    public static final String EXTRA_BEACON_DATA = "com.cb.sdk.le.attr.EXTRA_BEACON_DATA";

    public static final String EXTRA_DATA = "com.cb.sdk.le.attr.EXTRA_DATA";
    public static final String EXTRA_SERIAL_NUMBER = "com.cb.sdk.le.attr.EXTRA_SERIAL_NUMBER";
    public static final String EXTRA_DATA_DEVICE_ADDRESS = "com.cb.sdk.le.attr.EXTRA_DATA_DEVICE_ADDRESS";
    public static final String EXTRA_DATA_DEVICE_NAME = "com.cb.sdk.le.attr.EXTRA_DATA_DEVICE_NAME";
    public static final String ACTION_FIND_DEVICE = "com.cb.sdk.le.attr.ACTION_FIND_DEVICE";


    public static final UUID UART_SERVICE_UUID = convertUUID("0003CDD0-0000-1000-8000-00805f9b0131");
    public static final UUID UART_TX_CHAR_UUID = convertUUID("0003CDD1-0000-1000-8000-00805f9b0131");
    public static final UUID UART_TX_CCCD_UUID = convertUUID("00002902-0000-1000-8000-00805f9b34fb");

    private static UUID convertUUID(String uuid) {
        return UUID.fromString(uuid);
    }

}