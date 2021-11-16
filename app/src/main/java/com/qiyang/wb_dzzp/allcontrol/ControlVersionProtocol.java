package com.qiyang.wb_dzzp.allcontrol;

public class ControlVersionProtocol implements ProtocolBody {
	private   int request_mbody[] = new int[64];
	private   int response_mbody[] = new int[64];
	public static final String TYPE_REQUEST_COMMOND_CODE = "3001";
	public static final String TYPE_RESPONSE_COMMOND_CODE = "3002";
	private String commondCode;
	private String stationID;
	private String terminalSerialNumber;
	private static int configurationFileversion = 1;
	private int RFIDReaderVersion = 1;
	private int busInformationVersion = 2;
	private int governmentInformationVersion = 3;
	private int publicInformationVersion = 4;
	private int roadInformationVersion = 5;
	private int whiteCardList = 6;
	private int volumePolicyVersion = 7;
	private int customizePolicyVersion = 8;
	private int businessPolicyVersion = 9;
	private int logoVersion = 10;
	private int copyrightVersion = 11;
	private int ProgramVersion = 12;
	private int busBasedInformationVersion = 13;
	private int helpPictureVersion = 14;

	private int  serverCode;
	
	

	public static int getConfigurationFileversion() {
		return configurationFileversion;
	}

	public static void setConfigurationFileversion(int configurationFileversion) {
		ControlVersionProtocol.configurationFileversion = configurationFileversion;
	}

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

	public int getRFIDReaderVersion() {
		return RFIDReaderVersion;
	}

	public void setRFIDReaderVersion(int rFIDReaderVersion) {
		RFIDReaderVersion = rFIDReaderVersion;
	}

	public int getBusInformationVersion() {
		return busInformationVersion;
	}

	public void setBusInformationVersion(int busInformationVersion) {
		this.busInformationVersion = busInformationVersion;
	}

	public int getGovernmentInformationVersion() {
		return governmentInformationVersion;
	}

	public void setGovernmentInformationVersion(int governmentInformationVersion) {
		this.governmentInformationVersion = governmentInformationVersion;
	}

	public int getPublicInformationVersion() {
		return publicInformationVersion;
	}

	public void setPublicInformationVersion(int publicInformationVersion) {
		this.publicInformationVersion = publicInformationVersion;
	}

	public int getRoadInformationVersion() {
		return roadInformationVersion;
	}

	public void setRoadInformationVersion(int roadInformationVersion) {
		this.roadInformationVersion = roadInformationVersion;
	}

	public int getWhiteCardList() {
		return whiteCardList;
	}

	public void setWhiteCardList(int whiteCardList) {
		this.whiteCardList = whiteCardList;
	}

	public int getVolumePolicyVersion() {
		return volumePolicyVersion;
	}

	public void setVolumePolicyVersion(int volumePolicyVersion) {
		this.volumePolicyVersion = volumePolicyVersion;
	}

	public int getCustomizePolicyVersion() {
		return customizePolicyVersion;
	}

	public void setCustomizePolicyVersion(int customizePolicyVersion) {
		this.customizePolicyVersion = customizePolicyVersion;
	}

	public int getBusinessPolicyVersion() {
		return businessPolicyVersion;
	}

	public void setBusinessPolicyVersion(int businessPolicyVersion) {
		this.businessPolicyVersion = businessPolicyVersion;
	}

	public int getLogoVersion() {
		return logoVersion;
	}

	public void setLogoVersion(int logoVersion) {
		this.logoVersion = logoVersion;
	}

	public int getCopyrightVersion() {
		return copyrightVersion;
	}

	public void setCopyrightVersion(int copyrightVersion) {
		this.copyrightVersion = copyrightVersion;
	}

	public int getProgramVersion() {
		return ProgramVersion;
	}

	public void setProgramVersion(int programVersion) {
		ProgramVersion = programVersion;
	}

	public int getBusBasedInformationVersion() {
		return busBasedInformationVersion;
	}

	public void setBusBasedInformationVersion(int busBasedInformationVersion) {
		this.busBasedInformationVersion = busBasedInformationVersion;
	}

	public int getHelpPictureVersion() {
		return helpPictureVersion;
	}

	public void setHelpPictureVersion(int helpPictureVersion) {
		this.helpPictureVersion = helpPictureVersion;
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

}
