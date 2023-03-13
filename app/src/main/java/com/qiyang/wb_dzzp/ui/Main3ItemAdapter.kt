package com.qiyang.wb_dzzp.ui

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.vipulasri.timelineview.TimelineView
import com.qiyang.wb_dzzp.R
import com.qiyang.wb_dzzp.data.Station
import com.qiyang.wb_dzzp.view.autoTextView.AutofitTextView

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2022/5/5 5:37 下午
 * @company:
 * @email: lx802315@163.com
 */
class Main3ItemAdapter(layoutResId: Int, data: MutableList<Station>?) :
    BaseQuickAdapter<Station, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: Station?) {
        helper?.let {
            item?.let {

                var tvStationName = helper.getView<AutofitTextView>(R.id.tv_station_name)
                var timeline = helper.getView<TimelineView>(R.id.timeline)

                helper.setText(R.id.tv_station_name, it.stationName)

                if (!it.buses.isNullOrEmpty()) {
                    helper.setVisible(R.id.imv_bus, true)
                } else {
                    helper.setVisible(R.id.imv_bus, false)
                }

                if (it.devStationFlag) {
                    tvStationName.setBackgroundResource(R.drawable.bg_item_station_name)
                    timeline.marker = mContext.resources.getDrawable(R.drawable.icon_arrive)
                } else {
                    tvStationName.setBackgroundResource(R.color.white)
                    timeline.marker = mContext.resources.getDrawable(R.drawable.icon_unarrive)
                }

            }
        }
    }
}