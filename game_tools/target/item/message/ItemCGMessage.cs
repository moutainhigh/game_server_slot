using System.Collections;
public class ItemCGMessage{
	
  
 		/**
		 * 发送互动道具
		 * @param itemId 互动道具id
		 * @param playerId 玩家id
		 */
	public static void CG_SEND_INTERACTIVE_ITEM(int itemId,long playerId) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_SEND_INTERACTIVE_ITEM);
			msgBody.PutInt(itemId);
			msgBody.PutLong(playerId);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 群体发送互动道具
		 * @param itemId 互动道具id
		 */
	public static void CG_GROUP_SEND_INTERACTIVE_ITEM(int itemId) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_GROUP_SEND_INTERACTIVE_ITEM);
			msgBody.PutInt(itemId);
			PlatformService.Send(msgBody);
		}
}