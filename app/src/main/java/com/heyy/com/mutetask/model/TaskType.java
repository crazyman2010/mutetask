package com.heyy.com.mutetask.model;

/**
 * Created by mo on 16-11-7.
 */

public class TaskType {
    public static final int TASK_TYPE_ENABLE_MUTE = 1;
    public static final String TASK_TYPE_ENABLE_MUTE_NAME = "静音";

    public static final int TASK_TYPE_DISABLE_MUTE = 2;
    public static final String TASK_TYPE_DISABLE_MUTE_NAME = "打开声音";

    private int mId;
    private String mName;

    public TaskType(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
