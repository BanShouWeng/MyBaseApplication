package com.banshouweng.bswBase.crash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.banshouweng.bswBase.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CrashHandler implements UncaughtExceptionHandler {

    private final static String TAG = "UncaughtException";
    private UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler mInstance;
    private Context mContext;
    public String uid;

    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (mInstance == null)
            mInstance = new CrashHandler();
        return mInstance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handleException(throwable);
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            Log.e(TAG, "Error : ", e);
        }
        mDefaultHandler.uncaughtException(thread, throwable);
//        if (!handleException(throwable) && mDefaultHandler != null) {
//
//            // 如果用户没有处理则让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, throwable);
//        } else {
//            // Sleep一会后结束程序
//            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
//            try {
//                Thread.sleep(3000);
//            } catch (Exception e) {
//                Log.e(TAG, "Error : ", e);
//            }
//            try {
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(10);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
//        final String msg = ex.getLocalizedMessage(); 

        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        final String msg = stackTrace.toString();
        // 使用Toast来显示异常信息
        Log.e(TAG, "Error : ", ex);
        new Thread() {
            @Override
            public void run() {
                // Toast 显示需要出现在一个线程的消息队列中    
//                Looper.prepare();
                try {
                    Log.e("crash", msg);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
                    MailSenderInfo mailInfo = new MailSenderInfo();
                    mailInfo.setMailServerHost("smtp.163.com");
                    mailInfo.setMailServerPort("25");
                    mailInfo.setValidate(true);
                    mailInfo.setUserName("");               //你的邮箱地址
                    mailInfo.setPassword("");                 //您的邮箱密码
                    mailInfo.setFromAddress("");            //你的邮箱地址
                    mailInfo.setToAddress("");              //你的邮箱地址
//                    mailInfo.setSubject(mContext.getString(R.string.app_name) + ":Exception Catched  : DEVICE_NAME: " + Const.DEVICE_NAME + " ; APP_VER" + Const.APP_VER + " ; time : " + simpleDateFormat.format(new Date())
//                            + " ; uid " + App.getInstence().getKeyValueDBService().find(Keys.UID));
                    mailInfo.setSubject(mContext.getString(R.string.app_name) + ":Exception Catched  : time : " + simpleDateFormat.format(new Date()));
                    mailInfo.setContent(msg);

                    //这个类主要来发送邮件
                    SimpleMailSender sms = new SimpleMailSender();
//                    sms.sendTextMail(mailInfo);//发送文体格式
                    SimpleMailSender.sendHtmlMail(mailInfo);//发送html格式

                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
//                Looper.loop();
            }
        }.start();
        return true;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


}  