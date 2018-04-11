package com.gameserver.human.msg;

import com.gameserver.common.msg.MessageType;
import com.gameserver.common.msg.GCMessage;

/**
 * 显示 返回
 *
 * @author CodeGenerator, don't modify this file please.
 */
public class GCBankView extends GCMessage{
	
	/** 玩家 游戏中的筹码 */
	private long gold;
	/** 玩家 银行中的筹码 */
	private long bankGold;
	/** 玩家 总资产(游戏中的筹码+银行中的筹码) */
	private long totalGold;

	public GCBankView (){
	}
	
	public GCBankView (
			long gold,
			long bankGold,
			long totalGold ){
			this.gold = gold;
			this.bankGold = bankGold;
			this.totalGold = totalGold;
	}

	@Override
	protected boolean readImpl() {
		gold = readLong();
		bankGold = readLong();
		totalGold = readLong();
		return true;
	}
	
	@Override
	protected boolean writeImpl() {
		writeLong(gold);
		writeLong(bankGold);
		writeLong(totalGold);
		return true;
	}
	
	@Override
	public short getType() {
		return MessageType.GC_BANK_VIEW;
	}
	
	@Override
	public String getTypeName() {
		return "GC_BANK_VIEW";
	}

	public long getGold(){
		return gold;
	}
		
	public void setGold(long gold){
		this.gold = gold;
	}

	public long getBankGold(){
		return bankGold;
	}
		
	public void setBankGold(long bankGold){
		this.bankGold = bankGold;
	}

	public long getTotalGold(){
		return totalGold;
	}
		
	public void setTotalGold(long totalGold){
		this.totalGold = totalGold;
	}
}