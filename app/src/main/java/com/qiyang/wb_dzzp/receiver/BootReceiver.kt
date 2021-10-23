package com.qiyang.wb_dzzp.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kk.android.comvvmhelper.utils.LogUtils
import com.qiyang.wb_dzzp.ui.StartActivity

/**
 * @author: X_Meteor
 * @description: 开机广播监听
 * @version: V_1.0.0
 * @date: 2019/4/16 16:26
 * @company:
 * @email: lx802315@163.com
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            val mPendingIntentId = 1234567
            val mPendingIntent = PendingIntent.getActivity(
                context, mPendingIntentId,
                Intent(context, StartActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 30000, mPendingIntent)
            LogUtils.i("Auto Run After 20s")
        }
    }
}