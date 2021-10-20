package com.qiyang.wb_dzzp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.kk.android.comvvmhelper.utils.LogUtils
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.databinding.ActivityStartBinding
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.AppDateMgr
import com.qiyang.wb_dzzp.utils.AppSysMgr
import com.qiyang.wb_dzzp.utils.FileUtils
import com.qiyang.wb_dzzp.utils.FileUtils.Companion.saveDevId
import com.qiyang.wb_dzzp.utils.FileUtils.Companion.saveEquipId
import com.qiyang.wb_dzzp.utils.FileUtils.Companion.saveTime
import com.qiyang.wb_dzzp.viewmodel.MainModel

class StartActivity : BaseActivity<ActivityStartBinding>() {

    private val mViewModel: MainModel by lazy {
        MainModel(BusRepository.instance)
    }

    override fun getLayoutId(): Int = R.layout.activity_start

    override fun initActivity(savedInstanceState: Bundle?) {

        //判断是否注册过
        if (TextUtils.isEmpty(FileUtils.getSim())) {
            onRegister()
        } else {
            jumpToMain(BaseConfig.NORMAL_STATE)
        }

    }

    /**
     * @description: 设备注册
     * @date: 2019/9/16 14:44
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun onRegister() {
        var deviceId = FileUtils.getDevId()

        if (deviceId == "") {
            var time = FileUtils.getTime()

            if (TextUtils.isEmpty(time)) {
                time = AppDateMgr.getCurrentDayTimeMillis().toString()
                saveTime(time + "")
            }

            deviceId = (AppSysMgr.getSerial()
                    + AppSysMgr.getBluetoothMAC(this).replace(":", "")
                    + "-" + time)

            //本地唯一标示：设备序列号+ 手机唯一标示
            LogUtils.i("设备序列号：$deviceId")

            saveDevId(deviceId)
        }

        registerDev(deviceId)

    }

    //设备注册
    private fun registerDev(seqCode: String) {
        mViewModel.register(seqCode, {
            //注册成功后 跳转到主页
            if (it.regId != 0) {
                saveEquipId(it.regId.toString())
                jumpToMain(BaseConfig.NO_REGISTE)
            } else {
                jumpToMain(BaseConfig.NO_REGISTE)
            }
        }, {

        })

    }

    /**
     * @description: 跳转到首页
     * @date: 2019/9/16 14:45
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun jumpToMain(errorCode: Int) {
        LogUtils.i("当前布局: mainActivity")
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("errorCode", errorCode)
        startActivity(intent)

        finish()
    }
}