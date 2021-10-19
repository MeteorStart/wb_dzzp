package com.qiyang.wb_dzzp.network.service

import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.network.http.UrlConstant
import okhttp3.MultipartBody
import retrofit2.http.*


/**
 * @author: X_Meteor
 * @description: 接口
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

    @POST(UrlConstant.EXTEND)
    suspend fun extend(@Body body: ExtendBody): BaseBean<Any>

    @POST(UrlConstant.CUR_SET)
    suspend fun curSet(@Body body: CurSetBody): BaseBean<Any>

    @POST(UrlConstant.CUR_VERSION)
    suspend fun curVersion(@Body body: CurVersionBody): BaseBean<Any>

    //上传单张图片
    @Multipart
    @JvmSuppressWildcards
    @POST(UrlConstant.UPLOAD)
    suspend fun upLoadFile(
        @QueryMap map: MutableMap<String, Any>,
        @Part file: MultipartBody.Part
    ): BaseBean<Any>

}