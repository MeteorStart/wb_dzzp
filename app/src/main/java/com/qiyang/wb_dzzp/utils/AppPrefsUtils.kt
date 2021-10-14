package com.jtkj.dzzp_52_screen.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.qiyang.wb_dzzp.MyApplication

/**
 * founter：符乃辉
 * time：2019/1/10
 * email:wizarddev@163.com
 * description:
 */
object AppPrefsUtils {
    private var editor: SharedPreferences.Editor
    private var gson: Gson
    private var sp: SharedPreferences =
        MyApplication.context.getSharedPreferences("dzzpPrefs", Context.MODE_PRIVATE)

    init {
        editor = sp.edit()
        gson = Gson()
    }

    /**
     * 保存键值对数据
     */
    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun putLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }


    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, true)
    }

    fun getLong(key: String): Long {
        return sp.getLong(key, 0)
    }


    /**
     * 根据键获取数据
     */
    fun getValue(key: String): String {
        return sp.getString(key, "")!!.trim { it <= ' ' }
    }

    /**
     * 根据键值删除数据
     */
    fun remove(key: String) {
        editor.remove(key).commit()
    }

    /**
     * 清空数据
     */
    fun getClear() {
        editor.clear().commit()
    }


    /**
     * 针对复杂类型存储<对象>
     *
     */
    fun setObject(key: String, `object`: Any) {
        editor.putString(key, gson!!.toJson(`object`))
        editor.commit()
    }

    /**
     * 取出复杂对象
     *
     */
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        if (sp.contains(key)) {
            return try {
                gson.fromJson(
                    sp.getString(key, "")!!.trim { it <= ' ' },
                    clazz
                )
            } catch (e: Exception) {
                null
            }

        }
        return null
    }


}