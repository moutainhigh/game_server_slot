package com.robot.slot;

import com.core.msg.IMessage;
import com.robot.Robot;

public abstract class GCSlotBonusBase {
	

	public abstract void execute(Robot robot, IMessage message, int slotId, int slotType);
}
