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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hieun on 27/09/16.
 */
public class DailyFragment extends Fragment implements SensorEventListener {
    private View view;
    private SensorManager sm;
    private TextView tv_stepsTaken;
    private Sensor sensor_stepCounter;

    public DailyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily, container, false);
        tv_stepsTaken = (TextView)view.findViewById(R.id.DailyFragment_stepsTaken);

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tv_stepsTaken.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onResume() {
        super.onResume();
        sensor_stepCounter = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
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
}
