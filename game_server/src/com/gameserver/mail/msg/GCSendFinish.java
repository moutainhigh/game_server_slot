package com.gameserver.mail.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 用户发送完邮件 成功之后 返回展示信息
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCSendFinish extends GCMessage{
	
	/** 发送人ID */
	private long sendId;
	/** 发送人名称 */
	private String sendName;
	/** 收件人ID */
	private long receiveId;
	/** 收件人 名称 */
	private String receiveName;
	/** 礼物价值 */
	private long gold;
	/** 赠送时间 */
	private long sendTime;

	public GCSendFinish (){
	}
	
	public GCSendFinish (
			long sendId,
			String sendName,
			long receiveId,
			String receiveName,
			long gold,
			long sendTime ){
			this.sendId = sendId;
			this.sendName = sendName;
			this.receiveId = receiveId;
			this.receiveName = receiveName;
			this.gold = gold;
			this.sendTime = sendTime;
	}

	@Override
	protected boolean readImpl() {
		sendId = readLong();
		sendName = readString();
		receiveId = readLong();
		receiveName = readString();
		gold = readLong();
		sendTime = readLong();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeLong(sendId);
		writeString(sendName);
		writeLong(receiveId);
		writeString(receiveName);
		writeLong(gold);
		writeLong(sendTime);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.GC_SEND_FINISH;
	}
	
	@Override
	public String getTypeName() {
		return "GC_SEND_FINISH";
	}

	public long getSendId(){
		return sendId;
	}
		
	public void setSendId(long sendId){
		this.sendId = sendId;
	}

	public String getSendName(){
		return sendName;
	}
		
	public void setSendName(String sendName){
		this.sendName = sendName;
	}

	public long getReceiveId(){
		return receiveId;
	}
		
	public void setReceiveId(long receiveId){
		this.receiveId = receiveId;
	}

	public String getReceiveName(){
		return receiveName;
	}
		
	public void setReceiveName(String receiveName){
		this.receiveName = receiveName;
	}

	public long getGold(){
		return gold;
	}
		
	public void setGold(long gold){
		this.gold = gold;
	}

	public long getSendTime(){
		return sendTime;
	}
		
	public void setSendTime(long sendTime){
		this.sendTime = sendTime;
	}
}