package com.qiyang.wb_dzzp.ui

import android.os.Bundle
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.databinding.ActivityMainBinding
import com.qiyang.wb_dzzp.utils.RecycleViewUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var dataList = ArrayList<String>()
    val mainAdapter = MainAdapter(R.layout.item_bus, dataList)

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {
        NavigationBarStatusBar(this, true)
        initRecy()
        initData()
    }

    private fun initRecy() {
        recy_main.layoutManager = RecycleViewUtils.getVerticalLayoutManagerNoDecoration(this, recy_main)
        recy_main.adapter = mainAdapter
    }

    private fun initData() {
        for (i in 0..10) {
            dataList.add(i.toString())
        }
        mainAdapter.notifyDataSetChanged()
    }
}