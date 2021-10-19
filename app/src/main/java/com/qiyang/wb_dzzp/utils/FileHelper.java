package com.qiyang.wb_dzzp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2018/4/7 14:50
 * @company:
 * @email: lx802315@163.com
 */
public class FileHelper {
    private Context mContext;

    //空参数构造函数，传入的值为空时，不出错
    public FileHelper() {
    }

    public FileHelper(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /*
     * 定义文件保存的方法，写入到文件中，所以是输出流
     * */
    public void save(String name, String content) throws Exception {
        FileOutputStream output = mContext.openFileOutput(name, Context.MODE_APPEND);
        output.write(content.getBytes());  //将String字符串以字节流的形式写入到输出流中
        output.close();         //关闭输出流
    }

    //    往SD卡写入文件的方法
    public void savaFileToSD(String filename, String filecontent) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename + ".txt";
            File file = new File(filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(filename);
                output.write(filecontent.getBytes());
                //将String字符串以字节流的形式写入到输出流中
                output.close();
                //关闭输出流
            } else {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(filename);
                output.write(filecontent.getBytes());
                //将String字符串以字节流的形式写入到输出流中
                output.close();
                //关闭输出流
            }
        }
    }

    //读取SD卡中文件的方法
//定义读取文件的方法:
    public String readFromSD(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename + ".txt";
            File file = new File(filename);
            if (file.exists()){
                //打开文件输入流
                FileInputStream input = new FileInputStream(filename);
                byte[] temp = new byte[1024];

                int len = 0;
                //读取文件内容:
                while ((len = input.read(temp)) > 0) {
                    sb.append(new String(temp, 0, len));
                }
                //关闭输入流
                input.close();
            }
        }
        return sb.toString();
    }

    /*
     * 定义文件读取的方法
     * */
    public String read(String filename) throws IOException {
        //打开文件输入流
        FileInputStream input = mContext.openFileInput(filename);
        FileReader fileReader = new FileReader("/sdcard/axs" + "/mac_code.txt");
        //定义1M的缓冲区
        byte[] temp = new byte[1024];
        //定义字符串变量
        StringBuilder sb = new StringBuilder("");
        int len = 0;
        //读取文件内容，当文件内容长度大于0时，
        while ((len = input.read(temp)) > 0) {
            //把字条串连接到尾部
            sb.append(new String(temp, 0, len));
        }
        //关闭输入流
        input.close();
        //返回字符串
        return sb.toString();
    }
}
