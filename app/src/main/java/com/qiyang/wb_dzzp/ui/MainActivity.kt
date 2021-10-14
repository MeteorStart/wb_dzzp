package com.qiyang.wb_dzzp.ui

import android.os.Bundle
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseActivity
import com.qiyang.wb_dzzp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {
        NavigationBarStatusBar(this, true)
    }
}