package com.kongqw.serialportlibrary.thread;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;

import static com.kongqw.serialportlibrary.DataUtils.Byte2Hex;

/**
 * Created by Kongqw on 2017/11/14.
 * 串口消息读取线程
 */

public abstract class SerialTempReadThread extends Thread {

    public abstract void onDataReceived(byte[] bytes);

    private static final String TAG = SerialTempReadThread.class.getSimpleName();
    private DataInputStream mInputStream;
    private int mFlag = 0;

    public SerialTempReadThread(DataInputStream inputStream) {
        mInputStream = inputStream;
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
                Log.i(TAG, "serialPort=" + mFlag);

                byte head1 = (byte) mInputStream.read();//起始位
                byte head2 = (byte) mInputStream.read();//data长度

                byte head3 = (byte) mInputStream.read();//fc
                byte head4 = (byte) mInputStream.read();//data
                byte head5 = (byte) mInputStream.read();
                byte head6 = (byte) mInputStream.read();
                Log.d(TAG, "--------------------------串口返回的数据-------------------------------------");
                Log.d(TAG, "___head1=" + Byte2Hex(head1) + ", head2=" + Byte2Hex(head2) + ", head3=" + Byte2Hex(head3));
                Log.d(TAG, "___head4=" + Byte2Hex(head4) + ", head5=" + Byte2Hex(head5) + ", head6=" + Byte2Hex(head6));
                Log.d(TAG, "------------------------------------------------------------------------------");

                if (head1 == 0x40) {
                    byte[] temp;
                    temp = new byte[2];

                    if (head2 == 0x04) {
                        byte head7 = (byte) mInputStream.read();
                        byte head8 = (byte) mInputStream.read();//标志位
                    }
                    temp[0] = head6;
                    temp[1] = head5;
                    onDataReceived(temp);
                } else {
                    byte head7 = (byte) mInputStream.read();
                    byte head8 = (byte) mInputStream.read();//标志位
                    //老温测数据
                    byte head9 = (byte) mInputStream.read();
                    Log.d(TAG, "---------------------------------------------------------------");
                    Log.d(TAG, "___head1=" + Byte2Hex(head1) + ", head2=" + Byte2Hex(head2) + ", head3=" + Byte2Hex(head3));
                    Log.d(TAG, "___head4=" + Byte2Hex(head4) + ", head5=" + Byte2Hex(head5) + ", head6=" + Byte2Hex(head6));
                    Log.d(TAG, "___head7=" + Byte2Hex(head7) + ", head5=" + Byte2Hex(head8) + ", head6=" + Byte2Hex(head9));
                    byte[] buffer = new byte[9];
                    buffer[0] = head1;
                    buffer[1] = head2;
                    buffer[2] = head3;
                    buffer[3] = head4;
                    buffer[4] = head5;
                    buffer[5] = head6;
                    buffer[6] = head7;
                    buffer[7] = head8;
                    buffer[8] = head9;
                    onDataReceived(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                byte[] temp = new byte[1];
                temp[0] = 0x01;
                onDataReceived(temp);
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
