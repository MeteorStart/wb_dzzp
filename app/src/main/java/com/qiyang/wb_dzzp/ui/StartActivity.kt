package com.qiyang.wb_dzzp.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.data.CurVersionBody
import com.qiyang.wb_dzzp.databinding.ActivityStartBinding
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.*
import com.qiyang.wb_dzzp.utils.FileUtils.Companion.saveDevId
import com.qiyang.wb_dzzp.utils.FileUtils.Companion.saveEquipId
import com.qiyang.wb_dzzp.utils.FileUtils.Companion.saveTime
import com.qiyang.wb_dzzp.viewmodel.MainModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class StartActivity : BaseActivity<ActivityStartBinding>() , EasyPermissions.PermissionCallbacks {

    companion object {
        const val REQUEST_PERMISSION = 0x01
    }

    private val mViewModel: MainModel by lazy {
        MainModel(BusRepository.instance)
    }

    override fun getLayoutId(): Int = R.layout.activity_start

    override fun initActivity(savedInstanceState: Bundle?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            permission()
        }else{
            val appVersion = AppUtils.getVerName(this)
            //判断是否注册过
            if (TextUtils.isEmpty(FileUtils.getSim())) {
                onRegister()
            } else {
                jumpToMain(BaseConfig.NORMAL_STATE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @AfterPermissionGranted(REQUEST_PERMISSION)
    private fun permission() {
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val appVersion = AppUtils.getVerName(this)
            //判断是否注册过
            if (TextUtils.isEmpty(FileUtils.getSim())) {
                onRegister()
            } else {
                jumpToMain(BaseConfig.NORMAL_STATE)
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    REQUEST_PERMISSION,
                    *perms
                )
                    .setRationale("Dear users\n need to apply for storage Permissions for\n your better use of this application")
                    .setNegativeButtonText("NO")
                    .setPositiveButtonText("YES")
                    .build()
            )
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
            LogUtils.print("设备序列号：$deviceId")

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
        LogUtils.print("当前布局: mainActivity")
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("errorCode", errorCode)
        startActivity(intent)

        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("Granted", "onRequestPermissionsResult:$requestCode")
        if (requestCode == 1) {
            val appVersion = AppUtils.getVerName(this)
            //判断是否注册过
            if (TextUtils.isEmpty(FileUtils.getSim())) {
                onRegister()
            } else {
                jumpToMain(BaseConfig.NORMAL_STATE)
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, @NonNull perms: List<String?>) {
        Log.e("Granted", "onPermissionsGranted:$requestCode:$perms")
    }

    override fun onPermissionsDenied(requestCode: Int, @NonNull perms: List<String?>) {
        Log.e("Denied", "onPermissionsDenied:$requestCode:$perms")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("Tips")
                .setRationale("Dear users, in order to make better use of this application, you need to apply for storage permissions.")
                .setNegativeButton("Refuse")
                .setPositiveButton("Go To Set")
                .setRequestCode(0x001)
                .build()
                .show()
        }
    }
}