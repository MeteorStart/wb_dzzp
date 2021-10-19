package com.qiyang.wb_dzzp.network.service

import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.network.http.UrlConstant
import retrofit2.http.*


/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 3/29/21 9:58 AM
 * @company:
 * @email: lx802315@163.com
 */
interface BusService {

    @POST(UrlConstant.REGISTER)
    suspend fun register(@Body body: RegisterBody): BaseBean<RegisterBean>

    @GET(UrlConstant.GET_CONFIG)
    suspend fun getConfig(@Path("regId") regId: String): BaseBean<DeviceConfigBean>

    @POST(UrlConstant.STATION)
    suspend fun station(@Body body: StationBody): BaseBean<StationBean>
}