package com.qiyang.wb_dzzp.network.repository

import com.qiyang.wb_dzzp.data.RegisterBody
import com.qiyang.wb_dzzp.data.StationBody
import com.qiyang.wb_dzzp.network.DzzpNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 3/29/21 9:58 AM
 * @company:
 * @email: lx802315@163.com
 */
class BusRepository {

    companion object {
        val instance: BusRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BusRepository()
        }
    }

    suspend fun register(body: RegisterBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().register(body)
        response
    }

    suspend fun getConfig(regId: String) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().getConfig(regId)
        response
    }

    suspend fun station(body: StationBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().station(body)
        response
    }
}
