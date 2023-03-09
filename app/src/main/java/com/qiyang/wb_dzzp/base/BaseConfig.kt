package com.qiyang.wb_dzzp.base

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/8/5 11:36
 * @company:
 * @email: lx802315@163.com
 */
object BaseConfig {

    //城市ID
    const val CITY_ID = "310000"

    //城市Name
    const val CITY_NAME = "上海"

    //默认补光灯开启时间
    const val DEFUT_OFF_ON_TIME = "05|30|06|00|18|30|20|30"

    //默认运营时间
    const val STAND_TIME = "05|00|22|30"

    //上次上传的流量值
    val OLD_UP_TRAFFIC = "oldUpTraffic"

    //默认滚动时间
    const val DEFUT_SCOLL_TIME = 1000L * 30

    //默认获取线路信息时间
    const val DEFUT_GET_STATION_TIME = 1000L * 10

    //默认上传硬件时间
    const val DEFUT_SEND_STATION_TIME = 1000L * 40

    //数据正常跳转
    const val NORMAL_STATE = 1

    //未匹配站点
    const val NO_STATION = 2

    //注册未成功
    const val NO_REGISTE = 3

    //网络异常
    const val NO_NETWORK = 4

    //log文件最大长度
    const val LOG_MAX_BYTES = 102400 * 1024 // 100M averages to a 4000 lines per file

    const val MAX_WIDTH = 1920
    const val MAX_HEIGHT = 1080

    //    ARRIVE：到站
    const val STATES_ARRIVE = "ARRIVE"

    //    NEAR：即将到站
    const val STATES_NEAR = "NEAR"

    //    MOVE：距离n站
    const val STATES_MOVE = "MOVE"

    //    DISPATCH：有排班时间
    const val STATES_DISPATCH = "DISPATCH"

    //    NO_DISPATCH：无车无排班
    const val STATES_WAIT_DISPATCH = "WAIT_DISPATCH"

    //    NOT_RUN：非运营
    const val STATES_NOT_RUN = "NOT_RUN"

    const val DEFUT_TEXT = "欢迎使用公交电子站牌！"

    const val NOTICE = "NOTICE"

    const val PIC = "PIC"

    const val VIDEO = "VIDEO"

    //晴
    const val WEATHER_TYPE_QING = 1
    //阴
    const val WEATHER_TYPE_YIN = 2
    //多云
    const val WEATHER_TYPE_DUO_YUN = 3
    //雨
    const val WEATHER_TYPE_YU = 4
    //雪
    const val WEATHER_TYPE_XUE = 5

    const val COM_TTYS1 = "/dev/ttyS1"

    const val BAUDRATE_9600 = 9600

}