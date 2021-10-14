package com.qiyang.wb_dzzp.mqtt;

public class HeartBeats {

    private String deviceName;
    private String status;
    private String time;
    private String lastTime;
    private String utcLastTime;
    private String clientIp;

    @Override
    public String toString() {
        return "HeartBeats{" +
                "deviceName='" + deviceName + '\'' +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", utcLastTime='" + utcLastTime + '\'' +
                ", clientIp='" + clientIp + '\'' +
                '}';
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getUtcLastTime() {
        return utcLastTime;
    }

    public void setUtcLastTime(String utcLastTime) {
        this.utcLastTime = utcLastTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }




}
