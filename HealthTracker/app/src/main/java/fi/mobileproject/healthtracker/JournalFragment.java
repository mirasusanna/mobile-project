package fi.mobileproject.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by hieun on 27/09/16.
 */
public class JournalFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> listTitles = new ArrayList<>(), listContents = new ArrayList<>();

    public JournalFragment() {

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLite exerciseDB = new SQLite(getContext(), "exercisedb", null, 2);
        SQLiteDatabase db = exerciseDB.getReadableDatabase();

        Cursor c = db.query("exercisedb", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                listTitles.add(c.getString(0));
                listContents.add(c.getString(4) + " minutes");
            } while(c.moveToNext());
        }
        c.close();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.JournalFragment_recyclerView);
        adapter = new JournalFragmentRecyclerViewAdapter(listTitles, listContents);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        RecyclerItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // Do stuff when clicked.
                        System.out.println("You clicked on position: " + position);
                        Intent intent = new Intent(getContext(), JournalEntryActivity.class);
                        intent.putExtra("entry", position+1);
                        startActivity(intent);
                    }
                });

        return view;
    }
}
