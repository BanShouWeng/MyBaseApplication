package com.mybaseapplication.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.mybaseapplication.base.BaseActivity;
import com.mybaseapplication.utils.NetUtil;

/**
 * Created by dell on 2017/7/10.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetEvevt evevt = BaseActivity.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int mobileNetState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            try {
                evevt.onNetChanged(mobileNetState);
            } catch (Exception e){
            }
        }
    }

    // 自定义接口
    public interface NetEvevt {
        void onNetChanged(int mobileNetState);
    }
}
