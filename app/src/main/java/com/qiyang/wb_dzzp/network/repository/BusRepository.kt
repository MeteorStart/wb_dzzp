package com.qiyang.wb_dzzp.network.repository

import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.network.DzzpNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.QueryMap

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

    suspend fun extend(body: ExtendBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().extend(body)
        response
    }

    suspend fun curSet(body: CurSetBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().curSet(body)
        response
    }

    suspend fun curVersion(body: CurVersionBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().curVersion(body)
        response
    }

    suspend fun getWeather(body: GetWeatherBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().getWeather(body)
        response
    }

    suspend fun restart(body: RestartBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().restart(body)
        response
    }

    suspend fun screenshot(body: UpHeartBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().screenshot(body)
        response
    }

    suspend fun logUp(body: UpHeartBody) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().logUp(body)
        response
    }

    suspend fun upLoadFile(
        map: MutableMap<String, Any>,
        file: MultipartBody.Part
    ) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().upLoadFile(map, file)
        response
    }

    suspend fun download(fileUrl: String) = withContext(Dispatchers.IO) {
        val response = DzzpNetWork.getInstance().download(fileUrl)
        response
    }
}
