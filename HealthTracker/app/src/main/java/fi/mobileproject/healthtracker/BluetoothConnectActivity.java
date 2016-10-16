package fi.mobileproject.healthtracker;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toga on 12.10.2016.
 */

public class BluetoothConnectActivity extends AppCompatActivity {

    private TextView tv_title;
    private BluetoothScanner bts;
    private List<BluetoothDevice> deviceList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> listTitles = new ArrayList<>(), listContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_connect);

        tv_title = (TextView) findViewById(R.id.BluetoothConnectActivity_title);
        tv_title.setText(getString(R.string.scanning_bt_devices));

        bts = new BluetoothScanner(this, this);
        bts.scanDevices(5000);

        recyclerView = (RecyclerView) findViewById(R.id.bt_connect_list);
        adapter = new BluetoothConnectRecyclerViewAdapter(listTitles, listContents);
        layoutManager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceList = bts.getDeviceList();
                for (BluetoothDevice device : deviceList) {
                    listTitles.add(device.getName());
                    listContents.add(device.getAddress());
                }
                adapter.notifyDataSetChanged();
                tv_title.setText(getString(R.string.bt_device_scan_complete));
            }
        }, 6000);

        RecyclerItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        bts.setBtDevice(deviceList.get(position));

                        BluetoothDeviceDelegate delegate = BluetoothDeviceDelegate.INSTANCE;
                        delegate.setBtDevice(deviceList.get(position));
                        recyclerView.setAdapter(new BluetoothConnectRecyclerViewAdapter(new ArrayList<String>(), new ArrayList<String>()));
                        tv_title.setText(getString(R.string.bt_device_connection_complete));
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        bts.stopScanner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bts.stopScanner();
    }
}
