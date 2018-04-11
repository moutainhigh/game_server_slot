using System.Collections;

public class ItemGCMessage:AbstractMessageReceiver
{
	protected override void collectObservers()
	{
		register(NetMessageType.GC_HUMAN_BAG_INFO_DATA,GC_HUMAN_BAG_INFO_DATA);
		register(NetMessageType.GC_SEND_INTERACTIVE_ITEM,GC_SEND_INTERACTIVE_ITEM);
		register(NetMessageType.GC_GROUP_SEND_INTERACTIVE_ITEM,GC_GROUP_SEND_INTERACTIVE_ITEM);
	}
 
  	/**
	 * 客户端请求商品信息
	 * @param itemList 商品信息
	 */
	public void GC_HUMAN_BAG_INFO_DATA(InputMessage data) 
	{
		int i,size;
		ArrayList itemList = new ArrayList();
		size = data.GetShort();
		for(i=0; i<size; i++)
		{
			ItemInfoData itemList_Datas= new ItemInfoData();
			itemList_Datas.uuid = data.GetString();//数据库id
			itemList_Datas.templateId = data.GetInt();//模板id
			itemList_Datas.overlap = data.GetInt();//数量
			itemList_Datas.beginTime =data.GetLong();
			itemList_Datas.duration =data.GetLong();
			itemList.Add(itemList_Datas);
		}
		ItemHandler.Instance().GC_HUMAN_BAG_INFO_DATA(itemList);
	}
 
  	/**
	 * 发送互动道具
	 * @param fromId 发送玩家id
	 * @param itemId 互动道具id
	 * @param toId 玩家id
	 */
	public void GC_SEND_INTERACTIVE_ITEM(InputMessage data) 
	{
		long fromId = data.GetLong();
		int itemId = data.GetInt();		
		long toId = data.GetLong();
		ItemHandler.Instance().GC_SEND_INTERACTIVE_ITEM(fromId,itemId,toId);
	}
 
  	/**
	 * 群体发送互动道具
	 * @param fromId 发送玩家id
	 * @param itemId 互动道具id
	 */
	public void GC_GROUP_SEND_INTERACTIVE_ITEM(InputMessage data) 
	{
		long fromId = data.GetLong();
		int itemId = data.GetInt();		
		ItemHandler.Instance().GC_GROUP_SEND_INTERACTIVE_ITEM(fromId,itemId);
	}
}