package com.qiyang.wb_dzzp.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import com.google.gson.Gson
import com.jtkj.dzzp_52_screen.utils.AppPrefsUtils
import com.kk.android.comvvmhelper.utils.LogUtils
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.data.DeviceConfigBean
import com.qiyang.wb_dzzp.data.Route
import com.qiyang.wb_dzzp.data.StationBody
import com.qiyang.wb_dzzp.databinding.ActivityMainBinding
import com.qiyang.wb_dzzp.mqtt.*
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.FileHelper
import com.qiyang.wb_dzzp.utils.FileUtils
import com.qiyang.wb_dzzp.utils.RecycleViewUtils
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

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        errorCode = intent.getIntExtra("errorCode", 0)
        super.onCreate(savedInstanceState)
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        NavigationBarStatusBar(this, true)
        mBinding.model = mViewModel
//        initVideoView("")
        initRecy()
        initData()
    }

    private fun initRecy() {
        recy_main.layoutManager =
            RecycleViewUtils.getVerticalLayoutManagerNoDecoration(this, recy_main)
        recy_main.adapter = mMainAdapter
    }

    private fun initData() {
        val sim = FileUtils.getSim() + ""
        val regId = FileUtils.getEquipId() + ""
        when {
            sim.isNotEmpty() -> {
                getStation(sim)
                getWeather(sim)
                restart(sim)
                getConfig(regId)
            }
            regId.isNotEmpty() -> {
                getConfig(regId)
            }
            else -> {
                showErrorMsg("网络异常，请检查网络！")
            }
        }
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
            mMainAdapter.setNewData(it.routes)
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
    private fun getConfig(regId: String) {
        mViewModel.getConfigCycle(regId, {
            tv_error.visibility = View.GONE
            if (it.devCode.isNotEmpty()) {
                FileUtils.saveSim(it.devCode)
                getStation(it.devCode)
            }
            initMqtt(it)
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

    private fun uploadFile(fileName: String) {
        val file = FileHelper().getFile(fileName)
        if (file != null) {
            mViewModel.upLoadFile(file, {

            }, {

            })
        }
    }

    var mediaController: MediaController? = null

    fun initVideoView(url: String) {
        video.pause()
        video.stopPlayback()
        val file = File(url)
        if (file.exists()) {
            video.setVideoPath(url)
        } else {
            video.setVideoPath(
                "android.resource://" + this.packageName.toString() + "/" + R.raw.mov1
            )
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
            LogUtils.e("视频播放错误码$what")
            restartVideo(url)
            true
        })
    }

    private fun restartVideo(url: String) {
        try {
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot ")) //关机
            proc.waitFor()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun notifyVideo() {
        LogUtils.e("暂停视频播放")
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
    }

    var data = ""

    override fun setMessage(message: String?) {
        // 下行数据通知
        try {
            data = message!!
            LogUtils.i("接收推送成功: $data")
            val gson = Gson()
            val pushBean = gson.fromJson(data, PushBean::class.java)
            val type = pushBean.type + ""

            when (type) {
//                //图片下发
//                "picture" -> {
//                    val pushPicEvent =
//                        gson.fromJson(data, PicEventBean::class.java)
//                    EventBus.getDefault().postSticky(pushPicEvent.data)
//                }
//                //截图
//                "screenCapture" -> {
//                    EventBus.getDefault().postSticky(ScreenEvent())
//                }
//                //升级相关
//                "screenUpdate" -> {
//                    LogUtils.print("下发升级程序")
//
//                    val pushBean =
//                        gson.fromJson(data, UpDataBean::class.java)
//
//                    RetrofitManager.create(ConfigService::class.java)
//                        .download(pushBean.data.url)
//                        .compose(SchedulerUtils.ioToMain())
//                        .subscribe({
//                            LogUtils.print("下载成功" + it.message())
//                            var file = File("sdcard/update.apk")
//                            MyApplication.writeFile2Disk(it, file)
//                        }, {
//                            LogUtils.print("下载失败" + it.message)
//                        })
//                }
//                //公告下发
//                "notice" -> {
//                    LogUtils.print("通知下发：${pushBean.data.toString()}")
//                    try {
//                        val noticeBean = gson.fromJson<NoticeBean>(data, NoticeBean::class.java)
//                        val pushNoticeBean = noticeBean.data
//                        EventBus.getDefault().postSticky(pushNoticeBean)
//                    } catch (e: Exception) {
//                        LogUtils.print(e.toString())
//                    }
//                }
//                //布局更改
//                "layout" -> {
//                    val pushLayoutBean =
//                        gson.fromJson(pushBean.data.toString(), PushLayoutBean::class.java)
//                    LogUtils.printError(pushLayoutBean.toString())
//                    if (pushLayoutBean.layoutId == 1) {
//                        SharedPreferencesUtils.putInt(
//                            MyApplication.context,
//                            BaseConfig.LAYOUT_ID,
//                            R.layout.activity_main
//                        )
//                    } else {
//                        SharedPreferencesUtils.putInt(
//                            MyApplication.context,
//                            BaseConfig.LAYOUT_ID,
//                            R.layout.activity_main_2
//                        )
//                    }
//                    EventBus.getDefault().postSticky(Event())
//                }
//                //风扇控制
//                "onOffFan" -> {
//                    val d = pushBean.data.toString()
//                    LogUtils.print("风扇控制$d")
//                    EventBus.getDefault().postSticky(FanConcleEvent(d))
//                }
//                //开关机时间
//                "onOffTime" -> {
//                    val d = pushBean.data.toString()
//                    LogUtils.printError("补光灯时间$d")
//                    EventBus.getDefault().postSticky(LightEvent(d))
//
//                }
//                //运营时间
//                "standbyTime" -> {
//                    val d = pushBean.data.toString()
//                    LogUtils.print("运营时间$d")
//                    EventBus.getDefault().postSticky(StandbyEvent(d))
//                    AppPrefsUtils.putString(BaseConfig.STAND_TIME, d)
//                }
//                //更新线路列表
//                "updateRouteStatus" -> {
//                    LogUtils.print("更新线路")
//                    EventBus.getDefault().postSticky(UpdateRouteStatusEvent())
//                }
//                //底部样式改变
//                "bottom" -> {
//                    try {
//                        val bottomBean = gson.fromJson<BottomBean>(data, BottomBean::class.java)
//                        LogUtils.print("底部状态改变")
//                        EventBus.getDefault().postSticky(bottomBean.data)
//                    } catch (e: Exception) {
//                        LogUtils.print(e.toString())
//                    }
//                }
//                //上传日志信息
//                "uploadLog" -> {
//                    try {
//                        EventBus.getDefault().postSticky(UpLoadLogEvent())
//                    } catch (e: Exception) {
//
//                    }
//                }
//                //测试切换图片
//                "staticPicture" -> {
//                    val pushPicEvent =
//                        gson.fromJson(data, PicEventBean::class.java)
//                    EventBus.getDefault().postSticky(pushPicEvent.data)
//                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.i(e.toString())
        }

    }

    override fun setOnLineStatus(status: String?) {
        LogUtils.i("MQTT连接状态：$status")
    }
}