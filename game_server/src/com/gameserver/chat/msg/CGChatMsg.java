package com.gameserver.chat.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.chat.handler.ChatHandlerFactory;

/**
 * 聊天
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGChatMsg extends CGMessage{
	
	/** 频道 */
	private int channel;
	/** 接收玩家id */
	private long destRoleUUID;
	/** 内容 */
	private String content;
	
	public CGChatMsg (){
	}
	
	public CGChatMsg (
			int channel,
			long destRoleUUID,
			String content ){
			this.channel = channel;
			this.destRoleUUID = destRoleUUID;
			this.content = content;
	}
	
	@Override
	protected boolean readImpl() {
		channel = readInteger();
		destRoleUUID = readLong();
		content = readString();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeInteger(channel);
		writeLong(destRoleUUID);
		writeString(content);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_CHAT_MSG;
	}
	
	@Override
	public String getTypeName() {
		return "CG_CHAT_MSG";
	}

	public int getChannel(){
		return channel;
	}
		
	public void setChannel(int channel){
		this.channel = channel;
	}

	public long getDestRoleUUID(){
		return destRoleUUID;
	}
		
	public void setDestRoleUUID(long destRoleUUID){
		this.destRoleUUID = destRoleUUID;
	}

	public String getContent(){
		return content;
	}
		
	public void setContent(String content){
		this.content = content;
	}
	


	@Override
	public void execute() {
		ChatHandlerFactory.getHandler().handleChatMsg(this.getSession().getPlayer(), this);
	}
}