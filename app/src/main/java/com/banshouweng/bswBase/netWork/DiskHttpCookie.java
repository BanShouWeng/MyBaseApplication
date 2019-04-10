package com.banshouweng.bswBase.netWork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpCookie;

/**
 * 《一个Android工程的从零开始》
 * Cooker序列化
 * https://blog.csdn.net/dd864140130/article/details/52559437
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
class DiskHttpCookie implements Serializable {
    private static final long serialVersionUID = - 6370968478600008500L;
    private transient final HttpCookie mCookie;
    private transient HttpCookie mClientCookie;

    DiskHttpCookie(HttpCookie cookie) {
        mCookie = cookie;
    }

    HttpCookie getCookie() {
        HttpCookie cookie = mCookie;
        if (mClientCookie != null) {
            cookie = mClientCookie;
        }

        return cookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(mCookie.getName());
        out.writeObject(mCookie.getValue());
        out.writeObject(mCookie.getComment());
        out.writeObject(mCookie.getCommentURL());
        out.writeObject(mCookie.getDomain());
        out.writeLong(mCookie.getMaxAge());
        out.writeObject(mCookie.getPath());
        out.writeObject(mCookie.getPortlist());
        out.writeBoolean(mCookie.getSecure());
        out.writeBoolean(mCookie.getDiscard());
        out.writeInt(mCookie.getVersion());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        mClientCookie = new HttpCookie(name, value);
        mClientCookie.setComment((String) in.readObject());
        mClientCookie.setCommentURL((String) in.readObject());
        mClientCookie.setDomain((String) in.readObject());
        mClientCookie.setMaxAge(in.readLong());
        mClientCookie.setPath((String) in.readObject());
        mClientCookie.setPortlist((String) in.readObject());
        mClientCookie.setSecure(in.readBoolean());
        mClientCookie.setDiscard(in.readBoolean());
        mClientCookie.setVersion(in.readInt());

    }
}
