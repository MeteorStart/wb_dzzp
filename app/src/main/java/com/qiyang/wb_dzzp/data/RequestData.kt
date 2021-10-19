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
 * @description:
 * @date: 10/18/21 1:56 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class CurVersionBody(
    val app: String,
    val bottom: String,
    val cityCode: String,
    val devCode: String,
    val hide: String,
    val notice: String,
    val picture: String,
    val video: String
)

/**
 * @name: 上送设置信息
 * @description:
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
 * @description:
 * @date: 10/19/21 3:26 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
data class StationBody(
    val cityCode: String,
    val devCode: String
)