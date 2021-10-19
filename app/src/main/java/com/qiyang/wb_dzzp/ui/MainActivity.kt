package com.qiyang.wb_dzzp.ui

import android.os.Bundle
import android.view.View
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.databinding.ActivityMainBinding
import com.qiyang.wb_dzzp.network.repository.BusRepository
import com.qiyang.wb_dzzp.utils.FileUtils
import com.qiyang.wb_dzzp.utils.RecycleViewUtils
import com.qiyang.wb_dzzp.viewmodel.MainModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

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
        initRecy()
        initData()
    }

    private fun initRecy() {
        recy_main.layoutManager = RecycleViewUtils.getVerticalLayoutManagerNoDecoration(this, recy_main)
        recy_main.adapter = mMainAdapter
    }

    private fun initData() {
        for (i in 0..10) {
            dataList.add(i.toString())
        }
        mMainAdapter.notifyDataSetChanged()

        val sim = FileUtils.getSim() + ""
        val regId = FileUtils.getEquipId() + ""

        if (sim.isNotEmpty()) {

        } else if (regId.isNotEmpty()) {
            mViewModel.getConfigCycle(regId, {
                tv_error.visibility = View.GONE
            }, {
                tv_error.visibility = View.VISIBLE
                tv_error.text = it
            }, {
                toast(it)
            })
        }
    }
}