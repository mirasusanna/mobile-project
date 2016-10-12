package fi.mobileproject.healthtracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Toga on 12.10.2016.
 */

public class BluetoothConnectRecyclerViewAdapter extends RecyclerView.Adapter<ListViewRowHolder> {

    private ArrayList<String> titles, contents;

    public BluetoothConnectRecyclerViewAdapter(ArrayList<String> titles, ArrayList<String> contents) {
        this.titles = titles;
        this.contents = contents;
    }

    @Override
    public ListViewRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ListViewRowHolder holder = new ListViewRowHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListViewRowHolder holder, int position) {
        holder.cardTitle.setText(titles.get(position));
        holder.cardContent.setText(contents.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
}
