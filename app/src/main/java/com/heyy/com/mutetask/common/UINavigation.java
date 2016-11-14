package com.heyy.com.mutetask.common;

import android.app.Activity;
import android.content.Intent;

import com.heyy.com.mutetask.activity.EditTaskActivity;

/**
 * Created by mo on 16-11-7.
 */

public class UINavigation {
    public static final String EXTRA_INT_TASK_ID = "extra_int_task_id";

    public static final int REQUEST_CODE_EDIT_TASK = 1;

    public static void gotoNewTaskForResult(Activity activity) {
        Intent intent = new Intent(activity, EditTaskActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
    }

    public static void gotoEditTaskForResult(Activity activity, int taskId) {
        Intent intent = new Intent(activity, EditTaskActivity.class);
        intent.putExtra(EXTRA_INT_TASK_ID, taskId);
        activity.startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
    }
}
