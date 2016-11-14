package com.heyy.com.mutetask.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mo on 16-11-6.
 */

public class DBManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "db";
    private static DBManager instance;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    private DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public synchronized static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager(mContext, DB_NAME, null, 1);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TaskTable.createSqlString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
