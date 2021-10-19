package com.qiyang.wb_dzzp.network.http

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kk.android.comvvmhelper.utils.LogUtils
import com.qiyang.wb_dzzp.network.converter.JsonConverterFactory
import com.qiyang.wb_dzzp.utils.GsonUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @name: 网络请求封装
 * @description:
 * @date: 2020/6/12 3:36 PM
 * @author: Meteor
 * @email: lx802315@163.com
 */
object RetrofitManager {

    fun <T> create(baseUrl: String, serviceClass: Class<T>): T =
        getRetrofit(baseUrl).build().create(serviceClass)

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url.newBuilder()
                // Provide your custom parameter here
//                .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
//                .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                // Provide your custom header here
                .method(originalRequest.method, originalRequest.body)
                .addHeader("X-ClientType", "Android")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 获取retrofit的实例
     */
    private fun getRetrofit(baseUrl: String): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(baseUrl)  //自己配置
            .client(getOkHttpClient())
            .addConverterFactory(JsonConverterFactory.create(buildGson()))
            .addConverterFactory(GsonConverterFactory.create(buildGson()))
    }

    /**
     * 设置公共属性
     * */
    private fun getOkHttpClient(): OkHttpClient {
        //添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message ->
                if (GsonUtils.isGoodJson(message)) {
                    LogUtils.json(message)
                } else {
                    Log.i("RetrofitManager", message)
                }
            })
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(addQueryParameterInterceptor())  //参数添加
            .addInterceptor(addHeaderInterceptor()) // token过滤
//            .addInterceptor(addCacheInterceptor())
            .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
            .connectTimeout(300L, TimeUnit.SECONDS)
            .readTimeout(300L, TimeUnit.SECONDS)
            .writeTimeout(300L, TimeUnit.SECONDS)
            .build()
    }

    /**
     * 设置Gson转换器相关属性
     * */
    private fun buildGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            //                // 此处可以添加Gson 自定义TypeAdapter
            //                .registerTypeAdapter(UserInfo.class, new UserInfoTypeAdapter())
            .create()
    }
}
