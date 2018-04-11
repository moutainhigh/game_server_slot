package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 银行 输入验证码 确认  
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankMakeSureIdentifyingCode extends CGMessage{
	
	/** 验证码 */
	private String code;
	
	public CGBankMakeSureIdentifyingCode (){
	}
	
	public CGBankMakeSureIdentifyingCode (
			String code ){
			this.code = code;
	}
	
	@Override
	protected boolean readImpl() {
		code = readString();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeString(code);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_BANK_MAKE_SURE_IDENTIFYING_CODE;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_MAKE_SURE_IDENTIFYING_CODE";
	}

	public String getCode(){
		return code;
	}
		
	public void setCode(String code){
		this.code = code;
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankMakeSureIdentifyingCode(this.getSession().getPlayer(), this);
	}
}