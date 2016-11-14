package com.heyy.com.mutetask.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.heyy.com.mutetask.R;
import com.heyy.com.mutetask.common.ICallback;
import com.heyy.com.mutetask.common.UINavigation;
import com.heyy.com.mutetask.contract.MainContract;
import com.heyy.com.mutetask.dal.SPManager;
import com.heyy.com.mutetask.manager.TaskManager;
import com.heyy.com.mutetask.model.Task;

import java.util.Locale;

/**
 * Created by mo on 16-11-7.
 */

public class MainPresenter implements MainContract.Presenter {
    private TaskManager mTaskManager = TaskManager.getInstance();
    private Context mContext;
    private MainContract.View mView;

    public MainPresenter(Context context, MainContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void onNewTask() {
        UINavigation.gotoNewTaskForResult((Activity) mContext);
    }

    @Override
    public void onDeleteTask(final Task task) {
        new AlertDialog.Builder(mContext)
                .setMessage(String.format(Locale.getDefault(), mContext.getString(R.string.sure_delete), task.getTitle()))
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTaskManager.delTask(task);
                        mView.updateView();
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onEnableTask(Task task, int position) {
        mTaskManager.setTaskEnabled(task, true);
        mView.updateView(position);
    }

    @Override
    public void onDisableTask(Task task, int position) {
        mTaskManager.setTaskEnabled(task, false);
        mView.updateView(position);
    }

    @Override
    public void onEditTask(Task task) {
        UINavigation.gotoEditTaskForResult((Activity) mContext, task.getId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mView.updateView();
        }
    }

    @Override
    public void onCreate() {
        mTaskManager.loadTasks(new ICallback<Void>() {
            @Override
            public void onResult(Throwable error, Void data) {
                mView.showTasks(mTaskManager.getTasks());
            }
        });
        if (SPManager.getInstance().getFireUse()) {
            SPManager.getInstance().setFirstUse(false);
            new AlertDialog.Builder(mContext)
                    .setMessage(R.string.first_run_tip)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onDestroy() {

    }
}
