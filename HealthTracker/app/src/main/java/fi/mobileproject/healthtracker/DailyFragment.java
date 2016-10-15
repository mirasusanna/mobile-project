package fi.mobileproject.healthtracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * Created by hieun on 27/09/16.
 */
public class DailyFragment extends Fragment implements SensorEventListener, Observer {
    private SensorManager sm;
    private boolean running = false;
    private Button btn;
    private TextView tv_date;
    private TextView tv_distance;
    private TextView tv_calories;
    private TextView tv_BPM;
    private com.github.lzyzsd.circleprogress.DonutProgress progressCircle;
    private int progressCircle_maxValue = 10000;
    private int steps = 0;
    private BluetoothConnector btc;
    private BTDeviceDelegate delegate;
    private CalorieCounter calorieCounter;
    private int maxBPM = 0;
    private double calories;
    private double distance;
    private SharedPreferences prefs;
    private DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
    private Date date;

    public DailyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
        progressCircle_maxValue = Integer.parseInt(prefs.getString("goal", "10000"));

        date = new Date();

        calorieCounter = new CalorieCounter(getContext());

        delegate = BTDeviceDelegate.INSTANCE;

        btc = new BluetoothConnector(getContext());
        btc.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        btn = (Button) view.findViewById(R.id.DailyFragment_start_button);
        tv_date = (TextView) view.findViewById(R.id.DailyFragment_date);
        tv_date.setText(dateFormat.format(date));
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
                        System.out.println("No device");
                    }
                    btn.setText("STOP");
                } else {
                    running = false;

                    // Ask user to save entry to SQLite database
                    new AlertDialog.Builder(getContext())
                            .setTitle("Save entry")
                            .setMessage("Do you want to save this entry to your journal?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SQLite exerciseDB = new SQLite(getContext(), "exercisedb", null, 1);
                            SQLiteDatabase db = exerciseDB.getWritableDatabase();
                            if (db != null) {
                                String dbDistance = Double.toString(distance);
                                String dbCalories = Double.toString(calories);
                                String dbMaxBPM = Integer.toString(maxBPM);
                                ContentValues newRecord = new ContentValues();
                                newRecord.put("date", dateFormat.format(date));
                                newRecord.put("distance",dbDistance);
                                newRecord.put("calories",dbCalories);
                                newRecord.put("maxbpm",dbMaxBPM);
                                long duration = new Date().getTime() - date.getTime();
                                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                                newRecord.put("duration",Long.toString(diffInMinutes));

                                db.insert("exercisedb", null, newRecord);
                            }
                            db.close();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

                    btn.setText("START");
                }
            }
        });

        return view;
    }

    public SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("goal")) {
                System.out.println("---Debug---");
                // It would appear this progressCircle cannot change it's view during runtime? :(
                progressCircle_maxValue = Integer.parseInt(prefs.getString("goal", "10000"));
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println("Asd pasd it's taking steps!");
        if (running) {
            steps += 1;
        }
        if (steps <= progressCircle_maxValue) {
            progressCircle.setProgress(steps);
            distance = ((steps*0.7)/1000);
            tv_distance.setText(String.format(Locale.ENGLISH, "%.1f\nKM",distance));
            calories = calories + 0.05;
            tv_calories.setText(String.format(Locale.ENGLISH, "%.1f\nCAL",calories));
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
                if (maxBPM < Integer.parseInt(btc.getBPM()) && running) {
                    maxBPM = Integer.parseInt(btc.getBPM());
                }
                if (running) {
                    calories = calories + calorieCounter.countExerciseCalories(Integer.parseInt(btc.getBPM()));
                    tv_calories.setText(String.format(Locale.ENGLISH, "%.1f\nCAL",calories));
                }
                tv_BPM.setText(String.format(Locale.ENGLISH, "%s\nBPM", btc.getBPM()));
            }
        });
    }
}
