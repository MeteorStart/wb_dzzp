package com.qiyang.wb_dzzp.data.response;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/8/1 13:51
 * @company:
 * @email: lx802315@163.com
 */
public class DeviceConfigBean {

    /**
     * stopName : 师大瑶湖校区
     * stopPinYin : null
     * coordinate : 116.017673,28.677693
     * boardLogo : null
     * boardTitle : null
     * appVersion : null
     * installTime : 02:00
     * appUrl : https://diiing-stopboard.oss-cn-hangzhou.aliyuncs.com/test/upload/biz2/
     * noticeVersion : null
     * videoVersion : null
     * pictureVersion : null
     * deviceName : 360100000001
     * deviceSecret : xdbvp0IvF5Ae6HTaiTLoIKHo8QoKalcz
     * productKey : a1mTmHVLRLp
     * productSecret : komvh5czh5sRRaf5
     * endPoint : https://a1mTmHVLRLp.iot-as-http2.cn-shanghai.aliyuncs.com
     * subTopic : /a1mTmHVLRLp/360100000001/server_to_device,/broadcast/a1mTmHVLRLp/group1
     * pubTopic : /a1mTmHVLRLp/360100000001/device_to_server
     * statePeriod : 5
     * domainAddress :
     * refreshTime : null
     * onOffTime : 00|00|23|59|00|01|23|59
     * sysTime : 1568964668891
     * standbyTime : 00|00|23|59
     * screenVer : 0.0.1
     */

    private String stopName;
    private String stopPinYin;
    private String coordinate;
    private String boardLogo;
    private String boardTitle;
    private String appVersion;
    private String installTime;
    private String appUrl;
    private String noticeVersion;
    private String bottomVersion;
    private String videoVersion;
    private String pictureVersion;
    private String deviceName;
    private String deviceSecret;
    private String productKey;
    private String productSecret;
    private String endPoint;
    private String subTopic;
    private String pubTopic;
    private String statePeriod;
    private String domainAddress;
    private String refreshTime;
    private String onOffTime;
    private String sysTime;
    private String standbyTime;
    private String screenVer;
    private String mainFlag; //1-主屏，2-副屏，空串或null是未设置

    public String getMainFla() {
        return mainFlag;
    }

    public void setMainFla(String mainFla) {
        this.mainFlag = mainFla;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopPinYin() {
        return stopPinYin;
    }

    public void setStopPinYin(String stopPinYin) {
        this.stopPinYin = stopPinYin;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getBoardLogo() {
        return boardLogo;
    }

    public void setBoardLogo(String boardLogo) {
        this.boardLogo = boardLogo;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getNoticeVersion() {
        return noticeVersion;
    }

    public void setNoticeVersion(String noticeVersion) {
        this.noticeVersion = noticeVersion;
    }

    public String getVideoVersion() {
        return videoVersion;
    }

    public void setVideoVersion(String videoVersion) {
        this.videoVersion = videoVersion;
    }

    public String getPictureVersion() {
        return pictureVersion;
    }

    public void setPictureVersion(String pictureVersion) {
        this.pictureVersion = pictureVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public void setProductSecret(String productSecret) {
        this.productSecret = productSecret;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getPubTopic() {
        return pubTopic;
    }

    public void setPubTopic(String pubTopic) {
        this.pubTopic = pubTopic;
    }

    public String getStatePeriod() {
        return statePeriod;
    }

    public void setStatePeriod(String statePeriod) {
        this.statePeriod = statePeriod;
    }

    public String getDomainAddress() {
        return domainAddress;
    }

    public void setDomainAddress(String domainAddress) {
        this.domainAddress = domainAddress;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getOnOffTime() {
        return onOffTime;
    }

    public void setOnOffTime(String onOffTime) {
        this.onOffTime = onOffTime;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getStandbyTime() {
        return standbyTime;
    }

    public void setStandbyTime(String standbyTime) {
        this.standbyTime = standbyTime;
    }

    public String getScreenVer() {
        return screenVer;
    }

    public void setScreenVer(String screenVer) {
        this.screenVer = screenVer;
    }

    public String getBottomVersion() {
        return bottomVersion;
    }

    public void setBottomVersion(String bottomVersion) {
        this.bottomVersion = bottomVersion;
    }
}
