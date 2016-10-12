package fi.mobileproject.healthtracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hieun on 27/09/16.
 */
public class DailyFragment extends Fragment implements SensorEventListener, Observer {
    private SensorManager sm;
    private boolean running = false;
    private Button btn;
    private TextView tv_distance;
    private TextView tv_calories;
    private TextView tv_BPM;
    private com.github.lzyzsd.circleprogress.DonutProgress progressCircle;
    private int progressCircle_maxValue = 150;
    private int steps = 0;
    private BluetoothConnector btc;
    private BTDeviceDelegate delegate;

    public DailyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        delegate = BTDeviceDelegate.INSTANCE;

        btc = new BluetoothConnector(getContext());
        btc.addObserver(this);

        //if (delegate.getBtDevice() != null) {
        //    btc.connectToDevice(delegate.getBtDevice());
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        btn = (Button) view.findViewById(R.id.DailyFragment_start_button);
        tv_distance = (TextView) view.findViewById(R.id.DailyFragment_distance);
        tv_distance.setText("0\nKM");
        tv_calories = (TextView) view.findViewById(R.id.DailyFragment_calories);
        tv_calories.setText("0\nCAL");
        tv_BPM = (TextView) view.findViewById(R.id.DailyFragment_BPM);
        tv_BPM.setText("-\nBPM");
        progressCircle = (com.github.lzyzsd.circleprogress.DonutProgress) view.findViewById(R.id.donut_progress);
        progressCircle.setMax(progressCircle_maxValue);
        progressCircle.setSuffixText(" / " + progressCircle_maxValue + " steps");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    running = true;
                    if (delegate.getBtDevice() != null) {
                        btc.connectToDevice(delegate.getBtDevice());
                    } else {
                        System.out.println("AAAAAAA!!!");
                    }
                    btn.setText("STOP");
                } else {
                    running = false;
                    btn.setText("START");
                }
            }
        });

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            steps += 1;
        }
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
