package com.heyy.com.mutetask.contract;

import android.util.SparseIntArray;

import com.heyy.com.mutetask.model.TaskType;

import java.util.List;

/**
 * Created by mo on 16-11-7.
 */

public interface EditTaskContract {
    interface View extends IView {
        void showTitle(String title);

        void setTaskTypeVisible(boolean visible);

        void showTaskTypes(List<TaskType> taskTypes);

        void showDailyTime(int dailyTime);

        void showWeekTime(SparseIntArray weekDays);

        void showVibrate(boolean vibrate);

        int getTaskId();

        void finishSelf(int resultCode);
    }

    interface Presenter extends IPresenter {
        void onClickDailyTime();

        void onSetTaskType(int position);

        void onSetWeekTime(int weekDay, boolean select);

        void onSave();

        void onSetVibrate(boolean select);
    }
}
