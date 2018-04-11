package com.db.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.core.orm.BaseEntity;

/**
 * 用户信息
 * @author Thinker
 */
@Entity
@Table(name = "t_user_info")
public class UserInfo implements BaseEntity<Long>
{
	private static final long serialVersionUID = -6420558996304842663L;

	/** 登陆标识 */
	private long id;
	/** 用户名 */
	private String name;
	/**账号*/
	private String accountId;
	/**facebook id*/
	private String facebookId;
	/** img */
	private String img;
	/** join time*/
	private long joinTime;
	/** 上次登陆时间 */
	private long lastLoginTime;
	/** 权限 */
	private int role;
	/** 锁定状态 */
	private int lockStatus;
	/** 锁定时间 */
	private int muteTime;
	/** 锁定原因 */
	private String muteReason;
	/** 电话 */
	private String phoneNum;
	/** 移动设备Mac */
	private String deviceMac;
	 /*需要同步facebook信息到gameserver*/
	 private boolean updateFbInfo;
	
	@Id
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	@Column
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public long getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}
	@Column
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}


	@Column
	public int getRole() {
		return role;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	@Column
	public int getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}
	@Column
	public int getMuteTime() {
		return muteTime;
	}
	public void setMuteTime(int muteTime) {
		this.muteTime = muteTime;
	}
	@Column
	public String getMuteReason() {
		return muteReason;
	}
	@Column
	public void setMuteReason(String muteReason) {
		this.muteReason = muteReason;
	}

	@Column
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public boolean isUpdateFbInfo() {
		return updateFbInfo;
	}
	public void setUpdateFbInfo(boolean updateFbInfo) {
		this.updateFbInfo = updateFbInfo;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	

}
