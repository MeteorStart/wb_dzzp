package com.qiyang.wb_dzzp.ui

import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.base.BaseConfig.STATES_ARRIVE
import com.qiyang.wb_dzzp.base.BaseConfig.STATES_DISPATCH
import com.qiyang.wb_dzzp.base.BaseConfig.STATES_MOVE
import com.qiyang.wb_dzzp.base.BaseConfig.STATES_NEAR
import com.qiyang.wb_dzzp.base.BaseConfig.STATES_NOT_RUN
import com.qiyang.wb_dzzp.base.BaseConfig.STATES_WAIT_DISPATCH
import com.qiyang.wb_dzzp.data.Route

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 10/15/21 2:26 PM
 * @company:
 * @email: lx802315@163.com
 */
class Main2Adapter(layoutResId: Int, data: MutableList<Route>?) :
    BaseQuickAdapter<Route, BaseViewHolder>(layoutResId, data) {

    val list = ArrayList<String>()


    /**
     * @name: 线路详情
     * @param direction  方向：0-上行 1-下行
     * @param endStationName  终点站站名
     * @param endTime  末班时间
     * @param nearestBusInfo    最近一班车情况说明
     * @param price  票价
     * @param routeName    线路名
     * @param routeNum  线路编号
     * @param runFlag  运营状态：true-运营
     * @param startStationName  起点站站名
     * @param startTime  首班时间
     * @param stations  线路下站点信息（已排序）
     * @date: 10/19/21 3:32 PM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    override fun convert(helper: BaseViewHolder?, item: Route?) {
        helper?.let {
            item?.let {
                helper.setText(R.id.tv_bus_name, item.routeName)
                helper.setText(R.id.tv_end_name, item.endStationName)

                val layout = helper.getView<LinearLayout>(R.id.lay_now)

                when (item.realStatus) {
                    STATES_ARRIVE -> {
                        layout.setBackgroundResource(R.drawable.bg_red)
                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.arrive)
                    }
                    STATES_NEAR -> {
                        layout.setBackgroundResource(R.drawable.bg_blue)
                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.near)
                    }
                    STATES_MOVE -> {
                        layout.setBackgroundResource(R.drawable.bg_green)
                        helper.setText(R.id.tv_mode, R.string.move)
                        helper.setText(R.id.tv_content, "${item.distanceStations}站")
                    }
                    STATES_DISPATCH -> {
                        layout.setBackgroundResource(R.drawable.bg_orange)
                        helper.setText(R.id.tv_mode, R.string.dispatch)
                        helper.setText(R.id.tv_content, item.dispatchTime)
                    }
                    STATES_WAIT_DISPATCH -> {
                        layout.setBackgroundResource(R.drawable.bg_gray)
                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.wait_dispatch)
                    }
                    STATES_NOT_RUN -> {
                        layout.setBackgroundResource(R.drawable.bg_gray)
                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.not_run)
                    }
                    else -> {
                        layout.setBackgroundResource(R.drawable.bg_gray)
                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.error)
                    }
                }


            }
        }
    }
}