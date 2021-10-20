package com.qiyang.wb_dzzp.ui

import android.os.Bundle
import android.view.View
import com.qiyang.wb_dzzp.base.BaseConfig
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.data.StationBody
import com.qiyang.wb_dzzp.databinding.ActivityMainBinding
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.FileHelper
import com.qiyang.wb_dzzp.utils.FileUtils
import com.qiyang.wb_dzzp.utils.RecycleViewUtils
import com.qiyang.wb_dzzp.viewmodel.MainModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var dataList = ArrayList<String>()
    val mMainAdapter = MainAdapter(R.layout.item_bus, dataList)

    //错误编号
    private var errorCode: Int = 0

    private val mViewModel: MainModel by lazy {
        MainModel(BusRepository.instance)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        errorCode = intent.getIntExtra("errorCode", 0)
        super.onCreate(savedInstanceState)
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        NavigationBarStatusBar(this, true)
        mBinding.model = mViewModel
        initRecy()
        initData()
    }

    private fun initRecy() {
        recy_main.layoutManager =
            RecycleViewUtils.getVerticalLayoutManagerNoDecoration(this, recy_main)
        recy_main.adapter = mMainAdapter

        for (i in 0..10) {
            dataList.add(i.toString())
        }
        mMainAdapter.notifyDataSetChanged()

    }

    private fun initData() {
        val sim = FileUtils.getSim() + ""
        val regId = FileUtils.getEquipId() + ""
        when {
            sim.isNotEmpty() -> {
                getStation(sim)
                getWeather(sim)
                restart(sim)
            }
            regId.isNotEmpty() -> {
                getConfig(regId)
            }
            else -> {
                showErrorMsg("网络异常，请检查网络！")
            }
        }
    }

    /**
     * @description: 上传重启记录
     * @date: 10/20/21 3:19 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun restart(sim: String) {
        mViewModel.restart(sim) {
            toast(it)
        }
    }

    /**
     * @description: 获取天气
     * @date: 10/20/21 2:52 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun getWeather(sim: String) {
        mViewModel.getWeather(sim) {
            toast(it)
        }
    }

    /**
     * @description: 获取站点实时数据
     * @date: 10/20/21 2:49 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun getStation(sim: String) {
        mViewModel.stationCycle(StationBody(BaseConfig.CITY_ID, sim), {

        }, {

        })
    }

    /**
     * @description: 获取配置信息
     * @date: 10/19/21 3:02 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun getConfig(regId: String) {
        mViewModel.getConfigCycle(regId, {
            tv_error.visibility = View.GONE
            if (it.devCode.isNotEmpty()) {
                FileUtils.saveSim(it.devCode)
                getStation(it.devCode)
            }
        }, {
            showErrorMsg(it)
        }, {
            toast(it)
        })
    }

    /**
     * @description: 显示错误信息
     * @date: 10/19/21 4:37 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private fun showErrorMsg(it: String) {
        tv_error.visibility = View.VISIBLE
        tv_error.text = it
    }

    private fun uploadFile(fileName: String) {
        val file = FileHelper().getFile(fileName)
        if (file != null) {
            mViewModel.upLoadFile(file, {

            }, {

            })
        }
    }
}