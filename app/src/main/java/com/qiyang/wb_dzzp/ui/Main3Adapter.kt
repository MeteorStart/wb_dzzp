package com.qiyang.wb_dzzp.ui

import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
import com.qiyang.wb_dzzp.data.Station
import com.qiyang.wb_dzzp.utils.RecycleViewUtils


/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 10/15/21 2:26 PM
 * @company:
 * @email: lx802315@163.com
 */
class Main3Adapter(layoutResId: Int, data: MutableList<Route>?) :
    BaseQuickAdapter<Route, BaseViewHolder>(layoutResId, data) {

    val list = ArrayList<Station>()
    val adapter = Main3ItemAdapter(R.layout.item_bus3_item, list)

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

                helper.setText(R.id.tv_station_start, item.startStationName)
                helper.setText(R.id.tv_station_end, item.endStationName)

                helper.setText(R.id.tv_price, item.price + "元")
                helper.setText(R.id.tv_service_time, "运营时间：" + item.startTime + "-" + item.endTime)

                val layout = helper.getView<TextView>(R.id.tv_bus_name)
                val stationDetail = helper.getView<LinearLayout>(R.id.lay_station_detail)
                val tvContent = helper.getView<TextView>(R.id.tv_content)

                when (item.realStatus) {
                    STATES_ARRIVE -> {
                        layout.setBackgroundResource(R.drawable.bg_red2)
                        stationDetail.setBackgroundResource(R.drawable.bg_red4)

                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.arrive)

                        helper.setTextColor(R.id.tv_mode, Color.parseColor("#C43232"))
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#C43232"))

                        setTextSize(tvContent, 50)
                    }
                    STATES_NEAR -> {
                        layout.setBackgroundResource(R.drawable.bg_red2)
                        stationDetail.setBackgroundResource(R.drawable.bg_red4)

                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.near)

                        helper.setTextColor(R.id.tv_mode, Color.parseColor("#C43232"))
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#C43232"))

                        setTextSize(tvContent, 50)
                    }
                    STATES_MOVE -> {
                        layout.setBackgroundResource(R.drawable.bg_blue3)
                        stationDetail.setBackgroundResource(R.drawable.bg_blue4)

                        helper.setText(R.id.tv_content, R.string.move)
                        helper.setText(R.id.tv_mode, "${item.distanceStations}站")

                        setTextSize(tvContent, 20)
                        helper.setTextColor(R.id.tv_mode, Color.parseColor("#2666D5"))
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#2666D5"))


                    }
                    STATES_DISPATCH -> {
                        layout.setBackgroundResource(R.drawable.bg_orange2)
                        stationDetail.setBackgroundResource(R.drawable.bg_orange4)

                        helper.setText(R.id.tv_mode, R.string.dispatch)
                        helper.setText(R.id.tv_content, item.dispatchTime)

                        setTextSize(tvContent, 50)
                        helper.setTextColor(R.id.tv_mode, Color.parseColor("#F49500"))
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#F49500"))

                    }
                    STATES_WAIT_DISPATCH -> {
                        layout.setBackgroundResource(R.drawable.bg_gray2)
                        stationDetail.setBackgroundResource(R.drawable.bg_gray4)

                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.wait_dispatch)

                        setTextSize(tvContent, 50)
                        helper.setTextColor(R.id.tv_mode, Color.parseColor("#666666"))
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#666666"))

                    }
                    STATES_NOT_RUN -> {
                        layout.setBackgroundResource(R.drawable.bg_gray2)
                        stationDetail.setBackgroundResource(R.drawable.bg_gray4)

                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.not_run)

                        setTextSize(tvContent, 50)
                        helper.setTextColor(R.id.tv_mode, Color.parseColor("#666666"))
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#666666"))

                    }
                    else -> {
                        layout.setBackgroundResource(R.drawable.bg_gray2)
                        stationDetail.setBackgroundResource(R.drawable.bg_gray4)

                        helper.setGone(R.id.tv_mode, false)
                        helper.setText(R.id.tv_content, R.string.error)

                        setTextSize(tvContent, 50)
                        helper.setTextColor(R.id.tv_mode, Color.parseColor("#666666"))
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#666666"))
                    }
                }

                val stationRecy = helper.getView<RecyclerView>(R.id.recy_station)

                stationRecy.layoutManager =
                    RecycleViewUtils.getVerticalGridLayoutManager(
                        mContext,
                        item.stations.size,
                        stationRecy
                    )
                adapter.setNewData(it.stations)
                stationRecy.adapter = adapter


            }
        }
    }

    private fun setTextSize(view: TextView, textSize: Int) {
        view.textSize = textSize.toFloat()
    }
}