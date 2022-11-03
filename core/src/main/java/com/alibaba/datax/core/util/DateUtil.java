package com.alibaba.datax.core.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static final String yyyyMMdd = "yyyy-MM-dd";

    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

    public static final String yyyyMMddHHmmss_1 = "yyyyMMddHHmmss";

    /**
     * 时间格式化
     *
     * @param currentTimestamp
     * @param format
     * @return
     */
    public static String getFormatDate(long currentTimestamp, String format) {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(currentTimestamp);
        return getFormatDate(ca.getTime(), format);
    }

    public static String getFormatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static long getHourTime(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");// 注意小时是HH
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取上个月的开始时间
     *
     * @return
     */
    public static Date getBeginMonthday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取上个月的结束时间
     *
     * @return
     */
    public static Date getEndMonthday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取去年的开始时间
     *
     * @return
     */
    public static Date getBeginYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, getNowYear() - 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取去年的结束时间
     *
     * @return
     */
    public static Date getEndYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, getNowYear() - 1);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DATE, 31);
        return getDayStartTime(calendar.getTime());
    }


    /**
     * 获取今年是哪一年
     *
     * @return
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    /**
     * 获取本月是哪一月
     *
     * @return
     */
    public static int getNowMonth() {
        return getMonth(new Date());
    }

    public static int getMonth(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(5);
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param d
     * @return
     */
    public static Date getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param d
     * @return
     */
    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
                59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static String format(String format, Date date) {
        if (null == date) {
            return null;
        }
        DateFormat dFormat = new SimpleDateFormat(format);
        return dFormat.format(date);
    }

    /**
     * 获取几个月前的时间，格式为yyyy-MM-dd HH:mm:ss
     *
     * @param monthNumber 月份个数
     * @return
     */
    public static String getLastMonth(int monthNumber) {
        //获取当前时间
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //当前时间前去一个月，即一个月前的时间
        calendar.add(Calendar.MONTH, -monthNumber);
        String formatDate = getFormatDate(calendar.getTimeInMillis(), yyyyMMddHHmmss);
        return formatDate;
    }

    /**
     * 获取多少天之前的时间，格式为yyyy-MM-dd HH:mm:ss
     *
     * @param days 天数
     * @return
     */
    public static String getFewDaysAgo(int days) {
        return addDays(-days, yyyyMMddHHmmss);
    }

    public static String addDays(int days, String formate) {
        return getFormatDate(addDays(days), formate);
    }

    public static Date addDays(int days) {
        //获取当前时间
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();

    }

    /**
     * 校验时间格式是否为 yyyy-MM-dd HH:mm:ss
     *
     * @param date 字符串类型时间
     * @return Boolean类型，true表示格式正确，反之亦然
     */
    public static Boolean isDateVail(String date) {
        // 用于指定日期/时间模式
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(yyyyMMddHHmmss);
        boolean flag = true;
        try {
            LocalDateTime.parse(date, dtf);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取当前时间（格式为：yyyyMMddHHmmss）
     *
     * @return 当前时间（格式为：yyyyMMddHHmmss）
     */
    public static String nowTime() {
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat(yyyyMMddHHmmss_1);
        return sd.format(date);
    }

    /**
     * String类型日期转long型时间
     *
     * @param time
     * @param format
     * @return
     */
    public static Long stringToLong(String time, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * String类型日期转Date型时间
     *
     * @param time
     * @param format
     * @return
     */
    public static Date stringToDate(String time, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * Long型日期转String型时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String longTimeToString(Long time, String format) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(format);
        String str = dateFormat.format(new Date(time));
        return str;
    }

    /**
     * 将13位时间格式化成YYYY-MM-dd HH:mm:ss,并截取前10位，并根据comp补全时分秒
     *
     * @param time   13位long型时间
     * @param format 格式化样式
     * @param comp   补全时间样式
     * @return String
     */
    public static String formatLongToStrCompletion(Long time, String format, String comp) {
        // 格式化时间戳
        return longTimeToString(time, format).substring(0, 10) + " " + comp;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay 支持格式：yyyy-MM-dd，yyyy-MM-dd HH，
     *                     yyyy-MM-dd HH:mm，yyyy-MM-dd HH:mm:ss
     * @param format       支持格式：yyyy-MM-dd，yyyy-MM-dd HH，
     *                     yyyy-MM-dd HH:mm，yyyy-MM-dd HH:mm:ss
     * @return format
     */
    public static String getBeforeDay(String specifiedDay, String format) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(specifiedDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        return new SimpleDateFormat(format).format(c.getTime());
    }


    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay 支持格式：yyyy-MM-dd，yyyy-MM-dd HH，
     *                     yyyy-MM-dd HH:mm，yyyy-MM-dd HH:mm:ss
     * @param format       支持格式：yyyy-MM-dd，yyyy-MM-dd HH，
     *                     yyyy-MM-dd HH:mm，yyyy-MM-dd HH:mm:ss
     * @return format
     */
    public static String getAfterDay(String specifiedDay, String format) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return new SimpleDateFormat(format).format(c.getTime());
    }

}
