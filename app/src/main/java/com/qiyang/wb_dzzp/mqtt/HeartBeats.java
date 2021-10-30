package com.qiyang.wb_dzzp.mqtt;

public class HeartBeats {

    private String time;
    private String cityCode;
    private String devCode;

    @Override
    public String toString() {
        return "HeartBeats{" +
                "time='" + time + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", devCode='" + devCode + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }
}
