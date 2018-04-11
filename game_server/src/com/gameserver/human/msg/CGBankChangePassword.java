package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 修改银行密码
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankChangePassword extends CGMessage{
	
	/** 银行密码 旧的 */
	private String oldBankPassword;
	/** 银行密码 新的 */
	private String newBankPassword;
	
	public CGBankChangePassword (){
	}
	
	public CGBankChangePassword (
			String oldBankPassword,
			String newBankPassword ){
			this.oldBankPassword = oldBankPassword;
			this.newBankPassword = newBankPassword;
	}
	
	@Override
	protected boolean readImpl() {
		oldBankPassword = readString();
		newBankPassword = readString();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeString(oldBankPassword);
		writeString(newBankPassword);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_BANK_CHANGE_PASSWORD;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_CHANGE_PASSWORD";
	}

	public String getOldBankPassword(){
		return oldBankPassword;
	}
		
	public void setOldBankPassword(String oldBankPassword){
		this.oldBankPassword = oldBankPassword;
	}

	public String getNewBankPassword(){
		return newBankPassword;
	}
		
	public void setNewBankPassword(String newBankPassword){
		this.newBankPassword = newBankPassword;
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankChangePassword(this.getSession().getPlayer(), this);
	}
}