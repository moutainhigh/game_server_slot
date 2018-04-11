package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 修改银行密码 返回
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCBankChangePassword extends GCMessage{
	
	/** 修改银行密码  是否成功  是:0,否:1 */
	private int state;

	public GCBankChangePassword (){
	}
	
	public GCBankChangePassword (
			int state ){
			this.state = state;
	}

	@Override
	protected boolean readImpl() {
		state = readInteger();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeInteger(state);
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

	public int getState(){
		return state;
	}
		
	public void setState(int state){
		this.state = state;
	}
}