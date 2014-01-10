package com.angelic.hscard.model;

public class HsCard {

	private String cardid;// 卡牌编号
	private String name;// 卡牌名称
	private String ename;// 卡牌英文名(金色卡牌英文名为g-前缀)
	private String skill;// 技能(战吼,冲锋,连击,亡语,圣盾,过载,奥秘,沉默,潜行,嘲讽,风怒,抉择,激怒,法术伤害)
	private String description;// 技能描述
	private String occupation;// 职业(中立,盗贼,法师,德鲁伊,战士,猎人,萨满,圣骑士,牧师,术士)
	private String type;// 卡牌类型( 随从,法术,武器)
	private String race;// 种族(鱼人,恶魔,野兽,龙,海盗)
	private String cost;// 花费
	private String attack;// 攻击
	private String health;// 生命值
	private String decompound;// 分解尘数
	private String compound;// 合成尘数
	private String level;// 卡牌稀有等级(基础,普通,精良,史诗,传说)
	private String imgurl;// 卡牌图片URL地址(在线)
	private String gimgurl;// 金卡图片URL地址(在线)
	private String rank;// 卡牌等级(基本级,专家级)
	private String artist;// 画家
	private String cardps;// 卡牌描述
	private String isgolden;// 是否金卡(0:普通卡,1:金卡)

	public HsCard(){
		this.isgolden = "0";
	}
	
	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getAttack() {
		return attack;
	}

	public void setAttack(String attack) {
		this.attack = attack;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getDecompound() {
		return decompound;
	}

	public void setDecompound(String decompound) {
		this.decompound = decompound;
	}

	public String getCompound() {
		return compound;
	}

	public void setCompound(String compound) {
		this.compound = compound;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getGimgurl() {
		return gimgurl;
	}

	public void setGimgurl(String gimgurl) {
		this.gimgurl = gimgurl;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getCardps() {
		return cardps;
	}

	public void setCardps(String cardps) {
		this.cardps = cardps;
	}

	public String getIsgolden() {
		return isgolden;
	}

	public void setIsgolden(String isgolden) {
		this.isgolden = isgolden;
	}

}
