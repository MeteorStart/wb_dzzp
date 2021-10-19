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
 * @param iotPubTopic 	iot消息发送Topic
 * @param iotSubTopic 	iot消息接收Topic列表
 * @param latitude 	纬度
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
    val iotEndPoint: String,
    val iotProductKey: String,
    val iotProductSecret: String,
    val iotPubTopic: List<Any>,
    val iotSubTopic: List<Any>,
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
    val app: String,
    val bottom: String,
    val hide: String,
    val notice: String,
    val picture: String,
    val video: String
)