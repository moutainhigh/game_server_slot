package com.robot;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.core.helper.ConfigHelper;
import com.core.util.JsScriptHelper;
import com.game.webserver.Config.LocalConfig;
import com.game.webserver.exception.LocalException;
import com.game.webserver.login.ILoginServerHandler;
import com.game.webserver.login.LoginServerHandler;
import com.robot.common.Globals;
import com.robot.strategy.impl.BaccaratStrategy;
import com.robot.strategy.impl.SNGStrategy;
import com.robot.strategy.impl.TexasHallStrategy;



/**
 * 机器人主函数
 * @author Thinker
 *
 */
public class RobotMain
{	
	private static ILoginServerHandler synLoginServerHandler;
	
	public static void main(String[] args) throws Exception
	{
		if (args.length < 4)
		{
			System.out.println("Usage: java com.imop.tr.robot.RobotMain <robotIdStart> <robotCount> <ip> <port> [config path]");
			System.exit(1);
		}
		
		int robotIdStart = Integer.valueOf(args[0]);
		int robotCount = Integer.valueOf(args[1]);
		String targetServerIp = args[2];
		int port =  Integer.valueOf(args[3]);
		String serverId = args[4];
		String path = null;
		if(args.length > 5)
		{
			path = args[5];
		}
		URL url = ClassLoader.getSystemResource(RobotServer.GAME_SERVER_CONFIG_PATH);
		RobotServerConfig config = ConfigHelper.buildConfig(RobotServerConfig.class, url);
		//全球配置初始化
		Globals.init(config);
		
		Globals.start();

		// 完整测试
		completeTest(robotIdStart, robotCount, targetServerIp, port,serverId, path);
		try
		{
			System.out.println("press any key to continue ...");
			System.in.read();
		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 完整测试, 包括征收、建筑升级、装备强化、武将训练、农田占领、聊天等功能
	 * 
	 * @param robotIdStart
	 * @param robotCount
	 * @param targetServerIp
	 * @param port
	 * @throws LocalException 
	 * 
	 */
	private static void completeTest(int robotIdStart, int robotCount, String targetServerIp, int port,String serverId, String path) throws LocalException 
	{
		for (int i = robotIdStart; i < robotIdStart + robotCount; i++) 
		{
			
			Robot robot =  null;// createRobot(String.valueOf(i), serverId);
			//SNGStrategy st = new SNGStrategy(robot);
			BaccaratStrategy bt = new BaccaratStrategy(robot);
		//	TexasHallStrategy st = new TexasHallStrategy(robot);
			//RelationStrategy relationStrategy = new RelationStrategy(robot, 100, 10000);
			robot.addRobotStrategy(bt);
			// 加载具体脚本
			if(path != null)
			{
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("robot", robot);
				
				try
				{
					JsScriptHelper.executeScriptFile(path, params);
				} catch (Exception e)
				{
					
				}
			}


			robot.start();

		

			try {
				Thread.currentThread().sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
			}

		}
	}

	/**
	 * 创建机器人
	 * 
	 * @param robotId
	 * @param targetServerIp
	 * @param port
	 * @return
	 * @throws LocalException 
	 */
	/*private static Robot createRobot(String deviceMac,String serverId) throws LocalException
	{
		return new Robot(deviceMac,serverId);
	}*/



//	/**
//	 * 完整测试, 包括征收、建筑升级、装备强化、武将训练、农田占领、聊天等功能
//	 * 
//	 * @param robotIdStart
//	 * @param robotCount
//	 * @param targetServerIp
//	 * @param port
//	 * @throws LocalException 
//	 * 
//	 */
//	private static void completeTest(int robotIdStart, int robotCount, String targetServerIp, int port, String path) throws LocalException 
//	{
//		for (int i = robotIdStart; i < robotIdStart + robotCount; i++) 
//		{
//			
//			Robot robot = createRobot(i, targetServerIp, port);
//			BaccaratStrategy bs = new BaccaratStrategy(robot);
//			
//			//SNGStrategy st = new SNGStrategy(robot);
//			//TexasHallStrategy st = new TexasHallStrategy(robot);
//			//RelationStrategy relationStrategy = new RelationStrategy(robot, 100, 10000);
//			robot.addRobotStrategy(bs);
//			// 加载具体脚本
//			if(path != null)
//			{
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("robot", robot);
//				
//				try
//				{
//					JsScriptHelper.executeScriptFile(path, params);
//				} catch (Exception e)
//				{
//					
//				}
//			}
//
//
//			robot.start();
//
//		
//
//			try {
//				Thread.currentThread().sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				
//			}
//
//		}
//	}

	/**
	 * 创建机器人
	 * 
	 * @param robotId
	 * @param targetServerIp
	 * @param port
	 * @return
	 * @throws LocalException 
	 */
//	private static Robot createRobot(int robotId, String targetServerIp, int port) throws LocalException
//	{
//		return new Robot(""+robotId,robotId, targetServerIp, port);
//	}
//
//	public static ILoginServerHandler getSynLoginServerHandler() throws LocalException {
//		// TODO Auto-generated method stub
//		if(synLoginServerHandler == null)
//		{
//			
//			synLoginServerHandler = new LoginServerHandler(LocalConfig.robotLocalConfig());
//	
//		}
//		return synLoginServerHandler;
//	}
}
