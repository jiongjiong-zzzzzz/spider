package com.wb.pojo;

public class News {

	// 主键id
	private int id;
	// 公司id
	private int cid;
	// 自媒体账号id
	private int aid;
	// 时间id
	private int did;
	// 品牌id
	private int bid;
	// 媒体类别id
	private int tid;
	// 发布时间
	private String rtime;
	// 文章链接
	private String link;
	// 文章标题
	private String title;
	// 文章阅读数
	private int pv;
	// 点赞数/喜欢数
	private int lnum;
	// 拍砖数
	private int ulnum;
	// 点击数
	private int cnum;
	// 转发数
	private int tnum;
	//评论数
	private int conum;
	//来源
	private String cf;
	//文章正文
	private String body;
	//图片链接
	private String images;
	//发布账号
	private String reid;
	//发布账号头像链接
	private String reidsrc;
	//网站
	private String website;
	//板块
	private String section;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getRtime() {
		return rtime;
	}
	public void setRtime(String rtime) {
		this.rtime = rtime;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public int getLnum() {
		return lnum;
	}
	public void setLnum(int lnum) {
		this.lnum = lnum;
	}
	public int getUlnum() {
		return ulnum;
	}
	public void setUlnum(int ulnum) {
		this.ulnum = ulnum;
	}
	public int getCnum() {
		return cnum;
	}
	public void setCnum(int cnum) {
		this.cnum = cnum;
	}
	public int getTnum() {
		return tnum;
	}
	public void setTnum(int tnum) {
		this.tnum = tnum;
	}
	public int getConum() {
		return conum;
	}
	public void setConum(int conum) {
		this.conum = conum;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getReid() {
		return reid;
	}
	public void setReid(String reid) {
		this.reid = reid;
	}
	public String getReidsrc() {
		return reidsrc;
	}
	public void setReidsrc(String reidsrc) {
		this.reidsrc = reidsrc;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public News() {
	}
	public News(int cid, String rtime, String link, String title, int pv, int lnum, int ulnum, int cnum, int tnum,
			int conum, String cf, String body, String images, String reid, String reidsrc, String website) {
		super();
		this.cid = cid;
		this.rtime = rtime;
		this.link = link;
		this.title = title;
		this.pv = pv;
		this.lnum = lnum;
		this.ulnum = ulnum;
		this.cnum = cnum;
		this.tnum = tnum;
		this.conum = conum;
		this.cf = cf;
		this.body = body;
		this.images = images;
		this.reid = reid;
		this.reidsrc = reidsrc;
		this.website = website;
	}
	@Override
	public String toString() {
		return "News [id=" + id + ", cid=" + cid + ", aid=" + aid + ", did=" + did + ", bid=" + bid + ", tid=" + tid
				+ ", rtime=" + rtime + ", link=" + link + ", title=" + title + ", pv=" + pv + ", lnum=" + lnum
				+ ", ulnum=" + ulnum + ", cnum=" + cnum + ", tnum=" + tnum + ", conum=" + conum + ", cf=" + cf
				+ ", body=" + body + ", images=" + images + ", reid=" + reid + ", reidsrc=" + reidsrc + ", website="
				+ website + ", section=" + section + "]";
	}
	
	

}
