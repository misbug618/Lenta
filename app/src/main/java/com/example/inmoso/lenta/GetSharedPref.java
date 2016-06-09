package com.example.inmoso.lenta;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by inmoso on 23.04.16.
 */
public class GetSharedPref {

    private static final String PREF_NAME = "MyFile";
    private static final String MY_DATA = "NEWS_DATA";

    private static GetSharedPref getSharedPref;
    private static SharedPreferences sharedPreferences;
    private static Context mContext;

    private GetSharedPref() {}

    public static GetSharedPref getInstance(Context context) {
        mContext = context;
        if (getSharedPref == null) {
            getSharedPref = new GetSharedPref();
        }

        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        return getSharedPref;
    }

    public void putNewsData(String json) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_DATA, json);
        editor.commit();
    }

    public String getNewsData() {
        return sharedPreferences.getString(MY_DATA, "");
    }

}
