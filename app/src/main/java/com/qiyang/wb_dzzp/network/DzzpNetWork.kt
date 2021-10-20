package com.qiyang.wb_dzzp.network

import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.network.http.RetrofitManager
import com.qiyang.wb_dzzp.network.http.UrlConstant
import com.qiyang.wb_dzzp.network.service.BusService
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.QueryMap

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

    //上送硬件数据
    suspend fun extend(@Body body: ExtendBody) = busService.extend(body)

    //上送当前设置
    suspend fun curSet(@Body body: CurSetBody) = busService.curSet(body)

    //上送当前版本号
    suspend fun curVersion(@Body body: CurVersionBody) = busService.curVersion(body)

    //重启
    suspend fun restart(@Body body: RestartBody) = busService.restart(body)

    //上传截图
    suspend fun screenshot(@Body body: UpHeartBody) = busService.screenshot(body)

    //上传日志
    suspend fun logUp(@Body body: UpHeartBody) = busService.logUp(body)

    //获取天气
    suspend fun getWeather(@Body body: GetWeatherBody) = busService.getWeather(body)


    //上传文件
    suspend fun upLoadFile(
        @QueryMap map: MutableMap<String, Any>,
        @Part file: MultipartBody.Part
    ) = busService.upLoadFile(map, file)

}


