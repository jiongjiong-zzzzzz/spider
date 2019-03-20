package com.combanc.pojo;
/**
 * Title:           Field_Rules
 * Description:     解析规则
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/6/6
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class Field_Rules {
	
	private String url;//主页网址
	private String url_list_rule;//列表页规则
	private String id_rule;//jd的ID规则
	private String title_rule;//JD标题规则
	private String company_name_rule;//公司名称规则
	private String welfare_rule;//福利待遇规则
	private String salary_rule;//月薪规则
	private String city_rule;//工作地点规则
	private String publication_date_rule;//发布日期规则
	private String job_property_rule;//工作性质(全职、兼职等)规则
	private String years_rule;//工作经验(年限)规则
	private String education_rule;//学历规则
	private String recruitment_number_rule;//招聘人数规则
	private String job_category_rule;//职位类别规则
	private String job_description_rule;//职位描述规则
	private String company_description_rule;//公司介绍规则
	private String company_size_rule;//公司规模规则
	private String company_type_rule;//公司性质规则
	private String company_industry_rule;//公司行业规则
	private String company_web_rule;//公司主页规则
	private String company_address_rule;//公司地址规则
	private String create_time_rule;//入库时间规则
	private String next_page_rule;
	private String jd_num_rule;//列表页Jd个数
	private int page_num_rule;
	private String resource_rule; //JD来源规则
	
	
	public Field_Rules() {
		// TODO Auto-generated constructor stub
	}


	public Field_Rules(String url,String url_list_rule,String id_rule, String title_rule, String company_name_rule, String welfare_rule,
			String salary_rule, String city_rule, String publication_date_rule, String job_property_rule,
			String years_rule, String education_rule, String recruitment_number_rule, String job_category_rule,
			String job_description_rule, String company_description_rule, String company_size_rule,
			String company_type_rule, String company_industry_rule, String company_web_rule,
			String company_address_rule, String create_time_rule, String next_page_rule, String jd_num_rule,int page_num_rule, String resource_rule) {
		super();
		this.url = url;
		this.url_list_rule = url_list_rule;
		this.id_rule = id_rule;
		this.title_rule = title_rule;
		this.company_name_rule = company_name_rule;
		this.welfare_rule = welfare_rule;
		this.salary_rule = salary_rule;
		this.city_rule = city_rule;
		this.publication_date_rule = publication_date_rule;
		this.job_property_rule = job_property_rule;
		this.years_rule = years_rule;
		this.education_rule = education_rule;
		this.recruitment_number_rule = recruitment_number_rule;
		this.job_category_rule = job_category_rule;
		this.job_description_rule = job_description_rule;
		this.company_description_rule = company_description_rule;
		this.company_size_rule = company_size_rule;
		this.company_type_rule = company_type_rule;
		this.company_industry_rule = company_industry_rule;
		this.company_web_rule = company_web_rule;
		this.company_address_rule = company_address_rule;
		this.create_time_rule = create_time_rule;
		this.next_page_rule = next_page_rule;
		this.jd_num_rule = jd_num_rule;
		this.page_num_rule = page_num_rule;
		this.resource_rule = resource_rule;
	}


	public String getNext_page_rule() {
		return next_page_rule;
	}


	public void setNext_page_rule(String next_page_rule) {
		this.next_page_rule = next_page_rule;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public String getJd_num_rule() {
		return jd_num_rule;
	}


	public void setJd_num_rule(String jd_num_rule) {
		this.jd_num_rule = jd_num_rule;
	}


	public int getPage_num_rule() {
		return page_num_rule;
	}


	public void setPage_num_rule(int page_num_rule) {
		this.page_num_rule = page_num_rule;
	}


	public String getId_rule() {
		return id_rule;
	}


	public String getUrl_list_rule() {
		return url_list_rule;
	}


	public void setUrl_list_rule(String url_list_rule) {
		this.url_list_rule = url_list_rule;
	}


	public void setId_rule(String id_rule) {
		this.id_rule = id_rule;
	}


	public String getTitle_rule() {
		return title_rule;
	}


	public void setTitle_rule(String title_rule) {
		this.title_rule = title_rule;
	}


	public String getCompany_name_rule() {
		return company_name_rule;
	}


	public void setCompany_name_rule(String company_name_rule) {
		this.company_name_rule = company_name_rule;
	}


	public String getWelfare_rule() {
		return welfare_rule;
	}


	public void setWelfare_rule(String welfare_rule) {
		this.welfare_rule = welfare_rule;
	}


	public String getSalary_rule() {
		return salary_rule;
	}


	public void setSalary_rule(String salary_rule) {
		this.salary_rule = salary_rule;
	}


	public String getCity_rule() {
		return city_rule;
	}


	public void setCity_rule(String city_rule) {
		this.city_rule = city_rule;
	}


	public String getPublication_date_rule() {
		return publication_date_rule;
	}


	public void setPublication_date_rule(String publication_date_rule) {
		this.publication_date_rule = publication_date_rule;
	}


	public String getJob_property_rule() {
		return job_property_rule;
	}


	public void setJob_property_rule(String job_property_rule) {
		this.job_property_rule = job_property_rule;
	}


	public String getYears_rule() {
		return years_rule;
	}


	public void setYears_rule(String years_rule) {
		this.years_rule = years_rule;
	}


	public String getEducation_rule() {
		return education_rule;
	}


	public void setEducation_rule(String education_rule) {
		this.education_rule = education_rule;
	}


	public String getRecruitment_number_rule() {
		return recruitment_number_rule;
	}


	public void setRecruitment_number_rule(String recruitment_number_rule) {
		this.recruitment_number_rule = recruitment_number_rule;
	}


	public String getJob_category_rule() {
		return job_category_rule;
	}


	public void setJob_category_rule(String job_category_rule) {
		this.job_category_rule = job_category_rule;
	}


	public String getJob_description_rule() {
		return job_description_rule;
	}


	public void setJob_description_rule(String job_description_rule) {
		this.job_description_rule = job_description_rule;
	}


	public String getCompany_description_rule() {
		return company_description_rule;
	}


	public void setCompany_description_rule(String company_description_rule) {
		this.company_description_rule = company_description_rule;
	}


	public String getCompany_size_rule() {
		return company_size_rule;
	}


	public void setCompany_size_rule(String company_size_rule) {
		this.company_size_rule = company_size_rule;
	}


	public String getCompany_type_rule() {
		return company_type_rule;
	}


	public void setCompany_type_rule(String company_type_rule) {
		this.company_type_rule = company_type_rule;
	}


	public String getCompany_industry_rule() {
		return company_industry_rule;
	}


	public void setCompany_industry_rule(String company_industry_rule) {
		this.company_industry_rule = company_industry_rule;
	}


	public String getCompany_web_rule() {
		return company_web_rule;
	}


	public void setCompany_web_rule(String company_web_rule) {
		this.company_web_rule = company_web_rule;
	}


	public String getCompany_address_rule() {
		return company_address_rule;
	}


	public void setCompany_address_rule(String company_address_rule) {
		this.company_address_rule = company_address_rule;
	}


	public String getCreate_time_rule() {
		return create_time_rule;
	}


	public void setCreate_time_rule(String create_time_rule) {
		this.create_time_rule = create_time_rule;
	}


	public String getResource_rule() {
		return resource_rule;
	}


	public void setResource_rule(String resource_rule) {
		this.resource_rule = resource_rule;
	}


	@Override
	public String toString() {
		return "Field_Rules [id_rule=" + id_rule + ", title_rule=" + title_rule + ", company_name_rule="
				+ company_name_rule + ", welfare_rule=" + welfare_rule + ", salary_rule=" + salary_rule + ", city_rule="
				+ city_rule + ", publication_date_rule=" + publication_date_rule + ", job_property_rule="
				+ job_property_rule + ", years_rule=" + years_rule + ", education_rule=" + education_rule
				+ ", recruitment_number_rule=" + recruitment_number_rule + ", job_category_rule=" + job_category_rule
				+ ", job_description_rule=" + job_description_rule + ", company_description_rule="
				+ company_description_rule + ", company_size_rule=" + company_size_rule + ", company_type_rule="
				+ company_type_rule + ", company_industry_rule=" + company_industry_rule + ", company_web_rule="
				+ company_web_rule + ", company_address_rule=" + company_address_rule + ", create_time_rule="
				+ create_time_rule + ", resource_rule=" + resource_rule + "]";
	}
	
	
	
	
	
	
}
