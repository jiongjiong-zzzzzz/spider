package org.webmaic.example.util;
import org.apache.commons.lang3.time.DateFormatUtils;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类名称：DateUtil
 * 类描述：   时间操作工具
 * @version
 */
public class DateUtil {

    /**
     * 生成ISO-8601 规范的时间格式
     * @param date
     * @return
     */
    public static String formatISO8601DateString(Date date){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        return  DateFormatUtils.format(date, pattern);
    }


    /**
     * 获取原时间戳
     * @param reverseTime
     * @return
     */
    public static Long recoverReverseTime(Long reverseTime){
        long longTime = Long.MAX_VALUE - reverseTime;
        return longTime/1000000;
    }
    /**
     * 生成页面普通展示时间
     * @param date
     * @return
     */
    public static String formatNormalDateString(Date date){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 日期转换为字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getShotDate(Date date) {
        if (date == null){
            return "";
        }
        return dateStr(date, "yyyy-MM-dd");
    }

    /**
     * 日期转换为字符串 格式自定义
     *
     * @param date
     * @param f
     * @return
     */
    public static String dateStr(Date date, String f) {
        if (date == null){
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(f);
        String str = format.format(date);
        return str;
    }

    /**
     * 获得当前日期
     * @return
     */
    public static Date getNow() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        return currDate;
    }

    /**
     * 获取以输入相间隔天数的日期
     * @param number 间隔天数，负数为之前的日期，正数为往后的日期
     * @return
     */
    public static String getDate(int number,String format){
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, number);
        Date date = calendar.getTime();
        return sf.format(date);
    }

    /**
     * 获得当前日期，精确到毫秒
     * @return
     */
    public static Timestamp getNowInMillis() {
        Timestamp timeStamep = new Timestamp(getNow().getTime());
        return timeStamep;
    }

    public static Date getDateByStrTime(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 计算两个日期之间相差的天数
     * @param date1
     * @param date2
     * @return  date1>date2时结果<0,  date1=date2时结果=0, date2>date1时结果>0
     */
    public static int daysBetween(Date date1, Date date2, String format){
        DateFormat sdf=new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr(date1, format));
            Date d2 = sdf.parse(DateUtil.dateStr(date2, format));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf((time2 - time1) / 86400000L));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两个日期之间相差的小时数
     * @param date1
     * @param date2
     * @return
     */
    public static int hoursBetween(Date date1, Date date2) {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr(date1, "yyyyMMdd"));
            Date d2 = sdf.parse(DateUtil.dateStr(date2, "yyyyMMdd"));
            cal.setTime(d1);
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf((time2 - time1) / 3600000L));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两个日期相差的时间
     */
    public static void getBetweenDate () {
        String dateStart = "2017111520";
        String dateStop = "2017111620";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //毫秒ms
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print("两个时间相差：");
            System.out.print(diffDays + " 天, ");
            System.out.print(diffHours + " 小时, ");
            System.out.print(diffMinutes + " 分钟, ");
            System.out.print(diffSeconds + " 秒.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 计算两个日期之间相差的小时数
     * @param date1
     * @param date2
     * @return  date1>date2时结果<0,  date1=date2时结果=0, date2>date1时结果>0
     */
    public static int hoursBetweenByDateStr(Date date1, Date date2) {
        DateFormat sdf=new SimpleDateFormat("yyyyMMddHH");
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr(date1,"yyyyMMddHH"));
            Date d2 = sdf.parse(DateUtil.dateStr(date2,"yyyyMMddHH"));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf(((time2 - time1) / 3600000L)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两个日期之间相差的分钟数
     * @param date1
     * @param date2
     * @return  date1>date2时结果<0,  date1=date2时结果=0, date2>date1时结果>0
     */
    public static int minuteBetweenByDateStr(Date date1, Date date2) {
        DateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr(date1,"yyyyMMddHHmm"));
            Date d2 = sdf.parse(DateUtil.dateStr(date2,"yyyyMMddHHmm"));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf(((time2 - time1) / 60000L)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两个日期之间相差的秒数
     * @param date1
     * @param date2
     * @return  date1>date2时结果<0,  date1=date2时结果=0, date2>date1时结果>0
     */
    public static int secondBetweenByDateStr(Date date1, Date date2) {
        DateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr(date1, "yyyyMMddHHmmss"));
            Date d2 = sdf.parse(DateUtil.dateStr(date2, "yyyyMMddHHmmss"));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf(((time2 - time1) / 1000L)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(hoursBetweenByDateStr(getDateByStrTime("2018010223", "yyyyMMddHH"), getDateByStrTime("2018010323", "yyyyMMddHH")));
        System.out.println(minuteBetweenByDateStr(getDateByStrTime("201801052359", "yyyyMMddHHmm"), getDateByStrTime("201801060000", "yyyyMMddHHmm")));

        //2.活动结束时间>0 （ 默认2018年1月5号 20:30结束）
        if( DateUtil.minuteBetweenByDateStr (DateUtil.getDateByStrTime("201801111348","yyyyMMddHHmm"), DateUtil.getNow()) > 0){
            System.out.println(DateUtil.minuteBetweenByDateStr (DateUtil.getDateByStrTime("201801111348","yyyyMMddHHmm"), DateUtil.getNow()));
        }else{
            System.out.println("时间未结束");
        }

        System.out.println(DateUtil.secondBetweenByDateStr(DateUtil.getDateByStrTime("20180110150200","yyyyMMddHHmmss"), DateUtil.getNow()));

    }



}
