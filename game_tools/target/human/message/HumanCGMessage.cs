using System.Collections;
public class HumanCGMessage{
	
  
 		/**
		 * 手大礼包是否显示的 用户触发
		 */
	public static void CG_HUMAN_DETAIL_INFO_TODAY_VIEW() {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_HUMAN_DETAIL_INFO_TODAY_VIEW);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 新手引导记录
		 * @param guideId 0 大礼包 1 百家乐 2 德州 3 老虎
		 */
	public static void CG_HUMAN_NEW_GUIDE(int guideId) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_HUMAN_NEW_GUIDE);
			msgBody.PutInt(guideId);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 更改名字
		 * @param name 玩家名字
		 */
	public static void CG_HUMAN_CHANGE_NAME(string name) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_HUMAN_CHANGE_NAME);
			msgBody.PutString(name);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 更改性别
		 * @param sex 性别
		 */
	public static void CG_HUMAN_CHANGE_SEX(int sex) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_HUMAN_CHANGE_SEX);
			msgBody.PutInt(sex);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 更改图像 
		 * @param img 玩家图像 
		 */
	public static void CG_HUMAN_CHANGE_IMG(string img) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_HUMAN_CHANGE_IMG);
			msgBody.PutString(img);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 观看视频
		 */
	public static void CG_HUMAN_VIDEO_NUM() {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_HUMAN_VIDEO_NUM);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 修改角色年龄国际
		 * @param countries 国籍
		 * @param age 年龄
		 */
	public static void CG_CHANEAGE_COUNTRIES(string countries,int age) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_CHANEAGE_COUNTRIES);
			msgBody.PutString(countries);
			msgBody.PutInt(age);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 老虎机房间发礼物
		 * @param giftId 礼物ID
		 * @param send_type 发送类型 0 个人 1 全体
		 * @param rece_playerId 接收者ID在发送类型是0的时候有效果
		 */
	public static void CG_SLOT_ROOM_GIFT(int giftId,int send_type,long rece_playerId) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_SLOT_ROOM_GIFT);
			msgBody.PutInt(giftId);
			msgBody.PutInt(send_type);
			msgBody.PutLong(rece_playerId);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 邀请朋友加入老虎机房间
		 * @param req_playerId 邀请的好友ID
		 */
	public static void CG_SLOT_ROOM_PLEASE(long req_playerId) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_SLOT_ROOM_PLEASE);
			msgBody.PutLong(req_playerId);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * bigWin分享礼物
		 * @param giftId 礼物ID
		 */
	public static void CG_ROOM_BIGWIN_GIFT(int giftId) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_ROOM_BIGWIN_GIFT);
			msgBody.PutInt(giftId);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 双倍经验加成
		 */
	public static void CG_EXP_DOUBLE() {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_EXP_DOUBLE);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 查看银行状态 
		 */
	public static void CG_BANK_STATE() {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_STATE);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 设置银行密码
		 * @param bankPassword 银行密码
		 */
	public static void CG_BANK_SET_PASSWORD(string bankPassword) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_SET_PASSWORD);
			msgBody.PutString(bankPassword);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 输入银行密码 登录
		 * @param bankPassword 银行密码
		 */
	public static void CG_BANK_LOGIN(string bankPassword) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_LOGIN);
			msgBody.PutString(bankPassword);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 银行 存入
		 * @param storeGold 玩家 将要存入银行的筹码
		 */
	public static void CG_BANK_STORE(long storeGold) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_STORE);
			msgBody.PutLong(storeGold);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 银行 取出
		 * @param outGold 玩家 将要从银行取出的筹码
		 */
	public static void CG_BANK_OUT(long outGold) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_OUT);
			msgBody.PutLong(outGold);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 修改银行密码
		 * @param oldBankPassword 银行密码 旧的
		 * @param newBankPassword 银行密码 新的
		 */
	public static void CG_BANK_CHANGE_PASSWORD(string oldBankPassword,string newBankPassword) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_CHANGE_PASSWORD);
			msgBody.PutString(oldBankPassword);
			msgBody.PutString(newBankPassword);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 银行 发送验证码 
		 */
	public static void CG_BANK_SEND_IDENTIFYING_CODE() {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_SEND_IDENTIFYING_CODE);
			PlatformService.Send(msgBody);
		}
 
 		/**
		 * 银行 输入验证码 确认  
		 * @param code 验证码
		 */
	public static void CG_BANK_MAKE_SURE_IDENTIFYING_CODE(string code) {
			OutputMessage msgBody= new OutputMessage(NetMessageType.CG_BANK_MAKE_SURE_IDENTIFYING_CODE);
			msgBody.PutString(code);
			PlatformService.Send(msgBody);
		}
}