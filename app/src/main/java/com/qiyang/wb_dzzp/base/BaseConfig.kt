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
    val CITY_ID = "310000"

    //城市Name
    val CITY_NAME = "杭州"

    //默认补光灯开启时间
    val DEFUT_OFF_ON_TIME = "05|30|06|00|18|30|20|30"

    //默认运营时间
    val STAND_TIME = "05|00|22|30"

    //默认滚动时间
    val DEFUT_SCOLL_TIME = 30000L
    //默认获取线路信息时间
    val DEFUT_GET_STATION_TIME = 10000L

    //数据正常跳转
    val NORMAL_STATE = 1

    //未匹配站点
    val NO_STATION = 2

    //注册未成功
    val NO_REGISTE = 3

    //网络异常
    val NO_NETWORK = 4

    //log文件最大长度
    val LOG_MAX_BYTES = 102400 * 1024 // 100M averages to a 4000 lines per file

    val MAX_WIDTH = 1600
    val MAX_HEIGHT = 1200

}