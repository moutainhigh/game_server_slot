package com.gameserver.common.schedule;

import java.util.Date;

import org.slf4j.Logger;

import com.common.constants.Loggers;
import com.core.schedule.LLISchedule;
import com.core.util.TimeUtils;
import com.gameserver.common.Globals;

/**
 * 心跳服务器
 * @author wayne
 *
 */
public class ScheduleHeartBeatServer  implements LLISchedule{
	
	private Logger logger = Loggers.scheduleLogger;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		String time=TimeUtils.formatYMDHMSTime(new Date().getTime());
		logger.debug("======>start HeartBeatServer time is:"+time);
		Globals.getServerStatusService().syncStatus();
	}
	
}
