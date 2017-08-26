package com.banshouweng.mybaseapplication.reciever;

import com.banshouweng.mybaseapplication.base.BaseBean;

/**
 * Created by dell on 2017/8/26.
 */

public interface ResultCallBack {
    void success(String action, BaseBean baseBean);

    void error(String action, Throwable e);
}
