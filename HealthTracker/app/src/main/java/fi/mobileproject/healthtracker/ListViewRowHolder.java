package fi.mobileproject.healthtracker;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by hieun on 11/10/16.
 */

public class ListViewRowHolder extends RecyclerView.ViewHolder {
    TextView cardTitle;
    TextView cardContent;
    ImageView cardThumbnail;

    public ListViewRowHolder (View view) {
        super(view);
        this.cardThumbnail = (ImageView) view.findViewById(R.id.card_thumbnail);
        this.cardTitle = (TextView) view.findViewById(R.id.card_title);
        this.cardContent = (TextView) view.findViewById(R.id.card_content);
        view.setClickable(true);
    }
}
