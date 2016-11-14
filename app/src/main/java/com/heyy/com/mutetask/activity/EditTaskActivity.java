package com.heyy.com.mutetask.activity;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.heyy.com.mutetask.R;
import com.heyy.com.mutetask.common.DateUtils;
import com.heyy.com.mutetask.common.UINavigation;
import com.heyy.com.mutetask.contract.EditTaskContract;
import com.heyy.com.mutetask.model.TaskType;
import com.heyy.com.mutetask.presenter.EditTaskPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditTaskActivity extends BaseActivity implements EditTaskContract.View {
    private Spinner mSpinner;
    private EditTaskContract.Presenter mPresenter;

    private TextView mDailyTimeTextView;

    private CheckBox[] mWeekTimeViews = new CheckBox[Calendar.SATURDAY + 1];
    private CheckBox mVibrateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mPresenter = new EditTaskPresenter(this, this);

        mSpinner = (Spinner) findViewById(R.id.spanner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onSetTaskType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mWeekTimeViews[Calendar.SUNDAY] = (CheckBox) findViewById(R.id.cb_1);
        mWeekTimeViews[Calendar.MONDAY] = (CheckBox) findViewById(R.id.cb_2);
        mWeekTimeViews[Calendar.TUESDAY] = (CheckBox) findViewById(R.id.cb_3);
        mWeekTimeViews[Calendar.WEDNESDAY] = (CheckBox) findViewById(R.id.cb_4);
        mWeekTimeViews[Calendar.THURSDAY] = (CheckBox) findViewById(R.id.cb_5);
        mWeekTimeViews[Calendar.FRIDAY] = (CheckBox) findViewById(R.id.cb_6);
        mWeekTimeViews[Calendar.SATURDAY] = (CheckBox) findViewById(R.id.cb_7);
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            mWeekTimeViews[i].setTag(i);
            mWeekTimeViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onSetWeekTime((Integer) v.getTag(), ((CheckBox) v).isChecked());
                }
            });
        }
        mVibrateView = (CheckBox) findViewById(R.id.cb_vibrate);

        mDailyTimeTextView = (TextView) findViewById(R.id.tv_daily_time);
        mPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public void onClickDailyTime(View view) {
        mPresenter.onClickDailyTime();
    }

    @Override
    public void showTitle(String title) {
        setTitle(title);
    }

    @Override
    public void setTaskTypeVisible(boolean visible) {
        mSpinner.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showTaskTypes(List<TaskType> taskTypes) {
        List<String> data = new ArrayList<>();
        for (TaskType type : taskTypes) {
            data.add(type.getName());
        }
        mSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, data));
        mSpinner.setSelection(0);
    }

    @Override
    public void showDailyTime(int dailyTime) {
        if (dailyTime != 0) {
            mDailyTimeTextView.setText(DateUtils.formatDailyTime(dailyTime));
        } else {
            mDailyTimeTextView.setText("点击选择时间");
        }
    }

    @Override
    public void showWeekTime(SparseIntArray weekDays) {
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            mWeekTimeViews[i].setChecked(weekDays.get(i) != 0);
        }
    }

    @Override
    public void showVibrate(boolean vibrate) {
        mVibrateView.setChecked(vibrate);
    }

    @Override
    public int getTaskId() {
        return getIntent().getIntExtra(UINavigation.EXTRA_INT_TASK_ID, -1);
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickSave(View view) {
        mPresenter.onSave();
    }

    @Override
    public void finishSelf(int resultCode) {
        setResult(resultCode);
        finish();
    }

    public void onClickVibrate(View view) {
        mPresenter.onSetVibrate(mVibrateView.isChecked());
    }
}
