package com.qiyang.wb_dzzp.comPort

import com.qiyang.wb_dzzp.comPort.interfaces.SerialPortResult
import com.qiyang.wb_dzzp.utils.UtilsString
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author wanglei
 * @date 2022/3/10 16:23
 * @Description: 发送串口数据
 */
object SendPortUtils {

    /**
     * 去获取温湿度数据。
     */
    fun sendTemp(
        path: String,
        baudRate: Int,
        serialPortUtils: SerialPortUtils,
        serialPortResult: SerialPortResult
    ) {
        val byte: ByteArray = byteArrayOf(
            0x01,
            0x03,
            0x00,
            0x00,
            0x00,
            0x02,
            0xc4.toByte(),
            0x0b
        )
        serialPortUtils.openSerialPort(byte, path, baudRate, serialPortResult)
    }

    /**
     * 获取蓝牙信息
     */
    fun sendBluetooth(
        path: String,
        baudRate: Int,
        serialPortUtils: SerialPortUtils,
        serialPortResult: SerialPortResult,
    ){
        val byte=  byteArrayOf(
            0x01,
            0x03,
            0x00,
            0x02,
            0x00,
            0x11
        )
        serialPortUtils.openSerialPort(byte,path,baudRate,serialPortResult)
    }




}