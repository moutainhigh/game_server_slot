package com.gameserver.club.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 设置俱乐部
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCClubEdit extends GCMessage{
	
	/** 类型 0 成功 1 失败 */
	private int ret;

	public GCClubEdit (){
	}
	
	public GCClubEdit (
			int ret ){
			this.ret = ret;
	}

	@Override
	protected boolean readImpl() {
		ret = readInteger();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeInteger(ret);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.GC_CLUB_EDIT;
	}
	
	@Override
	public String getTypeName() {
		return "GC_CLUB_EDIT";
	}

	public int getRet(){
		return ret;
	}
		
	public void setRet(int ret){
		this.ret = ret;
	}
}