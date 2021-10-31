package com.qiyang.wb_dzzp.mqtt.EnventBean;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 10/31/21 12:03 PM
 * @company:
 * @email: lx802315@163.com
 */
public class SettingEvent {

    /**
     * type : operationSet
     * data : {"lightTime":"05|00|06|00|18|00|23|59","operationTime":"05|00|23|59"}
     * time : 2021-10-30 17:57:22
     */

    private String type;
    private DataDTO data;
    private String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class DataDTO {
        /**
         * lightTime : 05|00|06|00|18|00|23|59
         * operationTime : 05|00|23|59
         */

        private String lightTime;
        private String operationTime;

        public String getLightTime() {
            return lightTime;
        }

        public void setLightTime(String lightTime) {
            this.lightTime = lightTime;
        }

        public String getOperationTime() {
            return operationTime;
        }

        public void setOperationTime(String operationTime) {
            this.operationTime = operationTime;
        }
    }
}
