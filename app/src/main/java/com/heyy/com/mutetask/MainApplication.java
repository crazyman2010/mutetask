package com.heyy.com.mutetask;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.heyy.com.mutetask.common.XLogger;
import com.heyy.com.mutetask.dal.DBManager;
import com.heyy.com.mutetask.dal.SPManager;
import com.heyy.com.mutetask.service.KeepAliveService;

/**
 * Created by mo on 16-11-7.
 */

public class MainApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        XLogger.init("mutetask");
        DBManager.init(this);
        SPManager.getInstance().init(this);
        mContext = this;

        startService(new Intent(mContext, KeepAliveService.class));
    }

    public static Context getOurApplicationContext() {
        return mContext;
    }
}
