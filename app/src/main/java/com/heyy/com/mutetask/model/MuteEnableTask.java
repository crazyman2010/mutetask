package com.heyy.com.mutetask.model;

import android.content.Context;
import android.media.AudioManager;
import android.os.Parcel;

import com.heyy.com.mutetask.MainApplication;
import com.heyy.com.mutetask.common.XLogger;

/**
 * Created by mo on 16-11-6.
 */

public class MuteEnableTask extends MuteTask {
    public MuteEnableTask() {
        mType = TaskType.TASK_TYPE_ENABLE_MUTE;
        mTitle = "静音";
    }

    public MuteEnableTask(Parcel source) {
        super(source);
    }

    public static Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new MuteEnableTask(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    @Override
    public void execute() {
        XLogger.i("set mute!");
        Context context = MainApplication.getOurApplicationContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(mVibrate ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT);
        processStream(audioManager,AudioManager.STREAM_NOTIFICATION,0);
        processStream(audioManager,AudioManager.STREAM_MUSIC,AudioManager.FLAG_SHOW_UI);
    }

    private void processStream(AudioManager audioManager,int streamType,int flags){
        audioManager.setStreamVolume(streamType,0,flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
