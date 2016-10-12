package fi.mobileproject.healthtracker;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by hieun on 11/10/16.
 */

public class OverviewRecyclerViewAdapter extends RecyclerView.Adapter<ListViewRowHolder> {

    private String[] cardTitles, cardContents;
    private Context mContext;
    private ImageLoader imageLoader;
    private int focusedItem = 0;


    public OverviewRecyclerViewAdapter(String[] cardTitles, String[] cardContents) {
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
        listViewRowHolder.cardTitle.setText(cardTitles[position]);
        listViewRowHolder.cardContent.setText(cardContents[position]);
    }

    @Override
    public int getItemCount() {
        return cardTitles.length;
    }
}
