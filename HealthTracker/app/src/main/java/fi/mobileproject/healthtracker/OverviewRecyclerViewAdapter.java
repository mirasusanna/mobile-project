package fi.mobileproject.healthtracker;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hieun on 11/10/16.
 */

public class OverviewRecyclerViewAdapter extends RecyclerView.Adapter<ListViewRowHolder> {

    private ArrayList<String> cardTitles, cardContents = new ArrayList<>();
    private Context mContext;
    private ImageLoader imageLoader;
    private int focusedItem = 0;


    public OverviewRecyclerViewAdapter(ArrayList cardTitles, ArrayList cardContents) {
        this.cardTitles = cardTitles;
        this.cardContents = cardContents;
    }

    @Override
    public ListViewRowHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        ListViewRowHolder holder = new ListViewRowHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ListViewRowHolder listViewRowHolder, int position) {
        listViewRowHolder.cardTitle.setText(cardTitles.get(position));
        listViewRowHolder.cardContent.setText(cardContents.get(position));
    }

    @Override
    public int getItemCount() {
        return cardTitles.size();
    }
}
