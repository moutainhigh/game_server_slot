package com.gameserver.gift.template;

import com.core.annotation.ExcelRowBinding;
import java.util.Map;
import com.google.common.collect.Maps;

import com.core.template.TemplateObject;
import com.common.exception.TemplateConfigException;
import com.core.annotation.ExcelCellBinding;

/**
 * GiftNewTemplate
 * 
 * @author CodeGenerator, don't modify this file please.
 * 
 */
@ExcelRowBinding
public abstract class GiftNewTemplateVO extends TemplateObject {

	/** 名称 */
	@ExcelCellBinding(offset = 1)
	protected int nameId;

	/** 描述 */
	@ExcelCellBinding(offset = 2)
	protected int descrip;

	/** 描述 */
	@ExcelCellBinding(offset = 3)
	protected String langDesc;

	/** 图片 */
	@ExcelCellBinding(offset = 4)
	protected String icon;

	/** 礼包类型 1、用户发送的礼物 */
	@ExcelCellBinding(offset = 5)
	protected int giftType;

	/** 花费多少金币 */
	@ExcelCellBinding(offset = 6)
	protected int costGold;


	public int getNameId() {
		return this.nameId;
	}



	public void setNameId(int nameId) {
		this.nameId = nameId;
	}
	
	public int getDescrip() {
		return this.descrip;
	}



	public void setDescrip(int descrip) {
		this.descrip = descrip;
	}
	
	public String getLangDesc() {
		return this.langDesc;
	}



	public void setLangDesc(String langDesc) {
		this.langDesc = langDesc;
	}
	
	public String getIcon() {
		return this.icon;
	}



	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public int getGiftType() {
		return this.giftType;
	}



	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}
	
	public int getCostGold() {
		return this.costGold;
	}



	public void setCostGold(int costGold) {
		this.costGold = costGold;
	}
	

	/** 模板字典 */
	protected final static Map<Integer, GiftNewTemplateVO> _templates = Maps.newTreeMap();

	@Override
	public void check() 
		throws TemplateConfigException {
	}

	@SuppressWarnings("unchecked")
	public static <T extends GiftNewTemplateVO> T getTemplate(int templateID) {
		return  (T)_templates.get(templateID);
	}

	/**
	 * 获取模板列表
	 * 
	 */
	public final static Map<Integer, GiftNewTemplateVO> getTemplates() {
		return _templates;
	}

	@Override
	public String toString() {
		return "GiftNewTemplateVO [  nameId=" + nameId + ", descrip=" + descrip + ", langDesc=" + langDesc + ", icon=" + icon + ", giftType=" + giftType + ", costGold=" + costGold + ",]";
	}
}