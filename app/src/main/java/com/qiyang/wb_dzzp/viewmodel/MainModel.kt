package com.qiyang.wb_dzzp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.comvvmhelper.extension.repeatLaunch
import com.kk.android.comvvmhelper.extension.safeLaunch
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.base.BaseConfig.DEFUT_GET_STATION_TIME
import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.network.http.SUCESS
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.FileUtils
import kotlinx.coroutines.Job
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 10/18/21 10:30 AM
 * @company:
 * @email: lx802315@163.com
 */
class MainModel constructor(private val busRepository: BusRepository) : ViewModel() {

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

    var repeatJob: Job? = null

    fun getConfigCycle(regId: String, success: (DeviceConfigBean) -> Unit, showError: (String) -> Unit,
                       fail: (String) -> Unit) {
        repeatJob = viewModelScope.repeatLaunch(DEFUT_GET_STATION_TIME, {
            getConfig(regId, success, showError, fail)
        }, 60, DEFUT_GET_STATION_TIME)
    }

    fun getConfig(regId: String, success: (DeviceConfigBean) -> Unit, showError: (String) -> Unit,
                  fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                var result = busRepository.getConfig(regId)
                if (result.code == SUCESS) {
                    repeatJob?.cancel()
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

    fun stationCycle(body: StationBody, success: (StationBean) -> Unit, fail: (String) -> Unit) {
        repeatJob = viewModelScope.repeatLaunch(DEFUT_GET_STATION_TIME, {
            station(body, success, fail)
        }, Int.MAX_VALUE, DEFUT_GET_STATION_TIME)
    }

    fun station(body: StationBody, success: (StationBean) -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val result = busRepository.station(body)
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

    fun upLoadFile(file: File, success: () -> Unit, fail: (String) -> Unit) {
        viewModelScope.safeLaunch {
            block = {
                val sim = FileUtils.getSim()
                val params = HashMap<String, Any>()
                params["cityCode"] = BaseConfig.CITY_ID
                params["devCode"] = sim
                val requestBody: RequestBody = RequestBody.create("image/png".toMediaTypeOrNull(), file)
                val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)
                val result = busRepository.upLoadFile(params, body)
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
}