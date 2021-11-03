package com.qiyang.wb_dzzp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.comvvmhelper.extension.repeatLaunch
import com.kk.android.comvvmhelper.extension.safeLaunch
import com.qiyang.wb_dzzp.MyApplication
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.base.BaseConfig.DEFUT_GET_STATION_TIME
import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.network.http.SUCESS
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.FileUtils
import com.qiyang.wb_dzzp.utils.LogUtils
import kotlinx.coroutines.Job
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 * @author: X_Meteor
 * @description: 首页ViewModel
 * @version: V_1.0.0
 * @date: 10/18/21 10:30 AM
 * @company:
 * @email: lx802315@163.com
 */
class MainModel constructor(private val busRepository: BusRepository) : ViewModel() {

    val temperature = MutableLiveData<String>()
    val weather = MutableLiveData<String>()

    //循环请求协程
    var repeatJob: Job? = null

    /**
     * @description: 设备注册
     * @date: 10/20/21 3:13 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun register(seqCode: String, success: (RegisterBean) -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.register(RegisterBody(BaseConfig.CITY_ID, seqCode))
                if (result.code == SUCESS) {
                    success(result.data)
                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 循环获取配置信息
     * @date: 10/20/21 3:14 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun getConfigCycle(
        regId: String, success: (DeviceConfigBean) -> Unit, showError: (String) -> Unit,
        fail: (String) -> Unit
    ) {
        repeatJob = viewModelScope.repeatLaunch(DEFUT_GET_STATION_TIME, {
            getConfig(regId, success, showError, fail)
        }, 60, 0)
    }

    /**
     * @description: 获取配置信息
     * @date: 10/20/21 3:14 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun getConfig(
        regId: String, success: (DeviceConfigBean) -> Unit, showError: (String) -> Unit,
        fail: (String) -> Unit
    ) {
        viewModelScope.safeLaunch {
            block = {
                var result = busRepository.getConfig(regId)
                if (result.code == SUCESS) {
                    repeatJob?.cancel()
                    MyApplication.deviceConfigBean = result.data
                    success(result.data)
                } else if (result.code == "400602") {
                    showError(result.msg)
                } else {
                    fail(result.msg)
                }
            }
            onError = {
                repeatJob?.cancel()
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 循环获取线路信息
     * @date: 10/20/21 3:14 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun stationCycle(body: StationBody, success: (StationBean) -> Unit, fail: (String) -> Unit) {
        repeatJob = viewModelScope.repeatLaunch(DEFUT_GET_STATION_TIME, {
            station(body, success, fail)
        }, Int.MAX_VALUE, 0)
    }

    /**
     * @description: 获取线路信息
     * @date: 10/20/21 3:14 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun station(body: StationBody, success: (StationBean) -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.station(body)
                if (result.code == SUCESS) {
                    success(result.data)
                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 上送硬件信息
     * @date: 10/20/21 3:15 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun extend(body: ExtendBody, success: () -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.extend(body)
                if (result.code == SUCESS) {

                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 上传设置信息
     * @date: 10/20/21 3:15 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun curSet(body: CurSetBody, success: () -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.curSet(body)
                if (result.code == SUCESS) {

                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 上传版本信息
     * @date: 10/20/21 3:15 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun curVersion(body: CurVersionBody, success: () -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.curVersion(body)
                if (result.code == SUCESS) {

                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 获取天气
     * @date: 10/20/21 3:15 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun getWeather(sim: String, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val body = GetWeatherBody(BaseConfig.CITY_ID, BaseConfig.CITY_ID, sim)
                val result = busRepository.getWeather(body)
                if (result.code == SUCESS) {
                    temperature.value = result.data.temperature + "°"
                    weather.value = result.data.weather
                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 重启调用
     * @date: 10/20/21 3:15 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun restart(sim: String, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.restart(RestartBody(BaseConfig.CITY_ID, sim))
                if (result.code == SUCESS) {

                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 上传截图信息
     * @date: 10/20/21 3:15 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun screenshot(body: UpHeartBody, success: () -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.screenshot(body)
                if (result.code == SUCESS) {
                    success()
                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    /**
     * @description: 上传日志信息
     * @date: 10/20/21 3:16 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun logUp(body: UpHeartBody, success: () -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.logUp(body)
                if (result.code == SUCESS) {

                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    fun upLoadFile(file: File, success: (String) -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val sim = FileUtils.getSim()
                val params = HashMap<String, Any>()
                params["cityCode"] = BaseConfig.CITY_ID
                params["devCode"] = sim
                val requestBody: RequestBody =
                    RequestBody.create("image/png".toMediaTypeOrNull(), file)
                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", file.name, requestBody)
                val result = busRepository.upLoadFile(params, body)
                if (result.code == SUCESS) {
                    success(result.data.toString())
                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }


    fun upLoadLogFile(file: File, success: (String) -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val sim = FileUtils.getSim()
                val params = HashMap<String, Any>()
                params["cityCode"] = BaseConfig.CITY_ID
                params["devCode"] = sim
                val requestBody: RequestBody =
                    RequestBody.create("application/zip".toMediaTypeOrNull(), file)
                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", file.name, requestBody)
                val result = busRepository.upLoadFile(params, body)
                if (result.code == SUCESS) {
                    success(result.data.toString())
                } else {
                    fail(result.msg)
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }

    fun download(fileUrl: String, success: () -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.download(fileUrl)
                LogUtils.print("1")
                var file = File("sdcard/update.apk")
                MyApplication.myApplication.writeFile2Disk(result, file)
            }
            onError = {

            }
        }
    }

    fun downloadVideo(fileUrl: String, success: (String) -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.download(fileUrl)
                LogUtils.print("1")
                var file = File("sdcard/video.mp4")
                MyApplication.myApplication.writeFile2Disk(result, file)
                if (file.exists()){
                    success("sdcard/video.mp4")
                }else{
                    fail(result.message())
                }
            }
            onError = {
                fail(it.localizedMessage)
            }
        }
    }
}