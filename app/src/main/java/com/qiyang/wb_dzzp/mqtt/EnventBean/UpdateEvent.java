package com.qiyang.wb_dzzp.mqtt.EnventBean;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/8/9 11:14
 * @company:
 * @email: lx802315@163.com
 */
public class UpdateEvent {
    String updateVer = "";

    public UpdateEvent(String updateVer) {
        this.updateVer = updateVer;
    }

    public String getUpdateVer() {
        return updateVer;
    }

    public void setUpdateVer(String updateVer) {
        this.updateVer = updateVer;
    }
}
