package io.iflym.core.util;

import io.iflym.core.test.NonTest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author shijunzi
 * @date 2017-12-14
 */
@Slf4j
public class DateUtils {
    private static final Pattern DATE_PATTERN = Pattern.compile("(?:19|2[0-9])[0-9]{2}-(?:0?[1-9]|1[012])-(?:0?[1-9]|[12][0-9]|3[01])");
    private static final Pattern TIME_PATTERN = Pattern.compile("(?:[01]?[0-9]|2[0-3]):[0-5]?[0-9]:[0-5]?[0-9]");
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile(DATE_PATTERN.pattern() + " " + TIME_PATTERN.pattern());
    private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));

    private static final ThreadLocal<DateFormat> DATE_MONTH_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMM", Locale.CHINA));

    private static final ThreadLocal<DateFormat> TIME_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));

    private static final ThreadLocal<DateFormat> DATE_TIME_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ThreadLocal<DateFormat> DATE_TIME_FORMAT_FOR_HM = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    private static final ThreadLocal<Calendar> CALENDAR_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        //以星期一为第一天
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar;
    });

    /** 用于表示日期的常量对象 */
    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode(of = {"year", "month", "dayOfMonth"})
    public static class DateX implements Serializable {
        private final int year;
        private final int month;
        private final int dayOfMonth;

        /** 转换为日期 */
        public Date toDate() {
            return buildDate(year, month, dayOfMonth);
        }
    }

    /** 用于表示日期的常量对象 */
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode(of = {"hour", "minute", "second"})
    public static class TimeX implements Serializable {
        private final int hour;
        private final int minute;
        private final int second;
    }

    /** 用于表示日期时间的常量对象 */
    @RequiredArgsConstructor
    @EqualsAndHashCode(of = {"dateX", "timeX"})
    public static class DateTimeX implements Serializable {
        private final DateX dateX;
        private final TimeX timeX;

        @NonTest
        public DateTimeX(int year, int month, int dayOfMonth, int hour, int minute, int second) {
            this.dateX = new DateX(year, month, dayOfMonth);
            this.timeX = new TimeX(hour, minute, second);
        }

        @NonTest
        public int getYear() {
            return dateX.getYear();
        }

        @NonTest
        public int getMonth() {
            return dateX.getMonth();
        }

        @NonTest
        public int getDayOfMonth() {
            return dateX.getDayOfMonth();
        }

        @NonTest
        public int getHour() {
            return timeX.getHour();
        }

        @NonTest
        public int getMinute() {
            return timeX.getMinute();
        }

        @NonTest
        public int getSecond() {
            return timeX.getSecond();
        }
    }

    /** 转换日期至年月日时分 */
    public static String formatDateHM(Date date) {
        return DATE_TIME_FORMAT_FOR_HM.get().format(date);
    }

    /** 时间转年月日 */
    public static String formatDate(Date date) {
        return DATE_FORMAT_THREAD_LOCAL.get().format(date);
    }

    /** 时间转年月格式 */
    public static String formatDateMonth(Date date) {
        return DATE_MONTH_FORMAT_THREAD_LOCAL.get().format(date);
    }

    /** 时间转时分秒 */
    public static String formatTime(Date date) {
        return TIME_FORMAT_THREAD_LOCAL.get().format(date);
    }

    /** 时间转年月日 时分秒 */
    public static String formatDateTime(Date date) {
        return DATE_TIME_FORMAT_THREAD_LOCAL.get().format(date);
    }

    /** 时间转指定格式的字符串 */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
        return sdf.format(date);
    }

    /** 年月日转时间,转换失败时抛出转换异常 */
    public static Date parseDate(String dateStr) {
        Date d = parseDate(dateStr, null);
        if(d == null) {
            throw new IllegalArgumentException(dateStr + " 不能进行转换为Date");
        }
        return d;
    }

    /** 年月日转时间,转换失败时返回默认时间 */
    public static Date parseDate(String dateStr, Date defaultDate) {
        if(!DATE_PATTERN.matcher(dateStr).matches()) {
            return defaultDate;
        }

        return ExceptionUtils.doFunRethrowE(() -> DATE_FORMAT_THREAD_LOCAL.get().parse(dateStr));
    }

    /** 时分秒转时间,转换失败时抛出转换异常 */
    public static Date parseTime(String dateStr) {
        Date d = parseTime(dateStr, null);
        if(d == null) {
            throw new IllegalArgumentException(dateStr + " 不能转换为Time");
        }
        return d;
    }

    /** 时分秒转时间,转换失败时返回默认时间 */
    public static Date parseTime(String dateStr, Date defaultDate) {
        if(!TIME_PATTERN.matcher(dateStr).matches()) {
            return defaultDate;
        }

        return ExceptionUtils.doFunRethrowE(() -> TIME_FORMAT_THREAD_LOCAL.get().parse(dateStr));
    }

    /** 年月日 时分秒转时间,转换失败时抛出转换异常 */
    public static Date parseDateTime(String dateStr) {
        Date d = parseDateTime(dateStr, null);
        if(d == null) {
            throw new IllegalArgumentException(dateStr + " 不能转换为DateTime");
        }
        return d;
    }

    /** 年月日 时分秒转时间,转换失败时返回默认值 */
    public static Date parseDateTime(String dateStr, Date defaultDate) {
        if(!DATE_TIME_PATTERN.matcher(dateStr).matches()) {
            return defaultDate;
        }

        return ExceptionUtils.doFunRethrowE(() -> DATE_TIME_FORMAT_THREAD_LOCAL.get().parse(dateStr));
    }

    private static Calendar getCalendar(Date date) {
        Calendar calendar = CALENDAR_THREAD_LOCAL.get();
        calendar.setTime(date);
        return calendar;
    }

    /** 时间对换 开始 结束 */
    public static void swap(Date startDate, Date endDate) {
        if(startDate == null || endDate == null) {
            return;
        }
        if(startDate.after(endDate)) {
            long startDateLong = startDate.getTime();
            startDate.setTime(endDate.getTime());
            endDate.setTime(startDateLong);
        }
    }

    /** 获取指定时间的年份 */
    @NonTest
    public static int getFullYear(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.YEAR);
    }

    /** 获取指定时间的月份 */
    @NonTest
    public static int getMonth(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.MONTH);
    }

    /** 获取指定时间的天(按月计) */
    @NonTest
    public static int getDayOfMonth(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /** 获取指定时间的星期几(按星期计) */
    @NonTest
    public static int getDayOfWeek(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /** 获取小时 */
    @NonTest
    public static int getHour(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /** 获取分钟 */
    @NonTest
    public static int getMinute(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.MINUTE);
    }

    /** 获取秒 */
    @NonTest
    public static int getSecond(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.SECOND);
    }

    /** 获取年周 */
    public static int getWeekOfYear(Date date) {
        Calendar calendar = getCalendar(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        if(week == 1) {
            //如果为第1周，则尝试计算星期一，星期日所在年份，如果星期一和星期日年份相同，则表示都在新年。
            //如果与星期一相同，则表示是旧年，需要取星期一往前一天所在周+1
            int year = calendar.get(Calendar.YEAR);

            Calendar mondayCalendar = (Calendar) calendar.clone();
            mondayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            DateUtils.DateX monday = DateUtils.getDateX(mondayCalendar.getTime());

            Calendar sundayCalendar = (Calendar) calendar.clone();
            sundayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            DateUtils.DateX sunday = DateUtils.getDateX(sundayCalendar.getTime());

            //noinspection StatementWithEmptyBody
            if(monday.year == sunday.year) {
                //nothing to do 这里是故意的,表示如果周一和周日都在同一年,又因为是第1周,因为肯定是在新年中
            } else if(year == monday.year) {//这里年为周一所在年,即当前天为旧年数据,即表示是旧年的最后一周
                Calendar temp = getCalendar(DateUtils.buildDate(monday.getYear(), monday.getMonth(), monday.getDayOfMonth() - 1));
                week = temp.get(Calendar.WEEK_OF_YEAR) + 1;
            }
        }
        return week;
    }

    /** 设置指定的秒 */
    @NonTest
    public static Date setSecond(Date date, int second) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /** 设置分 */
    @NonTest
    public static Date setMinute(Date date, int minute) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /** 设置小时 */
    @NonTest
    public static Date setHour(Date date, int hour) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /** 设置天(按月计) */
    @NonTest
    public static Date setDayOfMonth(Date date, int dayOfMonth) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

    /** 设置天(按周计) */
    @NonTest
    public static Date setDayOfWeek(Date date, int dayOfWeek) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return calendar.getTime();
    }

    /** 设置月 */
    @NonTest
    public static Date setMonth(Date date, int month) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /** 设置年 */
    @NonTest
    public static Date setFullYear(Date date, int fullYear) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.YEAR, fullYear);
        return calendar.getTime();
    }

    /** 设置周 */
    @NonTest
    public static Date setWeekOfYear(Date date, int weekOfYear) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        return calendar.getTime();
    }

    /** 获取日期 */
    @NonTest
    public static DateX getDateX(Date date) {
        Calendar calendar = getCalendar(date);
        return new DateX(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /** 获取时间 */
    @NonTest
    public static TimeX getTimeX(Date date) {
        Calendar calendar = getCalendar(date);
        return new TimeX(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    /** 获取日期时间 */
    @NonTest
    public static DateTimeX getDateTimeX(Date date) {
        Calendar calendar = getCalendar(date);
        return new DateTimeX(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    /** 判断两个时间是否是同一天 */
    public static boolean isSameDay(Date aDate, Date bDate) {
        DateX aDateX = getDateX(aDate);
        DateX bDateX = getDateX(bDate);

        return aDateX.equals(bDateX);
    }

    /** 判断指定的时间是否为今天 */
    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    /** 根据年,月,日创建时间 */
    @NonTest
    public static Date buildDate(int year, int month, int dayOfMonth) {
        return buildDateTime(year, month, dayOfMonth, 0, 0, 0);
    }

    /** 根据小时,分,秒创建时间 */
    @NonTest
    public static Date buildTime(int hour, int minute, int second) {
        return buildDateTime(1970, 0, 1, hour, minute, second);
    }

    /** 根据年,月,日,小时,分,秒创建时间 */
    public static Date buildDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        Calendar calendar = getCalendar(new Date());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 在一个时间的基础上增加多少时间
     *
     * @param date   当前时间
     * @param field  增加的时间域
     * @param amount 增加时间数
     * @return 时间
     */
    public static Date addTime(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 将日期字段串按指定格式转换为日期类型。
     *
     * @param source  要转换的日期字符串。
     * @param pattern 日期格式。
     * @return 转换后的日期
     */
    public static Date parseDatePattern(String source, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try{
            date = sdf.parse(source);
        } catch(ParseException e) {
            throw new IllegalArgumentException(StringUtils.format("转换为Date失败.源:{},格式:{}", source, pattern), e);
        }
        return date;
    }

    /**
     * 将日期字段串按指定格式转换为日期类型,如果不能转换，则返回默认值
     *
     * @param source      要转换的日期字符串。
     * @param pattern     日期格式
     * @param defaultDate 默认值。
     * @return 转换后的日期
     */
    public static Date parseDatePattern(String source, String pattern, Date defaultDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try{
            date = sdf.parse(source);
        } catch(ParseException e) {
            log.error("解析时间失败.原始值:{},格式:{},错误信息:{}", source, pattern, e.getMessage());
            if(log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
            return defaultDate;
        }
        return date;
    }

    public static Date truncateToday() {
        return truncate(new Date());
    }

    public static Date truncateYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return truncate(calendar.getTime());
    }

    public static Date truncate(Date date) {
        return dayStart(date);
    }

    /**
     * 获取传入时间的当天开始时间：
     * 示例：
     * 输入：2015-11-11 11:11:11
     * 输出：2015-11-11 00:00:00
     *
     * @param date 要转换的日期
     * @return 当天的开始日期。
     */
    public static Date dayStart(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取传入时间的当天最后时间点。
     * 示例：
     * 输入：2015-11-11 11:11:11
     * 输出：2015-11-11 23:59:59
     *
     * @param date 要转换的日期
     * @return 当天最后时间
     */
    public static Date dayEnd(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /** 转换为星期一 */
    public static Date weekStart(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    /** 转换为星期天 */
    public static Date weekEnd(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    /** 转换为每月的1号 */
    public static Date monthStart(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /** 转换为当月的最后一天 */
    public static Date monthEnd(Date date) {
        Calendar calendar = getCalendar(date);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        return calendar.getTime();
    }

    /** 转换为当季度的第一天 */
    public static Date quarterStart(Date date) {
        Calendar calendar = getCalendar(date);
        int month = calendar.get(Calendar.MONTH);
        int firstMonth = month / 3 * 3 + 1;
        calendar.set(Calendar.MONTH, firstMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /** 转换为当季度的最后一天 */
    public static Date quarterEnd(Date date) {
        Calendar calendar = getCalendar(date);
        int month = calendar.get(Calendar.MONTH);
        int lastMonth = (month / 3 + 1) * 3;
        calendar.set(Calendar.MONTH, lastMonth - 1);
        // 2 11 即3 12 月 31天 5 8 即 6 9月 30天
        calendar.set(Calendar.DAY_OF_MONTH, lastMonth == 3 || lastMonth == 12 ? 31 : 30);

        return calendar.getTime();
    }

    /** 转换为当半年的第一天 */
    public static Date halfYearStart(Date date) {
        Calendar calendar = getCalendar(date);
        int month = calendar.get(Calendar.MONTH);
        int half = month / 6;
        int newMonth = half == 0 ? 0 : 6;

        calendar.set(Calendar.MONTH, newMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /** 转换为当半年的最后一天 */
    public static Date halfYearEnd(Date date) {
        Calendar calendar = getCalendar(date);
        int month = calendar.get(Calendar.MONTH);
        int half = month / 6;
        int newMonth = half == 0 ? 5 : 11;
        int day = half == 0 ? 30 : 31;

        calendar.set(Calendar.MONTH, newMonth);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /** 转换为当年的第一天 */
    public static Date yearStart(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /** 转换为当年的最后一天 */
    public static Date yearEnd(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return calendar.getTime();
    }
}

