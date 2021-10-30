package com.qiyang.wb_dzzp.mqtt;

/**
 * Created by oyl on 2019/3/30.
 */
public class PushBean<T> {

    String type;
    String time;
    T data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
