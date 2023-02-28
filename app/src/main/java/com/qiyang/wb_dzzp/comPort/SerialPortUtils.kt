package com.qiyang.wb_dzzp.comPort

import com.kongqw.serialportlibrary.SerialPortManager
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener
import com.qiyang.wb_dzzp.comPort.interfaces.SerialPortResult
import com.qiyang.wb_dzzp.utils.LogUtils
import java.io.File

/**
 * @author wanglei
 * @date 2022/3/10 15:29
 * @Description: 硬件串口对外开放类
 */
class SerialPortUtils {
    private var pathName: String? = null
    private lateinit var serialPortResult: SerialPortResult
    private var baudRate: Int = 0


    private var serialPortManager: SerialPortManager? = null
    private var mOpenSerialPort = false


    fun openSerialPort(
        bytes: ByteArray,
        pathName: String,
        baudRate: Int,
        serialPortResult: SerialPortResult
    ) {
        this.pathName = pathName
        this.baudRate = baudRate
        this.serialPortResult = serialPortResult
        Thread {
            if (mOpenSerialPort) {
                serialPortManager?.sendBytes(bytes)
            } else {
                mOpenSerialPort = startPort()
                serialPortManager?.sendBytes(bytes)
            }
        }.start()

    }

    private fun startPort(): Boolean {
        serialPortManager = SerialPortManager.instance()
        serialPortManager?.setOnSerialPortDataListener(mOnSerialPortDataListener)
        if (serialPortManager?.getmFd() != null) {
            return true
        }
        serialPortManager?.let {
            return it.openSerialPort(File(pathName), baudRate)
        }
        LogUtils.printError("串口启动失败！！！")
        return false
    }


    private val mOnSerialPortDataListener: OnSerialPortDataListener =
        object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray) {
                serialPortResult.result(bytes)
            }

            override fun onDataSent(bytes: ByteArray?) {
            }

        }

    fun closeSerialPort() {
        serialPortManager?.closeSerialPort()
        mOpenSerialPort = false
        serialPortManager = null
    }

    fun openSerialPort(
        pathName: String,
        baudRate: Int,
        serialPortResult: SerialPortResult
    ) {
        this.pathName = pathName
        this.baudRate = baudRate
        this.serialPortResult = serialPortResult
        Thread {
            mOpenSerialPort = startPort()

        }.start()
    }

}