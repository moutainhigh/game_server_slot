package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 输入银行密码 登录
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankLogin extends CGMessage{
	
	/** 银行密码 */
	private String bankPassword;
	
	public CGBankLogin (){
	}
	
	public CGBankLogin (
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
		return MessageType.CG_BANK_LOGIN;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_LOGIN";
	}

	public String getBankPassword(){
		return bankPassword;
	}
		
	public void setBankPassword(String bankPassword){
		this.bankPassword = bankPassword;
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankLogin(this.getSession().getPlayer(), this);
	}
}