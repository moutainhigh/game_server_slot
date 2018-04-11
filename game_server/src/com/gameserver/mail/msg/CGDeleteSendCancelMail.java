package com.gameserver.mail.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.mail.handler.MailHandlerFactory;

/**
 * 客户端请求  取消发送的邮件
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGDeleteSendCancelMail extends CGMessage{
	
	/** 邮件Id列表 */
	private long[] mailIdList;
	
	public CGDeleteSendCancelMail (){
	}
	
	public CGDeleteSendCancelMail (
			long[] mailIdList ){
			this.mailIdList = mailIdList;
	}
	
	@Override
	protected boolean readImpl() {
		short count=0;
		count = readShort();
		count = count < 0 ? 0 : count;
				mailIdList = new long[count];
				for(int i=0; i<count; i++){
			mailIdList[i] = readLong();
		}
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeShort(mailIdList.length);
		for(int i=0; i<mailIdList.length; i++){
			writeLong(mailIdList[i]);
		}
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_DELETE_SEND_CANCEL_MAIL;
	}
	
	@Override
	public String getTypeName() {
		return "CG_DELETE_SEND_CANCEL_MAIL";
	}

	public long[] getMailIdList(){
		return mailIdList;
	}

	public void setMailIdList(long[] mailIdList){
		this.mailIdList = mailIdList;
	}	
		@Override
	public boolean isCollect()
	{
		return true;
	}
	


	@Override
	public void execute() {
		MailHandlerFactory.getHandler().handleDeleteSendCancelMail(this.getSession().getPlayer(), this);
	}
}