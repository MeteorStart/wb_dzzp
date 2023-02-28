package com.kongqw.serialportlibrary.thread;


import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Kongqw on 2017/11/14.
 * 串口消息读取线程
 */

public abstract class SerialPortReadThread extends Thread {

    public abstract void onDataReceived(byte[] bytes);

    private static final String TAG = SerialPortReadThread.class.getSimpleName();
    private DataInputStream mInputStream;
    private byte[] mReadBuffer;

    public SerialPortReadThread(DataInputStream inputStream) {
        mInputStream = inputStream;
        mReadBuffer = new byte[1024];
    }


    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            try {
                if (null == mInputStream) {
                    return;
                }
                int size = mInputStream.read(mReadBuffer);
                byte[] readBytes = new byte[size];

                System.arraycopy(mReadBuffer, 0, readBytes, 0, size);

                onDataReceived(readBytes);


            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * 关闭线程 释放资源
     */
    public void release() {
        interrupt();

        if (null != mInputStream) {
            try {
                mInputStream.close();
                mInputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
