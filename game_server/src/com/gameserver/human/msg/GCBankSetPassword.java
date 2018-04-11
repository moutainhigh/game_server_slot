package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 设置银行密码 返回
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCBankSetPassword extends GCMessage{
	

	public GCBankSetPassword (){
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
		return MessageType.GC_BANK_SET_PASSWORD;
	}
	
	@Override
	public String getTypeName() {
		return "GC_BANK_SET_PASSWORD";
	}
}