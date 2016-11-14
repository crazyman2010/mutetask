package com.heyy.com.mutetask.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.heyy.com.mutetask.R;

/**
 * Created by mo on 16-11-7.
 */

public class BaseActivity extends AppCompatActivity {
    private TextView mRightTextView;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContentView(int contentView) {
        super.setContentView(contentView);

        mRightTextView = (TextView) findViewById(R.id.tv_right);
        mRightTextView.setVisibility(View.GONE);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mTitleTextView.setText(R.string.app_name);

    }

    public void setRightTextView(boolean visible, String text, View.OnClickListener onClickListener) {
        mRightTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mRightTextView.setText(text);
        mRightTextView.setOnClickListener(onClickListener);
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showAlertDialog(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public void finishSelf() {
        finish();
    }
}
