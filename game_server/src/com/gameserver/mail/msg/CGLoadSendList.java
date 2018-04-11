package com.gameserver.mail.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.mail.handler.MailHandlerFactory;

/**
 * 客户端请求 自己发送的邮件的列表
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGLoadSendList extends CGMessage{
	
	/** 邮件类型 */
	private int mailKind;
	
	public CGLoadSendList (){
	}
	
	public CGLoadSendList (
			int mailKind ){
			this.mailKind = mailKind;
	}
	
	@Override
	protected boolean readImpl() {
		mailKind = readInteger();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeInteger(mailKind);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_LOAD_SEND_LIST;
	}
	
	@Override
	public String getTypeName() {
		return "CG_LOAD_SEND_LIST";
	}

	public int getMailKind(){
		return mailKind;
	}
		
	public void setMailKind(int mailKind){
		this.mailKind = mailKind;
	}
		@Override
	public boolean isCollect()
	{
		return true;
	}
	


	@Override
	public void execute() {
		MailHandlerFactory.getHandler().handleLoadSendList(this.getSession().getPlayer(), this);
	}
}