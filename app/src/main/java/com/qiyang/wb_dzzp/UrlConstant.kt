package com.qiyang.wb_dzzp

/**
 * Created by xuhao on 2017/11/16.
 */
object UrlConstant {

    //演示环境
//    const val BASE_URL = "http://board.diiing.cn/"

    //正式环境
//    const val BASE_URL = "http://192.168.110.233/"    //正式环境
//    const val host = "tcp://192.168.110.233:1884"    //mqtt 正式
//    const val URL_REX = "http://192.168.110.233:8088/download/"   //正式文件上传前缀
//    const val isDebug = false

    //测试环境
    const val BASE_URL = "https://smartboard-dev.jintdev.com/"    //测试环境
    const val host = "tcp://101.37.34.20:1884"    //mqtt 测试
    const val URL_REX = "http://101.37.34.20:8088/download/"    //测试文件上传前缀
    const val isDebug = true

    //获取线路信息
    const val CURRENT_BUS = "api/boardapp/screen/currentBus"

    //获取线路实时车辆信息
    const val CURRENT_BUS_NO_LINE = "api/boardapp/screen/currentBusNoLine"

    //设备配置信息
    const val DEVICE_CONFIG = "api/boardapp/bus/getDeviceConfig"

    //获取墨水屏基础信息
    const val BASE_CONFIG = "api/boardapp/screen/getBasicData"

    //发送心跳
    const val SEND_NOTICE = "api/web/stop/sendNotice"

    //回传日志地址
    const val UPLOAD_LOG = "api/boardapp/stopmonitor/uploadLog"

    //发送截图地址
    const val SEND_SCREEN = "api/boardapp/stopmonitor/screenCapture"

    //设备注册
    const val REGISTER = "api/boardapp/screen/register"

    //获取虚拟设备编号sim接口
    const val GET_SIM_CODE = "api/boardapp/screen/getSimStopByRegisterId"

    //上传硬件数据
    const val SEND_STOP_MONITOR = "api/boardapp/stopmonitor/saveOrUpdateStopMonitor"

    //上传重启测试
    const val DEVICE_RESET = "api/boardapp/stopmonitor/deviceReset"

    //截图上传单张图片
    const val UPLOAD_FILE = "api/boardapp/uploadFile"

    //获取天气信息
    const val GET_WEATHER_BY_CITY = "api/boardapp/bus/getWeatherByCity"
    //apk下载地址
    const val DOWNLOAD_APK_URL =
        "https://diiing-stopboard.oss-cn-hangzhou.aliyuncs.com/nanchang/upload/biz2/upload.apk"
//    const val DOWNLOAD_APK_URL = "https://diiing-stopboard.oss-cn-hangzhou.aliyuncs.com/test/upload/biz2/upload.apk"

}