package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.CGMessage;
import com.gameserver.human.handler.HumanHandlerFactory;

/**
 * 银行 存入
 * 
 * @author CodeGenerator, don't modify this file please.
 */
public class CGBankStore extends CGMessage{
	
	/** 玩家 将要存入银行的筹码 */
	private long storeGold;
	
	public CGBankStore (){
	}
	
	public CGBankStore (
			long storeGold ){
			this.storeGold = storeGold;
	}
	
	@Override
	protected boolean readImpl() {
		storeGold = readLong();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeLong(storeGold);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.CG_BANK_STORE;
	}
	
	@Override
	public String getTypeName() {
		return "CG_BANK_STORE";
	}

	public long getStoreGold(){
		return storeGold;
	}
		
	public void setStoreGold(long storeGold){
		this.storeGold = storeGold;
	}
	


	@Override
	public void execute() {
		HumanHandlerFactory.getHandler().handleBankStore(this.getSession().getPlayer(), this);
	}
}