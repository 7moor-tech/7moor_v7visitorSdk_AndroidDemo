package com.moor.imkf.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/3/21
 *     @desc   : 时间转换工具类
 *     @version: 1.0
 * </pre>
 */
public class MoorDateUtil {
    public static final String YY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String YY_MM_DD_HH_MM_SS = "yyyy-MM-dd_HH:mm:ss";

    public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
    public static final SimpleDateFormat yearFormat = new SimpleDateFormat(YY_MM_DD);
    private static final long ONEDAY = 86400000;
    public static final int SHOW_TYPE_SIMPLE = 0;
    public static final int SHOW_TYPE_COMPLEX = 1;
    public static final int SHOW_TYPE_ALL = 2;
    public static final int SHOW_TYPE_CALL_LOG = 3;
    public static final int SHOW_TYPE_CALL_DETAIL = 4;

    public static String getDate(Date date, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        return format.format(date);
    }

    public static String getDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(s);
        return sdf.format(new Date());
    }

    /**
     * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
     *
     * @return
     */
    public static long getCurrentDayTime() {
        Date d = new Date(System.currentTimeMillis());
        String formatDate = yearFormat.format(d);
        try {
            return (yearFormat.parse(formatDate)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDate(int year, int month, int day) {
        Date d = new Date(year - 1900, month, day);
        return yearFormat.format(d);
    }

    public static long getDateMills(int year, int month, int day) {
        //Date d = new Date(year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        calendar.setTimeZone(TimeZone.getDefault());
        return calendar.getTimeInMillis();
    }

    public static String getDateString(long time, int type) {
        Calendar c = Calendar.getInstance();
        c = Calendar.getInstance(tz);
        c.setTimeInMillis(time);
        long currentTime = System.currentTimeMillis();
        Calendar current_c = Calendar.getInstance();
        current_c = Calendar.getInstance(tz);
        current_c.setTimeInMillis(currentTime);

        int currentYear = current_c.get(Calendar.YEAR);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        long t = currentTime - time;
        long t2 = currentTime - getCurrentDayTime();
        String dateStr = "";
        if (t < t2 && t > 0) {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_LOG) {
                dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = "今天  ";
            } else {
                dateStr = (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        } else if (t < (t2 + ONEDAY) && t > 0) {
            if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = "昨天  ";
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_LOG) {
                dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else {
                dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        } else if (y == currentYear) {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
                        + "日";
            } else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
                dateStr = (m < 10 ? "0" + m : m) + /* 月 */"/"
                        + (d < 10 ? "0" + d : d) + /* 日 */" "
                        + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
                        + (d < 10 ? "0" + d : d);
            } else {
                dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
                        + "日 " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        } else {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
                        + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
                        + (d < 10 ? "0" + d : d) + "日";
            } else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
                dateStr = y + /* 年 */"/" + (m < 10 ? "0" + m : m) + /* 月 */"/"
                        + (d < 10 ? "0" + d : d) + /* 日 */"  "/*
                 * + (hour < 10
                 * ? "0" + hour
                 * : hour) + ":"
                 * + (minute <
                 * 10 ? "0" +
                 * minute :
                 * minute)
                 */;
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
                        + (d < 10 ? "0" + d : d);
            } else {
                dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
                        + (d < 10 ? "0" + d : d) + "日 "
                        + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        }
        return dateStr;
    }

    /**
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getActiveTimelong(String result) {
        try {
            Date parse = yearFormat.parse(result);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
}
