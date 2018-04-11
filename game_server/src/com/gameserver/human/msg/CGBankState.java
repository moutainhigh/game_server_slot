package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 查看银行状态 
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankState extends CGMessage{
	
	
	public CGBankState (){
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
		return MessageType.CG_BANK_STATE;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_STATE";
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankState(this.getSession().getPlayer(), this);
	}
}