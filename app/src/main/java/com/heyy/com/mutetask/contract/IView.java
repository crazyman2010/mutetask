package com.heyy.com.mutetask.contract;

/**
 * Created by mo on 16-11-7.
 */

public interface IView {

    void showToast(String msg);

    void showAlertDialog(String msg);

    void finishSelf();
}
