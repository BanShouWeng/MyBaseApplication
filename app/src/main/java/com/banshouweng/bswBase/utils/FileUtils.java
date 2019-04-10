package com.banshouweng.bswBase.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * 文件工具类
 *
 * @author leiming
 * @date 2018/5/2 17:38
 */
public class FileUtils {

    public static final int AUDIO_RECORD = 0x011;
    public static final int WAV_OUTPUT = 0x012;
    public static final int VIDEO_RECORD = 0x013;
    public static final int IMAGE_CAPTURE = 0x014;
    public static final int AMR_RECORD = 0x015;
    public static final int RAW_RECORD = 0x016;

    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > - 1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }// MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static List<String> ergodic(File file, List<String> resultFileName) {
        File[] files = file.listFiles();
        if (files == null) return resultFileName;// 判断目录下是不是空的
        for (File f : files) {
            if (f.isDirectory()) {// 判断是否文件夹
                resultFileName.add(f.getPath());
                ergodic(f, resultFileName);// 调用自身,查找子目录
            } else
                resultFileName.add(f.getPath());
        }
        return resultFileName;
    }

    public static long getFileSizes(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }
        return s;
    }

    public static long getFileSizes(String filePath) throws Exception {
        long s = 0;
        File f = new File(filePath);
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }
        return s;
    }

    /**
     * 递归
     */
    public static long getDirectorySize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getDirectorySize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     */
    public static String formentFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小为Kb
     */
    public static int formentFileSize_Kb(long fileS) {
        return (int) fileS / 1024;
    }

    /**
     * 获取文件类型
     *
     * @param filepath 文件路径
     * @return 文件类型
     */
    public static int getFileType(String filepath) {
        int type = 0;
        if (MediaFileJudgeUtils.isImageFileType(filepath)) {
            type = 2;
        } else if (MediaFileJudgeUtils.isVideoFileType(filepath)) {
            type = 1;
        }
        return type;
    }

    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public static String renameFile(String path, String oldname, String newname) {
        if (! oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (! oldfile.exists()) {
                return "";//重命名文件不存在
            }
            if (newfile.exists()) {//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                newname = newNameExist(path, newname);
                oldfile.renameTo(new File(path + "/" + newname));
            } else {
                oldfile.renameTo(newfile);
            }
            return path + "/" + newname;
        }
        return null;
    }

    /**
     * 文件重命名
     *
     * @param allOldName 原来的完整文件名
     * @param allNewName 完整的新文件名
     */
    public static String renameFile(String allOldName, String allNewName) {
        if (! allOldName.equals(allNewName)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(allOldName);
            File newfile = new File(allNewName);
            if (! oldfile.exists()) {
                return "";//重命名文件不存在
            }
            if (newfile.exists()) {//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                allNewName = newNameExist(allNewName);
                oldfile.renameTo(new File(allNewName));
            } else {
                oldfile.renameTo(newfile);
            }
            return allNewName;
        }
        return null;
    }

    private static String newNameExist(String path, String newname) {
        newname += "a";
        if (new File(path + "/" + newname).exists()) {
            newname = newNameExist(path, newname);
        }
        return newname;
    }

    private static String newNameExist(String allNewName) {
        allNewName += "a";
        if (new File(allNewName).exists()) {
            allNewName = newNameExist(allNewName);
        }
        return allNewName;
    }

    public static void copy(String path, String copyPath) throws IOException {
        File filePath = new File(path);
        DataInputStream read;
        DataOutputStream write;
        if (filePath.isDirectory()) {
            new File(copyPath).mkdirs();
            File[] list = filePath.listFiles();
            for (int i = 0; i < list.length; i++) {
                String newPath = path + File.separator + list[i].getName();
                String newCopyPath = copyPath + File.separator + list[i].getName();
                copy(newPath, newCopyPath);
            }
        } else if (filePath.isFile()) {
            File copyFile = new File(copyPath);
            if (! copyFile.getParentFile().exists()) {
                copyFile.getParentFile().mkdirs();
            }
            read = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(path)));
            write = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(copyFile)));
            byte[] buf = new byte[1024 * 512];
            while (read.read(buf) != - 1) {
                write.write(buf);
            }
            read.close();
            write.close();
        } else {
            System.out.println("请输入正确的文件名或路径名");
        }
    }

//    public static String copyTempFile(String path, String copyName) throws IOException {
//        File filePath = new File(path);
//        File copyFile = new File(App.hfive_path + "/" + TEMPTAG + "/" + copyName);
//        if (! copyFile.getParentFile().exists()) {
//            copyFile.getParentFile().mkdirs();
//        }
//        DataInputStream read;
//        DataOutputStream write;
//        if (filePath.isFile()) {
//            read = new DataInputStream(
//                    new BufferedInputStream(new FileInputStream(path)));
//            write = new DataOutputStream(
//                    new BufferedOutputStream(new FileOutputStream(App.hfive_path + "/" + TEMPTAG + "/" + copyName)));
//            byte[] buf = new byte[1024 * 512];
//            while (read.read(buf) != - 1) {
//                write.write(buf);
//            }
//            read.close();
//            write.close();
//            return App.hfive_path + "/" + TEMPTAG + "/" + copyName;
//        }
//        return null;
//    }

    public static File getCompressPhoto(Context context, String picPath) {
        return Luban.get(context).load(new File(picPath)).launch();
    }

    public static File getCompressPhoto(Context context, File image) {
        return Luban.get(context).load(image).launch();
    }

    /**
     * 通过Uri返回File文件
     * 注意：通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     *
     * @param uri     文件uri
     * @param context 上下文
     * @return 文件
     */
    public static File getFileByUri(Context context, Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); ! cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            CommonUtils.log().i(FileUtils.class.getSimpleName(), "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    /**
     * 获得存储 录音文件的文件夹
     *
     * @return File类型
     * 存储 录音文件的文件夹 .AUDIO_RECORD是一个文件夹
     */
    public static File getNewFile(int fileType) {
        String filePath = null;
        switch (fileType) {
            case AUDIO_RECORD:
                filePath = Environment.getExternalStorageDirectory() + "/hzy/record" + formatInt() + System.currentTimeMillis() + ".pcm";
                break;

            case AMR_RECORD:
                filePath = Environment.getExternalStorageDirectory() + "/hzy/record" + formatInt() + System.currentTimeMillis() + ".amr";
                break;

            case RAW_RECORD:
                filePath = Environment.getExternalStorageDirectory() + "/hzy/record" + formatInt() + System.currentTimeMillis() + ".raw";
                break;

            case WAV_OUTPUT:
                filePath = Environment.getExternalStorageDirectory() + "/hzy/record" + formatInt() + System.currentTimeMillis() + ".wav";
                break;

            case VIDEO_RECORD:
                filePath = Environment.getExternalStorageDirectory() + "/hzy/video" + formatInt() + System.currentTimeMillis() + ".mp4";
                break;

            case IMAGE_CAPTURE:
                filePath = Environment.getExternalStorageDirectory() + "/hzy/image" + formatInt() + System.currentTimeMillis() + ".jpg";
                break;
        }
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (! file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (! file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                CommonUtils.log().e("FileUrils", e);
                return null;
            }
        }
        return file;
    }

    public static String getNewFileDir(int fileType) {
        File file = getNewFile(fileType);
        if (Const.isEmpty(file)) {
            return null;
        }
        return file.getAbsolutePath();
    }

    private static String formatInt() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (! file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (! dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((! dirFile.exists()) || (! dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                if (! flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (! flag)
                    break;
            }
        }
        if (! flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
//
//    public static String ioToBase64(String path) throws IOException {
////        String fileName = "D:/u18.jpg"; // 源文件
//        String source = path;
//        String strBase64 = null;
//        try {
//            InputStream in = new FileInputStream(source);
//            // in.available()返回文件的字节长度
//            byte[] bytes = new byte[in.available()];
//            // 将文件中的内容读入到数组中
//            in.read(bytes);
//            strBase64 = new BASE64Encoder().encode(bytes); // 将字节流数组转换为字符串
//            in.close();
//        } catch (FileNotFoundException fe) {
//            fe.printStackTrace();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        return strBase64;
//    }
}
