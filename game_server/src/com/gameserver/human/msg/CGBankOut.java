package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 银行 取出
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankOut extends CGMessage{
	
	/** 玩家 将要从银行取出的筹码 */
	private long outGold;
	
	public CGBankOut (){
	}
	
	public CGBankOut (
			long outGold ){
			this.outGold = outGold;
	}
	
	@Override
	protected boolean readImpl() {
		outGold = readLong();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeLong(outGold);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_BANK_OUT;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_OUT";
	}

	public long getOutGold(){
		return outGold;
	}
		
	public void setOutGold(long outGold){
		this.outGold = outGold;
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankOut(this.getSession().getPlayer(), this);
	}
}