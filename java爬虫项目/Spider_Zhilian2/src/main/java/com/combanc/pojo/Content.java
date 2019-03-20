package com.combanc.pojo;

import com.mongodb.DBObject;

/**
 * Title:           Content
 * Description:     内容页实体类
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/5/24
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class Content {
	
	private String id;//jd的ID
	private String title;//JD标题
	private String company_name;//公司名称
	private String welfare;//福利待遇
	private String salary;//月薪
	private String city;//工作地点
	private String publication_date;//发布日期
	private String job_property;//工作性质(全职、兼职等)
	private String years;//工作经验(年限)
	private String education;//学历
	private String recruitment_number;//招聘人数
	private String job_category;//职位类别
	private String job_description;//职位描述
	private String company_description;//公司介绍
	private String company_size;//公司规模
	private String company_type;//公司性质
	private String company_industry;//公司行业
	private String company_web;//公司主页
	private String company_address;//公司地址
	private String create_time;//入库时间
	private String resource; //JD来源
	
	public Content() {
		// TODO Auto-generated constructor stub
	}



	public Content(String id, String title, String company_name, String welfare, String salary, String city,
			String publication_date, String job_property, String years, String education, String recruitment_number,
			String job_category, String job_description, String company_description, String company_size,
			String company_type, String company_industry, String company_web, String company_address,
			String create_time,String resource) {
		super();
		this.id = id;
		this.title = title;
		this.company_name = company_name;
		this.welfare = welfare;
		this.salary = salary;
		this.city = city;
		this.publication_date = publication_date;
		this.job_property = job_property;
		this.years = years;
		this.education = education;
		this.recruitment_number = recruitment_number;
		this.job_category = job_category;
		this.job_description = job_description;
		this.company_description = company_description;
		this.company_size = company_size;
		this.company_type = company_type;
		this.company_industry = company_industry;
		this.company_web = company_web;
		this.company_address = company_address;
		this.create_time = create_time;
		this.resource = resource;
	}

	public void parse(DBObject db) {
		// TODO Auto-generated method stub
		this.setId((String) db.get("id"));
		this.setTitle((String) db.get("title"));
		this.setCompany_name((String) db.get("company_name"));
		this.setWelfare((String) db.get("welfare"));
		this.setCity((String) db.get("city"));
		this.setPublication_date((String) db.get("publication_date"));
		this.setJob_property((String) db.get("job_property"));
		this.setYears((String) db.get("years"));
		this.setEducation((String) db.get("education"));
		this.setRecruitment_number((String) db.get("recruitment_number"));
		this.setJob_category((String) db.get("job_category"));
		this.setJob_description((String) db.get("job_description"));
		this.setCompany_description((String) db.get("company_description"));
		this.setCompany_size((String) db.get("company_size"));
		this.setCompany_type((String) db.get("company_type"));
		this.setCompany_industry((String) db.get("company_size"));
		this.setCompany_web((String) db.get("company_web"));
		this.setCompany_address((String) db.get("company_address"));
		this.setCreate_time((String) db.get("create_time"));
		this.setResource((String) db.get("resource"));
	}



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCompany_size() {
		return company_size;
	}



	public String getCompany_web() {
		return company_web;
	}



	public void setCompany_web(String company_web) {
		this.company_web = company_web;
	}



	public void setCompany_size(String company_size) {
		this.company_size = company_size;
	}



	public String getCompany_type() {
		return company_type;
	}



	public void setCompany_type(String company_type) {
		this.company_type = company_type;
	}



	public String getCompany_industry() {
		return company_industry;
	}



	public void setCompany_industry(String company_industry) {
		this.company_industry = company_industry;
	}



	public String getCompany_address() {
		return company_address;
	}



	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}



	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getCompany_name() {
		return company_name;
	}


	public String getResource() {
		return resource;
	}



	public void setResource(String resource) {
		this.resource = resource;
	}



	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}


	public String getWelfare() {
		return welfare;
	}


	public void setWelfare(String welfare) {
		this.welfare = welfare;
	}


	public String getSalary() {
		return salary;
	}


	public void setSalary(String salary) {
		this.salary = salary;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getPublication_date() {
		return publication_date;
	}


	public void setPublication_date(String publication_date) {
		this.publication_date = publication_date;
	}


	public String getJob_property() {
		return job_property;
	}


	public void setJob_property(String job_property) {
		this.job_property = job_property;
	}


	public String getYears() {
		return years;
	}


	public void setYears(String years) {
		this.years = years;
	}


	public String getEducation() {
		return education;
	}


	public void setEducation(String education) {
		this.education = education;
	}


	public String getRecruitment_number() {
		return recruitment_number;
	}


	public void setRecruitment_number(String recruitment_number) {
		this.recruitment_number = recruitment_number;
	}


	public String getJob_category() {
		return job_category;
	}


	public void setJob_category(String job_category) {
		this.job_category = job_category;
	}


	public String getJob_description() {
		return job_description;
	}


	public void setJob_description(String job_description) {
		this.job_description = job_description;
	}


	public String getCompany_description() {
		return company_description;
	}


	public void setCompany_description(String company_description) {
		this.company_description = company_description;
	}


	public String getCreate_time() {
		return create_time;
	}


	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}



	@Override
	public String toString() {
		return "Content [id=" + id + ", title=" + title + ", company_name=" + company_name + ", welfare=" + welfare
				+ ", salary=" + salary + ", city=" + city + ", publication_date=" + publication_date + ", job_property="
				+ job_property + ", years=" + years + ", education=" + education + ", recruitment_number="
				+ recruitment_number + ", job_category=" + job_category + ", job_description=" + job_description
				+ ", company_description=" + company_description + ", company_size=" + company_size + ", company_type="
				+ company_type + ", company_industry=" + company_industry + ", company_web=" + company_web
				+ ", company_address=" + company_address + ", create_time=" + create_time + ", resource=" + resource
				+ "]";
	}






	
	
	
}
