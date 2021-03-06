package com.gameserver.slot.redismsg;

import com.gameserver.common.Globals;
import com.gameserver.player.Player;
import com.gameserver.redis.IRedisMessage;

public class ChangeInfo2 implements IRedisMessage {
		
	   private long passportId;
	    
		private Long playerId;
		
		private String img;
		
		private int level;
		
		private String countries;
		
		private String name;
		
		
		
	  public long getPassportId() {
			return passportId;
		}



		public void setPassportId(long passportId) {
			this.passportId = passportId;
		}



	public Long getPlayerId() {
			return playerId;
		}



		public void setPlayerId(Long playerId) {
			this.playerId = playerId;
		}



		public String getImg() {
			return img;
		}



		public void setImg(String img) {
			this.img = img;
		}



		public int getLevel() {
			return level;
		}



		public void setLevel(int level) {
			this.level = level;
		}



		public String getCountries() {
			return countries;
		}



		public void setCountries(String countries) {
			this.countries = countries;
		}


		

	public String getName() {
			return name;
		}



		public void setName(String name) {
			this.name = name;
		}



	@Override
	public void execute() {
		
		Player player =  Globals.getOnlinePlayerService().getPlayerByPassportId(passportId);
        if(player != null){
        	  Globals.getSlotRoomService().sendRoomMessage2(player, playerId, img, countries, level,name);
        }
	}

}
