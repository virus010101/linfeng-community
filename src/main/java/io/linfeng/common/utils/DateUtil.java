package io.linfeng.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Date工具类
 * @author linfeng
 * @date 2022/1/20 13:38
 */
public final class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YEAR = "yyyy";
    public static final String DATE_FORMAT_MONTH_DATE = "MM-dd";
    public static final String DATE_FORMAT_MONTH = "yyyy-MM";
    public static final String DATE_TIME_FORMAT_NUM = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_NUM = "yyyyMMdd";
    public static final String DATE_FORMAT_START = "yyyy-MM-dd 00:00:00";
    public static final String DATE_FORMAT_END = "yyyy-MM-dd 23:59:59";
    public static final String DATE_FORMAT_MONTH_START = "yyyy-MM-01 00:00:00";
    public static final String DATE_FORMAT_YEAR_START = "yyyy-01-01 00:00:00";
    public static final String DATE_FORMAT_YEAR_END = "yyyy-12-31 23:59:59";
    public static final String DATE_FORMAT_HHMMSS = "HH:mm:ss";
    public static final String DATE_FORMAT_START_PEREND = "00:00:00";
    public static final String DATE_FORMAT_END_PEREND = "23:59:59";
    public static final String DATE_FORMAT_HHMM = "yyyy-MM-dd HH:mm";

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDate() {
        return nowDate(DATE_FORMAT_NUM);
    }

    /**
     * 获取当前年,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + "";
    }

    /**
     * 获取上一年,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String lastYear() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        return cal.get(Calendar.YEAR) + "";
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Date nowDateTime() {
        return strToDate(nowDateTimeStr(), DATE_FORMAT);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDateTimeStr() {
        return nowDate(DATE_FORMAT);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDate(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return dft.format(new Date());
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDateTime(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return dft.format(new Date());
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Integer getNowTime() {
        long t = (System.currentTimeMillis()/1000L);
        return Integer.parseInt(String.valueOf(t));
    }

    /**
     * 获取当前时间戳（秒级）
     * @return
     */
    public static Long getTime() {
        return (System.currentTimeMillis()/1000L);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Date nowDateTimeReturnDate(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return strToDate(dft.format(new Date()), DATE_FORMAT);
    }

    /**
     * convert a date to string in a specifies fromat.
     *
     * @param date
     * @param DATE_FORMAT
     * @return
     */
    public static String dateToStr(Date date, String DATE_FORMAT) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        return myFormat.format(date);
    }

    /**
     * parse a String to Date in a specifies fromat.
     *
     * @param dateStr
     * @param DATE_FORMAT
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String DATE_FORMAT) {
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return myFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * date add Second
     *
     * @param date
     * @param num
     * @return
     */
    public static Date addSecond(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, num);
        return calendar.getTime();
    }

    /**
     * date add Second return String
     *
     * @param date
     * @param num
     * @return
     */
    public static String addSecond(Date date, int num, String DATE_FORMAT) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, num);
        return dateToStr(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     */
    public static final String addDay(String newDate, int num, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date currdate = format.parse(newDate);
            Calendar ca = Calendar.getInstance();
            ca.setTime(currdate);
            ca.add(Calendar.DATE, num);
            return format.format(ca.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     */
    public static final String addDay(Date newDate, int num, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar ca = Calendar.getInstance();
        ca.setTime(newDate);
        ca.add(Calendar.DATE, num);
        return format.format(ca.getTime());
    }

    /**
     * convert long to date
     *
     * @param date 待转换时间戳
     * @return 转换后时间
     */
    public static Date timeStamp11ToDate(Integer date) {
        return new Date(date);
    }

    /**
     * convert long to date string
     *
     * @param date 待转换时间戳
     * @param DATE_FORMAT 格式化时间
     * @return 格式化后的时间
     */
    public static String timeStamp11ToDate(Integer date, String DATE_FORMAT) {
        return dateToStr(new Date(date), DATE_FORMAT);
    }

    /**
     * compare two date String with a pattern
     *
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static int compareDate(String date1, String date2, String pattern) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(pattern);
        try {
            Date dt1 = DATE_FORMAT.parse(date1);
            Date dt2 = DATE_FORMAT.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 检查日期格式是否合法
     * @param date
     * @param style
     * @return
     */
    public static boolean checkDateFormat(String date, String style) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(style);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 计算两个时间之间的天数差
     * @param beforeDay 开始时间
     * @param afterDay  结束时间
     * @return  相差天数
     */
    public static long getTwoDateDays(Date beforeDay, Date afterDay) {
        SimpleDateFormat sm = new SimpleDateFormat(DATE_FORMAT_NUM);
        long days = -1;
        try {
            days = (sm.parse(sm.format(afterDay)).getTime() - sm.parse(sm.format(beforeDay)).getTime()) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }


    //获取时间戳11位
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime()/1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳11位
    public static int getSecondTimestamp(String date){
        if (null == date) {
            return 0;
        }
        Date date1 = strToDate(date, DATE_FORMAT);
        if(date1 == null){
            return 0;
        }
        String timestamp = String.valueOf(date1.getTime()/1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳10位
    public static int getSecondTimestamp(Long timeMillis){
        if (null == timeMillis) {
            return 0;
        }
        String timestamp = String.valueOf(timeMillis / 1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳11位
    public static int getSecondTimestamp(){
        Date date = strToDate(nowDateTime(DATE_FORMAT), DATE_FORMAT);
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime()/1000);
        return Integer.parseInt(timestamp);
    }

    /** 获得昨天日期:yyyy-MM-dd  HH:mm:ss */
    public static String getYesterdayStr() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return startSdf.format(c.getTime());
    }

    /** 获得本周第一天:yyyy-MM-dd HH:mm:ss */
    public static String getWeekStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_MONTH, 0);
        c.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return startSdf.format(c.getTime());
    }

    /** 获得本周最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getWeekEndDay() {
        return addDay(getWeekStartDay(), 7, DATE_FORMAT);
    }

    /** 获得上周第一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastWeekStartDay() {
        return addDay(getWeekStartDay(), -7, DATE_FORMAT);
    }

    /** 获得上周最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastWeekEndDay() {
        return addDay(getLastWeekStartDay(), 7, DATE_FORMAT);
    }

    /** 获得本月最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getMonthEndDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return endSdf.format(c.getTime());
    }

    /** 获得上月第一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastMonthStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        return startSdf.format(c.getTime());
    }

    /** 获得上月最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastMonthEndDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return endSdf.format(c.getTime());
    }

    /** 获得上年第一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastYearStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-01-01 00:00:00");
        return startSdf.format(c.getTime());
    }

    /** 获得上年最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastYearEndDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-12-31 23:59:59");
        return endSdf.format(c.getTime());
    }


    /**
     *  两个日期之前的相差天数
     * @param starDate 开始日期
     * @param endDate 结束日期
     * @return 相差天数
     */
    public static int daysBetween(Date starDate,Date endDate){

        Calendar cal = Calendar.getInstance();

        cal.setTime(starDate);

        long time1 = cal.getTimeInMillis();

        cal.setTime(endDate);

        long time2 = cal.getTimeInMillis();

        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));

    }

    /**
     * 获取指定日期指定格式字符串
     *
     * @param dateStr
     * @param DATE_FORMAT
     * @return
     * @throws ParseException
     */
    public static String appointedDayStrToFormatStr(String dateStr, String STR_DATE_FORMAT, String DATE_FORMAT) {
        Date date = DateUtil.strToDate(dateStr, STR_DATE_FORMAT);
        return DateUtil.dateToStr(date, DATE_FORMAT);
    }

    /**
     * 获取当前时间小时
     * @return 当前时间小时 默认24小时
     */
    public static int getCurrentHour(){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    /**
     * 13位时间戳转字符串
     * @param timestamp
     * @param dateFormat
     * @return
     */
    public static String timestamp2DateStr(Long timestamp, String dateFormat) {
        if (ObjectUtil.isNull(timestamp)) {
            return "";
        }
        if (StrUtil.isBlank(dateFormat)) {
            dateFormat = DATE_FORMAT;
        }
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }





}
