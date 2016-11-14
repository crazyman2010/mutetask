package com.heyy.com.mutetask.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

import com.heyy.com.mutetask.common.Utils;

import java.util.Calendar;

/**
 * Created by mo on 16-11-6.
 */

public abstract class Task implements Cloneable, Parcelable {
    protected int mId;
    protected String mTitle;
    protected int mDailyTime;
    protected SparseIntArray mWeekTime;
    protected boolean mEnabled;
    protected int mType;
    protected String mExtra;

    public abstract void execute();

    public Task() {
        mEnabled = true;
        mWeekTime = new SparseIntArray();
        mWeekTime.put(Calendar.MONDAY, Calendar.MONDAY);
        mWeekTime.put(Calendar.TUESDAY, Calendar.TUESDAY);
        mWeekTime.put(Calendar.WEDNESDAY, Calendar.WEDNESDAY);
        mWeekTime.put(Calendar.THURSDAY, Calendar.THURSDAY);
        mWeekTime.put(Calendar.FRIDAY, Calendar.FRIDAY);
    }

    public Task copy() {
        try {
            return (Task) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Task(Parcel source) {
        mId = source.readInt();
        mTitle = source.readString();
        mDailyTime = source.readInt();
        mWeekTime = Utils.stringToSparseIntArray(source.readString());
        mEnabled = source.readInt() == 1;
        mType = source.readInt();
        mExtra = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeInt(mDailyTime);
        dest.writeString(Utils.sparseIntArrayToString(mWeekTime, Calendar.SUNDAY, Calendar.SATURDAY + 1));
        dest.writeInt(mEnabled ? 1 : 0);
        dest.writeInt(mType);
        dest.writeString(mExtra);
    }

    public void setDailyTime(int dailyTime) {
        mDailyTime = dailyTime;
    }

    public int getDailyTime() {
        return mDailyTime;
    }

    public int getDailyTimeHour() {
        return Utils.getHourFromDailyTime(mDailyTime);
    }

    public int getDailyTimeMinute() {
        return Utils.getMinuteFromDailyTime(mDailyTime);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }


    public SparseIntArray getWeekTime() {
        return mWeekTime;
    }

    public void setWeekTime(SparseIntArray weekTime) {
        mWeekTime = weekTime;
    }

    public void addWeekTime(int weekDay) {
        mWeekTime.put(weekDay, weekDay);
    }

    public void deleteWeekTime(int weekDay) {
        mWeekTime.delete(weekDay);
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean mEnabled) {
        this.mEnabled = mEnabled;
    }

    public int getTypeId() {
        return mType;
    }

    public String getExtra() {
        return mExtra;
    }

    public void setExtra(String extra) {
        mExtra = extra;
    }
}
