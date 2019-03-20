package com.wb.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * ת���ַ����ı���
 */
public class ChangeCharset {
	public static final String US_ASCII = "US-ASCII";

	public static final String ISO_8859_1 = "ISO-8859-1";

	public static final String UTF_8 = "UTF-8";

	public static final String UTF_16BE = "UTF-16BE";

	public static final String UTF_16LE = "UTF-16LE";

	public static final String UTF_16 = "UTF-16";
	public static final String gb2312 = "gb2312";
	public static final String GBK = "GBK";

	public String toASCII(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, US_ASCII);
	}

	public String toISO_8859_1(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, ISO_8859_1);
	}

	public String toUTF_8(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_8);
	}

 
	public String toUTF_16BE(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_16BE);
	}

	 
	public String toUTF_16LE(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_16LE);
	}

	public String toUTF_16(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_16);
	}

	public String toGBK(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, GBK);
	}
	public String toGB2312(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, gb2312);
	}
	public String toutf8(String str){
		String urlStr = "";
		try {
			urlStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlStr;
	}

	public String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			byte[] bs = str.getBytes();
			return new String(bs, newCharset);
		}
		return null;
	}
	public String changeCharset(String str, String oldCharset, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}

}
