package com.gameserver.mail.handler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;

import com.common.LogReasons;
import com.common.constants.LangConstants;
import com.common.constants.Loggers;
import com.db.model.BazooAgentEntity;
import com.db.model.HumanEntity;
import com.gameserver.common.CommonLogic;
import com.gameserver.common.Globals;
import com.gameserver.common.data.RandRewardData;
import com.gameserver.currency.Currency;
import com.gameserver.human.Human;
import com.gameserver.item.template.ItemTemplate;
import com.gameserver.mail.Mail;
import com.gameserver.mail.MailLogic;
import com.gameserver.mail.data.MailInfoData;
import com.gameserver.mail.enums.MailHasAttachment;
import com.gameserver.mail.enums.MailStatus;
import com.gameserver.mail.enums.MailTypeEnum;
import com.gameserver.mail.msg.CGDealWithReward;
import com.gameserver.mail.msg.CGDeleteMail;
import com.gameserver.mail.msg.CGDeleteSendCancelMail;
import com.gameserver.mail.msg.CGDeleteSendMail;
import com.gameserver.mail.msg.CGLoadMailList;
import com.gameserver.mail.msg.CGLoadSendList;
import com.gameserver.mail.msg.CGReadMail;
import com.gameserver.mail.msg.CGReceiveAll;
import com.gameserver.mail.msg.CGSendMail;
import com.gameserver.mail.msg.GCDealWithReward;
import com.gameserver.mail.msg.GCDeleteMail;
import com.gameserver.mail.msg.GCLoadMailList;
import com.gameserver.mail.msg.GCLoadSendList;
import com.gameserver.mail.msg.GCReadMail;
import com.gameserver.mail.msg.GCReceiveAll;
import com.gameserver.mail.msg.GCSendFinish;
import com.gameserver.mail.msg.GCSendMail;
import com.gameserver.player.Player;
import com.gameserver.player.cache.PlayerCacheInfo;

/**
 * 邮件处理 
 * @author wayne
 *
 */
public class MailMessageHandler {

	private Logger logger = Loggers.mailLogger;
	
	/**
	 * 加载邮件
	 * @param player
	 * @param cgLoadMailList
	 */
	public void handleLoadMailList(Player player, CGLoadMailList cgLoadMailList) {
		
		if(player.getHuman()==null) return;
		int mailKindId=cgLoadMailList.getMailKind();
		mailList(player,mailKindId,"receive");
		
	}
	
	private void mailList(Player player,int mailKindId,String sor){
		MailTypeEnum mailKind = MailTypeEnum.valueOf(mailKindId);
		if(mailKind == null)
		{
			Loggers.mailLogger.warn("玩家id["+player.getHuman().getPassportId()+"]玩家名称["+player.getHuman().getName()+"]请求邮件类型不对");
			player.getHuman().sendSystemMessage(LangConstants.MAIL_KIDN_ERROR);
			return;
		}
		logger.debug("玩家id["+player.getPassportId()+"]玩家名称["+player.getHuman().getName()+"]邮件类型["+mailKind+"]请求加载邮件信息");
//		List<Mail> mailList=player.getHuman().getHumanMailManager().getMailListByMailKind(mailKind);
//		List<MailInfoData> mailInfoDataList=MailLogic.getInstance().buildMailInfoDataList(player.getHuman(), mailList);
//		GCLoadMailList gcLoadMailList=new GCLoadMailList();
//		gcLoadMailList.setMailKind(mailKindId);
//		gcLoadMailList.setMailInfoDataList(mailInfoDataList.toArray(new MailInfoData[mailInfoDataList.size()]));
//		
		player.getHuman().getHumanMailManager().buildHumanMailInfoData(player,mailKind,sor);
	}
	
	
	/**
	 * 发送邮件
	 * @param player
	 * @param cgSendMail
	 */
	public void handleSendMail(Player player, CGSendMail cgSendMail) {
//		// TODO Auto-generated method stub
		Human human = player.getHuman();
		if(human==null) return;
		long friendId=cgSendMail.getRoleId();
	
		if(friendId == player.getPassportId()){
			logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]发送给自己["+friendId+"]");
			return;
		}
		
		//判断邮件目标是否为好友
//		HumanRelationManager humanRelationManager = player.getHuman().getHumanRelationManager();
//		if(!humanRelationManager.checkIfAddFriend(friendId)){
//			Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]对方不是好友");
//			player.getHuman().sendErrorMessage(LangConstants.MAIL_FRIEND_ERR);
//			return;
//		}
		PlayerCacheInfo playerCacheInfo = Globals.getPlayerCacheService().getPlayerCacheById(friendId);
		if(playerCacheInfo==null) {
			logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]发送给玩家["+friendId+"]不存在");
			return;
		}
		
		Loggers.mailLogger.debug("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]发送邮件给好友["+friendId+"]");
		String content=cgSendMail.getContent();
		/* 过滤聊天中的不良信息 */
		content = Globals.getWordFilterService().filterHtmlTag(content);
		boolean hasDirtyWords = Globals.getWordFilterService().containKeywords(content);
		if (hasDirtyWords)
		{
			// 记录玩家发表不良信息的日志
			content = Globals.getWordFilterService().filter(content);
		}
		//有附件
		if(cgSendMail.getRandReward().length!=0){
			/*if(player.getPlayerRoleEnum() != PlayerRoleEnum.AGENT && player.getPlayerRoleEnum() != PlayerRoleEnum.GM ){
				logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"],玩家角色["+player.getPlayerRoleEnum()+"],发送邮件有附件]");
				return;
			}*/
			
			//检查附件内容
			if(cgSendMail.getRandReward().length!=1){
				logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"],玩家角色["+player.getPlayerRoleEnum()+"],发送邮件超过1个附件]");
				return;
			}
			
			RandRewardData tempRandRewardData = cgSendMail.getRandReward()[0];
			Currency tempCurrency= Currency.valueOf(tempRandRewardData.getRewardId());
			if(tempCurrency!=Currency.GOLD){
				logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"],玩家角色["+player.getPlayerRoleEnum()+"],发送邮件不是筹码]");
				return;
			}
			
			if(tempRandRewardData.getRewardCount()<=0){
				logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"],玩家角色["+player.getPlayerRoleEnum()+"],发送邮件筹码小于等于0");
				return;
			}
			
			//判断是否有足够筹码		
			if(!human.hasEnoughMoney(tempRandRewardData.getRewardCount(), tempCurrency)){
				logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"],玩家角色["+player.getPlayerRoleEnum()+"],没有足够的筹码发邮件");
				return;
			}
			String detailReason = MessageFormat.format(LogReasons.GoldLogReason.TRANSFER.getReasonText(),friendId,tempRandRewardData.getRewardCount());
			human.costMoney(tempRandRewardData.getRewardCount(), tempCurrency, true, LogReasons.GoldLogReason.TRANSFER, detailReason, -1, 1);
		}
		
		MailTypeEnum mailKind= MailTypeEnum.PLAYER;
		//如果是 用户发送的礼物 就 设置成礼物类型
		if(cgSendMail.getRandReward() != null && cgSendMail.getRandReward().length>0 && cgSendMail.getRandReward()[0].getGiftNewId() > 0){
			RandRewardData randRewardData = cgSendMail.getRandReward()[0];
			
			//校验如果低于 150000=15万 就不让发
			if(randRewardData.getRewardCount()<150000){
				logger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"],玩家礼物金币["+randRewardData.getRewardCount()+"],玩家发送礼物的金币小于15万");
				player.getHuman().sendSystemMessage(LangConstants.GIFT_NOT_ENOUGH);
				return;
			}
			
			//扣除2%的金币
			int changeGold = randRewardData.getRewardCount()*2/100;//2%
			
			mailKind=MailTypeEnum.PURE_GIFT;
			//判断是否是代理商 然后 是否加钱 是否减钱
			long roleId = cgSendMail.getRoleId();
			Player toPlayer = Globals.getOnlinePlayerService().getPlayerByPassportId(roleId);
			if(toPlayer != null){
				//都 不是代理
				if(toPlayer.getHuman().getBazooAgentEntity() == null && human.getBazooAgentEntity() == null){
					randRewardData.setRewardCount(randRewardData.getRewardCount()-changeGold);
					
				//只有一个代理 
				}else if(toPlayer.getHuman().getBazooAgentEntity() != null && human.getBazooAgentEntity() == null){
					randRewardData.setRewardCount(randRewardData.getRewardCount()-changeGold);
					
				//只有一个代理 
				}else if(toPlayer.getHuman().getBazooAgentEntity() == null && human.getBazooAgentEntity() != null){
					randRewardData.setRewardCount(randRewardData.getRewardCount()-changeGold);
					
				//都是代理 不做处理
				}else{
					
				}
				
			}else{
				BazooAgentEntity toEntity = Globals.getDaoService().getBazooAgentDao().getTheAgent(String.valueOf(roleId));
				//都 不是代理
				if(toEntity == null && human.getBazooAgentEntity() == null){
					randRewardData.setRewardCount(randRewardData.getRewardCount()-changeGold);
				//只有一个代理 
				}else if(toEntity != null && human.getBazooAgentEntity() == null){
					randRewardData.setRewardCount(randRewardData.getRewardCount()-changeGold);
				//只有一个代理 
				}else if(toEntity == null && human.getBazooAgentEntity() != null){
					randRewardData.setRewardCount(randRewardData.getRewardCount()-changeGold);
					
				//都是代理 不做处理
				}else{
					
				}
				
				
			}
			
		}
		String mailTitle= cgSendMail.getTitle();
		//cgSendMail.getRandRewardList();
		List<RandRewardData> tempRandRewardDataList = new ArrayList<RandRewardData>();
		for(RandRewardData tempRandRewardData:cgSendMail.getRandReward()){
			tempRandRewardDataList.add(tempRandRewardData);
		}
		
	   if(!MailLogic.getInstance().sendMailToRole(player.getHuman(), friendId,mailTitle,mailKind, content,tempRandRewardDataList))
	   {
		   Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]发送给玩家["+friendId+"]不存在");
			player.getHuman().sendSystemMessage(LangConstants.MAIL_RECEIVER_NO_EXIST);
			return;
	   }
	   
	  
	   if(cgSendMail.getRandReward() != null && cgSendMail.getRandReward().length>0 && cgSendMail.getRandReward()[0].getGiftNewId() > 0){
		   GCSendFinish gcSendFinish = new GCSendFinish();
		   gcSendFinish.setSendId(human.getPassportId());
		   gcSendFinish.setSendName(human.getName());
		   gcSendFinish.setReceiveId(cgSendMail.getRoleId());
		   Player toPlayer = Globals.getOnlinePlayerService().getPlayerByPassportId(cgSendMail.getRoleId());
		   if(toPlayer == null){
			   List<HumanEntity> humanEntityList =  Globals.getDaoService().getHumanDao().loadHumans(cgSendMail.getRoleId());
			   if(humanEntityList != null && humanEntityList.size()>0){
				   gcSendFinish.setReceiveName(humanEntityList.get(0).getName());
			   }
		   }else{
			   gcSendFinish.setReceiveName(toPlayer.getHuman().getName());
			   
		   }
		   gcSendFinish.setSendTime(new Date().getTime());
		   gcSendFinish.setGold(cgSendMail.getRandReward()[0].getRewardCount());
		   player.getHuman().sendMessage(gcSendFinish);
	   }else{
		   GCSendMail gcSendMail=new GCSendMail();
		   player.getHuman().sendMessage(gcSendMail);
	   }
	}

	/**
	 * 读邮件
	 * @param player
	 * @param cgReadMail
	 */
	public void handleReadMail(Player player, CGReadMail cgReadMail) {
		// TODO Auto-generated method stub
		Human human = player.getHuman();
		if(human == null) return;
		long mailId=cgReadMail.getMailId();
		logger.debug("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]读取邮件id["+mailId+"]");
		Mail mail=human.getHumanMailManager().getMailDataByMailId(mailId);
		if(mail==null)
		{
			Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]读取邮件id["+mailId+"]邮件不存在");
			human.sendSystemMessage(LangConstants.MAIL_ISNOTEXIST);
			return;
		}
		
		if(mail.getMailStatus()==MailStatus.UNREAD)
		{
			mail.setMailStatus(MailStatus.READ);
			mail.setModified();
		}
		
		//下发邮件详细内容
		GCReadMail gcReadMail=new GCReadMail();
		gcReadMail.setMailId(mailId);
		gcReadMail.setMailKind(mail.getMailType().getIndex());
		gcReadMail.setContent(mail.getContent());
		gcReadMail.setRandReward(mail.getRandRewardDataList().toArray(new RandRewardData[mail.getRandRewardDataList().size()]));
		gcReadMail.setHasAttachment(mail.getHasAttachment().getIndex());
		gcReadMail.setIsDealWith(mail.getMailStatus().getIndex());
		gcReadMail.setReceiveTime(mail.getCreateTime());
		player.sendMessage(gcReadMail);
		
	}


	/**
	 * 删除邮件
	 * @param player
	 * @param cgDeleteMail
	 */
	public void handleDeleteMail(Player player, CGDeleteMail cgDeleteMail) {
		if(player.getHuman()==null) return;
		logger.debug("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]玩家删除邮件");
		long[] mailIds=cgDeleteMail.getMailIdList();
		if(!MailLogic.getInstance().checkMailDelete(player,mailIds)) return;
		MailLogic.getInstance().deleteMails(player, mailIds);
		GCDeleteMail gcDeleteMail=new GCDeleteMail();
		player.getHuman().sendMessage(gcDeleteMail);
	}

	/**
	 * 领取奖励
	 * @param player
	 * @param cgDealWithReward
	 */
	public void handleDealWithReward(Player player,
			CGDealWithReward cgDealWithReward) {
		if(player.getHuman()==null) return;
		long mailId=cgDealWithReward.getMailId();
		Loggers.mailLogger.debug("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]处理邮件["+mailId+"]处理领奖");
		Mail mail=player.getHuman().getHumanMailManager().getMailDataByMailId(mailId);
		if(mail==null)
		{
			Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]处理邮件["+mailId+"]邮件不存在");
			player.getHuman().sendSystemMessage(LangConstants.MAIL_ISNOTEXIST);
			return;
		}
		if(mail.getHasAttachment()==MailHasAttachment.NOTHING)
		{
			Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]处理邮件["+mailId+"]奖励不存在");
			player.getHuman().sendSystemMessage(LangConstants.MAIL_REWARD_ISNOTEXIST);
			return;
		}
		if(mail.getMailStatus()== MailStatus.GET)
		{
			Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]处理邮件["+mailId+"]奖励 已经处理过");
			return;
		}
		/*mail.setMailStatus(MailStatus.GET);
		mail.setModified();*/
		//下发奖励给玩家
		List<RandRewardData> randRewardDataList=mail.getRandRewardDataList();
		CommonLogic.getInstance().giveRandReward(player.getHuman(), randRewardDataList,LogReasons.GoldLogReason.MAIL_REWARD,LogReasons.DiamondLogReason.MAIL_REWARD,LogReasons.CharmLogReason.MAIL_REWARD,LogReasons.ItemLogReason.MAIL_REWARD,false);
		mail.setMailStatus(MailStatus.GET);
		mail.setModified();
		/**
		 * 同步到收集系统
		 */
		for(RandRewardData RandRewardData:randRewardDataList){
			//大于100的都是道具
			if(RandRewardData.getRewardId() > 100){ 
				ItemTemplate ItemTemplate = Globals.getItemService().getItemTemplWithId(RandRewardData.getRewardId());
				if(ItemTemplate == null){
					continue;
				}
				if(ItemTemplate.getPoolType() == 1 || ItemTemplate.getPoolType() == 2 || ItemTemplate.getPoolType() == 3){
					player.getHuman().getHumanCollectManager().syncToCollect(RandRewardData.getRewardId(), RandRewardData.getRewardCount());
				}
			}
		}
		GCDealWithReward gcDealWithReward = new GCDealWithReward();
		gcDealWithReward.setMailId(mailId);
		player.sendMessage(gcDealWithReward);
		
		player.sendMessage(player.getHuman().getHumanBagManager().buildHumanBagInfoData());
		
		if(mail.getMailType() == MailTypeEnum.PLAYER_GIFT){
			player.getHuman().getHumanAchievementManager().updateReceiveGiving();
		}
	}

	public void handleReceiveAll(Player player, CGReceiveAll cgReceiveAll) {
		Loggers.mailLogger.debug("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]  全部领取邮件");
		
		List<Mail> mailList = player.getHuman().getHumanMailManager().getReceiveMailList();
		
		List<Long> mailIdList = new ArrayList<Long>();
		
		for(Mail mail : mailList){
			
			long mailId = mail.getDbId();
			
			if(mail.getHasAttachment()==MailHasAttachment.NOTHING){
				Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]处理邮件["+mailId+"]奖励不存在");
				//player.getHuman().sendSystemMessage(LangConstants.MAIL_REWARD_ISNOTEXIST);
				continue;
			}
			if(mail.getMailStatus()== MailStatus.GET){
				Loggers.mailLogger.warn("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]处理邮件["+mailId+"]奖励 已经处理过");
				continue;
			}
		
			//下发奖励给玩家
			List<RandRewardData> randRewardDataList=mail.getRandRewardDataList();
			CommonLogic.getInstance().giveRandReward(player.getHuman(), randRewardDataList,LogReasons.GoldLogReason.MAIL_REWARD,LogReasons.DiamondLogReason.MAIL_REWARD,LogReasons.CharmLogReason.MAIL_REWARD,LogReasons.ItemLogReason.MAIL_REWARD,true);
			mail.setMailStatus(MailStatus.GET);
			mail.setModified();
			/**
			 * 同步到收集系统
			 */
			for(RandRewardData RandRewardData:randRewardDataList){
				//大于100的都是道具
				if(RandRewardData.getRewardId() > 100){ 
					ItemTemplate ItemTemplate = Globals.getItemService().getItemTemplWithId(RandRewardData.getRewardId());
					if(ItemTemplate == null){
						continue;
					}
					if(ItemTemplate.getPoolType() == 1 || ItemTemplate.getPoolType() == 2 || ItemTemplate.getPoolType() == 3){
						player.getHuman().getHumanCollectManager().syncToCollect(RandRewardData.getRewardId(), RandRewardData.getRewardCount());
					}
				}
			}
			if(mail.getMailType() == MailTypeEnum.PLAYER_GIFT){
				player.getHuman().getHumanAchievementManager().updateReceiveGiving();
			}
			mailIdList.add(mailId);
		}
		
		player.sendMessage(player.getHuman().getHumanBagManager().buildHumanBagInfoData());
		GCReceiveAll message = new GCReceiveAll();
		long[] ids = new long[mailIdList.size()];
		for(int i = 0;i < mailIdList.size();i++){
			ids[i] = mailIdList.get(i);
		}
		message.setMailIdList(ids);
		player.sendMessage(message);
		
	}

	public void handleLoadSendList(Player player, CGLoadSendList cgLoadSendList) {
		if(player.getHuman()==null) return;
		int mailKindId=cgLoadSendList.getMailKind();
		mailList(player,mailKindId,"send");
		
	}

	public void handleDeleteSendMail(Player player,
			CGDeleteSendMail cgDeleteSendMail) {
		if(player.getHuman()==null) return;
		logger.debug("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]玩家删除邮件");
		long[] mailIds=cgDeleteSendMail.getMailIdList();
		if(!MailLogic.getInstance().checkSendMailDelete(player,mailIds)) return;
		MailLogic.getInstance().deleteSendMails(player, mailIds);
		GCDeleteMail gcDeleteMail=new GCDeleteMail();
		player.getHuman().sendMessage(gcDeleteMail);
		
	}

	/**
	 * 撤销邮件
	 * @param player
	 * @param cgDeleteSendCancelMail
	 */
	public void handleDeleteSendCancelMail(Player player,
			CGDeleteSendCancelMail cgDeleteSendCancelMail) {
		if(player.getHuman()==null) return;
		logger.debug("玩家id["+player.getHuman().getCharId()+"]玩家名称["+player.getHuman().getName()+"]玩家删除邮件");
		long[] mailIds=cgDeleteSendCancelMail.getMailIdList();
		
		//查询 接收邮件的用户
		for(long mailId:mailIds){
			Mail mail = MailLogic.getInstance().getSendMail(player, mailId);
			
			Player toPlayer = Globals.getOnlinePlayerService().getPlayerByPassportId(mail.getRecId());
			if(toPlayer != null){
				
				if(!MailLogic.getInstance().checkReceiveMailDelete(toPlayer,mailIds)){
					continue;
				}
				MailLogic.getInstance().deleteMails(toPlayer, mailIds);
				//每种类型 推送一次
				for(int i=0;i<MailTypeEnum.values().length;i++){
					MailTypeEnum mailType = MailTypeEnum.valueOf(i);
					List<Mail> mailList=player.getHuman().getHumanMailManager().getMailListByMailKind(mailType);
					List<MailInfoData> mailInfoDataList=MailLogic.getInstance().buildMailInfoDataList(player.getHuman(), mailList);
					GCLoadMailList gcLoadMailList=new GCLoadMailList();
					gcLoadMailList.setMailKind(mailType.getIndex());
					gcLoadMailList.setMailInfoDataList(mailInfoDataList.toArray(new MailInfoData[mailInfoDataList.size()]));
					toPlayer.sendMessage(gcLoadMailList);
				}
			}
		}
		
		if(!MailLogic.getInstance().checkSendMailDelete(player,mailIds)) return;
			MailLogic.getInstance().deleteSendMails(player, mailIds);
		
		
		//每种类型 推送一次
		for(int i=0;i<MailTypeEnum.values().length;i++){
			MailTypeEnum mailType = MailTypeEnum.valueOf(i);
			List<Mail> mailList=player.getHuman().getHumanMailManager().getSendMailListByMailKind(mailType);
			List<MailInfoData> mailInfoDataList=MailLogic.getInstance().buildMailInfoDataList(player.getHuman(), mailList);
			GCLoadSendList gcLoadSendList=new GCLoadSendList();
			gcLoadSendList.setMailKind(mailType.getIndex());
			gcLoadSendList.setMailInfoDataList(mailInfoDataList.toArray(new MailInfoData[mailInfoDataList.size()]));
			player.sendMessage(gcLoadSendList);
		}
		
		
	}

}
