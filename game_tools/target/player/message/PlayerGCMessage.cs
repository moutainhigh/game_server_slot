using System.Collections;

public class PlayerGCMessage:AbstractMessageReceiver
{
	protected override void collectObservers()
	{
		register(NetMessageType.GC_QUERY_PLAYER_INFO,GC_QUERY_PLAYER_INFO);
		register(NetMessageType.GC_QUERY_PLAYER_INFO_NAME,GC_QUERY_PLAYER_INFO_NAME);
		register(NetMessageType.GC_CHECK_PLAYER_LOGIN,GC_CHECK_PLAYER_LOGIN);
		register(NetMessageType.GC_NOTIFY_EXCEPTION,GC_NOTIFY_EXCEPTION);
		register(NetMessageType.GC_ENTER_SCENE,GC_ENTER_SCENE);
		register(NetMessageType.GC_CLIENT_VERSION,GC_CLIENT_VERSION);
	}
 
  	/**
	 * 客户端请求用户信息
	 * @param playerInfoData 玩家信息
	 */
	public void GC_QUERY_PLAYER_INFO(InputMessage data) 
	{
		PlayerInfoData playerInfoData = new PlayerInfoData();
		playerInfoData.playerId = data.GetLong();//玩家id
		playerInfoData.name = data.GetString();//名字
		playerInfoData.img = data.GetString();//图片
		playerInfoData.gold = data.GetLong();//玩家筹码
		playerInfoData.diamond = data.GetLong();//钻石
		playerInfoData.charm = data.GetLong();//玩家筹码
		playerInfoData.level = data.GetLong();//等级
		playerInfoData.sex = data.GetInt();//性别
		playerInfoData.viplevel = data.GetInt();//vip等级
		playerInfoData.countries = data.GetString();//国籍
		playerInfoData.age = data.GetInt();//年龄
		playerInfoData.slotRotate = data.GetLong();//总转次数
		playerInfoData.slotWin = data.GetLong();//总赢得分
		playerInfoData.slotSingleWin = data.GetLong();//单次赢取最大
		playerInfoData.slotWinNum = data.GetLong();//玩家总胜利次数
		playerInfoData.integral = data.GetLong();//排行榜积分
		playerInfoData.isRequest = data.GetInt();//是否已经申请添加好友 0:未申请,1:已申请 
		playerInfoData.newGuyGift = data.GetInt();//新手大礼包:1已购买，0 未购买
		playerInfoData.clubId = data.GetString();//俱乐部id，空字符串表示未加入俱乐部
		playerInfoData.clubIco = data.GetInt();//俱乐部图标
		playerInfoData.clubInvitedTimes = data.GetInt();//被俱乐部邀请次数，0为未被邀请
		PlayerHandler.Instance().GC_QUERY_PLAYER_INFO(playerInfoData);
	}
 
  	/**
	 * 客户端请求用户信息
	 * @param playerInfoData 玩家信息
	 */
	public void GC_QUERY_PLAYER_INFO_NAME(InputMessage data) 
	{
		int i,size;
		ArrayList playerInfoData = new ArrayList();
		size = data.GetShort();
		for(i=0; i<size; i++)
		{
			PlayerInfoData playerInfoData_Datas= new PlayerInfoData();
			playerInfoData_Datas.playerId =data.GetLong();
			playerInfoData_Datas.name = data.GetString();//名字
			playerInfoData_Datas.img = data.GetString();//图片
			playerInfoData_Datas.gold =data.GetLong();
			playerInfoData_Datas.diamond =data.GetLong();
			playerInfoData_Datas.charm =data.GetLong();
			playerInfoData_Datas.level =data.GetLong();
			playerInfoData_Datas.sex = data.GetInt();//性别
			playerInfoData_Datas.viplevel = data.GetInt();//vip等级
			playerInfoData_Datas.countries = data.GetString();//国籍
			playerInfoData_Datas.age = data.GetInt();//年龄
			playerInfoData_Datas.slotRotate =data.GetLong();
			playerInfoData_Datas.slotWin =data.GetLong();
			playerInfoData_Datas.slotSingleWin =data.GetLong();
			playerInfoData_Datas.slotWinNum =data.GetLong();
			playerInfoData_Datas.integral =data.GetLong();
			playerInfoData_Datas.isRequest = data.GetInt();//是否已经申请添加好友 0:未申请,1:已申请 
			playerInfoData_Datas.newGuyGift = data.GetInt();//新手大礼包:1已购买，0 未购买
			playerInfoData_Datas.clubId = data.GetString();//俱乐部id，空字符串表示未加入俱乐部
			playerInfoData_Datas.clubIco = data.GetInt();//俱乐部图标
			playerInfoData_Datas.clubInvitedTimes = data.GetInt();//被俱乐部邀请次数，0为未被邀请
			playerInfoData.Add(playerInfoData_Datas);
		}
		PlayerHandler.Instance().GC_QUERY_PLAYER_INFO_NAME(playerInfoData);
	}
 
  	/**
	 * 响应用户校验登录
	 * @param loginId 玩家的登录id
	 * @param facebookId facebookid
	 * @param accountId 账号id
	 * @param img 玩家图片
	 * @param utcOffset 间距时间
	 * @param playerRole 账户类型
	 */
	public void GC_CHECK_PLAYER_LOGIN(InputMessage data) 
	{
		long loginId = data.GetLong();
		string facebookId = data.GetString();		
		string accountId = data.GetString();		
		string img = data.GetString();		
		long utcOffset = data.GetLong();
		int playerRole = data.GetInt();		
		PlayerHandler.Instance().GC_CHECK_PLAYER_LOGIN(loginId,facebookId,accountId,img,utcOffset,playerRole);
	}
 
  	/**
	 * 通知客户端
	 * @param code 错误码
	 * @param msg 错误信息，如果为空就显示默认的
	 */
	public void GC_NOTIFY_EXCEPTION(InputMessage data) 
	{
		int code = data.GetInt();		
		string msg = data.GetString();		
		PlayerHandler.Instance().GC_NOTIFY_EXCEPTION(code,msg);
	}
 
  	/**
	 * 玩家已经进入场景
	 */
	public void GC_ENTER_SCENE(InputMessage data) 
	{
		PlayerHandler.Instance().GC_ENTER_SCENE();
	}
 
  	/**
	 * 玩家现在使用版本
	 */
	public void GC_CLIENT_VERSION(InputMessage data) 
	{
		PlayerHandler.Instance().GC_CLIENT_VERSION();
	}
}