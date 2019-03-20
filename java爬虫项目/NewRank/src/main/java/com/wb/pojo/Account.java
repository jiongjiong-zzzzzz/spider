package com.wb.pojo;

public class Account {

	private int id;
	private int tid; //account type
	private int did; // time ID
	private int bid; //品牌ID
	private int company_id; //公司id
	private String aname; //账号名称
	private String account_id;//账号 ID
	private String alogo;//账号LOGO
	private String subtool;//账号功能介绍
	private int snum; //发文数
	private int	 fans; //粉丝数
	private String approve; //认证主体
	private String rtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
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
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	public String getAlogo() {
		return alogo;
	}
	public void setAlogo(String alogo) {
		this.alogo = alogo;
	}
	public String getSubtool() {
		return subtool;
	}
	public void setSubtool(String subtool) {
		this.subtool = subtool;
	}
	public int getSnum() {
		return snum;
	}
	public void setSnum(int snum) {
		this.snum = snum;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public String getRtime() {
		return rtime;
	}
	public void setRtime(String rtime) {
		this.rtime = rtime;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", tid=" + tid + ", did=" + did + ", bid=" + bid + ", company_id=" + company_id
				+ ", aname=" + aname + ", account_id=" + account_id + ", alogo=" + alogo + ", subtool=" + subtool
				+ ", snum=" + snum + ", fans=" + fans + ", approve=" + approve + ", rtime=" + rtime + "]";
	}
	
}
