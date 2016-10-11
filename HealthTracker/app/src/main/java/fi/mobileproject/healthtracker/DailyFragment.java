package fi.mobileproject.healthtracker;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hieun on 27/09/16.
 */
public class DailyFragment extends Fragment implements SensorEventListener, Observer {
    private SensorManager sm;
    private TextView tv_distance;
    private TextView tv_calories;
    private TextView tv_BPM;
    private com.github.lzyzsd.circleprogress.DonutProgress progressCircle;
    private int progressCircle_maxValue = 300;
    private int steps = 0;

    BluetoothScanner bts;
    BluetoothConnector btc;

    List<BluetoothDevice> deviceList;

    public DailyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        bts = new BluetoothScanner(getContext(), getActivity());
        btc = new BluetoothConnector(getContext());
        btc.addObserver(this);
        bts.scanDevices(5000);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceList = bts.getDeviceList();

                btc.connectToDevice(deviceList.get(0));
            }
        }, 6000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        tv_distance = (TextView) view.findViewById(R.id.DailyFragment_distance);
        tv_distance.setText("0\nKM");
        tv_calories = (TextView) view.findViewById(R.id.DailyFragment_calories);
        tv_calories.setText("0\nCAL");
        tv_BPM = (TextView) view.findViewById(R.id.DailyFragment_BPM);
        tv_BPM.setText("-\nBPM");
        progressCircle = (com.github.lzyzsd.circleprogress.DonutProgress) view.findViewById(R.id.donut_progress);
        progressCircle.setMax(progressCircle_maxValue);
        progressCircle.setSuffixText(" / " + progressCircle_maxValue + " steps");

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //tv_stepsTaken.setText(String.valueOf(event.values[0]));
        //(int)event.values[0];
        steps += 1;
        if (steps <= progressCircle_maxValue) {
            progressCircle.setProgress(steps);
            tv_distance.setText(String.format(Locale.ENGLISH, "%.1f\nKM",((steps*0.7)/1000)));
            tv_calories.setText(String.format(Locale.ENGLISH, "%.1f\nCAL",(steps*0.05)));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor sensor_stepCounter = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor_stepCounter != null) {
            sm.registerListener(this, sensor_stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(getContext(), "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
        bts.stopScanner();
    }

    @Override
    public void update(Observable observable, Object o) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_BPM.setText(String.format(Locale.ENGLISH, "%s\nBPM", btc.getBPM()));
            }
        });
    }
}
