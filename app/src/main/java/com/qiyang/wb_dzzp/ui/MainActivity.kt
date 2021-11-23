package com.qiyang.wb_dzzp.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.widget.MediaController
import com.google.gson.Gson
import com.qiyang.wb_dzzp.MyApplication
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.allcontrol.ControlDevicesProtocol
import com.qiyang.wb_dzzp.allcontrol.DoorControl
import com.qiyang.wb_dzzp.allcontrol.WaterSensorControl
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.databinding.ActivityMainBinding
import com.qiyang.wb_dzzp.mqtt.*
import com.qiyang.wb_dzzp.mqtt.EnventBean.UpDataEvent
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.*
import com.qiyang.wb_dzzp.utils.FileUtils
import com.qiyang.wb_dzzp.utils.UpDateUtils.restartApplication
import com.qiyang.wb_dzzp.viewmodel.MainModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : BaseActivity<ActivityMainBinding>(), IGetMessageCallBack, IOnLineCallBack {

    var dataList = ArrayList<Route>()
    val mMainAdapter = MainAdapter(R.layout.item_bus, dataList)

    //错误编号
    private var errorCode: Int = 0

    private val mViewModel: MainModel by lazy {
        MainModel(BusRepository.instance)
    }

    private var serviceConnection: MyServiceConnection? = null

    val TEMPERATURE_SHOW = 1
    val HUMITURE_SHOW = 2
    var tempValue = 0f
    var humiValue = 0f

    var myHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> LogUtils.print("门禁结果 ：关门 !")
                1 -> LogUtils.print("门禁结果 ：开门 !")
            }
        }
    }

    var waterSensorHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> LogUtils.print("水浸传感器状态 ：没检测到有水 !")
                1 -> LogUtils.print("水浸传感器状态 ：检测到有水  !")
            }
        }
    }

    var myHumHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TEMPERATURE_SHOW -> {
                    mViewModel.tempValue.value = tempValue.toString()
//                    LogUtils.print("温度：$tempValue°")

                }

                HUMITURE_SHOW -> {
                    mViewModel.humiValue.value = humiValue.toString()
//                    LogUtils.print("湿度：$humiValue％")
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        errorCode = intent.getIntExtra("errorCode", 0)
        super.onCreate(savedInstanceState)
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        NavigationBarStatusBar(this, true)
        mBinding.model = mViewModel
        initRecy()
        initData()
    }

    private fun initRecy() {
        tv_version.text = "版本号：v" + AppUtils.getVerName(this)

        recy_main.layoutManager =
            RecycleViewUtils.getVerticalLayoutManagerNoDecoration(this, recy_main)
        recy_main.adapter = mMainAdapter
    }

    private fun initData() {
        val sim = FileUtils.getSim() + ""
        val regId = FileUtils.getEquipId() + ""
        when {
            sim.isNotEmpty() -> {
                tv_sim.text = sim
                getWeather(sim)
                restart(sim)
                getConfig(regId)
                sendNotice("notice_apk", AppUtils.getVerName(this))
            }
            regId.isNotEmpty() -> {
                getConfig(regId)
            }
            else -> {
                showErrorMsg("网络异常，请检查网络！")
            }
        }

        //判断是否有公告内容
        val notice = SharePreferencesUtils.getString(this, BaseConfig.NOTICE, "")
        if (notice.isNotEmpty()) {
            tv_notice.text = notice
        } else {
            tv_notice.text = BaseConfig.DEFUT_TEXT
        }

        //判断是否有图片内容
        val pic = SharePreferencesUtils.getString(this, BaseConfig.PIC, "")
        if (pic.isNotEmpty()) {
            showPic(pic)
        }else{
            //判断是否有视频内容
            val video = SharePreferencesUtils.getString(this, BaseConfig.VIDEO, "")
            if (video.isNotEmpty()) {
                initVideoView(video)
            } else {
                initVideoView("")
            }
        }

        initDoorIControl()

        initWaterControl()

        initHumidity()
    }

    /**
     * @description: 温湿度传感器
     * @date:  6:54 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun initHumidity() {
        val sm = this.getSystemService(SENSOR_SERVICE) as SensorManager
        val pm = this.getSystemService(POWER_SERVICE) as PowerManager
        pm.isScreenOn
        val temperature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        val humidity = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        sm.registerListener(
            mySensorListener,
            temperature,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sm.registerListener(
            mySensorListener,
            humidity,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private val mySensorListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, degree: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            // 传感器信息改变时执行该方法
            val con: ControlDevicesProtocol = ControlDevicesProtocol.getInstance()
            val msg: Message
            val values = event.values
            if (values.size <= 0) return
            val type = event.sensor.type
            when (type) {
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    tempValue = values[0]
                    msg =
                        myHumHandler.obtainMessage(TEMPERATURE_SHOW)
                    myHumHandler.sendMessage(msg)
                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    humiValue = values[0]
                    msg =
                        myHumHandler.obtainMessage(HUMITURE_SHOW)
                    myHumHandler.sendMessage(msg)
                }
                else -> {
                }
            }
        }
    }

    /**
     * @description: 水浸传感器
     * @date: 11/3/21 5:52 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun initWaterControl() {
        object : WaterSensorControl() {
            override fun waterSensorEvent(open_or_close: Int) {
                val msg: Message
                if (open_or_close == 0) {
                    msg = waterSensorHandler.obtainMessage(0)
                    waterSensorHandler.sendMessage(msg)
                } else if (open_or_close == 1) {
                    msg = waterSensorHandler.obtainMessage(1)
                    waterSensorHandler.sendMessage(msg)
                }
            }
        }.start()
    }

    /**
     * @description: 门控传感器
     * @date: 11/3/21 5:53 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun initDoorIControl() {
        object : DoorControl() {
            override fun doorEvent(open_or_close: Int) {
                val msg: Message
                if (open_or_close == 0) {
                    msg = myHandler.obtainMessage(0)
                    myHandler.sendMessage(msg)
                } else if (open_or_close == 1) {
                    msg = myHandler.obtainMessage(1)
                    myHandler.sendMessage(msg)
                }
            }
        }.start()
    }

    /**
     * @description: 上传重启记录
     * @date: 10/20/21 3:19 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun restart(sim: String) {
        mViewModel.restart(sim) {
            toast(it)
        }
    }

    /**
     * @description: 获取天气
     * @date: 10/20/21 2:52 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun getWeather(sim: String) {
        mViewModel.getWeather(sim) {
            toast(it)
        }
    }

    /**
     * @description: 获取站点实时数据
     * @date: 10/20/21 2:49 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun getStation(sim: String) {
        mViewModel.stationCycle(StationBody(BaseConfig.CITY_ID, sim), {
            if (MyApplication.deviceConfigBean.set.operationTime != null &&
                mViewModel.isStandTime(MyApplication.deviceConfigBean.set.operationTime)
            ) {
                tv_error.visibility = View.GONE
                tv_station_name.text = "当前站点：" + it.station.stationName
                mMainAdapter.setNewData(it.routes)
            } else {
                showErrorMsg("不在运营时间")
            }
        }, {
            if (it.equals("不在运营时间")) {
                showErrorMsg("不在运营时间")
            } else {
                toast(it)
            }
        })
    }

    /**
     * @description: 获取配置信息
     * @date: 10/19/21 3:02 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun getConfig(regId: String) {
        mViewModel.getConfigCycle(regId, {
            tv_error.visibility = View.GONE
            if (it.devCode.isNotEmpty()) {
                FileUtils.saveSim(it.devCode)
                tv_station_name.text = "当前站点：" + it.stationName
                getStation(it.devCode)
                tv_sim.text = it.devCode
                mViewModel.repeatSend(it.devCode, {
                    LogUtils.print("上传硬件数据成功！")
                }, {
                    toast(it)
                })
            }
            initMqtt(it)

        }, {
            showErrorMsg(it)
        }, {
            toast(it)
        })
    }


    /**
     * @description: 获取配置信息
     * @date: 10/19/21 3:02 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun getConfigOne(regId: String) {
        mViewModel.getConfig(regId, {
            if (mViewModel.isStandTime(it.set.operationTime)) {
                tv_error.visibility = View.GONE
            } else {
                showErrorMsg("不在运营时间")
            }
        }, {
            showErrorMsg(it)
        }, {
            toast(it)
        })
    }

    fun initMqtt(configBean: DeviceConfigBean) {
        val intent = Intent(this, MQTTService::class.java)
        serviceConnection = MyServiceConnection()
        serviceConnection?.setIGetMessageCallBack(this)
        serviceConnection?.setIonlineCallBack(this)
        bindService(intent, serviceConnection!!, BIND_AUTO_CREATE)
    }

    /**
     * @description: 显示错误信息
     * @date: 10/19/21 4:37 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun showErrorMsg(it: String) {
        tv_error.visibility = View.VISIBLE
        tv_error.text = it
    }

    var mediaController: MediaController? = null

    fun initVideoView(url: String) {

        video.visibility = View.VISIBLE
        image.visibility = View.GONE

        video.pause()
        video.stopPlayback()
        if (url.isNullOrEmpty()) {
            video.setVideoPath(
                "android.resource://" + this.packageName.toString() + "/" + R.raw.mov1
            )
        } else {
            val file = File(url)
            if (file.exists()) {
                video.setVideoPath(url)
            } else {
                video.setVideoPath(
                    "android.resource://" + this.packageName.toString() + "/" + R.raw.mov1
                )
            }
        }
        mediaController = MediaController(this)
        mediaController?.visibility = View.GONE
        video.setMediaController(mediaController)
        video.requestFocus()
        video.setZOrderMediaOverlay(true)
        video.start()
        video.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start()
            mediaPlayer.isLooping = true
        }
        video.setOnErrorListener(MediaPlayer.OnErrorListener { mediaPlayer, what, extra ->
            LogUtils.printError("视频播放错误码$what")
//            restartVideo(url)
            true
        })
    }

//    private fun restartVideo(url: String) {
//        try {
//            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot ")) //关机
//            proc.waitFor()
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }

    fun notifyVideo() {
        LogUtils.printError("暂停视频播放")
        if (video != null) {
            video.suspend()
            video.stopPlayback()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (video != null) {
            video.suspend()
        }
        if (null != serviceConnection) {
            unbindService(serviceConnection!!)
        }
        LogUtils.print("程序退出！")
        restartApplication(5000)
    }

    var data = ""

    //    日志消息，type=logUp
    //    截图消息，type=screenshot
    //    运营设置消息，type=operationSet
    //    通知消息，type说明：
    //    升级 notice_apk
    //    文字 notice_notice
    //    图片 notice_picture
    //    图片 notice_video
    override fun setMessage(message: String?) {
        // 下行数据通知
        try {
            data = message!!
            LogUtils.print("接收推送成功: $data")
            val gson = Gson()
            val pushBean = gson.fromJson(data, PushBean::class.java)
            val type = pushBean.type + ""

            when (type) {
                //日志下发
                "logUp" -> {
                    logUp()
                }
                //截图下发
                "screenshot" -> {
                    screenShot()
                }
                //升级下发
                "notice_apk" -> {
                    LogUtils.print("下发升级程序")
                    val pushBean =
                        gson.fromJson(data, UpDataEvent::class.java)
                    sendNotice(type, pushBean.data.version)
                    mViewModel.download(pushBean.data.url, {
                        LogUtils.print("下载成功")
                    }, {
                        LogUtils.printError(it)
                    })
                }
                //文字通知下发
                "notice_notice" -> {
                    LogUtils.print("下发文字通知")
                    val pushBean = gson.fromJson(data, UpDataEvent::class.java)
                    if (pushBean.data.version.contains("-1")) {
                        LogUtils.print(pushBean.data.content)
                        tv_notice.text = BaseConfig.DEFUT_TEXT
                        sendNotice(type, pushBean.data.version)
                        SharePreferencesUtils.saveString(
                            this,
                            BaseConfig.NOTICE,
                            ""
                        )
                    } else {
                        if (!pushBean.data.content.isNullOrEmpty()) {
                            SharePreferencesUtils.saveString(
                                this,
                                BaseConfig.NOTICE,
                                pushBean.data.content
                            )
                            LogUtils.print(pushBean.data.content)
                            tv_notice.text = pushBean.data.content
                            sendNotice(type, pushBean.data.version)
                        }
                    }
                }
                //视频下发
                "notice_video" -> {
                    LogUtils.print("下发视频通知")
                    val pushBean = gson.fromJson(data, UpDataEvent::class.java)
                    if (pushBean.data.version.contains("-1")) {
                        SharePreferencesUtils.saveString(this, BaseConfig.VIDEO, "")
                        restartApplication(5000)
                    } else {
                        mViewModel.downloadVideo(pushBean.data.url, {
                            LogUtils.print("下载成功")
                            SharePreferencesUtils.saveString(this, BaseConfig.VIDEO, it)
                            SharePreferencesUtils.saveString(this, BaseConfig.PIC, "")
                            initVideoView(it)
                            sendNotice(type, pushBean.data.version)
                        }, {
                            LogUtils.printError(it)
                        })
                    }
                }
                //设置下发
                "operationSet" -> {
                    getConfigOne(FileUtils.getSim())
                }
                //图片下发
                "notice_picture" -> {
                    LogUtils.print("下发图片通知")
                    val pushBean = gson.fromJson(data, UpDataEvent::class.java)
                    if (pushBean.data.version.contains("-1")) {
                        sendNotice(type, pushBean.data.version)
                        SharePreferencesUtils.saveString(this, BaseConfig.PIC, "")
                        restartApplication(5000)
                    } else {
                        mViewModel.downloadPic(pushBean.data.url, {
                            LogUtils.print("下载成功")
                            SharePreferencesUtils.saveString(this, BaseConfig.PIC, it)
                            SharePreferencesUtils.saveString(this, BaseConfig.VIDEO, "")
                            showPic(it)
                            sendNotice(type, pushBean.data.version)
                        }, {
                            LogUtils.printError(it)
                        })
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.print(e.toString())
        }

    }

    private fun showPic(url: String) {
        var bitmap = BitmapFactory.decodeFile(url)
        if (bitmap != null) {
            image.setImageBitmap(bitmap)
            video.visibility = View.GONE
            image.visibility = View.VISIBLE
            notifyVideo()
        } else {
            toast("图片设置异常，请联系管理员！")
        }
    }

    private fun logUp() {
        LogUtils.printError("日志上传")

        //压缩文件夹
        val cityCode = BaseConfig.CITY_ID
        val sim = FileUtils.getSim()
        ZipUtils.ZipFolder("sdcard/$cityCode/$sim", "sdcard/${sim}_logs.zip")

        val file = File("sdcard/${sim}_logs.zip")

        if (file.exists()) {
            mViewModel.upLoadLogFile(file, {
                LogUtils.print("日志文件上传成功")
                mViewModel.logUp(
                    UpHeartBody(
                        BaseConfig.CITY_ID,
                        FileUtils.getSim() + "",
                        it
                    ), {
                        LogUtils.print("日志地址上传成功！")
                    }, {
                        toast(it)
                    })
            }, {

            })
        }
    }

    override fun setOnLineStatus(status: String?) {
        LogUtils.print("MQTT连接状态：$status")
    }

    var filePath = ""
    var fileName = ""

    fun screenShot() {

        val bitmap = BitmapUtils.createBitmap(
            recy_main.rootView,
            BaseConfig.MAX_WIDTH,
            BaseConfig.MAX_HEIGHT
        )

        if (bitmap != null) {
            try {
                fileName = FileUtils.getSim() + "-" + System.currentTimeMillis()
                BitmapUtils.saveBitmap(bitmap, fileName)
            } catch (e: java.lang.Exception) {

            } finally {
                // 保存图片到SD卡上
                val file = File(
                    Environment.getExternalStorageDirectory(),
                    "$fileName.png"
                )

                filePath = Environment.getExternalStorageDirectory().toString() +
                        "$fileName.png"

                if (file.exists()) {
                    LogUtils.printError(file.path)
                    mViewModel.upLoadFile(file, {
                        LogUtils.print("文件上传成功！")
                        file.delete()
                        mViewModel.screenshot(
                            UpHeartBody(
                                BaseConfig.CITY_ID,
                                FileUtils.getSim() + "",
                                it
                            ), {
                                LogUtils.print("截图地址上传成功！")
                            }, {
                                toast(it)
                            })
                    }, {
                        toast(it)
                    })
                }

            }
        }
    }

    fun sendNotice(type: String, version: String) {
        var curVersionBody: CurVersionBody
        when (type) {
            "notice_apk" -> {
                curVersionBody = CurVersionBody(
                    version,
                    null,
                    BaseConfig.CITY_ID,
                    FileUtils.getSim(),
                    null,
                    null,
                    null
                )
            }
            "notice_notice" -> {
                curVersionBody = CurVersionBody(
                    null,
                    null,
                    BaseConfig.CITY_ID,
                    FileUtils.getSim(),
                    version,
                    null,
                    null
                )
            }
            "notice_picture" -> {
                curVersionBody = CurVersionBody(
                    null,
                    null,
                    BaseConfig.CITY_ID,
                    FileUtils.getSim(),
                    null,
                    version,
                    null
                )
            }
            "notice_video" -> {
                curVersionBody = CurVersionBody(
                    null,
                    null,
                    BaseConfig.CITY_ID,
                    FileUtils.getSim(),
                    null,
                    null,
                    version
                )
            }
            else -> {
                curVersionBody = CurVersionBody(
                    null,
                    null,
                    BaseConfig.CITY_ID,
                    FileUtils.getSim(),
                    null,
                    null,
                    null
                )
            }
        }
        mViewModel.curVersion(curVersionBody, {
            LogUtils.print("回传版本成功：${type + version}")
        }, {
            LogUtils.print("回传版本失败：${type + version} + $it")
        })
    }
}