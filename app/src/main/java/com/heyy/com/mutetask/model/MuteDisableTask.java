package com.heyy.com.mutetask.model;

import android.content.Context;
import android.media.AudioManager;
import android.os.Parcel;

import com.heyy.com.mutetask.MainApplication;
import com.heyy.com.mutetask.common.XLogger;

/**
 * Created by mo on 16-11-6.
 */

public class MuteDisableTask extends MuteTask {
    @Override
    public void execute() {
        XLogger.i("set not mute!");
        Context context = MainApplication.getOurApplicationContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        processStream(audioManager, AudioManager.STREAM_NOTIFICATION, 0);
        processStream(audioManager, AudioManager.STREAM_MUSIC, AudioManager.FLAG_SHOW_UI);
    }

    private void processStream(AudioManager audioManager, int streamType, int flags) {
        int volumeIndex = audioManager.getStreamMaxVolume(streamType) * 8 / 10;
        XLogger.i("set Volume " + streamType + " => " + volumeIndex);
        audioManager.setStreamVolume(streamType, volumeIndex, flags);
    }

    public MuteDisableTask() {
        mType = TaskType.TASK_TYPE_DISABLE_MUTE;
        mTitle = "打开声音";
    }

    public MuteDisableTask(Parcel source) {
        super(source);
    }

    public static Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new MuteDisableTask(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
