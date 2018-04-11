package com.gameserver.http.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import com.gameserver.common.Globals;
import com.gameserver.human.Human;
import com.gameserver.player.Player;

public class ChangeHumanLevel extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String passportId = req.getParameter("passportId");
		String level = req.getParameter("level");
		String gold = req.getParameter("gold");
		String type = req.getParameter("type");
		if(!NumberUtils.isNumber(passportId)){
			resp.getWriter().print("passportId 不正确-非数字");
			return;
		}
		Player player = Globals.getOnlinePlayerService().getPlayerByPassportId(Integer.valueOf(passportId));
		if(player == null){
			resp.getWriter().print(passportId+" 此用户未登录");
			return;
		}
		Human human = player.getHuman();
		if(NumberUtils.isNumber(level)){
			if(!"0".equals(level)){
				human.setLevel(Integer.valueOf(level));
			}
		}
		if(NumberUtils.isNumber(gold)){
			if("0".equals(type)){
				human.setGold(human.getGold()+Long.valueOf(gold).longValue());
			}else{
				human.setGold(Long.valueOf(gold));
			}
		}
		human.setModified();
		resp.getWriter().print("修改成功");
	}
}
