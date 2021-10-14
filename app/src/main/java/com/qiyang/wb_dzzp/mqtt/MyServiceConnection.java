package com.qiyang.wb_dzzp.mqtt;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {

    private MQTTService mqttService;
    private IGetMessageCallBack IGetMessageCallBack;
    private IOnLineCallBack iOnLineCallBack;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mqttService = ((MQTTService.CustomBinder)iBinder).getService();
        mqttService.setIGetMessageCallBack(IGetMessageCallBack);
        mqttService.setOnlineStatusCallBack(iOnLineCallBack);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public MQTTService getMqttService(){
        return mqttService;
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack){
        this.IGetMessageCallBack = IGetMessageCallBack;
    }

    public void setIonlineCallBack(IOnLineCallBack ionlineCallBack){
        this.iOnLineCallBack = ionlineCallBack;
    }

}
