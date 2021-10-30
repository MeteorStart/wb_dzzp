package com.qiyang.wb_dzzp.mqtt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.google.gson.Gson;
import com.kk.android.comvvmhelper.utils.LogUtils;
import com.qiyang.wb_dzzp.MyApplication;
import com.qiyang.wb_dzzp.data.DeviceConfigBean;
import com.qiyang.wb_dzzp.network.http.UrlConstant;
import com.qiyang.wb_dzzp.utils.AppDateMgr;
import com.qiyang.wb_dzzp.utils.SharedPreferencesUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Timer;
import java.util.TimerTask;

public class MQTTService extends Service {

    private MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    //测试
    private String host = UrlConstant.host + MyApplication.deviceConfigBean.getIotEndPoint();
    //正式
//    private String host = "tcp://192.168.110.233:1884";

    private String userName = MyApplication.deviceConfigBean.getIotProductKey();
    private String passWord = MyApplication.deviceConfigBean.getIotProductSecret();

    private static String publishTopic = "board/onlineStatus/device";   //发送心跳topic
    private static String deadTopic = MyApplication.deviceConfigBean.getIotWillTopic(); //遗愿topic

    //private String clientId = "1001000011";//客户端标识

    private IGetMessageCallBack IGetMessageCallBack;
    private DeviceConfigBean deviceConfigBean = MyApplication.deviceConfigBean;

    private IOnLineCallBack iOnLineCallBack;

    private TimerTask timerTask;
    private Timer netWorkTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        doClientConnection();
    }

    public void publish(String msg) {
        Integer qos = 0;
        Boolean retained = false;
        try {
            if (client != null) {
                client.publish(publishTopic, msg.getBytes(), qos.intValue(), retained.booleanValue());
                LogUtils.INSTANCE.i("publish     " + msg);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this, uri, deviceConfigBean.getStationName());
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);
        conOpt = new MqttConnectOptions();
        // 清除缓存
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(60);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());     //将字符串转换为字符串数组

        conOpt.setAutomaticReconnect(false);
        //是否清空客户端的连接记录。若为true，则断开后，broker将自动清除该客户端连接信息
        conOpt.setCleanSession(true);

        boolean doConnect = true;
        //String message = "{\"deviceName\":\"" + "deviceConfigBean.getDeviceName()" + "\"}";
        //String message = "{\"deviceName\":\"" + "deviceConfigBean.getDeviceName()" + ",status:" + "offline"+ "\"}";

        Integer qos = 0;
        Boolean retained = false;
        if ((!initWillMessgae().equals("")) || (!deadTopic.equals(""))) {
            // 最后的遗嘱
            // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
            //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
            //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
            try {
                conOpt.setWill(deadTopic, initWillMessgae().getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
                LogUtils.INSTANCE.i("Exception Occured" + e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }

        if (doConnect) {
            // doClientConnection();
        }
    }

    private String initWillMessgae() {
        WillMessage willMessage = new WillMessage();
        willMessage.setStatus("offline");
        willMessage.setDeviceName(deviceConfigBean.getStationName());
        Gson gson = new Gson();
        return gson.toJson(willMessage);
    }

    public String setHeartBeats(String onlineStatus) {
        HeartBeats beats = new HeartBeats();
        beats.setDevCode(deviceConfigBean.getDevCode());
        beats.setCityCode(deviceConfigBean.getCityCode());
        beats.setTime(AppDateMgr.timeStamp2Date(System.currentTimeMillis() + "", ""));
        return new Gson().toJson(beats);
    }

    @Override
    public void onDestroy() {
        stopSelf();
        try {
            if (null != client) {
                client.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        if (null != timerTask) {
            timerTask.cancel();
        }
        super.onDestroy();
    }

    /**
     * 连接MQTT服务器
     */
    public void doClientConnection() {
        init();
        if (!client.isConnected()) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                try {
                    client.connect(conOpt, null, iMqttActionListener);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }, 3000);
        } else {
            LogUtils.INSTANCE.i("client is connected ");
        }
        isOnlineSqual();
    }

    private void isOnlineSqual() {
        netWorkTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                netWorkIsOk();
            }
        };
        netWorkTimer.schedule(timerTask, 1000 * 60 * 2, 1000 * 60 * 2);
    }

    private void netWorkIsOk() {
        String s = SharedPreferencesUtils.getString(this, "deviceStatus");
        LogUtils.INSTANCE.i("重启" + s);
        if (s.equals("离线")) {
//            init();
            if (!client.isConnected()) {
                try {
                    client.connect(conOpt, null, iMqttActionListener);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                LogUtils.INSTANCE.i("client is connected ");
            }
//            try {
//                Log.e("重启1", "重启执行");
//                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "}); //关机
//                proc.waitFor();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }
    }

    // MQTT是否连接成功
    public IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken arg0) {
            LogUtils.INSTANCE.i("连接成功");
            publish(setHeartBeats("onLine"));
            DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
            disconnectedBufferOptions.setBufferEnabled(true);
            disconnectedBufferOptions.setBufferSize(100);
            disconnectedBufferOptions.setPersistBuffer(false);
            disconnectedBufferOptions.setDeleteOldestMessages(false);
            client.setBufferOpts(disconnectedBufferOptions);
            subTopic();
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            String msg = null == arg1.toString() ? "null" : arg1.toString();
            LogUtils.INSTANCE.i("连接失败：" + msg);
            if (iOnLineCallBack != null) {
                iOnLineCallBack.setOnLineStatus("离线");
                // isSet = false;
            }
            //doClientConnection();
            // 连接失败，重连
            SharedPreferencesUtils.putString(MyApplication.context, "deviceStatus", "离线");
        }
    };

    /**
     * @description: 订阅消息
     * @date: 2020/9/14 9:41 AM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    private void subTopic() {
        try {
            String subTop = deviceConfigBean.getIotSubTopic().get(0);
            client.subscribe(subTop, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogUtils.INSTANCE.i("sub success" + "--->subtopic：" + deviceConfigBean.getIotSubTopic() + "---->clientid：" + deviceConfigBean.getStationName());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // restartApplication(8000);
                    LogUtils.INSTANCE.i("sub fail" + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void restartApplication(long time) {
        Intent mStartActivity = getPackageManager().getLaunchIntentForPackage(getPackageName());
        int mPendingIntentId = 1234567;
        PendingIntent mPendingIntent = PendingIntent.getActivity(MyApplication.context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) MyApplication.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + time, mPendingIntent);
        System.exit(0);
    }

    private boolean isDisConnected = false;
    private boolean isSet = false;
    // MQTT监听并且接受消息
    private MqttCallbackExtended mqttCallback = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            LogUtils.INSTANCE.i("connectComplete" + reconnect);
            if (reconnect) {
                subTopic();
            }
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            String messageString = new String(message.getPayload());

            if (IGetMessageCallBack != null) {
                IGetMessageCallBack.setMessage(messageString);
            }

            String deviveStatus = SharedPreferencesUtils.getString(MyApplication.context, "deviceStatus");

            if (iOnLineCallBack != null && !deviveStatus.equals("在线")) {
                iOnLineCallBack.setOnLineStatus("在线");
            }
            SharedPreferencesUtils.putString(MyApplication.context, "deviceStatus", "在线");

            if (isDisConnected) {
                // publish("断线重连");
                publish(setHeartBeats("onLine"));
                isDisConnected = false;
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            LogUtils.INSTANCE.i("失去连接");
            String message = "{\"deviceName\":\"" + deviceConfigBean.getStationName() + ",status:" + "offline" + "\"}";
            Integer qos = 0;
            Boolean retained = false;
            LogUtils.INSTANCE.i("{\"deviceName\":\"" + deviceConfigBean.getStationName() + "\"}");
            conOpt.setWill(deadTopic, initWillMessgae().getBytes(), qos.intValue(), retained.booleanValue());
            isDisConnected = true;
            if (iOnLineCallBack != null) {
                iOnLineCallBack.setOnLineStatus("离线");
                //isSet = false;
            }

            SharedPreferencesUtils.putString(MyApplication.context, "deviceStatus", "离线");

            if (null != client) {
                Thread thread = new Thread(() -> {
                    try {
                        if (null != client) {
                            client.disconnect();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }
    };

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            LogUtils.INSTANCE.i("MQTT当前网络名称：" + name);
            return true;
        } else {
            LogUtils.INSTANCE.i("MQTT没有可用网络");
            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CustomBinder();
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack) {
        this.IGetMessageCallBack = IGetMessageCallBack;
    }

    public void setOnlineStatusCallBack(IOnLineCallBack iOnLineCallBack) {
        this.iOnLineCallBack = iOnLineCallBack;
    }

    public class CustomBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }

    /**
     * @name: 遗愿消息
     * @description:
     * @date: 2020/9/14 9:43 AM
     * @author: Meteor
     * @email: lx802315@163.com
     */
    class WillMessage {
        private String deviceName;
        private String status;

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
