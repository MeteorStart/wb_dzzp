package com.qiyang.wb_dzzp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kk.android.comvvmhelper.extension.repeatLaunch
import com.kk.android.comvvmhelper.extension.safeLaunch
import com.qiyang.wb_dzzp.BaseConfig
import com.qiyang.wb_dzzp.BaseConfig.DEFUT_GET_STATION_TIME
import com.qiyang.wb_dzzp.data.*
import com.qiyang.wb_dzzp.network.http.SUCESS
import com.qiyang.wb_dzzp.network.repository.BusRepository
import kotlinx.coroutines.Job
import java.util.*
import java.util.concurrent.TimeUnit

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

}