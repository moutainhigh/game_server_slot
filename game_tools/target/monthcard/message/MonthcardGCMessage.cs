using System.Collections;

public class MonthcardGCMessage:AbstractMessageReceiver
{
	protected override void collectObservers()
	{
		register(NetMessageType.GC_MONTH_CARD_GET,GC_MONTH_CARD_GET);
		register(NetMessageType.GC_HUMAN_MONTH_CARD_INFO_DATA,GC_HUMAN_MONTH_CARD_INFO_DATA);
	}
 
  	/**
	 * 领取月卡
	 * @param randRewardList 道具奖励
	 */
	public void GC_MONTH_CARD_GET(InputMessage data) 
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
		MonthcardHandler.Instance().GC_MONTH_CARD_GET(randRewardList);
	}
 
  	/**
	 * 月卡数据
	 * @param monthCardInfoData 月 卡数据
	 */
	public void GC_HUMAN_MONTH_CARD_INFO_DATA(InputMessage data) 
	{
		HumanMonthCardInfoData monthCardInfoData = new HumanMonthCardInfoData();
		monthCardInfoData.beginTime = data.GetLong();//开始时间
		monthCardInfoData.getTime = data.GetLong();//领取时间
		monthCardInfoData.duration = data.GetLong();//持续时间
		MonthcardHandler.Instance().GC_HUMAN_MONTH_CARD_INFO_DATA(monthCardInfoData);
	}
}