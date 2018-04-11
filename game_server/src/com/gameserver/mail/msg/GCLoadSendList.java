package com.gameserver.mail.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 响应客户端请求  自己发送的邮件的列表
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCLoadSendList extends GCMessage{
	
	/** 邮件类型 */
	private int mailKind;
	/** 邮件列表 */
	private com.gameserver.mail.data.MailInfoData[] mailInfoDataList;

	public GCLoadSendList (){
	}
	
	public GCLoadSendList (
			int mailKind,
			com.gameserver.mail.data.MailInfoData[] mailInfoDataList ){
			this.mailKind = mailKind;
			this.mailInfoDataList = mailInfoDataList;
	}

	@Override
	protected boolean readImpl() {
		short count=0;
		mailKind = readInteger();
		count = readShort();
		count = count < 0 ? 0 : count;
		mailInfoDataList = new com.gameserver.mail.data.MailInfoData[count];
		for(int i=0; i<count; i++){
			com.gameserver.mail.data.MailInfoData obj = new com.gameserver.mail.data.MailInfoData();
			obj.setMailId(readLong());
			obj.setSendId(readLong());
			obj.setSendName(readString());
			obj.setReceiveId(readLong());
			obj.setReceiveName(readString());
			obj.setGold(readLong());
			obj.setMailCdTime(readLong());
			obj.setHasAttachment(readInteger());
			obj.setMailStatus(readInteger());
			obj.setMailTitle(readString());
			obj.setMailCreatTime(readLong());
			obj.setIsFriendSend(readInteger());
			obj.setVipLevel(readInteger());
			obj.setHeadName(readString());
			mailInfoDataList[i] = obj;
		}
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeInteger(mailKind);
		writeShort(mailInfoDataList.length);
		for(int i=0; i<mailInfoDataList.length; i++){
			writeLong(mailInfoDataList[i].getMailId());
			writeLong(mailInfoDataList[i].getSendId());
			writeString(mailInfoDataList[i].getSendName());
			writeLong(mailInfoDataList[i].getReceiveId());
			writeString(mailInfoDataList[i].getReceiveName());
			writeLong(mailInfoDataList[i].getGold());
			writeLong(mailInfoDataList[i].getMailCdTime());
			writeInteger(mailInfoDataList[i].getHasAttachment());
			writeInteger(mailInfoDataList[i].getMailStatus());
			writeString(mailInfoDataList[i].getMailTitle());
			writeLong(mailInfoDataList[i].getMailCreatTime());
			writeInteger(mailInfoDataList[i].getIsFriendSend());
			writeInteger(mailInfoDataList[i].getVipLevel());
			writeString(mailInfoDataList[i].getHeadName());
		}
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.GC_LOAD_SEND_LIST;
	}
	
	@Override
	public String getTypeName() {
		return "GC_LOAD_SEND_LIST";
	}

	public int getMailKind(){
		return mailKind;
	}
		
	public void setMailKind(int mailKind){
		this.mailKind = mailKind;
	}

	public com.gameserver.mail.data.MailInfoData[] getMailInfoDataList(){
		return mailInfoDataList;
	}

	public void setMailInfoDataList(com.gameserver.mail.data.MailInfoData[] mailInfoDataList){
		this.mailInfoDataList = mailInfoDataList;
	}	
}