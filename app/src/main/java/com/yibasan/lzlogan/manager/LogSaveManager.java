package com.yibasan.lzlogan.manager;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.yibasan.lzlogan.Logz;
import com.yibasan.lzlogan.common.LogzConstant;
import com.yibasan.lzlogan.config.ILogzConfig;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSink;
import okio.Okio;

/**
 * Author : Create by Linxinyuan on 2018/08/07
 * Email : linxinyuan@lizhi.fm
 * Desc : android dev
 */
public final class LogSaveManager {
    private Context mContext;

    private File mLogFile;
    private File mAchieveFile;
    private String loginUserId;
    private long currentTarget;
    private boolean isPrintPhoneMsg;
    private ExecutorService executor;

    private static final String FILE_ACHIEVE = "/Achieve";
    private static final String FILE_NAME_SUFFIX = "_log.txt";
    private static final String FILE_DEFAULT_UNLOGIN = "unLogin";

    public LogSaveManager(Context mContext) {
        this.mContext = mContext;
        // 单核线程池用于日志写文件
        executor = Executors.newSingleThreadExecutor();
    }

    public void saveMessageToSDCard(final ILogzConfig configer, final int type, final String tag, final String message) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Logz.e("sdcard unmounted, skip dump exception");
            return;
        }
        // sdcard/LizhiFm/Logz/
        StringBuilder sb = new StringBuilder().append(LogzConstant.DEFAULT_SAVE_ROOT_PATH);

        // sdcard/LizhiFm/Logz/ + 2018/08/07/
        String dateFlag = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        sb.append(dateFlag + File.separator);

        // sdcard/LizhiFm/Logz/ + 2018/08/07/ + 51891225/
        loginUserId = String.valueOf(getLoginedUserId() != 0L ? getLoginedUserId() : FILE_DEFAULT_UNLOGIN);
        sb.append(loginUserId + File.separator);

        // sdcard/LizhiFm/Logz/ + 2018/08/07/ + 51891225/ + 190000/
        long now = System.currentTimeMillis();
        currentTarget = now - now % LogzConstant.LOG_FILE_INTERVAL;
        sb.append(new SimpleDateFormat("HH-mm-ss").format(new Date(currentTarget)) + File.separator);

        File dir = new File(sb.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File achieve = new File(dir.getAbsolutePath() + FILE_ACHIEVE);
        if (!achieve.exists()) {
            achieve.mkdirs();
        }

        mLogFile = new File(dir.getAbsolutePath() + File.separator + "com.yibasan.lizhifm" + FILE_NAME_SUFFIX);
        try {
            if (!mLogFile.exists()) {
                mLogFile.createNewFile();
                isPrintPhoneMsg = true;
            } else {
                isPrintPhoneMsg = false;
            }

            //文件大小切片
            if (mLogFile.length() >= configer.getLogFileCutSize()){
                //文件重命名与移动
                long achieveTime = System.currentTimeMillis();
                mAchieveFile = new File(achieve.getAbsolutePath()
                        + File.separator + "com.yibasan.lizhifm_"
                        + String.valueOf(achieveTime) + FILE_NAME_SUFFIX);
                mLogFile.renameTo(mAchieveFile);
                //新开文件打印日志
                saveMessageToSDCard(configer, type, tag, message);
                return;
            }

            if (isPrintPhoneMsg)
                wirteUserPhoneMessage(mLogFile);

            Observable.just(1).
                    observeOn(Schedulers.from(executor))
                    .map(new Function<Integer, Boolean>() {
                        @Override
                        public Boolean apply(Integer integer) {
                            try {
                                wirteLogFileWithOkio(type, tag, message, mLogFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }).subscribe();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户手机软硬件信息打印方法
     *
     * @param file
     * @throws Exception
     */
    private void wirteUserPhoneMessage(File file) {
        if (null == mContext)
            return;
        try {
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            //application version name and version code
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            sink.writeUtf8("App Version:");
            sink.writeUtf8(packageInfo.versionName);
            sink.writeUtf8(String.valueOf('_'));
            sink.writeUtf8(String.valueOf(packageInfo.versionCode) + LogzConstant.BR);

            //phone android version
            sink.writeUtf8("OS Version:");
            sink.writeUtf8(Build.VERSION.RELEASE);
            sink.writeUtf8(String.valueOf('_'));
            sink.writeUtf8(String.valueOf(Build.VERSION.SDK_INT) + LogzConstant.BR);

            //phone builder
            sink.writeUtf8("Vendor:");
            sink.writeUtf8(Build.MANUFACTURER);

            //phone type
            sink.writeUtf8("Model:");
            sink.writeUtf8(Build.MODEL);

            //cpu type
            sink.writeUtf8("CPU ABI:");
            sink.writeUtf8(Build.CPU_ABI + "\n");

            sink.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单行日志写文件(Okio无压缩方法)
     *
     * @param tag
     * @param message
     * @param file
     * @throws Exception
     */
    private void wirteLogFileWithOkio(int type, String tag, String message, File file) throws Exception {
//        BufferedSink sink = Okio.buffer(Okio.appendingSink(file));//append
//        String timeSecond = new SimpleDateFormat("MM-dd/HH:mm:ss")
//                .format(new Date(System.currentTimeMillis()));
//        Buffer buffer = new Buffer().writeUtf8(timeSecond + "\t"
//                // + ApplicationContext.getCurProcessName() + "\t"
//                + "com.yibasan.lizhifm" + "\t"
//                + getLevelTag(type) + tag + ":"
//                + message + LogzConstant.BR);
//        sink.write(buffer, buffer.size());
        //close file outputStream resource
//        sink.close();
    }

//    /**
//     * 单行日志写文件(NIO无压缩方法)
//     *
//     * @param type
//     * @param tag
//     * @param message
//     * @param file
//     * @throws Exception
//     */
//    private void writeLogFileWithOkioChannel(int type, String tag, String message, File file) throws Exception {
//        // NIO OPTION
//        //private static final Set<StandardOpenOption> read = EnumSet.of(READ);
//        //private static final Set<StandardOpenOption> write = EnumSet.of(WRITE);
//        //private static final Set<StandardOpenOption> append = EnumSet.of(WRITE, APPEND);
//
//        FileChannelSink sink = new FileChannelSink(FileChannel.open(file.toPath(), append), Timeout.NONE);//append
//
//        String timeSecond = new SimpleDateFormat("MM-dd/HH:mm:ss").format(new Date(System.currentTimeMillis()));
//        Buffer buffer = new Buffer().writeUtf8(timeSecond + "\t"
//                + ApplicationContext.getCurProcessName() + "\t"
//                + getLevelTag(type) + tag + ":"
//                + message + LogzConstant.BR);
//        sink.write(buffer, buffer.size());
//        sink.close();
//    }

//    /**
//     * 单行日志写文件(Okio压缩方法)
//     *
//     * @param tag
//     * @param message
//     * @param file
//     * @throws Exception
//     */
//    private void wirteLogFileWithOkioZip(int type, String tag, String message, File file) throws Exception {
//        GzipSink gzipSink = new GzipSink(Okio.buffer(Okio.appendingSink(file)));//append
//        BufferedSink sinkZip = Okio.buffer(gzipSink);
//        String timeSecond = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date(System.currentTimeMillis()));
//        Buffer buffer = new Buffer().writeUtf8(timeSecond + "\t"
//                + ApplicationContext.getCurProcessName() + "\t"
//                + getLevelTag(type) + tag + ":"
//                + message + LogzConstant.BR);
//        sinkZip.write(buffer, buffer.size());
//        //close file outputStream resource
//        sinkZip.close();
//        gzipSink.close();
//    }

    /**
     * Get login user id
     *
     * @return
     */
    private long getLoginedUserId() {
//        SessionDBHelper session = LizhiFMCore.getAccountStorage().getSession();
//        long loginUserId = (session.hasSession() ? session.getSessionUid() : 0L);
//        return loginUserId;
        return 10000;
    }

    /**
     * judge current log level
     *
     * @param type
     */
    private String getLevelTag(int type) {
        String logLevel = "UNKOWN/";
        //sort with log level
        switch (type) {
            case Log.VERBOSE:
                logLevel = "V/";
                break;
            case Log.INFO:
                logLevel = "I/";
                break;
            case Log.DEBUG:
                logLevel = "D/";
                break;
            case Log.WARN:
                logLevel = "W/";
                break;
            case Log.ERROR:
                logLevel = "E/";
                break;
            case Log.ASSERT:
                logLevel = "A/";
                break;
            default:
                break;
        }
        return logLevel;
    }
}
