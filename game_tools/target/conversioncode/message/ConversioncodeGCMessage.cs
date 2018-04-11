using System.Collections;

public class ConversioncodeGCMessage:AbstractMessageReceiver
{
	protected override void collectObservers()
	{
		register(NetMessageType.GC_CONVERSION_CODE,GC_CONVERSION_CODE);
	}
 
  	/**
	 * 兑换码兑换礼包返回
	 * @param randRewardList 道具奖励
	 */
	public void GC_CONVERSION_CODE(InputMessage data) 
	{
		int i,size;
		ArrayList randRewardList = new ArrayList();
		size = data.GetShort();
		for(i=0; i<size; i++)
		{
			RandRewardData randRewardList_Datas= new RandRewardData();
			randRewardList_Datas.rewardId = data.GetInt();//奖励id
			randRewardList_Datas.rewardCount = data.GetInt();//奖励数量
			randRewardList_Datas.vippoint = data.GetInt();//全服的赠送奖励 （只有全服的才根据 这个vippoint 给用户赠送奖励）
			randRewardList_Datas.giftNewId = data.GetInt();//用户给用户发送礼物的时候的 礼物ID 主要用于标识 礼物是啥 而且 需要花费多少金币
			randRewardList.Add(randRewardList_Datas);
		}
		ConversioncodeHandler.Instance().GC_CONVERSION_CODE(randRewardList);
	}
}