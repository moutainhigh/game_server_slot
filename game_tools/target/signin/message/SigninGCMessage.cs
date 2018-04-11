using System.Collections;

public class SigninGCMessage:AbstractMessageReceiver
{
	protected override void collectObservers()
	{
		register(NetMessageType.GC_SIGN_IN_INFO_DATA,GC_SIGN_IN_INFO_DATA);
		register(NetMessageType.GC_SIGN_IN,GC_SIGN_IN);
	}
 
  	/**
	 * 签到
	 * @param dayList 签到天数
	 */
	public void GC_SIGN_IN_INFO_DATA(InputMessage data) 
	{
		int i,size;
		ArrayList dayList = new ArrayList();
		size = data.GetShort();
		for(i=0; i<size; i++)
		{
			int dayList_Datas = data.GetInt();
			dayList.Add(dayList_Datas);
		}
		SigninHandler.Instance().GC_SIGN_IN_INFO_DATA(dayList);
	}
 
  	/**
	 * 签到
	 * @param day 签到天数
	 * @param randRewardList 道具奖励
	 */
	public void GC_SIGN_IN(InputMessage data) 
	{
		int i,size;
		int day = data.GetInt();		
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
		SigninHandler.Instance().GC_SIGN_IN(day,randRewardList);
	}
}