package fi.mobileproject.healthtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class JournalEntryActivity extends AppCompatActivity {

    private ArrayList<String> entryDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.JournalEntryActivity_toolbar);
        setSupportActionBar(toolbar);

        int entry = getIntent().getExtras().getInt("entry");

        SQLite exerciseDB = new SQLite(this, "exercisedb", null, 2);
        SQLiteDatabase db = exerciseDB.getReadableDatabase();

        Cursor c = db.query("exercisedb", null, null, null, null, null, null);

        if (c.move(entry)) {
            entryDetails.add(c.getString(0));
            entryDetails.add(c.getString(1));
            entryDetails.add(c.getString(2));
            entryDetails.add(c.getString(3));
            entryDetails.add(c.getString(4));
        }
        c.close();
        db.close();

        // String.format(Locale.ENGLISH, "%.1f\nKM",distance)

        TextView tv_date = (TextView) findViewById(R.id.JournalEntryActivity_date);
        tv_date.setText(entryDetails.get(0));
        TextView tv_duration = (TextView) findViewById(R.id.JournalEntryActivity_duration);
        tv_duration.setText(String.format(Locale.ENGLISH, "%s minutes",entryDetails.get(4)));
        TextView tv_distance = (TextView) findViewById(R.id.JournalEntryActivity_distance);
        tv_distance.setText(String.format(Locale.ENGLISH, "%.3s\nKM",entryDetails.get(1)));
        TextView tv_calories = (TextView) findViewById(R.id.JournalEntryActivity_calories);
        tv_calories.setText(String.format(Locale.ENGLISH, "%.3s\nCAL",entryDetails.get(2)));
        TextView tv_maxBPM = (TextView) findViewById(R.id.JournalEntryActivity_maxbpm);
        tv_maxBPM.setText(String.format(Locale.ENGLISH, "%s\nMax BPM",entryDetails.get(3)));

    }

}
