package com.qiyang.wb_dzzp.comPort

import android.util.Log
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
     * 校验时间
     * @param time String                        时间字符串
     * @param path String                        串口路径
     * @param baudRate Int                       波特率
     * @param serialPortUtils SerialPortUtils    串口工具类
     * @param serialPortResult SerialPortResult  串口结果回调
     */

    fun sendTime(
        time: String,
        path: String,
        baudRate: Int,
        serialPortUtils: SerialPortUtils,
        serialPortResult: SerialPortResult
    ) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateString = formatter.format(Date(time.toLong()))
        val split = dateString.split(" ")
        val begin = split[0]

        val beginDate = begin.split("-")
        val beginYear = beginDate[0]
        val year = UtilsString.getByte(beginYear.substring(beginYear.length - 2, beginYear.length))
        val month = UtilsString.getByte(beginDate[1])
        val day = UtilsString.getByte(beginDate[2])

        val beginTime = split[1]
        val originalTime = beginTime.split(":")
        val hour = UtilsString.getByte(originalTime[0])
        val min = UtilsString.getByte(originalTime[1])
        val sec = UtilsString.getByte(originalTime[2])

        val byte: ByteArray =
            byteArrayOf(
                0x7e,
                0xa1.toByte(),
                0x00,
                year.toByte(),
                month.toByte(),
                day.toByte(),
                hour.toByte(),
                min.toByte(),
                sec.toByte(),
                UtilsString.getBCC2(year, month, day, hour, min, sec).toInt(16).toByte(),
                0x7e
            )

        serialPortUtils.openSerialPort(byte, path, baudRate, serialPortResult)
    }


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