package com.banshouweng.bswBase.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取权限的工具类
 *
 * @author leiming
 * @date 2018/1/5
 */
public class PermissionUtils {

    // CALENDAR
    // 允许程序读取用户日历数据
    private static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    // 允许一个程序写入但不读取用户日历数据
    private static final String PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    // CAMERA
    // 请求访问使用照相设备
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    // CONTACTS
    // 允许程序读取用户联系人数据
    private static final String PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    // 允许程序写入但不读取用户联系人数据
    private static final String PERMISSION_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    // 访问GMail账户列表
    private static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    // LOCATION
    // 允许一个程序访问精良位置(如GPS)
    private static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // 允许一个程序访问CellID或WiFi热点来获取粗略的位置
    private static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    // MICROPHONE
    // 允许程序录制音频
    private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    // PHONE
    // 允许读取电话状态
    private static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    // 允许拨打电话
    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    // 允许获取通话记录
    private static final String PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    // 允许编辑通话记录
    private static final String PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    // 允许应用程序添加系统中的语音邮件
    private static final String PERMISSION_ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    // 允许程序使用SIP视频服务
    private static final String PERMISSION_USE_SIP = Manifest.permission.USE_SIP;
    // 允许应用程序监视、修改、忽略拨出的电话
    private static final String PERMISSION_PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    // SENSORS
    // 允许获取身体传感器信息
    private static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    // SMS
    // 允许程序发送SMS短信
    private static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    // 允许程序监控一个将收到短信息，记录或处理
    private static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    // 允许程序读取短信息
    private static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    // 允许程序监控将收到WAP PUSH信息
    private static final String PERMISSION_RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    // 允许一个程序监控将收到MMS彩信,记录或处理
    private static final String PERMISSION_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    // STORAGE
    // 读取外部储存卡
    private static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    // 写入外部储存卡
    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int CODE_READ_CALENDAR = 0;
    public static final int CODE_WRITE_CALENDAR = 1;
    public static final int CODE_CAMERA = 2;
    public static final int CODE_READ_CONTACTS = 3;
    public static final int CODE_WRITE_CONTACTS = 4;
    public static final int CODE_GET_ACCOUNTS = 5;
    public static final int CODE_ACCESS_FINE_LOCATION = 6;
    public static final int CODE_ACCESS_COARSE_LOCATION = 7;
    public static final int CODE_RECORD_AUDIO = 8;
    public static final int CODE_READ_PHONE_STATE = 9;
    public static final int CODE_CALL_PHONE = 10;
    public static final int CODE_READ_CALL_LOG = 11;
    public static final int CODE_WRITE_CALL_LOG = 12;
    public static final int CODE_ADD_VOICEMAIL = 13;
    public static final int CODE_USE_SIP = 14;
    public static final int CODE_PROCESS_OUTGOING_CALLS = 15;
    public static final int CODE_BODY_SENSORS = 16;
    public static final int CODE_SEND_SMS = 17;
    public static final int CODE_RECEIVE_SMS = 18;
    public static final int CODE_READ_SMS = 19;
    public static final int CODE_RECEIVE_WAP_PUSH = 20;
    public static final int CODE_RECEIVE_MMS = 21;
    public static final int CODE_READ_EXTERNAL_STORAGE = 22;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 23;
    public static final int CODE_ALL_PERMISSION = 100;

    private static final String[] requestPermissions = {
            PERMISSION_READ_CALENDAR, PERMISSION_WRITE_CALENDAR, PERMISSION_CAMERA, PERMISSION_READ_CONTACTS, PERMISSION_WRITE_CONTACTS,
            PERMISSION_GET_ACCOUNTS, PERMISSION_ACCESS_FINE_LOCATION, PERMISSION_ACCESS_COARSE_LOCATION, PERMISSION_RECORD_AUDIO,
            PERMISSION_READ_PHONE_STATE, PERMISSION_CALL_PHONE, PERMISSION_READ_CALL_LOG, PERMISSION_WRITE_CALL_LOG, PERMISSION_ADD_VOICEMAIL,
            PERMISSION_USE_SIP, PERMISSION_PROCESS_OUTGOING_CALLS, PERMISSION_BODY_SENSORS, PERMISSION_SEND_SMS, PERMISSION_RECEIVE_SMS,
            PERMISSION_READ_SMS, PERMISSION_RECEIVE_WAP_PUSH, PERMISSION_RECEIVE_MMS, PERMISSION_READ_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE
    };

    private static PermissionGrant utilGrant;

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            List<String> deniedPermission = new ArrayList<>();
            if (permissions.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        deniedPermission.add(permissions[i]);
                    }
                }
            }
            utilGrant.onRequestResult(deniedPermission);
        }
    }

    public static void setRequestPermissions(Fragment fragment, PermissionGrant grant) {
        utilGrant = grant;
        Integer[] permissions = grant.onPermissionGranted();
        if (Arrays.asList(permissions).contains(CODE_ALL_PERMISSION)) {
            fragment.requestPermissions(requestPermissions, 1);
        } else {
            int permissionLength = Const.judgeListNull(permissions);
            if (permissionLength > 0) {
                List<String> requestPermission = new ArrayList<>();
                for (int i = 0; i < permissionLength; i++) {
                    String permission = requestPermissions[permissions[i]];
                    if (! hasPermission(fragment, permission)) {
                        requestPermission.add(permission);
                    }
                }
                if (Const.judgeListNull(requestPermission) != 0) {
                    fragment.requestPermissions(requestPermission.toArray(new String[requestPermission.size()]), 1);
                } else {
                    grant.onRequestResult(new ArrayList<String>());
                }
            } else {
                grant.onRequestResult(new ArrayList<String>());
            }
        }
    }

    public static void setRequestPermissions(Activity activity, PermissionGrant grant) {
        utilGrant = grant;
        Integer[] permissions = grant.onPermissionGranted();
        if (Arrays.asList(permissions).contains(CODE_ALL_PERMISSION)) {
            ActivityCompat.requestPermissions(activity, requestPermissions, 1);
        } else {
            int permissionLength = Const.judgeListNull(permissions);
            if (permissionLength > 0) {
                List<String> requestPermission = new ArrayList<>();
                for (int i = 0; i < permissionLength; i++) {
                    String permission = requestPermissions[permissions[i]];
                    if (! hasPermission(activity, permission)) {
                        requestPermission.add(permission);
                    }
                }
                if (Const.judgeListNull(requestPermission) != 0) {
                    ActivityCompat.requestPermissions(activity, requestPermission.toArray(new String[requestPermission.size()]), 1);
                } else {
                    grant.onRequestResult(new ArrayList<String>());
                }
            } else {
                grant.onRequestResult(new ArrayList<String>());
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean hasPermission(Fragment fragment, String permission) {
        return ContextCompat.checkSelfPermission(fragment.getActivity().getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean hasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermission(Activity activity, Integer... permissionIds) {
        for (Integer permissionId : permissionIds) {
            if (ContextCompat.checkSelfPermission(activity, requestPermissions[permissionId]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermission(Context context, Integer... permissionIds) {
        for (Integer permissionId : permissionIds) {
            if (ContextCompat.checkSelfPermission(context, requestPermissions[permissionId]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public interface PermissionGrant {
        Integer[] onPermissionGranted();

        void onRequestResult(List<String> deniedPermission);
    }
}
