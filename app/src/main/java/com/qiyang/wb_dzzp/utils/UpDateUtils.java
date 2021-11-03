package com.qiyang.wb_dzzp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.jtkj.dzzp_52_screen.utils.AppPrefsUtils;
import com.qiyang.wb_dzzp.MyApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/5/22 8:45
 * @company:
 * @email: lx802315@163.com
 */
public class UpDateUtils {

    public static boolean pmInstall(String path) {
        String[] args = {"pm", "install", "-r", "-t", path};
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        String str = null;
        try {
            LogUtils.Companion.print("开始升级");
            AppPrefsUtils.INSTANCE.putBoolean("isUpdate", true);
            MyApplication.Companion.getMyApplication().restartApplication(60000, false);
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((str = successResult.readLine()) != null) {
                LogUtils.Companion.print("Success");
                successMsg.append(str);
            }
            while ((str = errorResult.readLine()) != null) {
                LogUtils.Companion.print("Error" + str);
                errorMsg.append(str);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.Companion.print(e.getMessage());
        }
        boolean s = successMsg.toString().equalsIgnoreCase("success");
        LogUtils.Companion.print("s : " + s);
        return successMsg.toString().equalsIgnoreCase("success");
    }

    public static boolean reboot() {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("adb reboot");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    private static boolean returnResult(int value) {
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

    public static void install(String filePath) {
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }
        chmod("777", filePath); //更改文件权限
        String command = "pm install " + "-r " + filePath;
        String com = "chmod 777 " + filePath;
        String start = "am start -n \"com.qiyang.wb_dzzp" +
                "/com.qiyang.wb_dzzp.ui.StartActivity\" -a android.intent.action.MAIN";
        Process process = null;
        DataOutputStream os = null;

        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(com + "\n");
            os.flush();
            Thread.sleep(100);
            os.writeBytes(command + "\n");
            os.flush();
            Thread.sleep(100);;
            os.writeBytes(start + "\n");
            os.flush();
            Thread.sleep(100);
            os.writeBytes("exit\n");
            Thread.sleep(100);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            restartApplication(90000);
        }
    }

    /**
     * 获取权限
     * @param permission 权限
     * @param path       路径
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //重启应用
    public static void restartApplication(long time) {
        Intent mStartActivity = MyApplication.myApplication.getPackageManager()
                .getLaunchIntentForPackage(MyApplication.myApplication.getPackageName());
        int mPendingIntentId = 1234567;
        PendingIntent mPendingIntent = PendingIntent.getActivity(MyApplication.myApplication
                , mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) MyApplication.myApplication.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + time, mPendingIntent);
        System.exit(0);
    }
}
