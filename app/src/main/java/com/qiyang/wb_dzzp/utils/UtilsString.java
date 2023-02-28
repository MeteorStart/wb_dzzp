package com.qiyang.wb_dzzp.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by jie on 18-2-9.
 */

public class UtilsString {

    // 整数到字节数组转换
    public static byte[] int2bytes(int n) {
        byte[] ab = new byte[4];
        ab[0] = (byte) (0xff & n);
        ab[1] = (byte) ((0xff00 & n) >> 8);
        ab[2] = (byte) ((0xff0000 & n) >> 16);
        ab[3] = (byte) ((0xff000000 & n) >> 24);
        return ab;
    }

    // 整数到字节数组转换
    public static byte[] int2byte(int n) {
        byte[] ab = new byte[1];
        ab[0] = (byte) (0xff & n);
        return ab;
    }

    public static void int2bytes(int t, byte[] bytes, int index) {
        bytes[index] = (byte) ((t & 0x000000FF));
        bytes[index + 1] = (byte) ((t & 0x0000FF00) >> 8);
        bytes[index + 2] = (byte) ((t & 0x00FF0000) >> 16);
        bytes[index + 3] = (byte) ((t & 0xFF000000) >> 24);
    }

    public static void int2bytesEndianLittleBig(int t, byte[] bytes, int index) {
        bytes[index] = (byte) ((t & 0xFF000000) >> 24);
        bytes[index + 1] = (byte) ((t & 0x00FF0000) >> 16);
        bytes[index + 2] = (byte) ((t & 0x0000FF00) >> 8);
        bytes[index + 3] = (byte) ((t & 0x000000FF));
    }

    // 字节数组到整数的转换
    public static int bytes2int(byte b[], int index) {
        int s = 0;
        s = ((((b[index] & 0xff) << 8 | (b[index + 1] & 0xff)) << 8) | (b[index + 2] & 0xff)) << 8
                | (b[index + 3] & 0xff);
        return s;
    }

    public static int bytes2intLittleEndian(byte b[], int index) {
        int s = 0;
        s = (b[index] & 0xff) | (((b[index + 1] & 0xff)) << 8) | (((b[index + 2] & 0xff)) << 16)
                | ((b[index + 3] & 0xff) << 24);
        return s;
    }

    // 字节转换到字符
    public static char byte2char(byte b) {
        return (char) b;
    }

    private final static byte[] hex = "0123456789ABCDEF".getBytes();

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    // 从字节数组到十六进制字符串转换
    public static String Bytes2HexString(byte[] b) {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return new String(buff);
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static int getByte(String time) {
        return Integer.valueOf(time).intValue();
    }


    public static String byte2String(byte[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        int temp;
        for (int i = 0; ; i++) {
            temp = a[i] & 0xff;
            b.append(temp);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 4);
        buf.append("[");

        for (int i = 0; i < bytes.length; i++) { // 使用String的format方法进行转换
            buf.append(String.format("0x%02x", new Integer(bytes[i] & 0xff)));
            if (i != bytes.length - 1) {
                buf.append(",");
            }
        }
        buf.append("]");

        return buf.toString();
    }


    /**
     * 最新的获取指令 ----校验位
     *
     * @return
     */
    public static String getBCC(int beginHour, int beginMin, int endHour, int endMin,
                                int lightOpenHour, int lightOpenMin, int lightCloseHour, int lightCloseMin,
                                int openFanTemp, int closeFanTemp) {
        byte[] data = {(byte) 0xa2, (byte) beginHour, (byte) beginMin, (byte) endHour, (byte) endMin,
                (byte) lightOpenHour, (byte) lightOpenMin, (byte) lightCloseHour, (byte) lightCloseMin,
                0x00, (byte) openFanTemp, 0x00, (byte) closeFanTemp};
        String ret = "";
        byte BCC[] = new byte[1];
        for (int i = 0; i < data.length; i++) {
            BCC[0] = (byte) (BCC[0] ^ data[i]);
        }
        String hex = Integer.toHexString(BCC[0] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        ret += hex.toUpperCase();
        return ret;
    }

    /**
     * 检验年月日，时分秒的
     *
     * @return
     */
    public static String getBCC2(int year, int month, int day, int Hour, int Min, int sec) {

//        7e a1 00 12 06 01 13 22 2d a8 7e
        byte[] data = {(byte) 0xa1, 0x00, (byte) year, (byte) month, (byte) day, (byte) Hour, (byte) Min, (byte) sec};
        String ret = "";
        byte BCC[] = new byte[1];
        for (int i = 0; i < data.length; i++) {
            BCC[0] = (byte) (BCC[0] ^ data[i]);
        }
        String hex = Integer.toHexString(BCC[0] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        ret += hex.toUpperCase();
        return ret;
    }


    /**
     * 获取指令异或值
     *
     * @param
     * @return
     */
    public static int getXOR(int beginHour, int beginMin, int endHour, int endMin) {

        byte[] datas = {0x7e, (byte) 0xa2, (byte) beginHour, (byte) beginMin, (byte) endHour, (byte) endMin, (byte) 0x11, 0x00, 0x17, 0x00, 0x00, 0x28, 0x00, 0x23};
        int temp = datas[0];              // 此处首位取1是因为本协议中第一个数据不参数异或校验，转为int防止结果出现溢出变成负数

        for (int i = 1; i < datas.length; i++) {
            int preTemp = temp;
            int iData;
            if (datas[i] < 0) {
                iData = datas[i] & 0xff;      // 变为正数计算
            } else {
                iData = datas[i];
            }
            if (temp < 0) {
                temp = temp & 0xff;          // 变为正数
            }
            temp ^= iData;

            System.out.println(preTemp + "异或" + iData + "=" + temp);
        }

        System.out.println(UtilsString.bytesToHexString(UtilsString.int2bytes(temp)));

        return temp;
    }
}
