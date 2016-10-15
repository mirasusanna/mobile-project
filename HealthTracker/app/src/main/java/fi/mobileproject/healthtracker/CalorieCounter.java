package fi.mobileproject.healthtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by MiraHiltunen on 08/10/16.
 */


public class CalorieCounter {

    private SharedPreferences prefs;
    private int age;
    private int weight;
    private String sex;
    private String activityLevel;

    public CalorieCounter(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
        age = Integer.parseInt(prefs.getString("age", "25"));
        weight = Integer.parseInt(prefs.getString("weight", "75"));
        sex = prefs.getString("sex", "Male");
        activityLevel = prefs.getString("activity-level", "Light");
    }

    public SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("age")) {
                age = Integer.parseInt(prefs.getString("age", "25"));
            } else if (key.equals("weight")) {
                weight = Integer.parseInt(prefs.getString("weight", "75"));
            } else if (key.equals("sex")) {
                sex = prefs.getString("sex", "Male");
            } else if (key.equals("activity-level")) {
                activityLevel = prefs.getString("activity-level", "Light");
            }
        }
    };

    /**
     * Counts estimate for basic daily calorires based on user information
     * @return
     */
    public double countDailyCalories(){
        // these two values could probably be represented as ints rather than doubles
        double dailyCalories = 0;
        double metabolism = 0;

        // Formula for calculating calories for daily metabolism (the amount body will consume for metabolism etc):
        // var1 * weight * var2, where var1 and var2 are age dependent

        // Men:
        // 18/-30 years: 15,3 W + 679
        // 30-60 years: 11,6 W + 897
        // yli 60 years: 13,5 W + 487

        // Women
        // 18-30 years: 14,7 W + 496
        // 30-60 years: 8,7 W + 829
        // over 60 years: 10,5 W + 596

        double var1 = 0;
        int var2 = 0;
        if (sex == "Male") {
            if(age < 30) {
                var1 = 15.3;
                var2 = 679;
            } else if (age >= 30 && age < 60) {
                var1 = 11.6;
                var2 = 897;
            } else if (age >= 60) {
                var1 = 13.5;
                var2 = 487;
            }
        } else {
            if(age < 30) {
                var1 = 14.7;
                var2 = 496;
            } else if (age >= 30 && age < 60) {
                var1 = 8.7;
                var2 = 829;
            } else if (age >= 60) {
                var1 = 10.5;
                var2 = 596;
            }
        }

        metabolism = var1 * weight + var2;

        // BUT WHAIT - THERE'S MORE!
        // Next we'll take in the exercise calories
        // That is basic calories * activity level
        // User will choose light (F: 1.56, M: 1.55), moderate (F: 1.64, M: 1.78) or hardcore (F: 1.82, M: 2.10)

        double activity = 0;

        if(sex == "Male") {
            switch (activityLevel) {
                case "Light":
                    activity = 1.55;
                    break;
                case "Moderate":
                    activity = 1.78;
                    break;
                case "Hardcore":
                    activity = 2.10;
                    break;
            }
        } else if(sex == "Female") {
            switch (activityLevel) {
                case "Light":
                    activity = 1.56;
                    break;
                case "Moderate":
                    activity = 1.64;
                    break;
                case "Hardcore":
                    activity = 1.82;
                    break;
            }
        }

        dailyCalories = activity * metabolism;

        // This value could probably be saved somewhere rather than returned?
        return dailyCalories;
    }

    /**
     * Counts exercise calories based on heart rate and exercise duration
     * (Code changed: no longer takes in duration, we need this value every second the HR sensor updates)
     * @return
     */
    public double countExerciseCalories(int heartRate) {
        double calories = 0;

        // I'm not sure how scientific this is but who cares

        // Formula for men:
        // ((-55.0969 + (0.6309 x HR) + (0.1988 x W) + (0.2017 x A))/4.184) x 60 x T

        // Formula for female:
        // ((-20.4022 + (0.4472 x HR) - (0.1263 x W) + (0.074 x A))/4.184) x 60 x T

        // Formula changed below to represent the change to second.

        // WHERE
        // HR = Heart rate (in beats/minute)
        // W = Weight (in kilograms)
        // A = Age (in years)
        // T = Exercise duration time (in hours)

        if (sex == "Male") {
            calories = ((-55.0969 + (0.6309 * heartRate) + (0.1988 * weight) + (0.2017 * age))/4.184) / 60;
        } else {
            calories = ((-20.4022 + (0.4472 * heartRate) - (0.1263 * weight) + (0.074 * age))/4.184) / 60;
        }

        return calories;
    }

}
