package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 查看银行状态 返回
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCBankState extends GCMessage{
	
	/** 银行 状态 是:0,否:1 */
	private int state;

	public GCBankState (){
	}
	
	public GCBankState (
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
		return MessageType.GC_BANK_STATE;
	}
	
	@Override
	public String getTypeName() {
		return "GC_BANK_STATE";
	}

	public int getState(){
		return state;
	}
		
	public void setState(int state){
		this.state = state;
	}
}