package fi.mobileproject.healthtracker;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by MiraHiltunen on 10/10/16.
 */

public class PreferencesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }

}
