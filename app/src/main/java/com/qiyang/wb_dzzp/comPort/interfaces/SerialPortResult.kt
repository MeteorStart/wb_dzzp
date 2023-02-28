package com.qiyang.wb_dzzp.comPort.interfaces

/**
 * @author wanglei
 * @date 2022/3/10 16:13
 * @Description: 串口结果
 */
interface SerialPortResult {
    fun result(bytes: ByteArray)
}