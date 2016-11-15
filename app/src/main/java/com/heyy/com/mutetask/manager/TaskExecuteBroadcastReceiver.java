package com.heyy.com.mutetask.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.heyy.com.mutetask.common.DateUtils;
import com.heyy.com.mutetask.common.XLogger;
import com.heyy.com.mutetask.model.Task;

/**
 * Created by mo on 16-11-13.
 */

public class TaskExecuteBroadcastReceiver extends BroadcastReceiver {
    public static final String EXTRA_PARCELABLE_TASK = "extra_parcelable_task";

    @Override
    public void onReceive(Context context, Intent intent) {
        XLogger.i("execute task:");
        Bundle b = intent.getBundleExtra("bundle");
        Task task = b.getParcelable(EXTRA_PARCELABLE_TASK);
        XLogger.i("run task " + task.getTitle() + "-" + task.getId() + " - " + DateUtils.formatDailyTime(task.getDailyTime()));
        task.execute();
        TaskManager.getInstance().postTask(task);
    }
}
