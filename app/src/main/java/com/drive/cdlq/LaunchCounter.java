package com.drive.cdlq;

import android.content.Context;
import android.content.SharedPreferences;

public class LaunchCounter {

    private static final String PREFS_NAME = "launch_prefs";
    private static final String KEY_COUNT = "launch_count";

    public static int increaseLaunchCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentCount = prefs.getInt(KEY_COUNT, 0) + 1;
        prefs.edit().putInt(KEY_COUNT, currentCount).apply();
        return currentCount;
    }

    public static int getCurrentCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_COUNT, 0);
    }
}
