package com.qiyang.wb_dzzp.network.http

/**
 * @author: X_Meteor
 * @description: 接口地址
 * @version: V_1.0.0
 * @date: 2019-11-20 14:26
 * @company:
 * @email: lx802315@163.com
 */

object UrlConstant {

    //测试接口地址
    const val TEST_BASE_URL = "http://120.26.79.180"
    const val host = "tcp://120.26.79.180:"    //mqtt 正式
    const val URL_REX = "http://192.168.110.233:8088/download/"   //正式文件上传前缀
    const val isDebug = true

    //设备注册
    const val REGISTER = "/stationBoard/api/app/device/register"

    //上送当前版本号
    const val CUR_VERSION = "/stationBoard/api/app/device/curVersion"

    //上送当前设置
    const val CUR_SET = "/stationBoard/api/app/device/curSet"

    //获取当前配置
    const val GET_CONFIG = "/stationBoard/api/app/device/config/{regId}"

    //获取实时数据
    const val STATION = "/stationBoard/api/app/device/real/station"

    //上送硬件信息
    const val EXTEND = "/stationBoard/api/app/device/extend"

    //重启接口
    const val RESTART = "/stationBoard/api/app/device/restart"

    //上传文件
    const val UPLOAD = "/stationBoard/api/app/file/upload/ftp"

    //上传文件
    const val SCREENSHOT = "/stationBoard/api/app/device/attach/screenshot"

    //上传文件
    const val LOG_UP = "/stationBoard/api/app/device/attach/logUp"

    //获取天气
    const val GET_WEATHER = "/stationBoard/api/app/weather/getWeather"


}