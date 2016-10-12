package fi.mobileproject.healthtracker;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * Created by Toga on 12.10.2016.
 */

public enum BTDeviceDelegate {
    INSTANCE;

    private BTDeviceDelegate() {
    }

    private BluetoothDevice device = null;

    public BluetoothDevice getBtDevice() {
        return this.device;
    }

    public void setBtDevice(BluetoothDevice device) {
        System.out.println("DEVICE SET!");
        this.device = device;
    }
}
