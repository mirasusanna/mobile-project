package fi.mobileproject.healthtracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toga on 11.10.2016.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BluetoothScanner {

    private Handler handler;
    private Context context;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private Activity activity;
    private BluetoothLeScanner leScanner;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private BluetoothDevice btDevice = null;

    public BluetoothScanner(Context context, Activity activity) {
        handler = new Handler();
        this.context = context;
        this.activity = activity;

        checkLocationPermission();

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        leScanner = btAdapter.getBluetoothLeScanner();

        /* check if BLE is supported */
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "BLE Not Supported", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    public void scanDevices(int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                leScanner.stopScan(leScanCallback);
                System.out.println("Scan stopped!");
            }
        }, delay);

        leScanner.startScan(leScanCallback);
    }

    public List<BluetoothDevice> getDeviceList() {
        return deviceList;
    }

    public void setBtDevice(BluetoothDevice btDevice) {
        this.btDevice = btDevice;
    }

    public BluetoothDevice getBtDevice() {
        return btDevice;
    }

    public void stopScanner() {
        leScanner.stopScan(leScanCallback);
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            System.out.println("BLE // onScanResult");
            System.out.println(result);

            BluetoothDevice btDevice = result.getDevice();
            deviceList.add(btDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            System.out.println("BLE// onBatchScanResults");
            for (ScanResult sr : results) {
                System.out.println(sr);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            System.out.println("BLE// onScanFailed");
            System.out.println(errorCode);
        }
    };

    private void checkLocationPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        switch(permissionCheck){
            case PackageManager.PERMISSION_GRANTED:
                break;

            case PackageManager.PERMISSION_DENIED:

                if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
                    //Show an explanation to user *asynchronously* -- don't block
                    //this thread waiting for the user's response! After user sees the explanation, try again to request the permission

                    Toast.makeText(context, "Location access is required to show Bluetooth devices nearby.", Toast.LENGTH_SHORT).show();

                }
                else{
                    //No explanation needed, we can request the permission
                    ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
                break;
        }
    }
}
