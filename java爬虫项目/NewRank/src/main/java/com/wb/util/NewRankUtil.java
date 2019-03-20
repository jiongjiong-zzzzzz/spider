package com.wb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class NewRankUtil {
	private static final String APP_KEY = "joker";
	private static final char[] CHAR_SOURCE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f' };
	private static final String URI = "/xdnphb/data/weixinuser/searchWeixinDataByCondition";
	private static final String URI2 = "/xdnphb/detail/getAccountArticle";
	private static final String FILTER = "";
	private static final String HASDEAL = "false";
	private static final String ORDER = "relation";

	/**
	 * 生成随机串
	 * 
	 * @return 随机串
	 */
	public static String genNonce() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			int e = (int) Math.floor(16 * Math.random());
			sb.append(CHAR_SOURCE[e]);
		}
		return sb.toString();
		// 经过测试发现这里返回任何字符串都行
		// return "";
	}

	/**
	 * 生成请求签名 签名规则：AppKey={appKey}&{将所有请求参数按照字典序升序排序然后以key=value形式拼接}&nonce={nonce}，
	 * 然后对上述拼接后的字符串计算md5值，计算结果就是方法签名
	 * 
	 * @param uri
	 *            请求uri
	 * @param params
	 *            请求参数
	 * @param nonce
	 *            随机串
	 * @return 请求签名
	 */
	public static String genXYZ(String uri, Map<String, String> params, String nonce) {
		List<String> keys = new ArrayList<String>(params.keySet());
		keys.sort(String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder(uri);
		sb.append("?AppKey=").append(APP_KEY).append("&");
		for (String key : keys) {
			sb.append(key).append("=").append(params.get(key));
			sb.append("&");
		}
		sb.append("nonce=").append(nonce);
		return DigestUtils.md5Hex(sb.toString());
	}

	/**
	 * nonce 随机9个CHAR_SOURCE里的字符串
	 * 
	 * @return
	 */
	public static String getNonce() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			int e = (int) Math.floor(16 * Math.random());
			sb.append(CHAR_SOURCE[e]);
		}
		return sb.toString();
	}

	/**
	 * xyz的值为URI+隐藏参数AppKey（固定joker）+filter+hasdeal+key+order+nonce 字符串的md5
	 * 
	 * @param key
	 *            搜索关键字
	 * @return
	 */
	public static String getXYZ(String key) {
		String nonce = getNonce();
		String str1 = URI + "?AppKey=" + APP_KEY + "&filter=" + FILTER + "&hasDeal=" + HASDEAL + "&keyName=" + key
				+ "&order=" + ORDER + "&nonce=" + nonce;
		String xyz = "&nonce=" + nonce + "&xyz=" + DigestUtils.md5Hex(str1);
		return xyz;
	}

	public static String getUUID32() {
		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		return uuid;
		// return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	public static String getXYZ2(String key, String uuid) {
		String nonce = getNonce();
		String str1 = URI2 + "?AppKey=" + APP_KEY + "&filter=" + FILTER + "&hasDeal=" + HASDEAL + "&account=" + key
				+ "&order=" + ORDER + "&nonce=" + nonce;
		String xyz = "&uuid=" + uuid + "&nonce=" + nonce + "&xyz=" + DigestUtils.md5Hex(str1);
		return xyz;
	}

	public static String getURL(String key) {
		String url = "https://www.newrank.cn/xdnphb/data/weixinuser/searchWeixinDataByCondition?filter=&hasDeal=false&keyName="
				+ key + "&order=relation" + getXYZ(key);
		System.out.println(url);
		return url;
	}

	public static String getURL2(String key, String uuid) {
		String url = "https://www.newrank.cn/xdnphb/detail/getAccountArticle?flag=true" + getXYZ2(key, uuid);
		System.out.println(url);
		return url;
	}

	public static void main(String[] args) {
		getURL("联想");
	}
}
