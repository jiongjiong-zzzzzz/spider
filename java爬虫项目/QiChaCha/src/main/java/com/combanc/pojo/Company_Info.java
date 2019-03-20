package com.combanc.pojo;
/**
 * @Title:           Company_Info
 * @Description:     网页内容
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/10/25
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class Company_Info {
	
	private String id;//ID
	private String conpany_name;//公司名称
	private String register_capital;//注册资本
	private String contributed_capital;//实缴资本
	private String operating_state;//经营状态
	private String establish_data;//成立日期 
	private String credit_code;//统一社会信用代码 
	private String taxpayer_code;//纳税人识别号	
	private String register_code;//注册号 
	private String organization_code;//组织机构代码 
	private String company_type;//公司类型 
	private String company_industry;//所属行业	
	private String approval_date;//核准日期
	private String register_office;//登记机关 
	private String area;//所属地区 
	private String english_name;//英文名
	private String before_name;//曾用名
	private String insured_num;//参保人数
	private String company_size;//人员规模
	private String business_term;//营业期限 
	private String company_address;//企业地址 
	private String business_scope;//经营范围
	
	
	public Company_Info() {
		// TODO Auto-generated constructor stub
	}

	
	

	public Company_Info(String id, String company_name,String register_capital, String contributed_capital, String operating_state,
			String establish_data, String credit_code, String taxpayer_code, String register_code,
			String organization_code, String company_type, String company_industry, String approval_date,
			String register_office, String area, String english_name, String before_name, String insured_num,
			String company_size, String business_term, String company_address, String business_scope) {
		super();
		this.id = id;
		this.conpany_name = company_name;
		this.register_capital = register_capital;
		this.contributed_capital = contributed_capital;
		this.operating_state = operating_state;
		this.establish_data = establish_data;
		this.credit_code = credit_code;
		this.taxpayer_code = taxpayer_code;
		this.register_code = register_code;
		this.organization_code = organization_code;
		this.company_type = company_type;
		this.company_industry = company_industry;
		this.approval_date = approval_date;
		this.register_office = register_office;
		this.area = area;
		this.english_name = english_name;
		this.before_name = before_name;
		this.insured_num = insured_num;
		this.company_size = company_size;
		this.business_term = business_term;
		this.company_address = company_address;
		this.business_scope = business_scope;
	}



//	public void parse(DBObject db) {
//		// TODO Auto-generated method stub
//		this.setId((String) db.get("id"));
//		this.setCompany_name((String) db.get("company_name"));
//		this.setCompany_url((String) db.get("company_url"));
//		
//	}
	
	

	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getConpany_name() {
		return conpany_name;
	}




	public void setConpany_name(String conpany_name) {
		this.conpany_name = conpany_name;
	}




	public String getRegister_capital() {
		return register_capital;
	}




	public void setRegister_capital(String register_capital) {
		this.register_capital = register_capital;
	}




	public String getContributed_capital() {
		return contributed_capital;
	}




	public void setContributed_capital(String contributed_capital) {
		this.contributed_capital = contributed_capital;
	}




	public String getOperating_state() {
		return operating_state;
	}




	public void setOperating_state(String operating_state) {
		this.operating_state = operating_state;
	}




	public String getEstablish_data() {
		return establish_data;
	}




	public void setEstablish_data(String establish_data) {
		this.establish_data = establish_data;
	}




	public String getCredit_Code() {
		return credit_code;
	}




	public void setCredit_Code(String credit_code) {
		this.credit_code = credit_code;
	}




	public String getTaxpayer_code() {
		return taxpayer_code;
	}




	public void setTaxpayer_code(String taxpayer_code) {
		this.taxpayer_code = taxpayer_code;
	}




	public String getRegister_code() {
		return register_code;
	}




	public void setRegister_code(String register_code) {
		this.register_code = register_code;
	}




	public String getOrganization_code() {
		return organization_code;
	}




	public void setOrganization_code(String organization_code) {
		this.organization_code = organization_code;
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




	public String getApproval_date() {
		return approval_date;
	}




	public void setApproval_date(String approval_date) {
		this.approval_date = approval_date;
	}




	public String getRegister_office() {
		return register_office;
	}




	public void setRegister_office(String register_office) {
		this.register_office = register_office;
	}




	public String getArea() {
		return area;
	}




	public void setArea(String area) {
		this.area = area;
	}




	public String getEnglish_name() {
		return english_name;
	}




	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}




	public String getBefore_name() {
		return before_name;
	}




	public void setBefore_name(String before_name) {
		this.before_name = before_name;
	}




	public String getInsured_num() {
		return insured_num;
	}




	public void setInsured_num(String insured_num) {
		this.insured_num = insured_num;
	}




	public String getCompany_size() {
		return company_size;
	}




	public void setCompany_size(String company_size) {
		this.company_size = company_size;
	}




	public String getBusiness_term() {
		return business_term;
	}




	public void setBusiness_term(String business_term) {
		this.business_term = business_term;
	}




	public String getCompany_address() {
		return company_address;
	}




	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}




	public String getBusiness_scope() {
		return business_scope;
	}




	public void setBusiness_scope(String business_scope) {
		this.business_scope = business_scope;
	}




	@Override
	public String toString() {
		return "Company_Info [id=" + id + ", conpany_name=" + conpany_name + ", register_capital=" + register_capital
				+ ", contributed_capital=" + contributed_capital + ", operating_state=" + operating_state
				+ ", establish_data=" + establish_data + ", credit_code=" + credit_code + ", taxpayer_code="
				+ taxpayer_code + ", register_code=" + register_code + ", organization_code=" + organization_code
				+ ", company_type=" + company_type + ", company_industry=" + company_industry + ", approval_date="
				+ approval_date + ", register_office=" + register_office + ", area=" + area + ", english_name="
				+ english_name + ", before_name=" + before_name + ", insured_num=" + insured_num + ", company_size="
				+ company_size + ", business_term=" + business_term + ", company_address=" + company_address
				+ ", business_scope=" + business_scope + "]";
	}






	
	
}
