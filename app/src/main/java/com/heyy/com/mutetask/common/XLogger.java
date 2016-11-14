package com.heyy.com.mutetask.common;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 功能：
 * 1.打开或关闭日志，可以按级别操作
 * 2.输出类名，函数名，行号，时间
 * 3.可以输出到logcat
 * 4.可以输出到文件
 * 5.可以设置全局TAG
 */
public class XLogger {
    private static String TAG = "XLogger";
    private static boolean mInit = false;
    public static final int LEVEL_VERBOSE = Log.VERBOSE;
    public static final int LEVEL_DEBUG = Log.DEBUG;
    public static final int LEVEL_INFO = Log.INFO;
    public static final int LEVEL_WARN = Log.WARN;
    public static final int LEVEL_ERROR = Log.ERROR;
    public static final int LEVEL_ASSERT = Log.ASSERT;
    public static final int LEVEL_CLOSE = Log.ASSERT + 1;

    public static final String LOG_FILE_ENDFIX = ".log";


    private static int WRITE_LEVEL_FILE = Log.INFO;//默认写文件级别
    private static int WRITE_LEVEL_LOGCAT = Log.VERBOSE;//默认写logcat级别

    private static String mLogFileDirPath;
    private static File mLogFile;
    private static FileWriter mFileWriter = null;
    private static boolean mRun;
    private static final Object mFileLock = new Object();
    private static List<String> mFileLogs = new ArrayList<>();
    private static List<String> mFileWriteLogs = new ArrayList<>();
    private static boolean mOpenLogcat = true;


    private static String mGloableTag = "";

    public synchronized static void init(String gloableTag) {
        if (mInit) {
            return;
        }
        mInit = true;
        mGloableTag = gloableTag;
    }

    /**
     * 初始化
     *
     * @param keepDays       保持天数
     * @param logFileDirPath 日志保存目录
     */
    public synchronized static void init(int keepDays, String logFileDirPath) {
        //只允许初始化一次
        if (mInit) {
            return;
        }
        mInit = true;
        mRun = true;
        mLogFileDirPath = logFileDirPath;

        if (mLogFileDirPath != null) {
            File logDir = new File(mLogFileDirPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            if (keepDays > 0) {
                cleanLogs(mLogFileDirPath, keepDays);
            }
            //创建日志输出文件
            mLogFile = new File(mLogFileDirPath + File.separator +
                    DateUtils.doLong2String(System.currentTimeMillis(), DateUtils.DF_JUST_DAY)
                    + LOG_FILE_ENDFIX);
            if (!mLogFile.exists()) {
                try {
                    mLogFile.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, "create file error:", e);
                }
            }
            if (!mLogFile.exists() || !mLogFile.canWrite()) {
                Log.e(TAG, "can not create or write the log file!");
            } else {
                try {
                    mFileWriter = new FileWriter(mLogFile, true);
                } catch (IOException ignore) {

                }
            }

            if (mFileWriter != null) {
                new FileWriteThread().start();
            }
        }
    }

    public synchronized static void deInit() {
        mRun = false;
        synchronized (mFileLock) {
            mFileLock.notifyAll();
        }
    }

    public static void setLogcat(boolean open) {
        mOpenLogcat = open;
    }

    /**
     * 设置输出到文件的级别
     *
     * @param level 日志级别
     */
    public static void setWriteFileLevel(int level) {
        WRITE_LEVEL_FILE = level;
    }

    /**
     * 设置输出到logcat的级别
     *
     * @param level 日志级别
     */
    public static void setWriteLogcatLevel(int level) {
        WRITE_LEVEL_LOGCAT = level;
    }

    public static void setGloableTag(String tag) {
        mGloableTag = tag;
    }

    /**
     * 将所有日志文件copy到某一个文件中
     *
     * @param toFile 目标文件
     */
    public static void copyLogsToFile(File toFile) {
        File logFileDir = new File(mLogFileDirPath);
        File[] logFiles = logFileDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(LOG_FILE_ENDFIX);
            }
        });
        if (logFiles.length > 0) {
            List<File> logFileList = Arrays.asList(logFiles);
            Collections.sort(logFileList, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return (int) (lhs.lastModified() - rhs.lastModified());
                }
            });
            try {
                int readLen;
                byte[] buffer = new byte[102400];
                FileOutputStream fileOutputStream = new FileOutputStream(toFile);
                for (File logFile : logFileList) {
                    FileInputStream fileInputStreams = new FileInputStream(logFile);
                    while ((readLen = fileInputStreams.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, readLen);
                        fileOutputStream.flush();
                    }
                    fileInputStreams.close();
                }
                fileOutputStream.close();
            } catch (Exception ignore) {

            }
        }
    }

    public static void v(String msg) {
        writeToLogSystem(LEVEL_VERBOSE, "", msg, null);
    }

    public static void v(String tag, String msg) {
        writeToLogSystem(LEVEL_VERBOSE, tag, msg, null);
    }


    public static void v(String tag, String msg, Throwable tr) {
        writeToLogSystem(LEVEL_VERBOSE, tag, msg, null);
    }


    public static void d(String msg) {
        writeToLogSystem(LEVEL_DEBUG, "", msg, null);
    }

    public static void d(String tag, String msg) {
        writeToLogSystem(LEVEL_DEBUG, tag, msg, null);
    }


    public static void d(String tag, String msg, Throwable tr) {
        writeToLogSystem(LEVEL_DEBUG, tag, msg, tr);
    }


    public static void i(String msg) {
        writeToLogSystem(LEVEL_INFO, "", msg, null);
    }

    public static void i(String tag, String msg) {
        writeToLogSystem(LEVEL_INFO, tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        writeToLogSystem(LEVEL_INFO, tag, msg, tr);
    }


    public static void w(String msg) {
        writeToLogSystem(LEVEL_WARN, "", msg, null);
    }

    public static void w(String tag, String msg) {
        writeToLogSystem(LEVEL_WARN, tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        writeToLogSystem(LEVEL_WARN, tag, msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        writeToLogSystem(LEVEL_WARN, tag, "", tr);
    }


    public static void e(String msg) {
        writeToLogSystem(LEVEL_ERROR, "", msg, null);
    }

    public static void e(String tag, String msg) {
        writeToLogSystem(LEVEL_ERROR, tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        writeToLogSystem(LEVEL_ERROR, tag, msg, tr);
    }

    private static void writeToLogSystem(int level, String tag, String msg, Throwable tr) {
        if (level >= WRITE_LEVEL_FILE) {
            writeToFile(level, tag, msg, tr);
        }
        if (mOpenLogcat && level >= WRITE_LEVEL_LOGCAT) {
            writeToLogcat(level, tag, msg, tr);
        }
    }

    private static void writeToLogcat(int level, String tag, String msg, Throwable tr) {
        if (tr != null) {
            msg += '\n' + Log.getStackTraceString(tr);
        }
        Log.println(level, mGloableTag + tag, msg == null ? "unknown" : msg);
    }

    private static void writeToFile(int level, String tag, String msg, Throwable tr) {
        if (mFileWriter != null) {
            msg = DateUtils.doLong2String(System.currentTimeMillis()) + "[" + tag + "]:" + msg;
            if (tr != null) {
                msg += '\n' + Log.getStackTraceString(tr);
            }
            msg += "\n";
            synchronized (mFileLock) {
                mFileLogs.add(msg);
                mFileLock.notifyAll();
            }
        }
    }

    //写文件，直到应用退出且文件日志列表为空
    private static class FileWriteThread extends Thread {
        @Override
        public void run() {
            Log.i(TAG, "file log thread start!");
            while (true) {
                synchronized (mFileLock) {
                    //等待有日志可以写
                    while (mRun && mFileLogs.size() == 0) {
                        try {
                            mFileLock.wait();
                        } catch (InterruptedException ignore) {
                            //忽略中断信号
                        }
                    }
                    if (!mRun && mFileLogs.size() == 0) {
                        Log.i(TAG, "file log thread exit");
                        return; //应用已退出，而且所有日志都已写入文件，那么退出线程
                    }
                    mFileWriteLogs.addAll(mFileLogs);
                    mFileLogs.clear();
                }
                //写入日志到文件
                try {
                    for (String log : mFileWriteLogs) {
                        mFileWriter.write(log);
                    }
                    mFileWriter.flush();
                } catch (IOException e) {
                    Log.e(TAG, "write log to file failed:" + e.getMessage());
                }
                mFileWriteLogs.clear();
            }
        }
    }

    //清理日志
    private static void cleanLogs(String logDirPath, int keepDays) {
        File logDir = new File(logDirPath);
        if (logDir.exists()) {
            List<File> logFileList = Arrays.asList(logDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(LOG_FILE_ENDFIX);
                }
            }));
            if (logFileList.size() > keepDays - 1) {
                Collections.sort(logFileList, new Comparator<File>() {
                    @Override
                    public int compare(File lhs, File rhs) {
                        return (int) (lhs.lastModified() - rhs.lastModified());
                    }
                });
                long removeCount = logFileList.size() - keepDays;

                List<File> removeList = new ArrayList<>();
                for (int i = 0; i < removeCount; i++) {
                    removeList.add(logFileList.get(i));
                }
                Iterator<File> iterator = removeList.iterator();
                while (iterator.hasNext()) {
                    File file = iterator.next();
                    Log.d(TAG, "remove log file :" + file.getName());
                    file.delete();
                    iterator.remove();
                }
            }
        }
    }
}
