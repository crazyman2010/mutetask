package com.heyy.com.mutetask.contract;

import android.content.Intent;

import com.heyy.com.mutetask.model.Task;

import java.util.List;

/**
 * Created by mo on 16-11-7.
 */

public interface MainContract {
    interface View extends IView {
        void showTasks(List<Task> taskList);

        void updateView(int viewPosition);

        void updateView();
    }

    interface Presenter extends IPresenter {
        void onNewTask();

        void onDeleteTask(Task task);

        void onEnableTask(Task task,int position);

        void onDisableTask(Task task,int position);

        void onEditTask(Task task);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
