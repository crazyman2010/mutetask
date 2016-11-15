package com.heyy.com.mutetask.model;

import android.os.Parcel;
import android.text.TextUtils;

/**
 * Created by mo on 16-11-6.
 */

public abstract class MuteTask extends Task {
    protected boolean mVibrate;

    MuteTask() {

    }

    MuteTask(Parcel parcel) {
        super(parcel);
        mVibrate = parcel.readInt() == 1;
    }

    public boolean getVibrate() {
        return mVibrate;
    }

    public void setVibrate(boolean vibrate) {
        mVibrate = vibrate;
    }

    @Override
    public void setExtra(String extra) {
        super.setExtra(extra);
        mVibrate = TextUtils.equals(extra, "1");
    }

    @Override
    public String getExtra() {
        return mVibrate ? "1" : "0";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mVibrate ? 1 : 0);
    }


}
