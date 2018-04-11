using System.Collections;

public class ChatGCMessage:AbstractMessageReceiver
{
	protected override void collectObservers()
	{
		register(NetMessageType.GC_CHAT_MSG,GC_CHAT_MSG);
	}
 
  	/**
	 * 聊天
	 * @param channel 频道   喇叭 SPEAKER(0),世界WORLD(1),房间ROOM(2),俱乐部CLUB(3),私聊 PRIVATE(4),百家乐BACCARAT(5);
	 * @param fromRoleImg 发送玩家头像
	 * @param fromRoleName 发送玩家名字
	 * @param fromRoleUUID 发送玩家id
	 * @param national 国家
	 * @param lv 等级
	 * @param viplv vip等级
	 * @param rank 排行榜
	 * @param sex 性别 1男    2女
	 * @param content 内容
	 */
	public void GC_CHAT_MSG(InputMessage data) 
	{
		int channel = data.GetInt();		
		string fromRoleImg = data.GetString();		
		string fromRoleName = data.GetString();		
		long fromRoleUUID = data.GetLong();
		string national = data.GetString();		
		int lv = data.GetInt();		
		int viplv = data.GetInt();		
		int rank = data.GetInt();		
		int sex = data.GetInt();		
		string content = data.GetString();		
		ChatHandler.Instance().GC_CHAT_MSG(channel,fromRoleImg,fromRoleName,fromRoleUUID,national,lv,viplv,rank,sex,content);
	}
}