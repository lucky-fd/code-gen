package com.yushi.code.common.core.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.yushi.code.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public final static String YYYY = "yyyy";

    public final static String YYYY_MM = "yyyy-MM";

    public final static String MM_DD = "MM-dd";

    public final static String YYYYMMDD = "yyyyMMdd";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    static Map<String, Integer> holidayMap = new HashMap<String, Integer>();



    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 根据传入月份，获得当月第一天
     */
    public static Date getMonthFirstDay(String month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY);
        String year = dateFormat.format(new Date());
        dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        int monthInt = Integer.parseInt(month);
        String dateStr = year + "-" + monthInt + "-" + "01";
        try {
            Date date = dateFormat.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据传入月份，得到下个月第一天
     */
    public static Date getNextMonthFirstDay(String month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY);
        String year = dateFormat.format(new Date());
        String dateStr = "";
        int monthInt = Integer.parseInt(month);
        if (monthInt == 12) {
            monthInt = 1;
        } else {
            monthInt++;
        }
        if (monthInt < 10) {
            dateStr = year + "-0" + monthInt + "-" + "01";
        } else {
            dateStr = year + "-" + monthInt + "-" + "01";
        }
        try {
            dateFormat = new SimpleDateFormat(YYYY_MM_DD);
            Date date = dateFormat.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }


    /*
        根据传入年份，获得当年第一天
     */
    public static Date getYearFirstDay(String year) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        String dateStr = year + "-" + "01" + "-01";
        try {
            Date date = dateFormat.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /*
    根据传入的年份，获得明年第一天
    */
    public static Date getNextYearFirstDay(String year) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        int yearInt = Integer.parseInt(year) + 1;
        year = String.valueOf(yearInt);
        String dateStr = year + "-" + "01" + "-01";
        try {
            Date date = dateFormat.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String yearNow() {
        return dateTimeNow(YYYY);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String dateTimeMonth(Date date) {
        return DateFormatUtils.format(date, "yyyyMM");
    }

    public static final Date getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间相差月份
     */
    public static Integer getDateMonth(Date endDate, Date nowDate) {
        long nm = 1000L * 24 * 60 * 60 * 30;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        //舍弃末尾小数点
        BigDecimal res = new BigDecimal(diff).divide(new BigDecimal(nm), 0, RoundingMode.DOWN);
        // 计算差多少月
        return res.intValue();
    }

    /**
     * 根据区间获取日期
     *
     * @param targetDate 目标日期
     * @param month      月
     * @return res
     */
    public static Date dateSubtraction(Date targetDate, Integer month) {
        long nm = 1000L * 24 * 60 * 60 * 30;
        return new Date(targetDate.getTime() - month * nm);
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 Date ==> LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate = localDate.minusDays(1);
        return localDate;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param
     * @param
     * @return
     * @throws ParseException
     */
    public static int daysBetweenBeginAndEnd(Date beginTime, Date endTIme) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int days = 0;
        try {
            beginTime = sdf.parse(sdf.format(beginTime));
            endTIme = sdf.parse(sdf.format(endTIme));
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginTime);
            long time1 = cal.getTimeInMillis();
            cal.setTime(endTIme);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            days = Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            days = 0;
        }
        return days;
    }


    /**
     * 计算当天日期相差小时数
     *
     * @param
     * @param
     * @return
     */
    public static float hoursBetweenSameDay(Date beginTime, Date endTime) {
        float beginHour = beginTime.getHours();
        float beginMin = (beginTime.getMinutes()) / 60f;
        float beginSecond = (beginTime.getSeconds()) / 3600f;
        float beginHours = beginHour + beginMin + beginSecond;
        float endHour = endTime.getHours();
        float endMin = (endTime.getMinutes()) / 60f;
        float endSecond = (endTime.getSeconds()) / 3600f;
        float endHours = endHour + endMin + endSecond;
        return endHours - beginHours;
    }

    /**
     * 计算两个日期之间相差小时数
     *
     * @param
     * @param
     * @return
     */
    public static float hoursBetweenBeginAndTime(Date beginTime, Date endTime) {
        return (endTime.getTime() - beginTime.getTime()) / 3600000f;
    }


    /**
     * 得到某天的总小时数
     *
     * @param time
     * @return
     */
    public static float hoursOneDay(Date time) {
        float hour = time.getHours();
        float min = (time.getMinutes()) / 60f;
        float second = (time.getSeconds()) / 3600f;
        return hour + min + second;
    }

    /**
     * 得到减去指定天数的时间
     *
     * @return
     */
    public static Date reduceDays(Date date, int days) {
        LocalDate localDate = DateUtils.toLocalDate(date);
        localDate = localDate.minusDays(--days);
        return DateUtils.toDate(localDate);
    }

    /**
     * 得到增加指定天数的时间
     *
     * @return
     */
    public static Date addDays(Date date, int days) {
        LocalDate localDate = DateUtils.toLocalDate(date);
        localDate = localDate.minusDays(days - 3);
        return DateUtils.toDate(localDate);
    }

    /**
     * 判断是否为假期
     * 1:节假日
     * 2：调休
     * 3：周天
     * 4：啥也不是
     *
     * @param judgeDate 判断日期
     * @return int
     */
    @Deprecated
    public static int isHoliday(Date judgeDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        String dateStr = sdf.format(judgeDate);
        if (holidayMap.containsKey(dateStr)) {
            if (holidayMap.get(dateStr).equals(1)) {
                return 1;
            }
            if (holidayMap.get(dateStr).equals(2)) {
                return 2;
            }
            if (holidayMap.get(dateStr).equals(3)) {
                return 3;
            }
        }
        return 4;
    }


    /**
     * 得到开始截止时间之间日期，不包括起始时间和结尾时间
     *
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static List<Date> betweenDates(Date beginTime, Date endTime) {
        ArrayList<Date> dates = new ArrayList<>();
        List<DateTime> times = DateUtil.rangeToList(beginTime, endTime, DateField.DAY_OF_YEAR);
        float beginHours = DateUtils.hoursOneDay(beginTime);
        float endHours = DateUtils.hoursOneDay(endTime);
        boolean lastDayFlag = false;
        if (endHours >= beginHours) {
            lastDayFlag = true;
        }

        if (ObjectUtils.isNotEmpty(times)) {
            for (int i = 0; i < times.size(); i++) {
                if (i == times.size() - 1) {
                    if (lastDayFlag) {
                        break;
                    }
                }
                if (i == 0 || i == times.size()) {

                } else {
                    dates.add(new Date(times.get(i).getTime()));
                }
            }
        }
        return dates;
    }

    /**
     * 得到开始截止时间之间日期，包括起始时间和结尾时间
     *
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static List<Date> betweenAllDates(Date beginTime, Date endTime) {
        ArrayList<Date> dates = new ArrayList<>();
        dates.add(beginTime);
        List<DateTime> times = DateUtil.rangeToList(beginTime, endTime, DateField.DAY_OF_YEAR);
        float beginHours = DateUtils.hoursOneDay(beginTime);
        float endHours = DateUtils.hoursOneDay(endTime);
        boolean lastDayFlag = false;
        if (endHours >= beginHours) {
            lastDayFlag = true;
        }

        if (ObjectUtils.isNotEmpty(times)) {
            for (int i = 0; i < times.size(); i++) {
                if (i == times.size() - 1) {
                    if (lastDayFlag) {
                        break;
                    }
                }
                if (i == 0 || i == times.size()) {

                } else {
                    dates.add(new Date(times.get(i).getTime()));
                }
            }
        }
        dates.add(endTime);
        return dates;
    }


    public static String format(LocalDateTime time, String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(s);
        return formatter.format(time);
    }

    public static String format(LocalDate time, String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(s);
        return formatter.format(time);
    }

    public static String format(LocalDateTime time) {
        return format(time, YYYY_MM_DD_HH_MM_SS);
    }

    public static String format(LocalDate time) {
        return format(time, YYYY_MM_DD);
    }

    //初始化为0:0:0
    public static Date initZeroTime(Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    //初始化为23:59:59
    public static Date initEveningTime(Date date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }

    //得到默认当前第一天
    public static Date getMonthFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String dateStr = sdf.format(date) + "-01";
        try {
            date = sdf.parse(dateStr);
            initZeroTime(date);
        } catch (Exception e) {
            log.error("时间解析失败");
            throw new ServiceException("时间解析失败");
        }
        return date;
    }


    //得到默认下个月第一天
    public static Date getNextMonthFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date date = new Date();
        int dateInt = 0;

        String dateStr = sdf.format(date);
        sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(date);

        if (dateStr.equals("12")) {
            dateInt = 1;
        } else {
            dateInt = (Integer.valueOf(dateStr) + 1);
        }
        if (dateInt < 10) {
            dateStr = year + "-0" + dateInt + "-01";
        } else {
            dateStr = year + "-" + dateInt + "-01";
        }

        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateStr);
            initZeroTime(date);
        } catch (Exception e) {
            log.error("时间解析失败");
            throw new ServiceException("时间解析失败");
        }
        String format = sdf.format(date);
        return date;
    }

    //得到默认上个月第一天
    public static Date getMonthLastDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDay = sdf.format(calendar.getTime());
        try {
            return sdf.parse(lastDay);
        } catch (Exception e) {
            throw new ServiceException("获取当月最后一天时间失败");
        }
    }

    /**
     * 将 Date 转换成 LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime of(Date date) {
        // 转为时间戳
        Instant instant = date.toInstant();
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static String now() {
        return format(LocalDateTime.now());
    }


    /**
     * 流程完成时间处理
     *
     * @param ms 时间戳
     * @return res
     */
    public static String handleTime(Long ms) {
        long day = ms / (24 * 60 * 60 * 1000);
        long hour = (ms / (60 * 60 * 1000) - day * 24);
        long minute = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }

    /*
        根据传入的格式获得时间
        sdf：SdfFormatConstants
     */
    public static Date getDateBySdf(String dateStr, String sdf) {
        SimpleDateFormat format = new SimpleDateFormat(sdf);
        try {
            Date date = format.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /*
       根据传入的格式字符获得时间字符串
       sdf：SdfFormatConstants
    */
    public static String getDateBySdf(Date date, String sdf) {
        SimpleDateFormat format = new SimpleDateFormat(sdf);
        try {
            String dateStr = format.format(date);
            return dateStr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Author：
     * @Description：更加输入日期，获取输入日期的前一天
     * @Date：
     * @strData：参数格式：yyyy-MM-dd
     * @return：返回格式：yyyy-MM-dd
     */
    public static Date getPreDateByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 - 1);
        return c.getTime();
    }

    /**
     * 获取指定日期明天时间
     *
     * @Author：
     * @Description：更加输入日期，获取输入日期的后一天
     */
    public static Date getAfterDateByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 + 1);
        return c.getTime();
    }

}

