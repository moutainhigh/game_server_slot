using System.Collections;

public class RandRewardData
{
	/** 奖励id */
	public int rewardId;
	/** 奖励数量 */
	public int rewardCount;
	/** 全服的赠送奖励 （只有全服的才根据 这个vippoint 给用户赠送奖励） */
	public int vippoint;
	/** 用户给用户发送礼物的时候的 礼物ID 主要用于标识 礼物是啥 而且 需要花费多少金币 */
	public int giftNewId;
}