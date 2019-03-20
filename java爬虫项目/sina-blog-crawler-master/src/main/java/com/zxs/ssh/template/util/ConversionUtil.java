package com.zxs.ssh.template.util;

/**
 * Project Name:weibo-crawler
 * File Name:ConversionUtil
 * Package Name:com.zxs.ssh.template.util
 * Date:2018/11/20
 * Author:zengxueshan
 * Description:10进制和62进制互转
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class ConversionUtil {

    private static String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int scale = 62;

    /**
     * 将数字转为62进制
     *
     * @param num    Long 型数字
     * @param length    将数字转为62进制后的字符串长度，若长度缺少就在前面补0
     * @return 62进制字符串
     */
    public static String encode(long num, int length) {
        StringBuilder sb = new StringBuilder();
        int remainder = 0;

        while (num > scale - 1) {
            /**
             * 对 scale 进行求余，然后将余数追加至 sb 中，由于是从末位开始追加的，因此最后需要反转（reverse）字符串
             */
            remainder = Long.valueOf(num % scale).intValue();
            sb.append(chars.charAt(remainder));

            num = num / scale;
        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        int lengthTemp = sb.length();
        for (int i = 0; i < length-lengthTemp; i++) {
            sb.append("0");
        }
        return sb.reverse().toString();
    }

    /**
     * 62进制字符串转为数字
     *
     * @param str 编码后的62进制字符串
     * @return  解码后的 10 进制字符串
     */
    public static long decode(String str) {
        /**
         * 将 0 开头的字符串进行替换
         */
        str = str.replace("^0*", "");
        long num = 0;
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            /**
             * 查找字符的索引位置
             */
            index = chars.indexOf(str.charAt(i));
            /**
             * 索引位置代表字符的数值
             */
            num += (long) (index * (Math.pow(scale, str.length() - i - 1)));
        }

        return num;
    }

    public static void main(String[] args) {
        System.out.println(decode("APsY"));
        System.out.println(decode("CGlo"));
        System.out.println(decode("G"));
    }
}
