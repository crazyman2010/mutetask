package com.heyy.com.mutetask.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.heyy.com.mutetask.common.Utils;
import com.heyy.com.mutetask.model.Task;
import com.heyy.com.mutetask.model.TaskFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mo on 16-11-7.
 */

public class TaskTable {
    public static final String TABLE_NAME = "task_table";
    public static final String C_ID = "_id";
    public static final String C_TITLE = "title";
    public static final String C_DAILY_TIME = "daily_time";
    public static final String C_WEEK_TIME = "week_time";
    public static final String C_TYPE = "type";
    public static final String C_VALID = "valid";
    public static final String C_EXTRA = "extra";

    public static String createSqlString() {
        return "CREATE TABLE " + TABLE_NAME + "("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + C_TITLE + " TEXT,"
                + C_DAILY_TIME + " INT NOT NULL,"
                + C_WEEK_TIME + " INT NOT NULL,"
                + C_TYPE + " INT NOT NULL,"
                + C_VALID + " INT NOT NULL,"
                + C_EXTRA + " TEXT);";
    }

    //将数据增加到数据库，并返回ID
    public static int add(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_TITLE, task.getTitle());
        contentValues.put(C_TYPE, task.getTypeId());
        contentValues.put(C_VALID, task.isEnabled() ? 1 : 0);
        contentValues.put(C_DAILY_TIME, task.getDailyTime());
        contentValues.put(C_WEEK_TIME, Utils.sparseIntArrayToString(task.getWeekTime(), Calendar.SUNDAY, Calendar.SATURDAY + 1));
        contentValues.put(C_EXTRA, task.getExtra());
        return (int) DBManager.getInstance().getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    public static void delete(Task task) {
        DBManager.getInstance().getWritableDatabase().delete(TABLE_NAME, C_ID + "=?", new String[]{String.valueOf(task.getId())});
    }

    public static void update(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_TITLE, task.getTitle());
        contentValues.put(C_TYPE, task.getTypeId());
        contentValues.put(C_VALID, task.isEnabled() ? 1 : 0);
        contentValues.put(C_DAILY_TIME, task.getDailyTime());
        StringBuilder weekTime = new StringBuilder();
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            if (task.getWeekTime().get(i) != 0) {
                weekTime.append(i).append(",");
            }
        }
        String weekTimeString = weekTime.toString();
        if (!TextUtils.isEmpty(weekTimeString)) {
            weekTimeString = weekTimeString.substring(0, weekTimeString.length() - 1);
        }
        contentValues.put(C_WEEK_TIME, weekTimeString);
        contentValues.put(C_EXTRA, task.getExtra());
        DBManager.getInstance().getWritableDatabase().update(TABLE_NAME, contentValues,
                C_ID + "=?", new String[]{String.valueOf(task.getId())});
    }

    public static List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = DBManager.getInstance().getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int type = cursor.getInt(cursor.getColumnIndex(C_TYPE));
            Task task = TaskFactory.createTask(type);
            assert task != null;
            task.setId(cursor.getInt(cursor.getColumnIndex(C_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(C_TITLE)));
            task.setDailyTime(cursor.getInt(cursor.getColumnIndex(C_DAILY_TIME)));
            task.setEnabled(cursor.getInt(cursor.getColumnIndex(C_VALID)) == 1);
            task.setExtra(cursor.getString(cursor.getColumnIndex(C_EXTRA)));
            String weekTimeString = cursor.getString(cursor.getColumnIndex(C_WEEK_TIME));
            if (!TextUtils.isEmpty(weekTimeString)) {
                String[] weeks = weekTimeString.split(",");
                for (String weekDay : weeks) {
                    task.addWeekTime(Integer.parseInt(weekDay));
                }
            }
            taskList.add(task);
        }
        cursor.close();
        return taskList;
    }
}
