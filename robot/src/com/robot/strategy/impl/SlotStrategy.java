package com.robot.strategy.impl;

import com.core.msg.IMessage;
import com.gameserver.human.msg.CGBankMakeSureIdentifyingCode;
import com.robot.Robot;
import com.robot.slot.SlotHandlerFactory;
import com.robot.strategy.OnceExecuteStrategy;

/**
 * 老虎机机器人
 * @author guojunwei
 *
 */
public class SlotStrategy extends OnceExecuteStrategy {
	

	public SlotStrategy(Robot robot) {
		super(robot);
	}

	
	@Override
	public void doAction() {
       //进入特定的老虎机并且转动
		/*CGSlotEnter message = new CGSlotEnter();
		message.setSlotId(this.getRobot().getRobotSlotCacheData().getSlotId());
		this.getRobot().sendMessage(message);*/
		
		/*CGHumanChangeName CGHumanChangeName = new CGHumanChangeName();
		CGHumanChangeName.setName("55555666666");
		this.getRobot().sendMessage(CGHumanChangeName);*/
		
		/*CGBankState cgBankState = new CGBankState();
		this.getRobot().sendMessage(cgBankState);*/
		
		/*CGBankSetPassword cgBankSetPassword = new CGBankSetPassword();
		cgBankSetPassword.setBankPassword("kkk564");
		this.getRobot().sendMessage(cgBankSetPassword);*/
		
		/*CGBankLogin cgBankLogin = new CGBankLogin();
		cgBankLogin.setBankPassword("kkk564");
		this.getRobot().sendMessage(cgBankLogin);*/
		
		/*CGBankStore cgBankStore = new CGBankStore();
		cgBankStore.setStoreGold(10000);
		this.getRobot().sendMessage(cgBankStore);*/
		
		/*CGBankOut cgBankOut = new CGBankOut();
		cgBankOut.setOutGold(10000);
		this.getRobot().sendMessage(cgBankOut);
		*/
		/*CGBankChangePassword cgBankChangePassword = new CGBankChangePassword();
		cgBankChangePassword.setOldBankPassword("kkk564");
		cgBankChangePassword.setNewBankPassword("oooyyy");
		this.getRobot().sendMessage(cgBankChangePassword);*/
		
		/*CGBankSendIdentifyingCode cgBankSendIdentifyingCode = new CGBankSendIdentifyingCode();
		this.getRobot().sendMessage(cgBankSendIdentifyingCode);*/
		
		CGBankMakeSureIdentifyingCode cgBankMakeSureIdentifyingCode = new CGBankMakeSureIdentifyingCode();
		cgBankMakeSureIdentifyingCode.setCode("999999999");
		this.getRobot().sendMessage(cgBankMakeSureIdentifyingCode);
		
		
	}

	@Override
	public void onResponse(IMessage message) {
		SlotHandlerFactory.getHandler().execute(this.getRobot(),message);
	}

}
