package com.qiyang.wb_dzzp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 项目名称: com.lx.androidday12
 * 类功能：封装本地存储SharedPreference的 存/取 方法
 * 作者：怜星
 * 时间：2016/9/22 10:09
 */
public class SharedPreferencesUtils {
    public static String PREFERENCES_NAME = "dzzpPrefs";

    public static boolean putArray(Context mContext, Set<String> set) {
        SharedPreferences sp = mContext.getSharedPreferences(PREFERENCES_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("Status_size", set.size());

        List<String> list = new ArrayList(set);

        for (int i = 0; i < list.size(); i++) {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, list.get(i));
        }
        return mEdit1.commit();
    }

    public static Set getArray(Context mContext) {
        Set<String> list = new HashSet<>();
        SharedPreferences mSharedPreference1 = mContext.getSharedPreferences(PREFERENCES_NAME, mContext.MODE_PRIVATE);
        list.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            list.add(mSharedPreference1.getString("Status_" + i, null));
        }
        return list;
    }

    public static void putStringSet(Context context, String key, Set<String> value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putStringSet(key, value);
        ed.commit();
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key, value);
        ed.commit();
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(key, value);
        ed.commit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key, value);
        ed.commit();
    }

    public static void putFloat(Context context, String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putFloat(key, value);
        ed.commit();
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(key, value);
        ed.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getStringSet(key, defValue);
    }

    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getStringSet(key, null);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public static Float getFloat(Context context, String key, float defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static Float getFloat(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, -1.0f);
    }

    public static Boolean getBoolean(Context context, String key, Boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static Long getLong(Context context, String key, Long defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    public static Long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, -1);
    }
}
