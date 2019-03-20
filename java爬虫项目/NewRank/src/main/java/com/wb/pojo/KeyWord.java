package com.wb.pojo;

public class KeyWord {
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String keyWord ;  
	private String company_name ;  
	public String getKeyWord() {
		return keyWord;
	}	

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
 
	@Override
	public String toString() {
		return   keyWord ;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public KeyWord(int id, String keyWord, String company_name) {
		super();
		this.id = id;
		this.keyWord = keyWord;
		this.company_name = company_name;
	}

	public KeyWord(int cid, String keyWord2) {
		super();
		this.id = id;
		this.keyWord = keyWord;
	}
	
	
	
}
