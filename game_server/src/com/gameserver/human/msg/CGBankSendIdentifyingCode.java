package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 银行 发送验证码 
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankSendIdentifyingCode extends CGMessage{
	
	
	public CGBankSendIdentifyingCode (){
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
		return MessageType.CG_BANK_SEND_IDENTIFYING_CODE;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_SEND_IDENTIFYING_CODE";
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankSendIdentifyingCode(this.getSession().getPlayer(), this);
	}
}