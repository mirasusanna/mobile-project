package fi.mobileproject.healthtracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by hieun on 27/09/16.
 */
public class OverviewFragment extends Fragment {
    private BarChart barChart;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> listTitles = new ArrayList<>();
    private ArrayList<String> listContents = new ArrayList<>();
    private double totalDistance = 0.0;
    private double totalCalories = 0.0;
    private int maxBPM = 0;
    private int totalDuration = 0;

    public OverviewFragment() {

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listTitles.add("Total Distance");
        listTitles.add("Total Calories");
        listTitles.add("Max BPM");
        listTitles.add("Total Duration");

        SQLite exerciseDB = new SQLite(getContext(), "exercisedb", null, 2);
        SQLiteDatabase db = exerciseDB.getReadableDatabase();

        Cursor c = db.query("exercisedb", null, null, null, null, null, null);
        System.out.println(c.getCount());

        if (c.moveToFirst()) {
            do {
                totalDistance += Double.parseDouble(c.getString(1));
                totalCalories += Double.parseDouble(c.getString(2));
                if (maxBPM < Integer.parseInt(c.getString(3))) {
                    maxBPM = Integer.parseInt(c.getString(3));
                }
                totalDuration += Integer.parseInt(c.getString(4));
            } while(c.moveToNext());
        }
        listContents.add(String.format(Locale.ENGLISH, "%.1f KM", totalDistance));
        listContents.add(String.format(Locale.ENGLISH, "%.1f cal", totalCalories));
        listContents.add(String.format(Locale.ENGLISH, "%s BPM", Integer.toString(maxBPM)));
        listContents.add(String.format(Locale.ENGLISH, "%s minutes", Integer.toString(totalDuration)));
        c.close();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        barChart = (BarChart) view.findViewById(R.id.barChart);

        recyclerView = (RecyclerView) view.findViewById(R.id.overviewInfo);
        adapter = new OverviewRecyclerViewAdapter(listTitles, listContents);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(5400, 0));
        barEntries.add(new BarEntry(5600, 1));
        barEntries.add(new BarEntry(9800, 2));
        barEntries.add(new BarEntry(6700, 3));
        barEntries.add(new BarEntry(6800, 4));
        barEntries.add(new BarEntry(3300, 5));
        barEntries.add(new BarEntry(3300, 6));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
        barDataSet.setColor(ColorTemplate.rgb("#E91E63"));

        ArrayList<String> barMonths = new ArrayList<>();
        barMonths.add("MON");
        barMonths.add("TUE");
        barMonths.add("WED");
        barMonths.add("THU");
        barMonths.add("FRI");
        barMonths.add("SAT");
        barMonths.add("SUN");

        BarData barData = new BarData(barMonths, barDataSet);
        barChart.setData(barData);
        barChart.setTouchEnabled(true);

        return view;
    }
}

