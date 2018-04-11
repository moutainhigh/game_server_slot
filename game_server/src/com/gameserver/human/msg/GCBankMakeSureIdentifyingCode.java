package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 银行 输入验证码 确认  返回
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCBankMakeSureIdentifyingCode extends GCMessage{
	
	/** 验证码 (是:0,否:1)正确 */
	private int codeState;

	public GCBankMakeSureIdentifyingCode (){
	}
	
	public GCBankMakeSureIdentifyingCode (
			int codeState ){
			this.codeState = codeState;
	}

	@Override
	protected boolean readImpl() {
		codeState = readInteger();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeInteger(codeState);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.GC_BANK_MAKE_SURE_IDENTIFYING_CODE;
	}
	
	@Override
	public String getTypeName() {
		return "GC_BANK_MAKE_SURE_IDENTIFYING_CODE";
	}

	public int getCodeState(){
		return codeState;
	}
		
	public void setCodeState(int codeState){
		this.codeState = codeState;
	}
}