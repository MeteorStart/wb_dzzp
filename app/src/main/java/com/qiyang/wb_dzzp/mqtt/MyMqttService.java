package com.qiyang.wb_dzzp.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import com.qiyang.wb_dzzp.UrlConstant;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyMqttService extends Service {


    private String serverUri = UrlConstant.host;
    private String userName = "admin";
    private String passWord = "password";

    private static String publishTopic = "board/onlineStatus/device";      //要发送消息的主题
    private static String deadTopic = "board/will/onlineStatus/device";      //遗嘱的主题


    private static final String TAG = "nlgMqttService";
    private static final String TOPIC_TO_QA = "/s2c/task_quality/";


    private MqttAndroidClient mqttAndroidClient;


    private String subTopic;
    private String pubTopic;
    private String deviceName;


    public MyMqttService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MqttService onCreate executed");

//        String config = SharedPreferencesUtils.getInstance().get(Constance.DEVICE_CONFIG);
//        Gson gson = new Gson();
//        DeviceConfigBean deviceConfigBean = gson.fromJson(config, DeviceConfigBean.class);
//        subTopic = deviceConfigBean.getSubTopic();
//        pubTopic = deviceConfigBean.getPubTopic();
//        deviceName = "deviceConfigBean.getDeviceName()";
        //mqtt服务器的地址
        //新建Client,以设备ID作为client ID
        mqttAndroidClient = new MqttAndroidClient(MyMqttService.this, serverUri, "cliendId");
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                //连接成功
                if (reconnect) {
                    Log.d(TAG, "connectComplete: " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeAllTopics();
                } else {
                    Log.d(TAG, "connectComplete: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                //连接断开
                Log.d(TAG, "connectionLost: connection was lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                //订阅的消息送达，推送notify
                String payload = new String(message.getPayload());
                Log.d(TAG, "Topic: " + topic + " ==> Payload: " + payload);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //即服务器成功delivery消息
            }
        });
        //新建连接设置
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        //断开后，是否自动连接
        mqttConnectOptions.setAutomaticReconnect(true);
        //是否清空客户端的连接记录。若为true，则断开后，broker将自动清除该客户端连接信息
        mqttConnectOptions.setCleanSession(false);
        //设置超时时间，单位为秒
        //mqttConnectOptions.setConnectionTimeout(2);
        //心跳时间，单位为秒。即多长时间确认一次Client端是否在线
        //mqttConnectOptions.setKeepAliveInterval(2);
        //允许同时发送几条消息（未收到broker确认信息）
        //mqttConnectOptions.setMaxInflight(10);
        //选择MQTT版本

        boolean doConnect = true;
        String topic = subTopic;
        Integer qos = 0;
        Boolean retained = false;
        String message = "{\"terminal_uid\":\"" + deviceName + "\"}";
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
            //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
            //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
            try {
                mqttConnectOptions.setWill(deadTopic, message.getBytes(), qos.intValue(), retained.booleanValue());

            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
            }
        }


        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        try {
            Log.d(TAG, "onCreate: Connecting to " + serverUri);
            //开始连接
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: Success to connect to " + serverUri);
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    //成功连接以后开始订阅
                    subscribeAllTopics();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //连接失败
                    Log.d(TAG, "onFailure: Failed to connect to " + serverUri);
                    exception.printStackTrace();
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }

    //订阅所有消息
    private void subscribeAllTopics() {
        subscribeToTopic(TOPIC_TO_QA);
    }

    /**
     * 订阅消息
     */
    public void subscribeToTopic(String subscriptionTopic) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: Success to Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure: Failed to subscribe");
                }
            });
        } catch (MqttException ex) {
            Log.d(TAG, "subscribeToTopic: Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    /**
     * 发布消息
     */
    public void publishMessage(String msg) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());
            mqttAndroidClient.publish(publishTopic, message);
            Log.d(TAG, "publishMessage: Message Published: " + msg);
        } catch (MqttException e) {
            Log.d(TAG, "publishMessage: Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MqttService onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mqttAndroidClient != null) {
                //服务退出时client断开连接
                mqttAndroidClient.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "MqttService onDestroy executed");
    }

}
