package com.kongqw.serialportlibrary.thread;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;

import static com.kongqw.serialportlibrary.DataUtils.Byte2Hex;

/**
 * Created by Kongqw on 2017/11/14.
 * 串口消息读取线程
 */

public abstract class SerialCardReadThread extends Thread {

    public abstract void onDataReceived(byte[] bytes);

    private static final String TAG = SerialCardReadThread.class.getSimpleName();
    private DataInputStream mInputStream;
    private byte[] mReadBuffer;

    public SerialCardReadThread(DataInputStream inputStream) {
        mInputStream = inputStream;
        mReadBuffer = new byte[1024];
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "threadName=" + Thread.currentThread().getName());
        while (!isInterrupted()) {
            try {
                if (null == mInputStream) {
                    return;
                }


                Log.i(TAG, "run: ");
                    int size = mInputStream.read(mReadBuffer);

                    if (0 >= size) {
                        return;
                    }

                    byte[] readBytes = new byte[size];

                    System.arraycopy(mReadBuffer, 0, readBytes, 0, size);

                    Log.i(TAG, "run: readBytes = " + new String(readBytes));
                    onDataReceived(readBytes);
            } catch (Exception e) {
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
