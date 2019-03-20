package com.wb.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.wb.pojo.KeyWord;

public class MysqlOpertion {
	static MysqlConnect jdbc = new MysqlConnect();

	public static ArrayList<KeyWord> getKeyWord() {
		Connection conn = jdbc.buildMysql();
		// TODO Auto-generated method stub
		// String keyWordSql = "select id,abbreviation,company_name from t_company where
		// abbreviation is not null order by id desc";
		String keyWordSql = "select id,abbreviation,company_name from t_company where (two_level_industry_id is null or two_level_industry_id ='') and business_license_pic is null order by id desc";
		return jdbc.selectKeyWord(conn, keyWordSql);
	}

	public static ArrayList<KeyWord> getKeyWord2() {
		Connection conn = jdbc.buildMysql();
		// TODO Auto-generated method stub
		String keyWordSql = "select id,company_name from t_company";
		return jdbc.selectKeyWord2(conn, keyWordSql);
	}
}
