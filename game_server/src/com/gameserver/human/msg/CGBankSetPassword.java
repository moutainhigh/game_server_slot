package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 设置银行密码
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankSetPassword extends CGMessage{
	
	/** 银行密码 */
	private String bankPassword;
	
	public CGBankSetPassword (){
	}
	
	public CGBankSetPassword (
			String bankPassword ){
			this.bankPassword = bankPassword;
	}
	
	@Override
	protected boolean readImpl() {
		bankPassword = readString();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeString(bankPassword);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_BANK_SET_PASSWORD;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_SET_PASSWORD";
	}

	public String getBankPassword(){
		return bankPassword;
	}
		
	public void setBankPassword(String bankPassword){
		this.bankPassword = bankPassword;
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankSetPassword(this.getSession().getPlayer(), this);
	}
}