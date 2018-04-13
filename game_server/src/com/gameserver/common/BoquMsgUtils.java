package com.gameserver.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gameserver.common.vo.BoquCode;
import com.gameserver.weixin.util.HttpClientUtil;

/**
 * 调用博趣的接口 发送短信验证码
 * @author JavaServer
 *
 */
public class BoquMsgUtils {

	
	public static Logger log = LogManager.getLogger(BoquMsgUtils.class);
	public static String sendMsg(String phoneNum){
		Random random = new Random();
		String num = "";
		for(int i=0;i<6;i++){
			num+=random.nextInt(10);
		}
		
		String returnNum = num;
		String url = Globals.getServerConfig().getBoquSendMsgUrl();
		HashMap<String,String> params = new HashMap<String,String>();
		String client_id = Globals.getServerConfig().getClientId()+"";
		String now = new Date().getTime()/1000+"";
		num = "【圣堂物语】验证码"+num+"，请用此验证码进行圣堂物语身份验证";
		params.put("client_id", client_id);
		params.put("mobile", phoneNum);
		params.put("message", num);
		params.put("timestamp", now);
		params.put("sign", getJSONStr(phoneNum,num,client_id,now));
		
		log.info("当前url:"+url);
		log.info(params.toString());
		String res = HttpClientUtil.postUrl(url, params);
		log.info("res:"+res);
		JSONObject jb = JSON.parseObject(res);
		if(jb.getInteger("status_code").intValue() == 200){
			return returnNum;
		}
		
		return null;
	}
	
	private static String getJSONStr(String phoneNum,String num,String client_id,String now){
		BoquCode boquCode = new BoquCode();
		boquCode.setClient_id(client_id);
//		boquCode.setMessage(num);
		boquCode.setMobile(phoneNum);
		boquCode.setTimestamp(now);
		String client_secret = Globals.getServerConfig().getClientSecret();
		String ss = JSON.toJSONString(boquCode)+client_secret;
		log.info(ss);
		return MD5Utils.getMD5(ss).toLowerCase();
	}
	
	  
	/*public static void main(String[] args){
		sendMsg("18201100545");
	}*/
}
