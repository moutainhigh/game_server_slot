package com.db.dao;

import java.util.List;

import com.core.orm.DBService;
import com.db.model.BazooAgentEntity;

public class BazooAgentDao extends BaseDao<BazooAgentEntity>{
	
	
	private static final String GET_THE_AGENT = "queryTheBazooAgent";

	public BazooAgentDao(DBService dbServ) {
		super(dbServ);
	}

	
	@Override
	protected Class<BazooAgentEntity> getEntityClazz() {
		return BazooAgentEntity.class;
	}
	
	
	

	public BazooAgentEntity getTheAgent(String passportId )
	{
		List<BazooAgentEntity> list =  this._dbServ.findByNamedQueryAndNamedParam(GET_THE_AGENT,new String[]{"passportId"},new Object[]{Long.valueOf(passportId)});
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
