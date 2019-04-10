package com.banshouweng.bswBase.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.format.DateUtils;


import com.banshouweng.bswBase.utils.db.DbUtils;
import com.banshouweng.bswBase.utils.rxbus2.Logger;
import com.banshouweng.bswBase.utils.rxbus2.RxBus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author leiming
 * @date 2018/12/19.
 */
public class CommonUtils {

    /**
     * Toast
     */
    @SuppressLint("StaticFieldLeak")
    private static ToastUtils toastUtils;

    /**
     * SharedPreference工具类
     */
    private static SPUtils spUtils;

    /**
     * 正则判断工具
     */
    private static RegularJudgeUtils regularJudgeUtils;

    /**
     * 文本格式化类
     */
    private static TxtUtils txtUtils;

    /**
     * 日期格式化类
     */
    private static DateUtils dateFormatUtils;

    /**
     * RxBus
     */
    private static RxBus rxBus;
    /**
     * 日志打印
     */
    private static Logger logger;
    /**
     * glide图片工具
     */
    private static GlideUtils glideUtils;
    /**
     * 控件测量工具
     */
    private static MeasureUtil measureUtil;
    /**
     * 屏幕测量工具
     */
    private static ScreenUtil screenUtil;
    private static TimeUpUtils timeUpUtils;
    private static FileUtils fileUtils;

    private static ExecutorService threadPoolExecutor;
    /*-------------------------------------------线程池--------------------------------------------*/

    /**
     * 启动线程池
     *
     * @param runnable 需要在子线程执行的程序
     */
    public static void threadPoolExecute(Runnable runnable) {
        if (isEmpty(threadPoolExecutor)) {
            threadPoolExecutor = Executors.newFixedThreadPool(3);
        }
        threadPoolExecutor.execute(runnable);
    }

    /*-------------------------------------------长度判断-------------------------------------------*/

    /**
     * 判断集合的长度
     *
     * @param list 所要判断的集合
     * @return 集合的大小，若为空也则返回0
     */
    public static int judgeListNull(List list) {
        if (list == null || list.size() == 0) {
            return 0;
        } else {
            return list.size();
        }
    }

    /**
     * 判断集合的长度
     *
     * @param map 所要判断的集合
     * @return 集合的大小，若为空也则返回0
     */
    public static int judgeListNull(Map map) {
        if (map == null || map.size() == 0) {
            return 0;
        } else {
            return map.size();
        }
    }

    /**
     * 判断集合的长度
     *
     * @param list 索要获取长度的集合
     * @return 该集合的长度
     */
    public static <T> int judgeListNull(T[] list) {
        if (list == null || list.length == 0) {
            return 0;
        } else {
            return list.length;
        }
    }

    /*-------------------------------------------空判断-------------------------------------------*/

    /**
     * 当前对象为空判断
     *
     * @param o 被判断对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object o) {
        return null == o;
    }

    /**
     * 当前对象存在判断
     *
     * @param o 被判断对象
     * @return 是否存在
     */
    public static boolean notEmpty(Object o) {
        return null != o;
    }

    /*-------------------------------------------Activity判断-------------------------------------------*/

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     */
    public static void jumpTo(Context context, Class<?> targetActivity) {
        jumpTo(context, targetActivity, null);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     * @param bundle         跳转所携带的信息
     */
    public static void jumpTo(Context context, Class<?> targetActivity, Bundle bundle) {
        if (! timeUpUtils().isTimeUp(TimeUpUtils.TIME_UP_JUMP, System.currentTimeMillis())) {
            return;
        }
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     */
    public static void receiverJumpTo(Context context, Class<?> targetActivity) {
        receiverJumpTo(context, targetActivity, null);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     * @param bundle         跳转所携带的信息
     */
    public static void receiverJumpTo(Context context, Class<?> targetActivity, Bundle bundle) {
        if (! timeUpUtils().isTimeUp(TimeUpUtils.TIME_UP_JUMP, System.currentTimeMillis())) {
            return;
        }
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的Activity类
     * @param requestCode    请求码
     */
    public static void jumpTo(Context context, Class<?> targetActivity, int requestCode) {
        jumpTo(context, targetActivity, requestCode, null);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的Activity类
     * @param bundle         跳转所携带的信息
     * @param requestCode    请求码
     */
    public static void jumpTo(Context context, Class<?> targetActivity, int requestCode, Bundle bundle) {
        if (! timeUpUtils().isTimeUp(TimeUpUtils.TIME_UP_JUMP, System.currentTimeMillis())) {
            return;
        }
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /*-------------------------------------------类名获取-------------------------------------------*/

    /**
     * 获取类名
     *
     * @param clz 需要获取名称的类
     * @return 类名字符串
     */
    public static String getName(Class<?> clz) {
        return clz.getClass().getSimpleName();
    }

    /*-------------------------------------------工具类封装-------------------------------------------*/

    /**
     * toast打印
     *
     * @param mContext  上下文
     * @param messageId 打印文本的Id
     */
    public static void toast(Context mContext, @StringRes int messageId) {
        if (isEmpty(toastUtils)) {
            toastUtils = new ToastUtils(mContext);
        }
        toastUtils.toast(messageId);
    }

    /**
     * toast打印
     *
     * @param mContext 上下文
     * @param message  打印的文本
     */
    public static void toast(Context mContext, String message) {
        if (isEmpty(toastUtils)) {
            toastUtils = new ToastUtils(mContext);
        }
        toastUtils.toast(message);
    }

    /**
     * 获取MD5加密字符串
     *
     * @param string 待加密字符串
     * @return 加密后的字符串
     */
    public static String getMD5Str(String string) {
        return MD5.getMD5Str(string);
    }

    /**
     * 获取SharedPreference工具
     *
     * @param mContext 上下文
     * @return 工具类
     */
    public static SPUtils getSPUtils(Context mContext) {
        if (isEmpty(spUtils)) {
            spUtils = SPUtils.getInstance(mContext);
        }
        return spUtils;
    }

    /**
     * 正则判断工具类
     *
     * @return 正则判断工具类
     */
    public static RegularJudgeUtils judge() {
        if (isEmpty(regularJudgeUtils)) {
            regularJudgeUtils = new RegularJudgeUtils();
        }
        return regularJudgeUtils;
    }

    /**
     * 文本整理工具类
     *
     * @return 文本整理工具类
     */
    public static TxtUtils text() {
        if (isEmpty(txtUtils)) {
            txtUtils = new TxtUtils();
        }
        return txtUtils;
    }

    /**
     * 时间工具类
     *
     * @return 时间工具类
     */
    public static DateUtils date() {
        if (isEmpty(dateFormatUtils)) {
            dateFormatUtils = new DateUtils();
        }
        return dateFormatUtils;
    }

    /**
     * 注册RxBus
     *
     * @param subscriber RxBus订阅者，一般为当前Activity或Fragment
     */
    public static void registerRxBus(Object subscriber) {
        if (isEmpty(rxBus)) {
            rxBus = RxBus.get();
        }
        rxBus.register(subscriber);
    }

    /**
     * 退出RxBus注册
     *
     * @param subscriber RxBus订阅者，一般为当前Activity或Fragment
     */
    public static void unRegisterRxBus(Object subscriber) {
        if (isEmpty(rxBus)) {
            rxBus = RxBus.get();
        }
        rxBus.unRegister(subscriber);
    }

    /**
     * 获取RxBus工具
     *
     * @return RxBus工具
     */
    public static RxBus getRxBus() {
        if (isEmpty(rxBus)) {
            rxBus = RxBus.get();
        }
        return rxBus;
    }

    /**
     * 获取日志打印工具
     *
     * @return 日志打印工具
     */
    public static Logger log() {
        if (isEmpty(logger)) {
            logger = new Logger();
        }
        return logger;
    }

    /**
     * 获取图片加载工具
     *
     * @return 图片加载工具
     */
    public static GlideUtils glide() {
        if (isEmpty(glideUtils)) {
            glideUtils = new GlideUtils();
        }
        return glideUtils;
    }

    /**
     * 获取控件测量工具
     *
     * @return 控件测量工具
     */
    public static MeasureUtil measureView() {
        if (isEmpty(measureUtil)) {
            measureUtil = new MeasureUtil();
        }
        return measureUtil;
    }

    /**
     * 获取屏幕测量工具
     *
     * @return 屏幕测量工具
     */
    public static ScreenUtil measureScreen() {
        if (isEmpty(screenUtil)) {
            screenUtil = new ScreenUtil();
        }
        return screenUtil;
    }

    /**
     * 数据库类获取
     * 避免有新表无法刷新出现，所以每次重新创建数据库工具
     *
     * @param mContext 上下文
     * @return 数据库
     */
    public static DbUtils getDbUtils(Context mContext) {
        return new DbUtils(mContext);
    }

    /**
     * 超时工具类获取
     *
     * @return 数据库
     */
    public static TimeUpUtils timeUpUtils() {
        if (CommonUtils.isEmpty(timeUpUtils)) {
            timeUpUtils = new TimeUpUtils();
        }
        return timeUpUtils;
    }

    /**
     * 文件管理工具类整理
     *
     * @return 数据库
     */
    public static FileUtils getFileUtils() {
        if (isEmpty(fileUtils)) {
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }
}
