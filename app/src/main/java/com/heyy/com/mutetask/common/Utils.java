package com.heyy.com.mutetask.common;

import android.text.TextUtils;
import android.util.SparseIntArray;

/**
 * Created by mo on 16-11-13.
 */

public class Utils {
    public static String sparseIntArrayToString(SparseIntArray array, int start, int end) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (array.get(i) != 0) {
                result.append(i).append(",");
            }
        }
        String resultString = result.toString();
        if (!TextUtils.isEmpty(resultString)) {
            resultString = resultString.substring(0, resultString.length() - 1);
        }
        return resultString;
    }

    public static SparseIntArray stringToSparseIntArray(String source) {
        SparseIntArray array = new SparseIntArray();
        String[] strings = source.split(",");
        for (String s : strings) {
            int value = Integer.parseInt(s);
            array.put(value, value);
        }
        return array;
    }

    public static int getHourFromDailyTime(int dailyTime) {
        return dailyTime / 1000 / 3600;
    }

    public static int getMinuteFromDailyTime(int dailyTime) {
        return ((dailyTime / 1000) % 3600) / 60;
    }
}
