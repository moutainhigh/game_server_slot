package com.gameserver.vip.msg;

import java.util.HashMap;
import java.util.Map;

import com.gameserver.common.msg.MessageType;
import com.common.MessageMappingProvider;

/**
 *  Generated by MessageCodeGenerator,don't modify please.
 *  Need to register in<code>GameMessageRecognizer#init</code>
 */
public class VipMsgMappingProvider implements MessageMappingProvider {

	@Override
	public Map<Short, Class<?>> getMessageMapping() {
		Map<Short, Class<?>> map = new HashMap<Short, Class<?>>();
		map.put(MessageType.CG_VIP_BUY, CGVipBuy.class);
		map.put(MessageType.CG_VIP_GET, CGVipGet.class);
		map.put(MessageType.CG_VIP_CREATE_ROOM, CGVipCreateRoom.class);
		return map;
	}

}
