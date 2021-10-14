package com.qiyang.wb_dzzp.utils;

import com.jtkj.dzzp_52_screen.utils.AppPrefsUtils;
import com.kk.android.comvvmhelper.utils.LogUtils;
import com.qiyang.wb_dzzp.MyApplication;

import java.io.BufferedReader;
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
            LogUtils.INSTANCE.e("开始升级");
            AppPrefsUtils.INSTANCE.putBoolean("isUpdate", true);
            MyApplication.Companion.getMyApplication().restartApplication(60000, false);
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((str = successResult.readLine()) != null) {
                LogUtils.INSTANCE.i("Success");
                successMsg.append(str);
            }
            while ((str = errorResult.readLine()) != null) {
                LogUtils.INSTANCE.i("Error" + str);
                errorMsg.append(str);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.INSTANCE.i(e.getMessage());
        }
        boolean s = successMsg.toString().equalsIgnoreCase("success");
        LogUtils.INSTANCE.i("s : " + s);
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
}
