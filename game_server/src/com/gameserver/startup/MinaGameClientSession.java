package com.gameserver.startup;

import org.apache.mina.common.IoSession;

import com.common.constants.Loggers;
import com.common.constants.SharedConstants;
import com.core.msg.IMessage;
import com.core.session.MinaSession;
import com.core.util.IpUtils;
import com.gameserver.player.Player;

/**
 * 与 GameServer 连接的客户端的会话信息
 * @author Thinker
 */
public class MinaGameClientSession extends MinaSession implements GameClientSession
{
	/** 当前会话绑定的玩家对象，登录验证成功之后实例化 */
	private Player player;

	public MinaGameClientSession(IoSession s) 
	{
		super(s);
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		if(player!=null){
			player.asyncNetTime();
		}
		return player;
	}

	@Override
	public void write(IMessage msg)
	{
		if (session == null || !session.isConnected())
		{
			return;
		}
		
		final IoSession _session = this.session;
		if (_session != null) 
		{
			if (GameServerRuntime.isWriteTrafficControl()) 
			{
				// 检查玩家的输出缓存是否达到上限,如果已经达到,则关闭连接,以此避免那些
				// 较慢连接对服务器的影响
				final int _sessionWriteSize = session.getScheduledWriteBytes();
				if (_sessionWriteSize > GameServerRuntime.MAX_WRITE_BYTES_INQUEUE) 
				{
					Loggers.msgLogger.warn("SessionBufferSize" + msg);
					session.close();
					return;
				}
			}
			
			if (Loggers.msgLogger.isDebugEnabled())
			{
				if (player.getHuman() != null)
				{
					Loggers.msgLogger.debug("[[ Send ]] " + msg + " to player : "
							+ player.getHuman().getName() + ", uuid : "
							+ player.getHuman().getUUID() + ", id : "
							+ player.getHuman().getId());
				} else
				{
					Loggers.msgLogger.debug("[[ Send ]] " + msg);
				}
			}
			_session.write(msg);
			Loggers.msgLogger.debug("[[ length ]] " + msg.getLength());
			SharedConstants.downlinkLen=SharedConstants.downlinkLen+msg.getLength();
			SharedConstants.downlinkCount=SharedConstants.downlinkCount+1;
		}
	}

	@Override
	public String getIp()
	{
		return IpUtils.getIp(this.getIoSession());
	}
}
