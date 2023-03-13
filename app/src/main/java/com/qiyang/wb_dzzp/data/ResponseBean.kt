package com.qiyang.wb_dzzp.data

import com.google.gson.annotations.SerializedName


/**
 * @author: X_Meteor
 * @description: 返回数据基类
 * @version: V_1.0.0
 * @date: 10/18/21 1:38 PM
 * @email: lx802315@163.com
 */
data class BaseBean<T>(
    val code: String,
    val data: T,
    val msg: String,
    val total: Int
)

/**
 * @name: 注册返回实体类
 * @date: 10/18/21 1:42 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class RegisterBean(
    val regId: Int,
    val seqCode: String
)

/**
 * @name: 配置信息实体类
 * @param cityCode 城市编号
 * @param devCode 设备code
 * @param direction 方向 0-上行 1-下行
 * @param iotEndPoint iot接入点地址
 * @param iotProductKey iot登录key
 * @param iotProductSecret iot登录secret
 * @param iotPubTopic    iot消息发送Topic
 * @param iotSubTopic    iot消息接收Topic列表
 * @param latitude    纬度
 * @param longitude 经度
 * @param regId 主板id
 * @param state 状态
 * @param stationName 站点名称
 * @param stationNum 站点编号
 * @param time 基础配置获取时间
 * @date: 10/18/21 1:59 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class DeviceConfigBean(
    val cityCode: String,
    val curSet: CurSet,
    val curVersion: CurVersion,
    val devCode: String,
    val direction: Int,
    val iotClientId: String,
    val iotEndPoint: String,
    val iotOnlineTopic: String,
    val iotWillTopic: String,
    val iotProductKey: String,
    val iotProductSecret: String,
    val iotPubTopic: List<String>,
    val iotSubTopic: List<String>,
    val latitude: String,
    val longitude: String,
    val regId: Int,
    val set: Set,
    val state: Int,
    val stationName: String,
    val stationNum: String,
    val time: String,
    val version: Version
)

/**
 * @name: 当前设置
 * @description:
 * @date: 10/18/21 1:59 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class CurSet(
    val hardwareSend: String,
    val lightTime: String,
    val onFanTemp: String,
    val operationTime: String,
    val refreshTime: String
)

/**
 * @name: 当前通知版本
 * @description:
 * @date: 10/18/21 1:59 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class CurVersion(
    val app: String,
    val bottom: String,
    val hide: String,
    val notice: String,
    val picture: String,
    val video: String
)

/**
 * @name: 设置
 * @description:
 * @date: 10/18/21 1:59 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class Set(
    val hardwareSend: String,
    val lightTime: String,
    val onFanTemp: String,
    val operationTime: String,
    val refreshTime: String
)

/**
 * @name: 通知版本
 * @description:
 * @date: 10/18/21 2:00 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class Version(
    val apk: String,
    val below: String,
    val cityCode: String,
    val devCode: String,
    val notice: String,
    val picture: String,
    val video: String
)

/**
 * @name: 实时数据
 * @param devCode 设备编号
 * @param routes 线路列表
 * @param station 当前站点信息
 * @param status 设备状态
 * @date: 10/19/21 3:30 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class StationBean(
    val devCode: String,
    val routes: List<Route>,
    val station: Station,
    val status: String
)

/**
 * @name: 线路详情
 * @param direction  方向：0-上行 1-下行
 * @param endStationName  终点站站名
 * @param endTime  末班时间
 * @param price  票价
 * @param routeName    线路名
 * @param routeNum  线路编号
 * @param runFlag  运营状态：true-运营
 * @param startStationName  起点站站名
 * @param startTime  首班时间
 * @param stations  线路下站点信息（已排序）
 * @param dispatchTime  发车时间
 * @param distanceStations  距离本站n站
 * @param realStatus  实时数据状态
 * @date: 10/19/21 3:32 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class Route(
    val direction: String,
    val dispatchTime: String,
    val distanceStations: Int,
    val endStationName: String,
    val endTime: String,
    val price: String,
    val realStatus: String,
    val routeName: String,
    val routeNum: String,
    val runFlag: Boolean,
    val startStationName: String,
    val startTime: String,
    var stations: ArrayList<Station>
)
/**
 * @name: 线路下站点信息（已排序）
 * @param buses  站点下车辆列表
 * @param devStationFlag  是否当前站点：true-是
 * @param stationName  站点名称
 * @param stationNum  站点编号
 * @date: 10/19/21 3:34 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class Station(
    val buses: ArrayList<Buse>,
    val devStationFlag: Boolean,
    val stationName: String,
    val stationNum: String
)

/**
 * @param busNum 车辆编号
 * @date: 10/19/21 3:36 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class Buse(
    val busNum: String
)

/**
 * @name: 设备重启
 * @param cityCode 城市编号
 * @param devCode 设备编号
 * @date: 10/20/21 2:37 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class RestartBody(
    val cityCode: String,
    val devCode: String
)

/**
 * @name: 天气数据
 * @param adcode 区域编码
 * @param city 城市名
 * @param humidity 空气湿度
 * @param province 省份名
 * @param reporttime 数据发布的时间
 * @param temperature 实时气温（摄氏度）
 * @param weather 天气现象（汉字描述）
 * @param winddirection 风向描述
 * @param windpower 风力级别（级）
 * @date: 10/20/21 2:21 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class WeatherBean(
    val adcode: String,
    val city: String,
    val humidity: String,
    val province: String,
    val reporttime: String,
    val temperature: String,
    val weather: String,
    val winddirection: String,
    val windpower: String
)

