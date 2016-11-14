package com.heyy.com.mutetask.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.SparseIntArray;
import android.widget.TimePicker;

import com.heyy.com.mutetask.R;
import com.heyy.com.mutetask.common.Utils;
import com.heyy.com.mutetask.contract.EditTaskContract;
import com.heyy.com.mutetask.manager.TaskManager;
import com.heyy.com.mutetask.model.MuteTask;
import com.heyy.com.mutetask.model.Task;
import com.heyy.com.mutetask.model.TaskFactory;

import java.util.Calendar;

/**
 * Created by mo on 16-11-7.
 */

public class EditTaskPresenter implements EditTaskContract.Presenter {
    private TaskManager mTaskManager = TaskManager.getInstance();
    private Context mContext;
    private EditTaskContract.View mView;
    private int mTaskTypeId;
    private int mDailyTime;
    private SparseIntArray mWeekTime;
    private Task mTask;
    private boolean mVibrate;

    public EditTaskPresenter(Context context, EditTaskContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void onCreate() {
        mTask = mTaskManager.getTask(mView.getTaskId());
        if (mTask == null) {
            mView.showTitle(mContext.getString(R.string.new_task));
            mView.setTaskTypeVisible(true);
            mView.showTaskTypes(mTaskManager.getTaskTypes());

            mTaskTypeId = mTaskManager.getTaskTypes().get(0).getId();
            mDailyTime = -1;
            mWeekTime = new SparseIntArray();
            mWeekTime.put(Calendar.MONDAY, Calendar.MONDAY);
            mWeekTime.put(Calendar.TUESDAY, Calendar.TUESDAY);
            mWeekTime.put(Calendar.WEDNESDAY, Calendar.WEDNESDAY);
            mWeekTime.put(Calendar.THURSDAY, Calendar.THURSDAY);
            mWeekTime.put(Calendar.FRIDAY, Calendar.FRIDAY);

            mVibrate = true;
        } else {
            mView.showTitle(mContext.getString(R.string.edit_task));
            mView.setTaskTypeVisible(false);

            mTaskTypeId = -1;
            mDailyTime = mTask.getDailyTime();
            mWeekTime = mTask.getWeekTime().clone();

            mVibrate = ((MuteTask) mTask).getVibrate();
        }
        if (mDailyTime > 0) {
            mView.showDailyTime(mDailyTime);
        }
        mView.showWeekTime(mWeekTime);
        mView.showVibrate(mVibrate);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onClickDailyTime() {
        TimePicker timePicker = new TimePicker(mContext);
        timePicker.setIs24HourView(true);
        if(mDailyTime>0) {
            if(Build.VERSION.SDK_INT<23) {
                timePicker.setCurrentHour(Utils.getHourFromDailyTime(mDailyTime));
                timePicker.setCurrentMinute(Utils.getMinuteFromDailyTime(mDailyTime));
            }else{
                timePicker.setHour(Utils.getHourFromDailyTime(mDailyTime));
                timePicker.setMinute(Utils.getMinuteFromDailyTime(mDailyTime));
            }
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDailyTime = hourOfDay * 3600 * 1000 + minute * 60 * 1000;
                mView.showDailyTime(mDailyTime);
            }
        });
        new AlertDialog.Builder(mContext)
                .setView(timePicker)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onSetTaskType(int position) {
        mTaskTypeId = mTaskManager.getTaskTypes().get(position).getId();
    }

    @Override
    public void onSetWeekTime(int weekDay, boolean select) {
        if (select) {
            mWeekTime.put(weekDay, weekDay);
        } else {
            mWeekTime.delete(weekDay);
        }
    }

    @Override
    public void onSave() {
        if (mDailyTime == -1) {
            mView.showToast(mContext.getString(R.string.not_set_daily_time));
            return;
        }
        if (mWeekTime.size() == 0) {
            mView.showToast(mContext.getString(R.string.not_set_week_time));
            return;
        }
        if (mTask == null) {
            mTask = TaskFactory.createTask(mTaskTypeId);
        }
        assert mTask != null;
        mTask.setWeekTime(mWeekTime);
        mTask.setDailyTime(mDailyTime);
        ((MuteTask) mTask).setVibrate(mVibrate);

        if (mTask.getId() > 0) {
            mTaskManager.updateTask(mTask);
        } else {
            mTaskManager.addTask(mTask);
        }

        mView.finishSelf(Activity.RESULT_OK);
    }

    @Override
    public void onSetVibrate(boolean select) {
        mVibrate = select;
    }
}
