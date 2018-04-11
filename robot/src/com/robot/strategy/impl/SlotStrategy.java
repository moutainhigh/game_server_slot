package com.robot.strategy.impl;

import com.core.msg.IMessage;
import com.gameserver.human.msg.CGHumanChangeName;
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
		
		CGHumanChangeName CGHumanChangeName = new CGHumanChangeName();
		CGHumanChangeName.setName("55555666666");
		this.getRobot().sendMessage(CGHumanChangeName);
	}

	@Override
	public void onResponse(IMessage message) {
		SlotHandlerFactory.getHandler().execute(this.getRobot(),message);
	}

}
