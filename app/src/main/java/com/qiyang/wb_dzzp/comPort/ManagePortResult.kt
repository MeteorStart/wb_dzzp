package com.qiyang.wb_dzzp.comPort

import android.util.Log
import com.qiyang.wb_dzzp.comPort.interfaces.SerialPortResult
import com.kongqw.serialportlibrary.DataUtils

/**
 * @author wanglei
 * @date 2022/3/11 11:38
 * @Description:专门处理串口返回数据的类
 */
class ManagePortResult : SerialPortResult {
    override fun result(bytes: ByteArray) {
        Log.e("aaaa", "返回信息：${DataUtils.ByteArrToHex(bytes)}")

    }

}