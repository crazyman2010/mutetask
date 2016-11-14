package com.heyy.com.mutetask.model;

/**
 * Created by mo on 16-11-7.
 */

public class TaskFactory {
    public static Task createTask(int type) {
        switch (type) {
            case TaskType.TASK_TYPE_ENABLE_MUTE:
                return new MuteEnableTask();
            case TaskType.TASK_TYPE_DISABLE_MUTE:
                return new MuteDisableTask();
        }
        return null;
    }
}
