package com.qiyang.wb_dzzp

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.os.*
import android.text.TextUtils
import android.util.Log
import com.orhanobut.logger.*
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.base.CrashHandlerUtil
import com.qiyang.wb_dzzp.data.DeviceConfigBean
import com.qiyang.wb_dzzp.utils.FileUtils
import com.qiyang.wb_dzzp.utils.FileUtils.Companion.getSim
import com.qiyang.wb_dzzp.utils.LogUtils
import com.qiyang.wb_dzzp.utils.UpDateUtils
import com.qiyang.wb_dzzp.utils.WriteHandler
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2018/10/26 13:59
 * @company:
 * @email: lx802315@163.com
 */
@Suppress("UNREACHABLE_CODE")
class MyApplication : Application() {

    companion object {
        private val TAG = "X_Meteor"

        lateinit var context: Context

        lateinit var deviceConfigBean: DeviceConfigBean

        @SuppressLint("StaticFieldLeak")
        lateinit var myApplication: MyApplication

        var layoutId = R.layout.activity_main

        var refreshTime = BaseConfig.DEFUT_SCOLL_TIME

        var stationName = ""

        /**
         * @name: 判断当前应用是否是debug状态
         * @description: 方法描述
         * @date: 2018/9/17 15:09
         * @company:
         * @author: Meteor
         */
        fun isApkInDebug(context: Context): Boolean {
            return BuildConfig.DEBUG
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        myApplication = this
        initLogger()
        initCrash()
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }

    //将下载的文件写入本地存储
    fun writeFile2Disk(response: Response<ResponseBody>, file: File?) {
        var currentLength: Long = 0
        var os: OutputStream? = null

        LogUtils.printError("开始写入文件")
        val input = response.body()?.byteStream() //获取下载输入流
        val totalLength = response.body()?.contentLength()

        try {
            os = FileOutputStream(file) //输出流
            var len: Int = -1
            val buff = ByteArray(1024)
            var flag = true
            while (flag) {
                //读取数据，返回下标
                len = input!!.read(buff)
                flag = len != -1
                if (flag) {
                    os!!.write(buff, 0, len)
                    currentLength += len.toLong()
                    //当百分比为100时下载结束，调用结束回调，并传出下载后的本地路径
                    if ((100 * currentLength / totalLength!!).toInt() == 100) {
                        LogUtils.printError("下载完成")
//                        UpDateUtils.pmInstall("/sdcard/update.apk")
                        UpDateUtils.install("/sdcard/update.apk")
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (os != null) {
                try {
                    os!!.close() //关闭输出流
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (input != null) {
                try {
                    input!!.close() //关闭输入流
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    //将下载的文件写入本地存储
    fun writeFile2DiskNoRestart(response: Response<ResponseBody>, file: File?) {
        var currentLength: Long = 0
        var os: OutputStream? = null

        LogUtils.printError("开始写入文件")
        val input = response.body()?.byteStream() //获取下载输入流
        val totalLength = response.body()?.contentLength()

        try {
            os = FileOutputStream(file) //输出流
            var len: Int = -1
            val buff = ByteArray(1024)
            var flag = true
            while (flag) {
                //读取数据，返回下标
                len = input!!.read(buff)
                flag = len != -1
                if (flag) {
                    os!!.write(buff, 0, len)
                    currentLength += len.toLong()
                    //当百分比为100时下载结束，调用结束回调，并传出下载后的本地路径
                    if ((100 * currentLength / totalLength!!).toInt() == 100) {
                        LogUtils.printError("下载完成")
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (os != null) {
                try {
                    os!!.close() //关闭输出流
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (input != null) {
                try {
                    input!!.close() //关闭输入流
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


    private fun initCrash() {
        if (!isApkInDebug(this)) {
            //崩溃处理
            val crashHandlerUtil = CrashHandlerUtil.getInstance()
            crashHandlerUtil.init(this)
            crashHandlerUtil.setCrashTip("很抱歉，程序出现异常，即将退出！")
        }
    }

    /**
     * @description: 初始化log打印
     * @date: 2018/10/10 16:20
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun initLogger() {
        if (isApkInDebug(this)) {
            val preFormaatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(1)
                .methodOffset(1)
                .tag("X_Meteor")
                .build()
            Logger.addLogAdapter(AndroidLogAdapter(preFormaatStrategy))
        }else {
            val sim = FileUtils.getSim()
            val cityCode = BaseConfig.CITY_ID
            //存储位置   sdcard/"城市编号"/"设备id"
            val folder = "sdcard/$cityCode/$sim"
            val ht = HandlerThread("AndroidFileLogger.$folder")
            ht.start()
            val handler: Handler = WriteHandler(
                ht.looper,
                folder,
                BaseConfig.LOG_MAX_BYTES// 10M averages to a 4000 lines per file
            )
            val logStrategy = DiskLogStrategy(handler)
            val dateFormat = SimpleDateFormat("MM.dd HH:mm:ss", Locale.UK)
            val csvFormatStrategy =
                CsvFormatStrategy.newBuilder()
                    .dateFormat(dateFormat)
                    .logStrategy(logStrategy)
                    .tag("X_Meteor")
                    .build()
            Logger.addLogAdapter(DiskLogAdapter(csvFormatStrategy))
            LogUtils.isDebug = false
        }
    }

    /**
     * @name: 重启当前应用
     * @description: 方法描述
     * @date: 2018/9/17 14:59
     * @company:
     * @author: Meteor
     */
    fun restartApplication(time: Long) {
        val mStartActivity =
            applicationContext.packageManager.getLaunchIntentForPackage(packageName)
        val mPendingIntentId = 1234567
        val mPendingIntent =
            PendingIntent.getActivity(
                this,
                mPendingIntentId,
                mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + time, mPendingIntent)
        LogUtils.printError("定时开始")
        killAppProcess()
    }

    /**
     * @name: 重启当前应用
     * @description: 方法描述
     * @date: 2018/9/17 14:59
     * @company:
     * @author: Meteor
     */
    fun restartApplication(time: Long, update: Boolean) {
        val mStartActivity =
            applicationContext.packageManager.getLaunchIntentForPackage(packageName)
        val mPendingIntentId = 1234567
        val mPendingIntent =
            PendingIntent.getActivity(
                this,
                mPendingIntentId,
                mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + time, mPendingIntent)
        LogUtils.printError("定时开始")
//        killAppProcess()
    }

    fun killAppProcess() {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        val mActivityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mList = mActivityManager.runningAppProcesses
        for (runningAppProcessInfo in mList) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid)
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }

    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }
    }
}

