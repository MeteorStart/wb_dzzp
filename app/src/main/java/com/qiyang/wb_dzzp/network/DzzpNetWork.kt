package com.qiyang.wb_dzzp.network

import com.qiyang.wb_dzzp.data.RegisterBody
import com.qiyang.wb_dzzp.data.StationBody
import com.qiyang.wb_dzzp.network.http.RetrofitManager
import com.qiyang.wb_dzzp.network.http.UrlConstant
import com.qiyang.wb_dzzp.network.service.BusService
import retrofit2.http.Body

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019-11-13 18:28
 * @company:
 * @email: lx802315@163.com
 */
class DzzpNetWork {

    val busService = RetrofitManager.create(UrlConstant.TEST_BASE_URL, BusService::class.java)

    companion object {

        private var network: DzzpNetWork? = null

        fun getInstance(): DzzpNetWork {
            if (network == null) {
                synchronized(DzzpNetWork::class.java) {
                    if (network == null) {
                        network = DzzpNetWork()
                    }
                }
            }
            return network!!
        }
    }

    fun clear() {
        network = null
    }

    //打卡
    suspend fun register(body: RegisterBody) = busService.register(body)

    //获取配置信息
    suspend fun getConfig(regId: String) = busService.getConfig(regId)

    //获取实时数据
    suspend fun station(@Body body: StationBody) = busService.station(body)

}


