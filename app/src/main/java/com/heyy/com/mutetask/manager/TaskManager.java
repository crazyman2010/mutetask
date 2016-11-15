package com.heyy.com.mutetask.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.heyy.com.mutetask.MainApplication;
import com.heyy.com.mutetask.common.DateUtils;
import com.heyy.com.mutetask.common.ICallback;
import com.heyy.com.mutetask.common.XLogger;
import com.heyy.com.mutetask.dal.TaskTable;
import com.heyy.com.mutetask.model.Task;
import com.heyy.com.mutetask.model.TaskType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mo on 16-11-6.
 */

public class TaskManager {
    private static TaskManager instance = new TaskManager();
    private List<TaskType> mTaskTypes;
    private List<Task> mTasks;
    private Map<Task, PendingIntent> mPendingIntents;

    private TaskManager() {
        mTasks = new ArrayList<>();
        mTaskTypes = new ArrayList<>();
        mPendingIntents = new HashMap<>();

        mTaskTypes.add(new TaskType(TaskType.TASK_TYPE_ENABLE_MUTE, TaskType.TASK_TYPE_ENABLE_MUTE_NAME));
        mTaskTypes.add(new TaskType(TaskType.TASK_TYPE_DISABLE_MUTE, TaskType.TASK_TYPE_DISABLE_MUTE_NAME));
    }

    public static TaskManager getInstance() {
        return instance;
    }

    public void loadTasks(ICallback<Void> callback) {
        mTasks.clear();
        mPendingIntents.clear();
        mTasks.addAll(TaskTable.getTasks());
        for (Task task : mTasks) {
            postTask(task);
        }
        callback.onResult(null, null);
    }

    public List<Task> getTasks() {
        return mTasks;
    }

    public void addTask(Task task) {
        task.setId(TaskTable.add(task));
        mTasks.add(task);
        postTask(task);
    }

    public void delTask(Task task) {
        cancelTask(task);
        mTasks.remove(task);
        TaskTable.delete(task);
    }

    public Task getTask(int id) {
        for (Task task : mTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public void updateTask(Task task) {
        TaskTable.update(task);
        cancelTask(task);
        postTask(task);
    }

    public void setTaskEnabled(Task task, boolean enabled) {
        task.setEnabled(enabled);
        TaskTable.update(task);
        if (!enabled) {
            cancelTask(task);
        } else {
            postTask(task);
        }
    }

    public List<TaskType> getTaskTypes() {
        return mTaskTypes;
    }

    public void postTask(Task task) {
        Context context = MainApplication.getOurApplicationContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Bundle b = new Bundle();
        b.putParcelable(TaskExecuteBroadcastReceiver.EXTRA_PARCELABLE_TASK, task);
        Intent intent = new Intent(context, TaskExecuteBroadcastReceiver.class);
        intent.putExtra("bundle", b);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        int taskHour = task.getDailyTimeHour();
        int taskMinute = task.getDailyTimeMinute();
        int diffDays = 0;

        if (task.getWeekTime().get(currentDayOfWeek) != 0
                && (taskHour > currentHourOfDay || (taskHour == currentHourOfDay && taskMinute > currentMinute))) {
            //如果任务在当天可以运行
            diffDays = 0;
        } else {
            for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY * 2; i++) {
                int temp = task.getWeekTime().get(i);
                if (temp != 0 && temp > currentDayOfWeek) {
                    diffDays = temp - currentDayOfWeek;
                    break;
                }
            }
        }
        calendar.setTimeInMillis(calendar.getTimeInMillis() + diffDays * 24 * 3600 * 1000);
        calendar.set(Calendar.HOUR_OF_DAY, taskHour);
        calendar.set(Calendar.MINUTE, taskMinute);
        calendar.set(Calendar.SECOND, 0);
        XLogger.i("set task [ " + task.getTitle() + "-" + task.getId() + " ] run at :" + DateUtils.doLong2String(calendar.getTimeInMillis()));
        mPendingIntents.put(task, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void cancelTask(Task task) {
        PendingIntent pendingIntent = mPendingIntents.remove(task);
        if (pendingIntent != null) {
            XLogger.i("cancel task [ " + task.getTitle() + "-" + task.getId() + " ] ");
            Context context = MainApplication.getOurApplicationContext();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }
}
