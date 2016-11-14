package com.heyy.com.mutetask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.heyy.com.mutetask.R;
import com.heyy.com.mutetask.common.DateUtils;
import com.heyy.com.mutetask.contract.MainContract;
import com.heyy.com.mutetask.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mo on 16-11-8.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private List<Task> mTasks;
    private MainContract.Presenter mPresenter;

    public TaskListAdapter(MainContract.Presenter presenter) {
        mPresenter = presenter;
        mTasks = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mTasks.get(position),position);
    }

    public void setData(List<Task> tasks) {
        mTasks = tasks;
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDailyTimeTextView;
        private TextView mWeekTimeTextView;
        private TextView mTitleTextView;
        private CheckBox mEnableCheckBox;
        private Task mTask;
        private int mPosition;

        ViewHolder(View itemView) {
            super(itemView);

            mDailyTimeTextView = (TextView) itemView.findViewById(R.id.tv_daily_time);
            mWeekTimeTextView = (TextView) itemView.findViewById(R.id.tv_week_time);
            mTitleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            mEnableCheckBox = (CheckBox) itemView.findViewById(R.id.cb_enable);

            itemView.findViewById(R.id.btn_delete).setOnClickListener(onClickDelete);
            itemView.findViewById(R.id.btn_edit).setOnClickListener(onClickEdit);
            mEnableCheckBox.setOnClickListener(onClickEnable);
        }

        void bind(Task task, int position) {
            mPosition = position;
            mTask = task;
            mTitleTextView.setText(task.getTitle());
            mDailyTimeTextView.setText(DateUtils.formatDailyTime(task.getDailyTime()));
            StringBuilder weekTimeBuilder = new StringBuilder();
            for (int i = Calendar.MONDAY; i <= Calendar.SATURDAY; i++) {
                if (task.getWeekTime().get(i) != 0) {
                    weekTimeBuilder.append(DateUtils.weekToString(i)).append("  ");
                }
            }
            if (task.getWeekTime().get(Calendar.SUNDAY) != 0) {
                weekTimeBuilder.append(DateUtils.weekToString(Calendar.SUNDAY)).append("  ");
            }
            mWeekTimeTextView.setText(weekTimeBuilder.toString());
            mEnableCheckBox.setChecked(task.isEnabled());
        }

        private View.OnClickListener onClickEdit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onEditTask(mTask);
            }
        };

        private View.OnClickListener onClickDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onDeleteTask(mTask);
            }
        };

        private View.OnClickListener onClickEnable = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                if (c.isChecked()) {
                    mPresenter.onEnableTask(mTask, mPosition);
                } else {
                    mPresenter.onDisableTask(mTask, mPosition);
                }
            }
        };
    }
}
