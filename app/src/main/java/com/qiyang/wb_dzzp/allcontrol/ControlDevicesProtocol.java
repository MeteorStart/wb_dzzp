package com.qiyang.wb_dzzp.allcontrol;

public class ControlDevicesProtocol implements ProtocolBody {
	private  int request_mbody[] = new int[64];
	private  int response_mbody[] = new int[64];
	public static final String TYPE_REQUEST_COMMOND_CODE = "3001";
	public static final String TYPE_RESPONSE_COMMOND_CODE = "3002";
	private String commondCode;
	private String stationID;
	private String terminalSerialNumber;
	private int configurationFileversion = ControlVersionProtocol.getConfigurationFileversion();
	private int higtTemperatureValue = 1200;
	private int highHumidityValue = 1300;
	private String LCDDisplay = "1";
	private String statusFan = "0";
	private int fanAutoValue = 1400;
	private int managementTime = 1500;
	private int alarmTime = 1600;
	private String statusdoor = "0";
	private int deviceVolume = 1700;
	private String statusLED = "1";
	private static ControlDevicesProtocol controlDevicesProtocol = null;

	private int  serverCode;
	
	public int getServerCode() {
		return serverCode;
	}

	public void setServerCode(int serverCode) {
		this.serverCode = serverCode;
	}

	public String getCommondCode() {
		return commondCode;
	}

	public void setCommondCode(String commondCode) {
		this.commondCode = commondCode;
	}

	public String getStationID() {
		return stationID;
	}

	public void setStationID(String stationID) {
		this.stationID = stationID;
	}

	public String getTerminalSerialNumber() {
		return terminalSerialNumber;
	}

	public void setTerminalSerialNumber(String terminalSerialNumber) {
		this.terminalSerialNumber = terminalSerialNumber;
	}

	public int getConfigurationFileversion() {
		return ControlVersionProtocol.getConfigurationFileversion();
	}

	public void setConfigurationFileversion(int configurationFileversion) {
		ControlVersionProtocol.setConfigurationFileversion(configurationFileversion);
	}

	public int getHigtTemperatureValue() {
		return higtTemperatureValue;
	}

	public void setHigtTemperatureValue(int higtTemperatureValue) {
		this.higtTemperatureValue = higtTemperatureValue;
	}

	public int getHighHumidityValue() {
		return highHumidityValue;
	}

	public void setHighHumidityValue(int highHumidityValue) {
		this.highHumidityValue = highHumidityValue;
	}

	public String getLCDDisplay() {
		return LCDDisplay;
	}

	public void setLCDDisplay(String lCDDisplay) {
		LCDDisplay = lCDDisplay;
	}

	public String getStatusFan() {
		return statusFan;
	}

	public void setStatusFan(String statusFan) {
		this.statusFan = statusFan;
	}

	public int getFanAutoValue() {
		return fanAutoValue;
	}

	public void setFanAutoValue(int fanAutoValue) {
		this.fanAutoValue = fanAutoValue;
	}

	public int getManagementTime() {
		return managementTime;
	}

	public void setManagementTime(int managementTime) {
		this.managementTime = managementTime;
	}

	public int getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(int alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getStatusdoor() {
		return statusdoor;
	}

	public void setStatusdoor(String statusdoor) {
		this.statusdoor = statusdoor;
	}

	public int getDeviceVolume() {
		return deviceVolume;
	}

	public void setDeviceVolume(int deviceVolume) {
		this.deviceVolume = deviceVolume;
	}

	public String getStatusLED() {
		return statusLED;
	}

	public void setStatusLED(String statusLED) {
		this.statusLED = statusLED;
	}

	public  int[] getRequest_mbody() {
		return request_mbody;
	}

	public  void setRequest_mbody(int[] request_mbody) {
		this.request_mbody = request_mbody;
	}

	public  int[] getResponse_mbody() {
		return response_mbody;
	}

	public  void setResponse_mbody(int[] response_mbody) {
		this.response_mbody = response_mbody;
	}

	@Override
	public int[] getBody() {
		// TODO Auto-generated method stub
		if (this.commondCode.equals(TYPE_REQUEST_COMMOND_CODE)) {
			return request_mbody;
		} else if (this.commondCode.equals(TYPE_REQUEST_COMMOND_CODE)) {
			return response_mbody;
		}
		return request_mbody;
	}

	public static ControlDevicesProtocol getInstance() {
		if(controlDevicesProtocol == null)
		{
			controlDevicesProtocol = new ControlDevicesProtocol();
		}
		return controlDevicesProtocol;
	}

}
