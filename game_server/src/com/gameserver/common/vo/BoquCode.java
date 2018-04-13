package com.gameserver.common.vo;

import com.alibaba.fastjson.annotation.JSONField;


public class BoquCode {
	@JSONField(ordinal = 1)
	private String client_id;
	
	@JSONField(ordinal = 2)
	private String message;
	
	@JSONField(ordinal = 3)
	private String mobile;
	
	
	@JSONField(ordinal = 4)
	private String timestamp;
	
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
