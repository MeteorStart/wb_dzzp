package com.qiyang.wb_dzzp.network.http

/**
 * @author: X_Meteor
 * @description: 错误 CODE 对照表
 * @version: V_1.0.0
 * @date: 2019-11-13 11:22
 * @company:
 * @email: lx802315@163.com
 */
val SUCESS = "0"            // 0-成功
val NO_RETURN_VALUE = "97"            // 无返回值
val REQUEST_TIMEOUT = "98"            // 请求超时
val NO_AVAILABLE_SER = "99"            // 无可用服务
val REQUEST_EER = "100"        // 请求错误
val ILLEGAL_ACCESS = "101"        // 非法访问
val SECURE_KEY_ERR = "102"        // 密钥错误
val USER_NOT_LOGIN = "103"        // 用户未登录
val PARAM_VER_ERR = "104"        // 参数校验失败
val INVALID_TOKEN = "105"        // 无效token
val SERVER_ERR = "106"        // 系统异常
val OBJ_NOT_EXIST = "107"        // 对象不存在
val TOO_FREQUENT = "108"        // 操作频繁
val OTHER_ERROR = "109"        // 其他失败
val OBJ_ALREADY_EXIST = "110"        // 对象已存在
val OUT_SINGIN_SCOPE = "16001"     //不在当前考勤范围内