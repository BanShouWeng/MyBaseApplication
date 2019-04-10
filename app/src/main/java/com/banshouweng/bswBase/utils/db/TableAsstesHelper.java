package com.banshouweng.bswBase.utils.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.banshouweng.bswBase.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class TableAsstesHelper {
    private final String TAG = "MultiTableHelper";
    private String[] tableNames;
    private String[] sqls;
    private String dbName;
    private SQLiteDatabase mSQLiteDatabase;
    String pathStr = App.getInstance().getDirPath() + File.separator + "db";
    String filePath = "";

    public TableAsstesHelper(Context context, String dbName, int version) {
        this.dbName = dbName;
        filePath = pathStr + File.separator + dbName;
        mSQLiteDatabase = openDatabase(context);
    }

    private SQLiteDatabase openDatabase(Context context) {
        System.out.println("filePath:" + filePath);
        File jhPath = new File(filePath);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            Log.i("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            //不存在先创建文件夹
            File path = new File(pathStr);
            Log.i("test", "pathStr=" + path);
            if (path.mkdirs()) {
                Log.i("test", "创建成功");
            } else {
                Log.i("test", "创建失败");
            }
            ;
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到数据库的输入流
                InputStream is = am.open(dbName);
                Log.i("test", is + "");
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                Log.i("test", "fos=" + fos);
                Log.i("test", "jhPath=" + jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    Log.i("test", "得到");
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context);
        }
    }

    public SQLiteDatabase getDatabase() {
        return mSQLiteDatabase;
    }

    public void close() {
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
    }
}
