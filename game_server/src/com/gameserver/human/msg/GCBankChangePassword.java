package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 修改银行密码 返回
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCBankChangePassword extends GCMessage{
	

	public GCBankChangePassword (){
	}
	

	@Override
	protected boolean readImpl() {
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.GC_BANK_CHANGE_PASSWORD;
	}
	
	@Override
	public String getTypeName() {
		return "GC_BANK_CHANGE_PASSWORD";
	}
}