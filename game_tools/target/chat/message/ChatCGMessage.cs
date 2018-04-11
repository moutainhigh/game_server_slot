using System.Collections;
public class ChatCGMessage{
	
  
 		/**
		 * 聊天
		 * @param channel 频道
		 * @param destRoleUUID 接收玩家id
		 * @param content 内容
		 */
	public static void CG_CHAT_MSG(int channel,long destRoleUUID,string content) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_CHAT_MSG);
			msgBody.PutInt(channel);
			msgBody.PutLong(destRoleUUID);
			msgBody.PutString(content);
			PlatformService.Send(msgBody);
		}
}