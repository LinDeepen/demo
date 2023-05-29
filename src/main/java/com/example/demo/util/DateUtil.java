package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间处理工具类
 */
@Component
public class DateUtil {

    private final String format = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间转字符
     */
    public static String format(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    /**
     * 当前时间转字符
     */
    public static String format(String format) {
        return (new SimpleDateFormat(format)).format(new Date());
    }

    /**
     * 字符转时间（自定义格式）
     */
    public static Date parseDate(String src, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(src);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符转时间（年月日,忽略详细时间）
     */
    public static Date parseDate(String src) {
        return parseDate(src, "yyyy-MM-dd");
    }

    /**
     * 字符时间校验
     *
     * @param str
     * @return Date "yyyy-MM-dd"
     */
    public static boolean isDate(String str) {
        // YYYY-MM-DD的正则校验
        String eL = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-" +
                "(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((" +
                "([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(str);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取当天的开始时间
     */
    public static Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天的结束时间
     */
    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    //获取某个日期的开始时间
    public static Date getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    //获取某个日期的结束时间
    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Date(calendar.getTimeInMillis());
    }

    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    //获取本周的结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    // 获得上个年月日 type(1年2月3日)
    public static String getLastMonth(int type) {
        LocalDate today = LocalDate.now();
        String DateType = "";
        if (type == 1) {
            today = today.minusYears(1);
            DateType = "yyyy";
        } else if (type == 2) {
            today = today.minusMonths(1);
            DateType = "yyyy-MM";
        } else if (type == 3) {
            today = today.minusDays(1);
            DateType = "yyyy-MM-dd";
        } else if (type == 4) {
            today = today.minusDays(0);
            DateType = "yyyy-MM-dd";
        }
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(DateType);
        return formatters.format(today);
    }

    /**
     * 获取五分钟前的时间
     */
    public static Date getTimeBeforeFive() {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5);// 5分钟之前的时间
        return beforeTime.getTime();
    }

    /**
     * 获取指定时间下一天的日期 "yyyy-MM-dd"
     */
    public static String getDayAfter(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatter.parse(time, pos));
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        return formatter.format(calendar.getTime());
    }

    /**
     * 获取两个时间的分钟差
     */
    public static int getMinuteDiff(Date start_date, Date end_date) {
        return (int) (end_date.getTime() - start_date.getTime()) / (1000 * 60);
    }

    /**
     * 根据 年、月 获取对应的月份 的 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    //获取本月的第一天
    public static String getBeginDayOfMonth(int Month) {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Month, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(c.getTime());
    }

    //获取本月的最后一天
    public static String getEndDayOfMonth(int Month) {
        //获取当前月最后一天：
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(cale.getTime());
    }

    //获取当前(上，下)周的日期范围如：...,-1上一周，0本周，1下一周...
    public static Map getWeekDays(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayOfWeek) {
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 获得当前日期是一个星期的第几天
        int day = calendar1.get(Calendar.DAY_OF_WEEK);
        //获取当前日期前（下）x周同星几的日期
        calendar1.add(Calendar.DATE, 7 * i);
        calendar1.add(Calendar.DATE, calendar1.getFirstDayOfWeek() - day);
        String beginDate = sdf.format(calendar1.getTime());
        calendar1.add(Calendar.DATE, 6);
        String endDate = sdf.format(calendar1.getTime());
        Map map = new HashMap();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        System.out.println(beginDate + " 到 " + endDate);
        return map;
    }

    //获取本月的每周日期
    public static List getYearWeekDays(String time) {
        // Java8  LocalDate
        LocalDate date = LocalDate.parse("2018-08-01");
        // 该月第一天
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        // 该月最后一天
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        // 该月的第一个周一
        LocalDate start = date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        List<String> list = new ArrayList<>();
        // 处理每个月的1号不是周一的情况
        if (!firstDay.equals(start)) {
            StringBuilder strbur = new StringBuilder();
            strbur.append(firstDay.toString())
                    .append("至")
                    .append(start.plusDays(-1).toString());
            list.add(strbur.toString());
        }

        while (start.isBefore(lastDay)) {

            StringBuilder strbur = new StringBuilder();
            strbur.append(start.toString());

            LocalDate temp = start.plusDays(6);
            if (temp.isBefore(lastDay)) {

                strbur.append("至")
                        .append(temp.toString());
            } else {

                strbur.append("至")
                        .append(lastDay.toString());
            }

            list.add(strbur.toString());
            start = start.plusWeeks(1);
        }
        System.out.println(list.toString());
        return list;
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 计算2个日期之间相差的  以年、月、日为单位，各自计算结果是多少
     * 比如：2011-02-02 到  2017-03-02
     * 以年为单位相差为：6年
     * 以月为单位相差为：73个月
     * 以日为单位相差为：2220天
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static Map dayCompare(Date fromDate, Date toDate) {
        Map map = new HashMap();
        if (fromDate == null || toDate == null) {
            return null;
        }
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        //只要年月
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);

        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);

        int year = toYear - fromYear;
        int month = toYear * 12 + toMonth - (fromYear * 12 + fromMonth);
        int day = (int) ((to.getTimeInMillis() - from.getTimeInMillis()) / (24 * 3600 * 1000));
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);
        return map;
    }

    /**
     * 计算两个日期相差的天,时,分,秒
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static Map getDatePoor(Date endDate, Date nowDate) {
        if (endDate == null || nowDate == null) {
            return null;
        }
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        Map map = new HashMap();
        map.put("day", day);
        map.put("hour", hour);
        map.put("min", min);
        map.put("sec", sec);
        return map;
        //return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个日期相差多少小时
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static Long getDateHour(Date endDate, Date nowDate) {
        if (endDate == null || nowDate == null) {
            return null;
        }
        long between = (endDate.getTime() - nowDate.getTime()) / 1000;//除以1000是为了转换成秒
        long hour = between / 60 / 60;
        return hour;
    }

    //一个指定日期加上指定天数得到新日期
    public static Date addDate(Date date, long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    //判断时间是否是当天
    public static boolean isToday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date).equals(sdf.format(new Date()));
    }

    /**
     * @param mss 转化毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static HashMap<String, Long> formatDuring(long mss) {
        //天
        long days = mss / (1000 * 60 * 60 * 24);
        //小时
        long hours = mss / (1000 * 60 * 60);
        //分钟
        long minutes = mss / (1000 * 60);
        //秒
        long seconds = mss / 1000;
        HashMap<String, Long> longHashMap = new HashMap<>();
        longHashMap.put("days", days);
        longHashMap.put("hours", hours);
        longHashMap.put("minutes", minutes);
        longHashMap.put("seconds", seconds);
        return longHashMap;
    }

    /**
     * @param start 开始时间
     * @param end   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    public static boolean isEffectiveDate(Date start, Date end) {
        Date now = new Date();
        if (now.getTime() == start.getTime() || now.getTime() == end.getTime()) {
            return true;
        }
        return now.after(start) && now.before(end);
    }

}
