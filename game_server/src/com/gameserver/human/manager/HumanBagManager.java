package com.gameserver.human.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.common.InitializeRequired;
import com.common.constants.Loggers;
import com.core.util.Assert;
import com.core.util.TimeUtils;
import com.db.model.HumanItemEntity;
import com.gameserver.common.Globals;
import com.gameserver.common.db.RoleDataHolder;
import com.gameserver.human.Human;
import com.gameserver.human.data.LevelMoreGiftData;
import com.gameserver.human.msg.GCLevelGiftMore;
import com.gameserver.item.Item;
import com.gameserver.item.ItemService;
import com.gameserver.item.data.ItemInfoData;
import com.gameserver.item.msg.GCHumanBagInfoData;
import com.gameserver.item.template.ItemTemplate;



/**
 * 背包
 * @author wayne
 *
 */
public class HumanBagManager implements RoleDataHolder, InitializeRequired {
	
	private Logger logger = Loggers.bagLogger;

	private static final int MAX_SLOT = 1024;
	private Human owner;

	private List<Item> itemList = new ArrayList<Item>();
	private ItemService itemService;

	/**
	 * 每次转动老虎机（或者其他方式，但目前 只在转动老虎机里） ，会 因为 levelGift 获取 道具或者金币 ,然后，通知前边 ，下边的Map是缓存 当时的 东西有啥，数量是多少
	 */
	private Map<Integer,Integer> itemIdNumMap = new HashMap<Integer,Integer>();
	/**
	 * 构造器
	 * 
	 * @param owner
	 */
	public HumanBagManager(Human owner) {
		this.owner = owner;
		this.itemService = Globals.getItemService();
	}
	
	public Human getOwner(){
		return this.owner;
	}
	

	
	public void load(){
	
		List<HumanItemEntity> humanItemEntityList=Globals.getDaoService().getHumanItemDao().getItemsByCharId(owner.getPassportId());
		
		if(humanItemEntityList!=null&&humanItemEntityList.size()>0){
			for(HumanItemEntity humanItemEntity:humanItemEntityList){
				Item item=new Item();
				item.setOwner(owner);
				item.fromEntity(humanItemEntity);
				this.itemList.add(item);
			}
		}
	}
	
	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		checkExpireItems();
	}

	@Override
	public void checkAfterRoleLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBeforeRoleEnter() {
		// TODO Auto-generated method stub
		
	}
	
	public void checkExpireItems(){
		Iterator<Item> iter = itemList.iterator();
		while(iter.hasNext()){
			Item item =iter.next();
			if(item.ifExpire())
			{
				item.onDelete();
				iter.remove();
				logger.info("玩家["+owner.getPassportId()+"]移除过期道具["+item.getTemplateId()+"]");
			}
		}
	}
	
	
	/**
	 * 移除物品
	 * @param templateId
	 * @param count
	 * @return
	 */
	public void removeItem(int templateId,int count)
	{
		Assert.isTrue(count>0,"道具删除不能是0或负数");
		ItemTemplate itemTemplate =  itemService.getItemTemplWithId(templateId);
		Assert.notNull(itemTemplate,"找不到道具");
		Assert.notNull(getCount(templateId) < count,"道具不足 ");
		
		
		Iterator<Item> it = itemList.iterator();
		while(it.hasNext())
		{
			Item item = it.next();
			if(item.getTemplateId() == templateId)
			{
				//身上多了
				if(count <item.getOverlap())
				{
					item.setOverlap(item.getOverlap()-count);
					item.setModified();
					break;
				}
				else
				{
					item.onDelete();
					it.remove();
				}
			}
		}

	}	
	
	/**
	 * 添加物品
	 * @param templateId
	 * @param count
	 */
	public List<Item> addItem(int templateId,int count)
	{
		Assert.isTrue(count>0,"道具添加不能是0或负数");
		ItemTemplate itemTemplate =  itemService.getItemTemplWithId(templateId);
		Assert.notNull(itemTemplate,"找不到道具");
		List<Item> tempItemList = new ArrayList<Item>();
		
		//判断是否能全放下
		int maxCanAdd =getMaxCanAdd(templateId);
		if(maxCanAdd!=-1 && maxCanAdd<count)
		{
			return tempItemList;
		}
		
		//延长时间
		if(itemTemplate.getTime()>0)
		{
			boolean found =false;
			Item item=null;
			for(Item tempItem :itemList)
			{
				if(tempItem.getTemplateId() == templateId)
				{
					found  = true;
					if(tempItem.ifExpire()){
						long tempBeginTime = Globals.getTimeService().now();
						tempItem.setBeginTime(tempBeginTime);
						tempItem.setDuration(itemTemplate.getTime()*TimeUtils.MIN);
					}
					else
						tempItem.setDuration(tempItem.getDuration() +itemTemplate.getTime()*TimeUtils.MIN );
					item = tempItem;
					break;
				}
			}
			if(!found)
			{
				
				item = Item.createNewInstance(templateId,1);
				item.setOwner(owner);
				item.setCharId(owner.getPassportId());
				item.active();
				item.setInDb(false);
				
				long tempBeginTime = Globals.getTimeService().now();
				item.setBeginTime(tempBeginTime);
				item.setDuration(itemTemplate.getTime()*TimeUtils.MIN);
				itemList.add(item);
		
			}
			item.setModified();
			tempItemList.add(item);
			return tempItemList;
		}
		
		if(itemTemplate.getOverLimit() == 1)
		{
			Item item = null;
			for(int i=0;i<count;i++)
			{
				item = Item.createNewInstance(templateId,1);
				item.setOwner(owner);
				item.setCharId(owner.getPassportId());
				item.active();
				item.setInDb(false);
				item.setModified();
				itemList.add(item);
				tempItemList.add(item);
			}
			
		
			return tempItemList;
		}
		
		int remain = count;
		//可以叠加的时候优先叠加
	
		for(Item tempItem :itemList)
		{
			if(tempItem.getTemplateId() == templateId)
			{
				int slotRemain = itemTemplate.getOverLimit() - tempItem.getOverlap();
				if(slotRemain <=0)
					continue;
				
				int tempAdd = 0;
				if(slotRemain>=remain)
				{
					tempAdd = remain;
				}else
				{
					tempAdd = slotRemain;
				}
				
				tempItemList.add(tempItem);
				tempItem.setOverlap(tempItem.getOverlap() +tempAdd );
				remain -= tempAdd;
				tempItem.setModified();
			}
		}
		
		//添加物品
		if(remain>0)
		{
			while(remain>0)
			{
				int slotRemain = remain;
				if(slotRemain>itemTemplate.getOverLimit())
					slotRemain = itemTemplate.getOverLimit();
				Item item = Item.createNewInstance(templateId,slotRemain);
				item.setOwner(owner);
				item.setCharId(owner.getPassportId());
				item.active();
				item.setInDb(false);
				item.setModified();
				itemList.add(item);
				remain -= slotRemain;
				tempItemList.add(item);
			}
		
			return tempItemList;
		}
		return tempItemList;
	}

	/**
	 * 获取背包道具数量
	 * @param templateId
	 * @return
	 */
	public int getCount(int templateId)
	{
		int count = 0;
		for(Item item : itemList)
		{
			if(item.getTemplateId() == templateId)
				count += item.getOverlap();
		}
		return count;
	}
	

	
	/**
	 * 获得最大数量
	 * @param templateId
	 * @return
	 */
	public int getMaxCanAdd(int templateId)
	{
		ItemTemplate itemTemplate =  itemService.getItemTemplWithId(templateId);
		//延长时间
		if(itemTemplate.getTime()>0)
		{
			for(Item item : itemList){
				if(item.getTemplateId() == templateId)
				{
					return -1;
				}
			}
			if( itemList.size()<MAX_SLOT)
				return -1;
			else
				return 0;
		}
		
		//判断是否可以叠加
		int maxRemain = 0;
		for(Item item : itemList){
			if(item.getTemplateId() == templateId)
			{
				maxRemain+=(itemTemplate.getOverLimit()-item.getOverlap());
			}
		}
		maxRemain+=(MAX_SLOT-itemList.size())*itemTemplate.getOverLimit();
		return  maxRemain;
	
	}
	
	/**
	 * 送经验卡
	 */
	
	public void addDoubleExp(Human human,int itemId,int itemNum){
		human.getHumanBagManager().addItem(itemId, itemNum);
		ItemTemplate itemTemplate = Globals.getItemService().getItemTemplWithId(itemId);
		int min = itemTemplate.getTime();
		//设置并且推送消息
		Globals.getCollectServer().setDoubleExpEndTime(human, min);
	}
	
	/**
	 * 发送给客户端背包数据
	 * @return
	 */
	public GCHumanBagInfoData buildHumanBagInfoData(){
		GCHumanBagInfoData gcHumanBagInfoData = new GCHumanBagInfoData();
		ItemInfoData[] itemInfoDataList= new ItemInfoData[itemList.size()];
		for(int i=0;i<itemList.size();i++)
		{
			Item item  = itemList.get(i);
			itemInfoDataList[i] = ItemInfoData.convertFromItem(item);
		}
		gcHumanBagInfoData.setItemList(itemInfoDataList);
		return gcHumanBagInfoData;
	}
	
	public Item getCouponItem(){
		List<ItemTemplate> itemTemplateList= Globals.getItemService().getItemIdByItemType(15);
		for(ItemTemplate itemTemplate:itemTemplateList){
			for(Item item:itemList){
				if(item.getTemplateId() ==itemTemplate.getId()){
					return item;
				}
			}
		}
		return null;
	}

	public Map<Integer, Integer> getItemIdNumMap() {
		return itemIdNumMap;
	}

	public void setItemIdNumMap(Map<Integer, Integer> itemIdNumMap) {
		this.itemIdNumMap = itemIdNumMap;
	}

	/**
	 *  这个方法用于缓存金币
	 * @param itemId
	 * @param itemNum
	 */
	public void addGoldCache(int itemId, int itemNum) {
		if(this.itemIdNumMap.containsKey(itemId)){
			int itemNumExist = itemIdNumMap.get(itemId);
			itemIdNumMap.put(itemId, itemNumExist+itemNum);
		}else{
			itemIdNumMap.put(itemId, itemNum);
		}
		
	}
	
	/**
	 *  这个方法用于缓存 道具
	 * @param itemId
	 * @param itemNum
	 */
	public void addItemCache(int itemId, int itemNum) {
		if(this.itemIdNumMap.containsKey(itemId)){
			int itemNumExist = itemIdNumMap.get(itemId);
			itemIdNumMap.put(itemId, itemNumExist+itemNum);
		}else{
			itemIdNumMap.put(itemId, itemNum);
		}
		
	}
	
	/**
	 * 通知前端  remind
	 */
	public void remind(Human human){
		if(itemIdNumMap != null && itemIdNumMap.size() > 0){
			List<LevelMoreGiftData> LevelMoreGiftDataList = new ArrayList<LevelMoreGiftData>(); 
			for(Entry<Integer,Integer> entry:itemIdNumMap.entrySet()){
				int itemId = entry.getKey();
				int itemNum = entry.getValue();
				LevelMoreGiftData LevelMoreGiftData = new LevelMoreGiftData();
				LevelMoreGiftData.setGiftType(itemId);
				LevelMoreGiftData.setRewardNum(itemNum);
				LevelMoreGiftDataList.add(LevelMoreGiftData);
			}
			LevelMoreGiftData[] LevelMoreGiftDataArr  = new LevelMoreGiftData[LevelMoreGiftDataList.size()];
			for(int i=0;i<LevelMoreGiftDataList.size();i++){
				LevelMoreGiftDataArr[i]=LevelMoreGiftDataList.get(i);
			}
			GCLevelGiftMore gCLevelGiftMore = new GCLevelGiftMore();
			gCLevelGiftMore.setLevelMoreGiftData(LevelMoreGiftDataArr);
			human.sendMessage(gCLevelGiftMore);
			itemIdNumMap.clear();
			human.getPlayer().sendMessage(human.getHumanBagManager().buildHumanBagInfoData());
		}
		
	}
}
