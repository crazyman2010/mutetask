package com.heyy.com.mutetask.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.heyy.com.mutetask.R;
import com.heyy.com.mutetask.adapter.TaskListAdapter;
import com.heyy.com.mutetask.contract.MainContract;
import com.heyy.com.mutetask.model.Task;
import com.heyy.com.mutetask.presenter.MainPresenter;

import java.util.List;

public class MainActivity extends BaseActivity implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private TaskListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this, this);

        setRightTextView(true, "新增", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onNewTask();
            }
        });
        mAdapter = new TaskListAdapter(mPresenter);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 64);
            }
        });
        recyclerView.setAdapter(mAdapter);

        mPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void showTasks(List<Task> taskList) {
        mAdapter.setData(taskList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateView(int viewPosition) {
        mAdapter.notifyItemChanged(viewPosition);
    }

    @Override
    public void updateView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
