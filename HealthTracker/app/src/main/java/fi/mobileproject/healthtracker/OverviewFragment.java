package fi.mobileproject.healthtracker;

import android.content.Context;
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

/**
 * Created by hieun on 27/09/16.
 */
public class OverviewFragment extends Fragment {
    private BarChart barChart;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    String[] cardTitles = {"Distance", "Steps", "Calories", "BPM"};
    String[] cardContents = {"8.4km", "7646", "Calories", "109"};

    public OverviewFragment() {

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        barChart = (BarChart) view.findViewById(R.id.barChart);

        recyclerView = (RecyclerView) view.findViewById(R.id.overviewInfo);
        adapter = new OverviewRecyclerViewAdapter(cardTitles, cardContents);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        System.out.println("recycler view?");
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(5400, 0));
        barEntries.add(new BarEntry(5600, 1));
        barEntries.add(new BarEntry(9800, 2));
        barEntries.add(new BarEntry(6700, 3));
        barEntries.add(new BarEntry(6800, 4));
        barEntries.add(new BarEntry(3300, 5));
        barEntries.add(new BarEntry(7700, 6));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
        barDataSet.setColor(ColorTemplate.rgb("#E91E63"));

        ArrayList<String> barMonths = new ArrayList<>();
        barMonths.add("FEB");
        barMonths.add("MAR");
        barMonths.add("APR");
        barMonths.add("MAY");
        barMonths.add("JUN");
        barMonths.add("JUL");
        barMonths.add("AUG");
        barMonths.add("SEP");

        BarData barData = new BarData(barMonths, barDataSet);
        barChart.setData(barData);
        barChart.setTouchEnabled(true);

        return view;
    }
}

