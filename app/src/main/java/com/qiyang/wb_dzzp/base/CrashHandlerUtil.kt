package com.qiyang.wb_dzzp.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.qiyang.wb_dzzp.utils.LogUtils
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/4/17 9:28
 * @company:
 * @email: lx802315@163.com
 */
class CrashHandlerUtil : Thread.UncaughtExceptionHandler {
    val TAG = "CrashHandlerUtil"

    //系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    //程序的Context对象
    private var mContext: Context? = null
    //用来存储设备信息和异常信息
    private var infos = HashMap<String, String>()

    //用于格式化日期,作为日志文件名的一部分
    private val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA)
    private var crashTip = "很抱歉，程序出现异常，即将退出！"

    fun getCrashTip(): String {
        return crashTip
    }

    fun setCrashTip(crashTip: String) {
        this.crashTip = crashTip
    }

    companion object {
        //CrashHandler实例
        private val INSTANCE = CrashHandlerUtil()

        /**
         * 获取CrashHandler实例 ,单例模式
         *
         * @return 单例
         */
        fun getInstance(): CrashHandlerUtil {
            return INSTANCE
        }
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        mContext = context
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * @param thread 线程
     * @param ex     异常
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            LogUtils.printError(ex.message!!)
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                LogUtils.printError(e.toString())
                Log.e(TAG, "error : ", e)
                e.printStackTrace()
            }

            //退出并重启程序
//            exitApplication()
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param throwable 异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(throwable: Throwable?): Boolean {
        if (throwable == null) {
            return false
        }
        //使用Toast来显示异常信息
        object : Thread() {
            override fun run() {
                Looper.prepare()
                throwable.printStackTrace()
                Toast.makeText(mContext, getCrashTip(), Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        }.start()
        //收集设备参数信息
        collectDeviceInfo(mContext)
        //保存日志文件
        saveCrashInfo2File(throwable)
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx 上下文
     */
    fun collectDeviceInfo(ctx: Context?) {
        try {
            val pm = ctx!!.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                infos.put("versionName", versionName)
                infos.put("versionCode", versionCode)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occured when collect package info", e)
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos.put(field.name, field.get(null).toString())
                Log.d(TAG, field.name + " : " + field.get(null))
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }

        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 异常
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable): String? {

        val sb = StringBuffer()
        for (entry in infos.entries) {
            val key = entry.key
            val value = entry.value
            sb.append(key + "=" + value + "\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatter.format(Date())
            val fileName = "crash-$time-$timestamp.log"
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                val path = Environment.getExternalStorageDirectory().getPath() + "/crash/"
                val dir = File(path)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val fos = FileOutputStream(path + fileName)
                fos.write(sb.toString().toByteArray())
                fos.close()
            }
            return fileName
        } catch (e: Exception) {
            Log.e(TAG, "an error occured while writing file...", e)
        }

        return null
    }
}

