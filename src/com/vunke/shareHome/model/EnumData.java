package com.vunke.shareHome.model;

public class EnumData {

	private String enumType ;
	private String enumKey ;
	private String enumValue ;
	private String enumName ;
	
	public EnumData() {}
	
	
	/**
	 *  
	 * @param enumType 列的名字，如：'status'
	 * @param enumName 表的名字    如：'sp_info'
	 */
	public EnumData(String enumType ,String enumName ) {
		this.enumType = enumType ;
		this.enumName = enumName ;
	}
	
	public EnumData(String enumType ,String enumName ,String enumKey) {
		this.enumType = enumType ;
		this.enumName = enumName ;
		this.enumKey = enumKey ;
	}
	
	public String getEnumType() {
		return enumType;
	}
	public void setEnumType(String enumType) {
		this.enumType = enumType;
	}
	public String getEnumKey() {
		return enumKey;
	}
	public void setEnumKey(String enumKey) {
		this.enumKey = enumKey;
	}
	public String getEnumValue() {
		return enumValue;
	}
	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}
	public String getEnumName() {
		return enumName;
	}
	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}
	
	
	
}
