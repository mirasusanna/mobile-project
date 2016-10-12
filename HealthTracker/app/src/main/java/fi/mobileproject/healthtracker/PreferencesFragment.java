package fi.mobileproject.healthtracker;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by MiraHiltunen on 10/10/16.
 */

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
