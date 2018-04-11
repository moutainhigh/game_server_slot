package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 银行 发送验证码  返回
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCBankSendIdentifyingCode extends GCMessage{
	
	/** 验证码 (是:0,否:1) 发送成功（每个用户发送验证码的 次数有限，超过次数 就不会 发了） */
	private int codeState;

	public GCBankSendIdentifyingCode (){
	}
	
	public GCBankSendIdentifyingCode (
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
		return MessageType.GC_BANK_SEND_IDENTIFYING_CODE;
	}
	
	@Override
	public String getTypeName() {
		return "GC_BANK_SEND_IDENTIFYING_CODE";
	}

	public int getCodeState(){
		return codeState;
	}
		
	public void setCodeState(int codeState){
		this.codeState = codeState;
	}
}