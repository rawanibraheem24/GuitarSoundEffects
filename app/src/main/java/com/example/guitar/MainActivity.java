package com.example.guitar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.widget.AdapterView;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.bluetooth.le.ScanCallback;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.bluetooth.ScanResult;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.guitar.ui.presets.PresetAdapter;
import com.example.guitar.ui.presets.PresetsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, MainFragment.OnSaveButtonClickListener{

    private boolean hasBluetoothConnectPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
    }
    private final static String TAG = MainActivity.class.getSimpleName();
    private BluetoothGatt bluetoothGatt;
    private Button btn_Scan; // Declare the button variable
    public static final int REQUEST_ENABLE_BT = 1;

    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 2;
    public static final int BTLE_SERVICES = 2;

    private HashMap<String, BTLE_Device> mBTDevicesHashMap;
    private ArrayList<BTLE_Device> mBTDevicesArrayList;
    private ListAdapter_BTLE_Devices adapter;
    private ListView listView;
    // Define a constant for the permission request
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private static final int BUTTON_SCAN_ID = R.id.btn_scan;

    private BroadcastReceiver_BTState mBTStateUpdateReceiver;
    private Scanner_BTLE mBTLeScanner;

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt; // Replace with the actual reference to your BluetoothGatt object
    }

    public void sendDataOverBluetooth(BluetoothGatt gatt, String dataToSend) {
        BluetoothGattService service = gatt.getService(UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66"));
        // Then, you can get a reference to a specific characteristic within the service:
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"));

        if (gatt != null && characteristic != null) {  // Fix the variable name
            byte[] data = dataToSend.getBytes(); // Convert your data to bytes
            characteristic.setValue(data); // Fix the variable name

            @SuppressLint("MissingPermission") boolean success = gatt.writeCharacteristic(characteristic); // Fix the variable name

            if (success) {
                // Data sent successfully
                // You can add a callback here to handle success or failure
            } else {
                // Failed to send data
                // Handle the error here
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }
        }

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.toast(getApplicationContext(), "BLE not supported");
            finish();
        }

        mBTStateUpdateReceiver = new BroadcastReceiver_BTState(getApplicationContext());
        mBTLeScanner = new Scanner_BTLE(this, 5000, -100);

        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        // Initialize the adapter here and set it to the ListView
        adapter = new ListAdapter_BTLE_Devices(this, R.layout.btle_device_list_item, mBTDevicesArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);



        btn_Scan = (Button) findViewById(R.id.btn_scan);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
        btn_Scan.setOnClickListener((View.OnClickListener) this);

        ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Bluetooth is enabled. You can perform the desired action here.
                    } else {
                        // User didn't enable Bluetooth.
                        Utils.toast(getApplicationContext(), "Please turn on Bluetooth");
                    }
                }
        );


        // Find the BottomNavigationView
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Set the default fragment to MainFragment when the app starts
        //showFragment(new MainFragment());

        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (itemId == R.id.navigation_presets) {
                // Hide the other fragments
                MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentByTag("main_fragment");
                if (mainFragment != null) {
                    transaction.hide(mainFragment);
                }

                // Show or add the PresetsFragment
                PresetsFragment presetsFragment = (PresetsFragment) fragmentManager.findFragmentByTag("presets_fragment");
                if (presetsFragment == null) {
                    presetsFragment = new PresetsFragment();
                    transaction.add(R.id.fragment_container, presetsFragment, "presets_fragment");
                } else {
                    transaction.show(presetsFragment);
                }

                btn_Scan.setVisibility(View.GONE);
            } else if (itemId == R.id.navigation_main) {
                // Hide the other fragments
                PresetsFragment presetsFragment = (PresetsFragment) fragmentManager.findFragmentByTag("presets_fragment");
                if (presetsFragment != null) {
                    transaction.hide(presetsFragment);
                }

                // Show or add the MainFragment
                MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentByTag("main_fragment");
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.fragment_container, mainFragment, "main_fragment");
                } else {
                    transaction.show(mainFragment);
                }

                btn_Scan.setVisibility(View.GONE);
            } else if (itemId == R.id.navigation_ble) {
                // Handle navigation to the BLE activity (activity_main.xml)
                btn_Scan.setVisibility(View.VISIBLE);

                // Hide the other fragments
                PresetsFragment presetsFragment = (PresetsFragment) fragmentManager.findFragmentByTag("presets_fragment");
                if (presetsFragment != null) {
                    transaction.hide(presetsFragment);
                }

                MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentByTag("main_fragment");
                if (mainFragment != null) {
                    transaction.hide(mainFragment);
                }
            }

            transaction.commit();
            return true;
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start scanning
                Utils.toast(getApplicationContext(), "Permission granted. Starting scan.");
                checkAndRequestBluetoothScanPermission();
                startScan();
            } else {
                // Permission denied, inform the user
                Utils.toast(getApplicationContext(), "Permission denied. Cannot start scanning.");
            }
        }
    }

    private void checkAndRequestBluetoothScanPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_SCAN}, REQUEST_BLUETOOTH_SCAN_PERMISSION);
        } else {
            // Permission is already granted, start scanning here.
            startScan();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndRequestBluetoothScanPermission();
        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();

//        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();

//        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkAndRequestBluetoothScanPermission();
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
//                Utils.toast(getApplicationContext(), "Thank you for turning on Bluetooth");
            } else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Please turn on Bluetooth");
            }
        } else if (requestCode == BTLE_SERVICES) {
            // Do something
        }
    }
    // Inside onItemClick method
    @SuppressLint("MissingPermission")
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Get the BTLE_Device at the clicked position
        BTLE_Device btleDevice = adapter.getBTLEDevice(position);

        if (btleDevice != null) {
            String deviceName = btleDevice.getName();
            String deviceAddress = btleDevice.getAddress();


            // Get a reference to the BluetoothDevice
            BluetoothDevice bluetoothDevice = btleDevice.getBluetoothDevice();


            // Create a GATT callback to handle GATT connection and operations
            BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        // Connection established, discover services
                        gatt.discoverServices();
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        // Handle disconnection
                    }
                }

                @SuppressLint("MissingPermission")
                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        // Services discovered, you can now interact with the device
                        // For example, you can get a reference to a specific service:
                        BluetoothGattService service = gatt.getService(UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66"));

                        // Then, you can get a reference to a specific characteristic within the service:
                        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"));

                        if (characteristic != null) {
                            // Prepare the data you want to send as a byte array
                            byte[] data = "Connected".getBytes();

                            // Set the value of the characteristic with the data
                            characteristic.setValue(data);

                            // Write the data to the characteristic
                            gatt.writeCharacteristic(characteristic);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.toast(MainActivity.this, "Connected");
                                }
                            });
                        }
                    } else {
                        // Handle service discovery failure
                    }
                }
            };

            // Update the class member with the GATT connection
            bluetoothGatt = bluetoothDevice.connectGatt(getApplicationContext(), false, gattCallback);

            // Initiate the GATT connection
            @SuppressLint("MissingPermission") BluetoothGatt bluetoothGatt = bluetoothDevice.connectGatt(getApplicationContext(), false, gattCallback);
        }
    }

    @Override
    public void onSaveButtonClicked(String dataToSend) {
        if(bluetoothGatt!=null){
            sendDataOverBluetooth(bluetoothGatt, dataToSend);}
        else{
            Utils.toast(getApplicationContext(), "Connect to BLE Device");
        }
    }

    public void onClick(View v) {
        checkAndRequestBluetoothScanPermission();
        if (v.getId() == R.id.btn_scan) {
            if (hasBluetoothConnectPermission()) {
                Utils.toast(getApplicationContext(), "Scan Button Pressed");
                if (!mBTLeScanner.isScanning()) {
                    mBTLeScanner.start(); // Start the scan
                    btn_Scan.setText("Scanning...");
                } else {
                    mBTLeScanner.stop(); // Stop the scan
                    btn_Scan.setText("Scan Again");
                }
            } else {
                // Request the BLUETOOTH_CONNECT permission from the user
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
            }
        } else {
            // Handle other cases if needed
        }
    }

    public void addDevice(BluetoothDevice device, int rssi) {


        String address = device.getAddress();
        if (!mBTDevicesHashMap.containsKey(address)) {
            BTLE_Device btleDevice = new BTLE_Device(device);
            btleDevice.setRSSI(rssi);

            mBTDevicesHashMap.put(address, btleDevice);
            mBTDevicesArrayList.add(btleDevice);
        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan() {
        btn_Scan.setText("Scanning...");

        // Clear the previous list of devices
        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();

        // Notify the adapter to refresh the ListView
        adapter.notifyDataSetChanged();

        mBTLeScanner.start();
    }

    public void stopScan() {
        mBTLeScanner.stop();
        btn_Scan.setText("Scan Again");
        // Notify the adapter to refresh the ListView
        adapter.notifyDataSetChanged();
    }

    private void showFragment(Fragment fragment) {
        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the provided fragment
        transaction.replace(R.id.fragment_container, fragment);

        // Add the transaction to the back stack (optional, for navigating back)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}

