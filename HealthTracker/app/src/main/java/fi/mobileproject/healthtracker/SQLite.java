package fi.mobileproject.healthtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Toga on 15.10.2016.
 */

public class SQLite extends SQLiteOpenHelper {

    String createSQL = "CREATE TABLE exercisedb (date TEXT, distance REAL, calories REAL, maxbpm INTEGER, duration TEXT)";

    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS exercisedb");
        sqLiteDatabase.execSQL(createSQL);
    }
}
