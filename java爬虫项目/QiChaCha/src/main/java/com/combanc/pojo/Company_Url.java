package com.combanc.pojo;

import com.mongodb.DBObject;

/**
 * @Title:           Company_Url
 * @Description:     url链接
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/10/24
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class Company_Url {
	
	private String id;//ID
	private String company_name;//公司名称
	private String company_url;//企查查公司内容页URL
	
	public Company_Url() {
		// TODO Auto-generated constructor stub
	}




	public void parse(DBObject db) {
		// TODO Auto-generated method stub
		this.setId((String) db.get("id"));
		this.setCompany_name((String) db.get("company_name"));
		this.setCompany_url((String) db.get("company_url"));
		
	}




	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getCompany_name() {
		return company_name;
	}




	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}




	public String getCompany_url() {
		return company_url;
	}




	public void setCompany_url(String company_url) {
		this.company_url = company_url;
	}




	@Override
	public String toString() {
		return "Company_Url [id=" + id + ", company_name=" + company_name + ", company_url=" + company_url + "]";
	}









	
	
	
}
