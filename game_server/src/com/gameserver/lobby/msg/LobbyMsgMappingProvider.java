package com.gameserver.lobby.msg;

import java.util.HashMap;
import java.util.Map;

import com.gameserver.common.msg.MessageType;
import com.common.MessageMappingProvider;

/**
 *  Generated by MessageCodeGenerator,don't modify please.
 *  Need to register in<code>GameMessageRecognizer#init</code>
 */
public class LobbyMsgMappingProvider implements MessageMappingProvider {

	@Override
	public Map<Short, Class<?>> getMessageMapping() {
		Map<Short, Class<?>> map = new HashMap<Short, Class<?>>();
		map.put(MessageType.CG_JACKPOT_LIST_DATA, CGJackpotListData.class);
		map.put(MessageType.CG_GAMETYPE_JACKPOT, CGGametypeJackpot.class);
		map.put(MessageType.CG_NEW_JACKPOT, CGNewJackpot.class);
		map.put(MessageType.CG_SLOT_NEW_JACKPOTS, CGSlotNewJackpots.class);
		map.put(MessageType.CG_ALL_SLOT_NEW_JACKPOTS, CGAllSlotNewJackpots.class);
		return map;
	}

}
