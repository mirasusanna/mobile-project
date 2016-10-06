package fi.mobileproject.healthtracker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by hieun on 27/09/16.
 */
public class OverviewFragment extends Fragment {
    private RelativeLayout overviewLayout;
    private LineChart lChart;

    public OverviewFragment() {

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        lChart = (LineChart) view.findViewById(R.id.lineChart);
        LineDataSet dataSet = new LineDataSet(getDataSet(), "Label");
        dataSet.setColor(Color.argb(1, 44, 44, 44));
        dataSet.setValueTextColor(Color.WHITE);
        LineData lineData = new LineData(dataSet);
        lChart.setData(lineData);
        lChart.invalidate();

        // chart config
        lChart.setDescription("Progress");


        return view;
    }

    private ArrayList<Entry> getDataSet() {
        ArrayList<Entry> dataSets = null;

        ArrayList<Entry> valueSet1 = new ArrayList<>();
        int i = 0;
        for (Integer value : getYAxisValues()) {
            valueSet1.add(new Entry(value, i));
            i++;
        }

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        return xAxis;
    }

    private ArrayList<Integer> getYAxisValues() {
        ArrayList<Integer> yAxis = new ArrayList<>();
        yAxis.add(5600);
        yAxis.add(6000);
        yAxis.add(5900);
        yAxis.add(6100);
        yAxis.add(6300);
        yAxis.add(6300);
        yAxis.add(7000);
        return yAxis;
    }

    private void setXAxis(LineChart chart){
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLUE);
        //xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
    }
}
