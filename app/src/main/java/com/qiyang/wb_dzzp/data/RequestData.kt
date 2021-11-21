package com.qiyang.wb_dzzp.data

import com.google.gson.annotations.SerializedName


/**
 * @author: X_Meteor
 * @description: 设备注册请求类
 * @version: V_1.0.0
 * @date: 10/18/21 1:37 PM
 * @company:
 * @email: lx802315@163.com
 */
data class RegisterBody(
    val cityCode: String,
    val seqCode: String
)

/**
 * @name: 上送版本信息
 * @param apk
 * @param below
 * @param cityCode
 * @param devCode
 * @param notice
 * @param picture
 * @param video
 * @date: 10/18/21 1:56 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class CurVersionBody(
    val apk: String?,
    val below: String?,
    val cityCode: String,
    val devCode: String,
    val notice: String?,
    val picture: String?,
    val video: String?
)


/**
 * @name: 上送设置信息
 * @param cityCode 城市编号
 * @param devCode 设备编号
 * @param hardwareSend 硬件信息上送间隔设置（分钟）
 * @param lightTime 补光灯时间（xx|xx|xx|xx）
 * @param onFanTemp 风扇温度阈值设置（摄氏度）
 * @param operationTime 运营时间（xx|xx|xx|xx）
 * @param refreshTime 刷新间隔（秒）
 * @date: 10/18/21 1:57 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class CurSetBody(
    val cityCode: String,
    val devCode: String,
    val hardwareSend: String,
    val lightTime: String,
    val onFanTemp: String,
    val operationTime: String,
    val refreshTime: String
)

/**
 * @name: 获取实时数据
 * @param cityCode 城市编码
 * @param devCode 设备编号
 * @date: 10/19/21 3:26 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class StationBody(
    val cityCode: String,
    val devCode: String
)

/**
 * @name: 上传硬件信息
 * @param batteryControlTemp 电池控制板温度
 * @param batteryStatus 电池状态,可用值:DeviceExtend.CommonStatus.ABNORMAL,DeviceExtend.CommonStatus.NORMAL
 * @param chargeBattery 充电电池
 * @param chargeI 充电i
 * @param chargeV 充电v
 * @param cityCode 城市编号
 * @param devCode 设备编号
 * @param dischargeI 放电i
 * @param dischargeV 放电v
 * @param evmControlTemp 环境控制板温度
 * @param fanStatus 风扇状态,可用值:DeviceExtend.FanStatus.OFF,DeviceExtend.FanStatus.ON
 * @param flow 流量kb
 * @param humidity 湿度
 * @param inWaterStatus 浸水状态,可用值:DeviceExtend.CommonStatus.ABNORMAL,DeviceExtend.CommonStatus.NORMAL
 * @param mainCycle 主电池循环次数
 * @param mainSoc 主电池电量%
 * @param mainV 主电池v
 * @param secCycle    副电池循环次数
 * @param secSoc    副电池电量%
 * @param secV 副电池v
 * @param temp 温度
 * @param useBattery 使用电池
 * @date: 10/19/21 4:50 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class ExtendBody(
    val batteryControlTemp: Int?,
    val batteryStatus: String?,
    val chargeBattery: String?,
    val chargeI: Int?,
    val chargeV: Int?,
    val cityCode: String?,
    val devCode: String?,
    val dischargeI: Int?,
    val dischargeV: Int?,
    val evmControlTemp: Int?,
    val fanStatus: String?,
    val flow: Int?,
    val humidity: String?,
    val inWaterStatus: String?,
    val mainCycle: Int?,
    val mainSoc: Int?,
    val mainV: Int?,
    val secCycle: Int?,
    val secSoc: Int?,
    val secV: Int?,
    val temp: String?,
    val useBattery: String?
) {
    constructor(cityCode: String?, devCode: String?, humidity: String?, temp: String?,flow: Int?) : this(
        null, null, null,
        null, null, cityCode,
        devCode, null, null,
        null, null, flow,
        humidity, null, null,
        null, null, null,
        null, null, temp, null
    )
}

/**
 * @name: 获取天气
 * @param ammapAdcode 高德城市adcode
 * @param cityCode 城市编号
 * @param devCode 设备编号
 * @date: 10/20/21 2:09 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class GetWeatherBody(
    val ammapAdcode: String,
    val cityCode: String,
    val devCode: String
)

/**
 * @name: 上传文件心跳
 * @param cityCode 城市编号
 * @param devCode 设备编号
 * @param url 文件地址
 * @date: 10/20/21 2:25 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class UpHeartBody(
    val cityCode: String,
    val devCode: String,
    val url: String
)