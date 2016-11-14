package com.heyy.com.mutetask.dal;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mo on 16-11-13.
 */

public class SPManager {
    private static SPManager instance = new SPManager();
    private SharedPreferences mSharedPreferences;
    private final String KEY_FIRST_USE = "key_first_use";

    private SPManager() {

    }

    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
    }

    public static synchronized SPManager getInstance() {
        return instance;
    }

    public void setFirstUse(boolean firstUse) {
        mSharedPreferences.edit().putBoolean(KEY_FIRST_USE, firstUse).apply();
    }

    public boolean getFireUse() {
        return mSharedPreferences.getBoolean(KEY_FIRST_USE, true);
    }
}
