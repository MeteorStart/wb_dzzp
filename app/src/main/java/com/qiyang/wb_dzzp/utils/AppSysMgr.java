package com.qiyang.wb_dzzp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2018/2/28 22:17
 * @company:
 * @email: lx802315@163.com
 */
public class AppSysMgr {
    /**
     * 获取蓝牙地址
     *
     * @param context
     * @return
     */
    //<uses-permission android:name="android.permission.BLUETOOTH"/>
    @SuppressWarnings("MissingPermission")
    public static String getBluetoothMAC(Context context) {
        /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //        String macAddress= "";
//        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        macAddress = wifiInfo.getMacAddress();
//        return macAddress;

        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * 获取手机CPU序列号
     *
     * @return String cpu序列号(16位) 读取失败为"0000000000000000"
     */
    public static String getSysCPUSerialNum() {
        String str = "", strCPU = "", cpuSerialNum = "0000000000000000";
        try {
            //读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            //查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    //查找到序列号所在行
                    if (str.indexOf("Serial") > -1) {
                        //提取序列号
                        strCPU = str.substring(str.indexOf(":") + 1,
                                str.length());
                        //去空格
                        cpuSerialNum = strCPU.trim();
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
//            AppLogMessageMgr.e("AppSysMgr-->>getSysCPUSerialNum", );
            LogUtils.Companion.print(e.getMessage().toString());
        }
        return cpuSerialNum;
    }

    /**
     * 获取主板信息
     *
     * @return
     */
    public static String getBoard() {
        return Build.BOARD;
    }

    /**
     * 获取设备序列号
     *
     * @return
     */
    public static String getSerial() {
        return Build.SERIAL;
    }

    /**
     * 修改系统时间。
     */
    public void testDate(String time) {
        try {
            Process process = Runtime.getRuntime().exec("su");
//            String datetime="20131023.112800"; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            String datetime = time; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读唯一的设备ID(唯一的设备ID【GSM手机的IMEI】和【CDMA手机的 MEID】,如果获取不到返回一个默认字符串)
     *
     * @param content 上下文
     * @return String 获取设备序列号
     */
    public static String getSysTelephoneSerialNum(Context content) {
        @SuppressLint("MissingPermission") String deviceId = getSysTelephonyManager(content).getDeviceId();
        return deviceId;
    }

    /**
     * 获得电话管理实例对象
     *
     * @param content 上下文
     * @return TelephonyManager 电话管理实例对象
     */
    private static TelephonyManager getSysTelephonyManager(Context content) {
        TelephonyManager telephonyManager = null;
        telephonyManager = (TelephonyManager) content.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager;
    }

    /**
     * Android设备物理唯一标识符
     *
     * @return
     */
    public static String getPsuedoUniqueID() {
        String devIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            devIDShort += (Build.SUPPORTED_ABIS[0].length() % 10);
        } else {
            devIDShort += (Build.CPU_ABI.length() % 10);
        }
        devIDShort += (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            serial = "ESYDV000";
        }
        return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * Android设备物理唯一标识符
     *
     * @return
     */
    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }


    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }
}
