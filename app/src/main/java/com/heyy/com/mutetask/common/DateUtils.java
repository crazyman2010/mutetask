package com.heyy.com.mutetask.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by SD_ZR on 16-4-11.
 * 用来处理时间的工具类
 */
public class DateUtils {
    public static final String DF_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DF_JUST_DAY = "yyyy-MM-dd";
    public static final String DF_DEFAULT_ZH = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DF_JUST_DAY_WITHOUT_LINE = "yyMMdd";
    public static final String DF_JUST_TIME = "HH:mm:ss";
    public static final String DF_WITH_CN_ALL = "yyyy年MM月dd日 HH:mm:ss";

    private static final Object lockObj = new Object();

    public static String formatDailyTime(int dailyTime) {
        dailyTime = dailyTime / 1000 / 60;
        int h = dailyTime / 60;
        int m = dailyTime % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", h, m);
    }

    public static String[] WEEK_DAYS = new String[]{
            "NOT_SET", "周日", "周一", "周二", "周三", "周四", "周五", "周六"
    };

    public static String weekToString(int weekDay) {
        return WEEK_DAYS[weekDay];
    }

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    public static synchronized SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            tl = sdfMap.get(pattern);
            if (tl == null) {
                tl = new ThreadLocal<SimpleDateFormat>() {
                    @Override
                    protected SimpleDateFormat initialValue() {
                        return new SimpleDateFormat(pattern, Locale.getDefault());
                    }
                };
                sdfMap.put(pattern, tl);
            }
        }
        return tl.get();
    }

    /**
     * 和当前时间比较，是否为同一天
     *
     * @param date
     * @return
     */
    public static boolean isSameDayWithNow(Date date) {
        if (date != null) {
            Date now = new Date();
            return getSdf(DF_JUST_DAY).format(now).equals(getSdf(DF_JUST_DAY).format(date));
        }
        return false;
    }

    /*************
     * 字符串日期到Date,失败返回 null
     ************************/
    public static Date doString2Date(String dateString, String df) {
        try {
            return getSdf(df).parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date doString2Date(String dateString) {
        return doString2Date(dateString, DF_DEFAULT);
    }

    /**************
     * 字符串日期到long,失败返回0
     **********************************/
    public static long doString2Long(String dateString, String df) {
        try {
            Date date = getSdf(df).parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException ignore) {

        }
        return 0;
    }

    public static long doString2Long(String dateString) {
        return doString2Long(dateString, DF_DEFAULT);
    }

    /********************************************
     * 日期到字符串, 日期到Long直接使用Date.getTime()
     ********************************************/

    public static String doDate2String(Date date, String df) {
        return getSdf(df).format(date);
    }

    public static String doDate2String(Date date) {
        return doDate2String(date, DF_DEFAULT);
    }

    /***********************************************
     * Long 到字符串, Long到Date直接使用new Date(Long)
     *************************************************/
    public static String doLong2String(long date, String df) {
        return doDate2String(new Date(date), df);
    }

    public static String doLong2String(long date) {
        return doDate2String(new Date(date));
    }


    /**********************************************
     * 字符串，一种格式到另外一种,失败返回null
     *****************************************/
    public static String doString2String(String dateString, String oldFormat, String newFormat) {
        Date date = doString2Date(dateString, oldFormat);
        if (date != null) {
            return doDate2String(date, newFormat);
        }
        return null;
    }

    public static String doString2StringZH(String date) {
        return doString2String(date, DF_DEFAULT, "yyyy年MM月dd日 HH:mm:ss");
    }
}
