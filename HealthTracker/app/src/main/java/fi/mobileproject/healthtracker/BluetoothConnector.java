package fi.mobileproject.healthtracker;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import java.util.List;
import java.util.Observable;

/**
 * Created by Toga on 11.10.2016.
 */

public class BluetoothConnector extends Observable {

    private Context context;
    private BluetoothGatt btGatt;
    private String deviceName;
    private String hr_value;

    public BluetoothConnector(Context context) {
        this.context = context;
    }

    public void connectToDevice(BluetoothDevice device) {
        if (btGatt == null) {
            btGatt = device.connectGatt(context, false, gattCallback);
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            System.out.println("Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    System.out.println("STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    System.out.println("STATE_DISCONNECTED");
                    break;
                default:
                    System.out.println("STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            BluetoothGattCharacteristic hrm_char = services.get(2).getCharacteristics().get(0);
            for (BluetoothGattDescriptor descriptor : hrm_char.getDescriptors()) {
                descriptor.setValue( BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }
            gatt.setCharacteristicNotification(hrm_char, true);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println(gatt);
            System.out.println(characteristic);
            System.out.println(status);
            System.out.println("read: "+characteristic.getUuid());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

            int char_int_value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8,1);

            deviceName = gatt.getDevice().getName();
            hr_value = Integer.toString(char_int_value);

            setChanged();
            notifyObservers();

            System.out.println(deviceName);
            System.out.println(hr_value);
        }
    };

    public String getBPM() {
        return hr_value;
    }
}
