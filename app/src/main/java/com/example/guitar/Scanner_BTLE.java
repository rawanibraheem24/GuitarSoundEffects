package com.example.guitar;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;

public class Scanner_BTLE {
    private MainActivity ma;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private boolean mScanning;
    private Handler mHandler;
    private long scanPeriod;
    private int signalStrength;

    public Scanner_BTLE(MainActivity mainActivity, long scanPeriod, int signalStrength) {
        ma = mainActivity;
        mHandler = new Handler();
        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager = (BluetoothManager) ma.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void start() {
        if (!Utils.checkBluetooth(mBluetoothAdapter)) {
            Utils.requestUserBluetooth(ma);
            mScanning = false;
            ma.stopScan();
        } else {
            scanLeDevice(true);
        }
    }

    public void stop() {
        mScanning = false;
        scanLeDevice(false);
    }

    @SuppressLint("MissingPermission")
    private void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {
            Utils.toast(ma.getApplicationContext(), "Starting BLE scan...");
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    if (mScanning) { // Check if scanning is still in progress
                        Utils.toast(ma.getApplicationContext(), "Stopping BLE scan...");
                        mScanning = false;
                        mBluetoothLeScanner.stopScan(mScanCallback); // Stop the scan
                        ma.stopScan();
                    }
                }
            }, scanPeriod);
            mScanning = true;
            // Start a scan
            mBluetoothLeScanner.startScan(mScanCallback);
        } else {
            if (mScanning) { // Check if scanning is in progress before stopping
                mScanning = false;
                // Stop the scan
                mBluetoothLeScanner.stopScan(mScanCallback);
            }
        }
    }

    // Device scan callback.
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            int rssi = result.getRssi();
            if (rssi > signalStrength) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ma.addDevice(device, rssi);
                    }
                });
            }
        }
    };
}
