 package com.gameserver.recharge.handler;



import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.common.LogReasons;
import com.common.constants.LangConstants;
import com.common.constants.Loggers;
import com.game.webserver.recharge.CheckGooglePlayRechargeParam;
import com.game.webserver.recharge.CheckGooglePlayRechargeRes;
import com.game.webserver.recharge.CheckIOSRechargeParam;
import com.game.webserver.recharge.CheckIOSRechargeRes;
import com.gameserver.common.Globals;
import com.gameserver.common.data.RandRewardData;
import com.gameserver.human.Human;
import com.gameserver.human.manager.HumanRechargeManager;
import com.gameserver.player.Player;
import com.gameserver.player.enums.ChannelTypeEnum;
import com.gameserver.recharge.HumanRechargeOrder;
import com.gameserver.recharge.RechargeLogic;
import com.gameserver.recharge.async.AsyncHttpOperation;
import com.gameserver.recharge.async.AsyncHttpOperation.IAsyncHttpCallBack;
import com.gameserver.recharge.data.AmazonDelivery;
import com.gameserver.recharge.data.HumanRechargeOrderInfoData;
import com.gameserver.recharge.data.PurchaseData;
import com.gameserver.recharge.enums.OrderStatus;
import com.gameserver.recharge.enums.TopUpType;
import com.gameserver.recharge.https.AmazonVerifyReceipt;
import com.gameserver.recharge.https.AuthCodeInfo;
import com.gameserver.recharge.https.Parameter;
import com.gameserver.recharge.https.TradeQuery;
import com.gameserver.recharge.msg.CGCouponExist;
import com.gameserver.recharge.msg.CGOrderAmazon;
import com.gameserver.recharge.msg.CGOrderAmazonDelivery;
import com.gameserver.recharge.msg.CGRequestOrder;
import com.gameserver.recharge.msg.CGRequestOrderMol;
import com.gameserver.recharge.msg.CGRequestOrderMycard;
import com.gameserver.recharge.msg.CGValidateOrder;
import com.gameserver.recharge.msg.CGValidateOrderMol;
import com.gameserver.recharge.msg.CGValidateOrderMycard;
import com.gameserver.recharge.msg.GCCouponExist;
import com.gameserver.recharge.msg.GCRequestOrder;
import com.gameserver.recharge.msg.GCValidateOrder;
import com.gameserver.recharge.template.RechargeTemplate;

/**
 * 充值
 * @author wayne
 *
 */
public class RechargeMessageHandler implements IAsyncHttpCallBack<CheckGooglePlayRechargeRes> {

	private Logger logger = Loggers.rechargeLogger;
	
	/**
	 * 处理申请订单
	 * @param player
	 * @param cgRequestOrder
	 */
	public void handleRequestOrder(Player player, CGRequestOrder cgRequestOrder) {
		
		Human human = player.getHuman();
		
		// 数据表里面的PID
		int productId = cgRequestOrder.getProductId();
		
		
		RechargeTemplate  rechargeTemplate =  Globals.getRechargeService().getTemplate(player.getChannelType().getIndex(), productId);
		
		if(rechargeTemplate == null){
			player.sendSystemMessage(LangConstants.PRODUCT_NO_EXIST);
			logger.warn("玩家["+human.getPassportId()+"]申请订单,产品id["+productId+"]不存在");
			return;
		}
		
		//创建订单
		HumanRechargeOrder order = Globals.getRechargeService().generateOrder(human,productId,rechargeTemplate.getNum());
		order.setTopUpType(TopUpType.COMMON.getIndex());
		
		order.setModified();
		
		//加入角色未处理订单列表
		human.getHumanRechargeManager().addOrder(order);
		
		HumanRechargeOrderInfoData humanRechargeOrderInfoData= HumanRechargeOrderInfoData.convertFromHumanRechargeOrder(order);
		GCRequestOrder gcRequestOrder = new GCRequestOrder();
		gcRequestOrder.setOrder(humanRechargeOrderInfoData);
		player.sendMessage(gcRequestOrder);
		logger.info("玩家["+human.getPassportId()+"]申请订单 订单号["+order.getId()+"],产品id["+productId+"]");
		Globals.getLogService().sendRechargeLog(human, LogReasons.RechargeLogReason.REQUEST_ORDER,  LogReasons.RechargeLogReason.REQUEST_ORDER.getReasonText(), order.getId(), productId,order.getCost());
		
	}
	
	
	

	/**
	 * 处理验证订单
	 * @param player
	 * @param cgValidateOrder
	 * @throws UnsupportedEncodingException 
	 */
	public void handleValidateOrder(Player player,CGValidateOrder cgValidateOrder)  {

		Human human = player.getHuman();
		PurchaseData purchaseData = null;
		
		HumanRechargeManager humanRechargeManager= human.getHumanRechargeManager();
		logger.info("开始验证订单... ...userId:"+player.getPassportId()+"--当前渠道："+player.getChannelType());
		if(player.getChannelType() == ChannelTypeEnum.IOS18){
			logger.info("开始验证订单... IOS ...userId");
			String orderIdStr = cgValidateOrder.getSignature();
			logger.info("-----------orderId::"+orderIdStr);
			long orderId = Long.parseLong(orderIdStr);
			HumanRechargeOrder order  = humanRechargeManager.getOrderByChannelOrderId(orderIdStr);
			//检查订单是否存在
			if(order != null)
			{
				logger.warn("玩家["+human.getPassportId()+"]验证订单,渠道订单id["+order.getChannelOrderId()+"]已经验证");
				GCValidateOrder gcValidateOrder = new GCValidateOrder();
				gcValidateOrder.setOrderId(orderId);
				gcValidateOrder.setResult(1);
				gcValidateOrder.setRandRewardList(new RandRewardData[0]);
				human.sendMessage(gcValidateOrder);
				return;
			}
			if(Globals.getServerConfig().isTest()){
				//AAA:ios修改
				/*purchaseData = JSON.parseObject(cgValidateOrder.getPurchaseData(),PurchaseData.class);
				if(purchaseData.getProductId().equalsIgnoreCase("android.test.purchased"))
				{*/
					HumanRechargeOrder orderH =humanRechargeManager.getOrderById(Long.valueOf(orderIdStr));
					if(orderH != null){
						RechargeLogic.onRecharge(player,orderH.getId(),orderH.getCost());
					}
					return;
//				}
			}
		  logger.info("IOS-1--订单... ...userId:"+player.getPassportId());
		  ios(player,orderId,cgValidateOrder.getPurchaseData());
		}else{
			try{
				logger.info("安卓-2--订单... ...userId");
				purchaseData = JSON.parseObject(cgValidateOrder.getPurchaseData(),PurchaseData.class);
			}catch(Exception e){
				logger.error("玩家["+human.getPassportId()+"]请求验证订单,数据["+cgValidateOrder.getPurchaseData()+"],签名["+cgValidateOrder.getSignature()+"]有问题,异常["+e.getMessage()+"]");
				return;
			}
			
			String orderIdStr = purchaseData.getDeveloperPayload();
			
			long orderId = Long.parseLong(orderIdStr);
		
		
			HumanRechargeOrder order =humanRechargeManager.getOrderById(orderId);
			
			logger.info("玩家["+human.getPassportId()+"]请求验证订单,数据["+cgValidateOrder.getPurchaseData()+"],签名["+cgValidateOrder.getSignature()+"]");
			
			//检查订单是否存在
			if(order == null){
				player.sendSystemMessage(LangConstants.ORDER_NO_EXIST);
				logger.warn("玩家["+human.getPassportId()+"]验证订单,订单id["+orderId+"]不存在");
				GCValidateOrder gcValidateOrder = new GCValidateOrder();
				gcValidateOrder.setOrderId(orderId);
				gcValidateOrder.setResult(0);
				gcValidateOrder.setRandRewardList(new RandRewardData[0]);
				human.sendMessage(gcValidateOrder);
				return;
			}

			//检查订单是否已经处理
			if(order.getOrderStatus() != OrderStatus.NON_VALIDATE )
			{
				player.sendSystemMessage(LangConstants.ORDER_ALEADY_HANDLE);
				logger.warn("玩家["+human.getPassportId()+"]验证订单,订单id["+orderId+"]已经验证过");
				GCValidateOrder gcValidateOrder = new GCValidateOrder();
				gcValidateOrder.setOrderId(orderId);
				gcValidateOrder.setResult(0);
				gcValidateOrder.setRandRewardList(new RandRewardData[0]);
				human.sendMessage(gcValidateOrder);
				return;
			}
			
			RechargeTemplate rechargeTemplate =  Globals.getRechargeService().getTemplate(player.getChannelType().getIndex(), order.getProductId());
			if(rechargeTemplate == null)
			{
				player.sendSystemMessage(LangConstants.PRODUCT_NO_EXIST);
				logger.warn("玩家["+human.getPassportId()+"]验证订单,产品id["+order.getProductId()+"]不存在");
				GCValidateOrder gcValidateOrder = new GCValidateOrder();
				gcValidateOrder.setOrderId(orderId);
				gcValidateOrder.setResult(0);
				gcValidateOrder.setRandRewardList(new RandRewardData[0]);
				human.sendMessage(gcValidateOrder);
				return;
			}
			
			//检查充值项目是否使用
			if(rechargeTemplate.getUsed()==0){
				player.sendSystemMessage(LangConstants.PRODUCT_NO_EXIST);
				logger.warn("玩家["+human.getPassportId()+"]验证订单,充值项["+order.getProductId()+"]不存在");
				GCValidateOrder gcValidateOrder = new GCValidateOrder();
				gcValidateOrder.setOrderId(orderId);
				gcValidateOrder.setResult(0);
				gcValidateOrder.setRandRewardList(new RandRewardData[0]);
				human.sendMessage(gcValidateOrder);
				return;
			}
		
			logger.info("玩家["+human.getPassportId()+"]请求验证订单,包名["+purchaseData.getPackageName()+"],订单id["+orderId+"],签名["+cgValidateOrder.getSignature()+"]");
	
			googleplay(player,order,purchaseData.getPackageName(),purchaseData.getProductId(),purchaseData.getPurchaseToken());
	
		}
	}
	
	
	private void ios(Player player,long orderId,String receiptData){
		Human human = player.getHuman();
		if(human==null){
			logger.warn("玩家["+player.getPassportId()+"]已经下线");
			return;
		}
		
		
		logger.warn("IOS 去登录服务验证前：["+player.getPassportId()+"]");
		
		//发送异步调用
		CheckIOSRechargeParam checkIOSRechargeParam = new CheckIOSRechargeParam(Globals.getLocalConfig().getRequestDomain(),"api/recharge/ios.json",true);
		checkIOSRechargeParam.setOrderId(orderId);
		checkIOSRechargeParam.setReceiptData(receiptData);
		checkIOSRechargeParam.setSandbox(Globals.getServerConfig().isTest());
		logger.warn("IOS 去登录服务验证后：["+player.getPassportId()+"]"+orderId);
		logger.warn("IOS 去登录服务验证后：["+player.getPassportId()+"]"+receiptData);
		
		
		AsyncHttpOperation<CheckIOSRechargeRes> asyncHttpOper = new AsyncHttpOperation<CheckIOSRechargeRes>(player,checkIOSRechargeParam, new IOSRechargeCallBack());
		Globals.getAsyncService().createGlobalAsyncOperationAndExecuteAtOnce(asyncHttpOper);
		
	}

	private void googleplay(Player player,HumanRechargeOrder order,String packageName,String productIdStr,String token){
		Human human = player.getHuman();
		if(human==null){
			logger.warn("玩家["+player.getPassportId()+"]已经下线");
			return;
		}
		
		
		if(Globals.getServerConfig().isTest())
		{
			if(productIdStr.equalsIgnoreCase("android.test.purchased"))
			{
				logger.info("玩家["+player.getPassportId()+"]测试充值,产品id["+order.getProductId()+"]"+productIdStr);
				RechargeLogic.onRecharge(player,order.getId(),order.getCost());
				return;
			}
		}

		//发送异步调用
		CheckGooglePlayRechargeParam checkGooglePlayRechargeParam = new CheckGooglePlayRechargeParam(Globals.getLocalConfig().getRequestDomain(),"api/recharge/googleplay.json",true);
		checkGooglePlayRechargeParam.setGoogleId(order.getChannelId());
		checkGooglePlayRechargeParam.setPackageName(packageName);
		checkGooglePlayRechargeParam.setProductId(productIdStr);
		checkGooglePlayRechargeParam.setToken(token);
		
		
		AsyncHttpOperation<CheckGooglePlayRechargeRes> asyncHttpOper = new AsyncHttpOperation<CheckGooglePlayRechargeRes>(player,checkGooglePlayRechargeParam,RechargeHandlerFactory.getHandler());
		Globals.getAsyncService().createGlobalAsyncOperationAndExecuteAtOnce(asyncHttpOper);
		
	}
	
	/**
	 * 订单取消
	 * @param player
	 * @param orderId
	 */
	private void onCancel(Player player,long orderId){
		Human human = player.getHuman();
		if(human==null){
			logger.warn("玩家["+player.getPassportId()+"]已经下线");
			return;
		}
		
		HumanRechargeManager humanRechargeManager = human.getHumanRechargeManager();
		HumanRechargeOrder order =  humanRechargeManager.getOrderById(orderId);
		if(order == null){
			logger.warn("玩家["+player.getPassportId()+"]"+"回调 orderid["+ orderId+"]不存在");
			return;
		}
		humanRechargeManager.cancelOrder(order);
		
		GCValidateOrder gcValidateOrder = new GCValidateOrder();
		gcValidateOrder.setOrderId(orderId);
		gcValidateOrder.setResult(0);
		gcValidateOrder.setRandRewardList(new RandRewardData[0]);
		human.sendMessage(gcValidateOrder);
		logger.info("玩家["+human.getPassportId()+"]取消 订单号["+order.getId()+"],产品id["+order.getProductId()+"]");
		
	}
	



	@Override
	public void onFinished(Player player,int errorCode, CheckGooglePlayRechargeRes res) {
		// TODO Auto-generated method stub
		Human human = player.getHuman();
		if(human==null){
			logger.warn("玩家["+player.getPassportId()+"]已经下线");
			return;
		}
		
		if(errorCode!=0){
			logger.warn("玩家["+player.getPassportId()+"]请求本地服务器数据失败");
			return ;
		}
		
		long orderId=0L;
		try{
			orderId  = Long.parseLong(res.getDeveloperPayload());
		}catch(Exception e){
			logger.error("玩家["+human.getPassportId()+"]订单回调,订单数据错误["+res.getDeveloperPayload()+"]");
			return;
		}
		
		//付款无效
		if(res.getPurchaseState()!=0){
			logger.warn("玩家["+player.getPassportId()+"]付款无效");
			onCancel(player,orderId);
			return;
		}
		
		//已经使用过了
		if(res.getConsumptionState()==1){
			logger.warn("玩家["+player.getPassportId()+"]使用过了");
			GCValidateOrder gcValidateOrder = new GCValidateOrder();
			gcValidateOrder.setOrderId(orderId);
			gcValidateOrder.setResult(0);
			gcValidateOrder.setRandRewardList(new RandRewardData[0]);
			human.sendMessage(gcValidateOrder);
			return;
		}
		logger.info("-2-安卓-验证订单通过... 开始发货...userId:"+player.getPassportId());
		
		RechargeLogic.onRecharge(player,orderId,0);
		
	}
	

	
	class IOSRechargeCallBack implements IAsyncHttpCallBack<CheckIOSRechargeRes>{

		@Override
		public void onFinished(Player player, int errorCode,
				CheckIOSRechargeRes res) {
			// TODO Auto-generated method stub
			Human human = player.getHuman();
			if(human==null){
				logger.warn("玩家["+player.getPassportId()+"]已经下线");
				return;
			}
			
			if(errorCode!=0){
				logger.warn("玩家["+player.getPassportId()+"]请求本地服务器数据失败");
				return ;
			}
			
			long orderId=0L;
			try{
				orderId  = Long.parseLong(res.getDeveloperPayload());
			}catch(Exception e){
				logger.error("玩家["+human.getPassportId()+"]订单回调,订单数据错误["+res.getDeveloperPayload()+"]");
				return;
			}
			
			RechargeTemplate tempRechargeTemplate = Globals.getRechargeService().rechargeTemplateBySkuId(player.getChannelType(),res.getProductId());
			
			HumanRechargeOrder order = Globals.getRechargeService().generateOrder(human,tempRechargeTemplate.getPid(),tempRechargeTemplate.getNum());
			
			order.setChannelOrderId(res.getDeveloperPayload());
			order.setModified();
			human.getHumanRechargeManager().addOrder(order);
			
			logger.info("-2-IOS-验证订单通过... 开始发货...userId:"+player.getPassportId());
			RechargeLogic.onRecharge(player,order.getId(),order.getCost());
		}
		
	}

	/**
	 * mycard 支付返回
	 * @param player
	 * @param cgValidateOrderMycard
	 */
	public void handleValidateOrderMycard(Player player, CGValidateOrderMycard cgValidateOrderMycard) {
		
	
		long orderId = cgValidateOrderMycard.getOrderId();
		
		Human human = player.getHuman();
		
		HumanRechargeOrder hro = human.getHumanRechargeManager().getOrderById(orderId);
		
		if(hro != null && !hro.getAuthCode().equals("")){
			
			Globals.getAsyncService().createGlobalAsyncOperationAndExecuteAtOnce(new TradeQuery(human,hro.getAuthCode()));
		}
	}




	public void handleRequestOrderMycard(Player player, CGRequestOrderMycard cgRequestOrderMycard) {
		Human human = player.getHuman();
		
		// 数据表里面的PID
		int productId = cgRequestOrderMycard.getProductId();
		
		
		RechargeTemplate  rechargeTemplate =  Globals.getRechargeService().getTemplate(player.getChannelType().getIndex(), productId);
		
		if(rechargeTemplate == null){
			player.sendSystemMessage(LangConstants.PRODUCT_NO_EXIST);
			logger.warn("玩家["+human.getPassportId()+"]申请订单,产品id["+productId+"]不存在");
			return;
		}
		
		//创建订单
		HumanRechargeOrder order = Globals.getRechargeService().generateOrder(human,productId,rechargeTemplate.getNum());
		order.setTopUpType(TopUpType.MYCARD.getIndex());
		order.setModified();
		
		//加入角色未处理订单列表
		human.getHumanRechargeManager().addOrder(order);
		
		HumanRechargeOrderInfoData humanRechargeOrderInfoData= HumanRechargeOrderInfoData.convertFromHumanRechargeOrder(order);
		GCRequestOrder gcRequestOrder = new GCRequestOrder();
		gcRequestOrder.setOrder(humanRechargeOrderInfoData);
		player.sendMessage(gcRequestOrder);
		logger.info("玩家["+human.getPassportId()+"]申请订单 订单号["+order.getId()+"],产品id["+productId+"]");
		Globals.getLogService().sendRechargeLog(human, LogReasons.RechargeLogReason.REQUEST_ORDER,  LogReasons.RechargeLogReason.REQUEST_ORDER.getReasonText(), order.getId(), productId,order.getCost());
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("FacServiceId", Parameter.facServiceId);
    	paramMap.put("FacTradeSeq", String.valueOf(order.getId()));
    	paramMap.put("TradeType", "1");
    	paramMap.put("CustomerId", String.valueOf(human.getPassportId()));
    	paramMap.put("ProductName", rechargeTemplate.getProductId());
    	paramMap.put("Amount", String.valueOf(rechargeTemplate.getNum()));
    	paramMap.put("Currency", "TWD");
    	paramMap.put("SandBoxMode", "true");
    
		Globals.getAsyncService().createGlobalAsyncOperationAndExecuteAtOnce(new AuthCodeInfo(human,paramMap,order.getId()));
	}



    /**
     * 亚马逊验证订单
     * @param player
     * @param cgOrderAmazon
     */
	public void handleOrderAmazon(Player player, CGOrderAmazon cgOrderAmazon) {
		
        long orderId = cgOrderAmazon.getOrderId();
		
		Human human = player.getHuman();
		
		HumanRechargeOrder hro = human.getHumanRechargeManager().getOrderById(orderId);
		
		if(hro != null){
			//客户亚马逊ID
			String userId = cgOrderAmazon.getUserId();
			//收据ID
			String receiptId = cgOrderAmazon.getReceiptId();
			
		   Globals.getAsyncService().createGlobalAsyncOperationAndExecuteAtOnce(new AmazonVerifyReceipt(human,userId,receiptId,orderId));
		}
	}



    /**
     * 处理亚马逊没有验证过的订单
     * @param player
     * @param cgOrderAmazonDelivery
     */
	public void handleOrderAmazonDelivery(Player player, CGOrderAmazonDelivery cgOrderAmazonDelivery) {
		
		AmazonDelivery[] list = cgOrderAmazonDelivery.getAmazonDeliveryList();
		
		logger.info("AmazonDelivery() ===  "+JSON.toJSONString(list));
		
		Human human = player.getHuman();
		
		for(AmazonDelivery ad : list){
			//客户亚马逊ID
			String userId = ad.getUserId();
			//收据ID
			String receiptId = ad.getReceiptId();
			//产品ID
			int productId = ad.getProductId();
			
			HumanRechargeOrder hro = human.getHumanRechargeManager().getOrderByProductId(productId,human.getPlayer().getChannelType().getIndex());
			
			if(hro != null){
				 Globals.getAsyncService().createGlobalAsyncOperationAndExecuteAtOnce(new AmazonVerifyReceipt(human,userId,receiptId,hro.getId()));
			}else{
				logger.error("Amazon == error == "+JSON.toJSONString(ad));
			}
			
		}
	}



    /**
     * 获取MOL订单
     * @param player
     * @param cgRequestOrderMol
     */
	public void handleRequestOrderMol(Player player, CGRequestOrderMol cgRequestOrderMol) {
		
        Human human = player.getHuman();
		
		// 数据表里面的PID
		int productId = cgRequestOrderMol.getProductId();
		
		
		RechargeTemplate  rechargeTemplate =  Globals.getRechargeService().getTemplate(player.getChannelType().getIndex(), productId);
		
		if(rechargeTemplate == null){
			player.sendSystemMessage(LangConstants.PRODUCT_NO_EXIST);
			logger.warn("玩家["+human.getPassportId()+"]申请订单,产品id["+productId+"]不存在");
			return;
		}
		
		//创建订单
		HumanRechargeOrder order = Globals.getRechargeService().generateOrder(human,productId,rechargeTemplate.getNum());
		order.setTopUpType(TopUpType.MOL.getIndex());
		order.setModified();
		
		//加入角色未处理订单列表
		human.getHumanRechargeManager().addOrder(order);
		
		HumanRechargeOrderInfoData humanRechargeOrderInfoData= HumanRechargeOrderInfoData.convertFromHumanRechargeOrder(order);
		GCRequestOrder gcRequestOrder = new GCRequestOrder();
		gcRequestOrder.setOrder(humanRechargeOrderInfoData);
		player.sendMessage(gcRequestOrder);
		logger.info("玩家["+human.getPassportId()+"]申请订单 订单号["+order.getId()+"],产品id["+productId+"]");
		Globals.getLogService().sendRechargeLog(human, LogReasons.RechargeLogReason.REQUEST_ORDER,  LogReasons.RechargeLogReason.REQUEST_ORDER.getReasonText(), order.getId(), productId,order.getCost());
		
	}



    /**
     * 验证MOL订单
     * @param player
     * @param cgValidateOrderMol
     */
	public void handleValidateOrderMol(Player player, CGValidateOrderMol vom) {
		
		/** 订单号 */
		long orderId = vom.getOrderId();
		/** 币种 */
		String currencyCode = vom.getCurrencyCode();
		/** 单位是分 */
		int amount = vom.getAmount();
		/** 发放的游戏币PID为-1的时候有效 */
		double virtualCurrencyAmount = Double.valueOf(vom.getVirtualCurrencyAmount());
		
		String paymentId = vom.getPaymentId();
		
		Human human = player.getHuman();
		
		HumanRechargeOrder hro = human.getHumanRechargeManager().getOrderById(orderId);
		
		if(hro == null){
			return;
		}
		hro.setCurrencyCode(currencyCode);
		hro.setAmountmol(amount);
		hro.setPaymentIdmol(paymentId);
		if(hro.getProductId() == -1){//点卡支付
			RechargeLogic.onRechargeMOL(player,orderId,virtualCurrencyAmount,amount);
		}else{//普通购买商品支付
			RechargeLogic.onRecharge(player,orderId,0);
		}
		
		hro.setModified();
		
	}




	public void handleCouponExist(Player player, CGCouponExist cgCouponExist) {
		GCCouponExist gCCouponExist = new GCCouponExist();
		Date date = player.getHuman().getCouponDurationDate();
		long now = Globals.getTimeService().now();
		if(date != null && now >= date.getTime()){//没有优惠券
			gCCouponExist.setCouponExist(0);
		}else{//有优惠券
			gCCouponExist.setCouponExist(1);
			//付费新手引导 通知客户端
			player.getHuman().getHumanPayguideManager().sendCouponPayGuide();
		}
		player.sendMessage(gCCouponExist);
	}
	
}
