package io.iflym.core.util;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** @author tanhua */
@Slf4j
public class DateUtilsTest {
    private static Date newDate(int year, int month, int dayOfMonth, int hour, int minute, int second, int milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, milliSecond);

        return calendar.getTime();
    }

    @DataProvider
    public Object[][] p4testBuildDate() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 4;
        Object[] objects;

        //1 正常的数据 2014-02-28
        objects = new Object[paramLength];
        objects[0] = 2014;
        objects[1] = 1;//2月为1
        objects[2] = 28;
        objects[3] = newDate(2014, 1, 28, 0, 0, 0, 0);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    @Test(dataProvider = "p4testBuildDate")
    public void testBuildDate(int year, int month, int dayOfMonth, Date expectDate) {
        Date date = DateUtils.buildDate(year, month, dayOfMonth);

        Assert.assertEquals(date, expectDate);
    }

    @DataProvider
    public Object[][] p4testBuildTime() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 4;
        Object[] objects;

        //1 正常的数据 12:12:12
        objects = new Object[paramLength];
        objects[0] = 12;
        objects[1] = 12;
        objects[2] = 12;
        objects[3] = newDate(1970, 0, 1, 12, 12, 12, 0);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    @Test(dataProvider = "p4testBuildTime")
    public void testBuildTime(int hour, int minute, int second, Date expectDate) {
        Date date = DateUtils.buildTime(hour, minute, second);

        Assert.assertEquals(date, expectDate);
    }

    @DataProvider
    public Object[][] p4testBuildDateTime() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 7;
        Object[] objects;

        //1 正常的数据 2014-01-02 03:04:05
        objects = new Object[paramLength];
        objects[0] = 2014;
        objects[1] = 0;
        objects[2] = 2;
        objects[3] = 3;
        objects[4] = 4;
        objects[5] = 5;
        objects[6] = newDate(2014, 0, 2, 3, 4, 5, 0);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    @Test(dataProvider = "p4testBuildDateTime")
    public void testBuildDateTime(int year, int month, int dayOfMonth, int hour, int minute, int second, Date expectDate) {
        Date date = DateUtils.buildDateTime(year, month, dayOfMonth, hour, minute, second);

        Assert.assertEquals(date, expectDate);
    }

    @DataProvider
    public Object[][] p4testGetWeekOfYear() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //1 2015-01-01 第1周
        objects = new Object[paramLength];
        objects[0] = newDate(2015, 0, 1, 0, 0, 0, 1);
        objects[1] = 1;
        objectsList.add(objects);

        //2 2014-12-31 第53周
        objects = new Object[paramLength];
        objects[0] = newDate(2014, 11, 31, 0, 0, 0, 1);
        objects[1] = 53;
        objectsList.add(objects);

        //3 2014-12-27 第52周(星期天算最后一天)
        objects = new Object[paramLength];
        objects[0] = newDate(2014, 11, 27, 0, 0, 0, 1);
        objects[1] = 52;
        objectsList.add(objects);

        //4 2001-01-01 第1周 周一和周日均在同一年(新年)
        objects = new Object[paramLength];
        objects[0] = newDate(2001, 0, 1, 0, 0, 0, 1);
        objects[1] = 1;
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取年周 */
    @Test(dataProvider = "p4testGetWeekOfYear")
    public void testGetWeekOfYear(Date date, int expectValue) {
        int week = DateUtils.getWeekOfYear(date);

        Assert.assertEquals(week, expectValue);
    }

    /**
     * 在一个时间的基础上增加多少时间测试
     */
    @Test
    public void testAddTime() {
        String dt = "20001010";
        Date date = DateUtils.parseDatePattern(dt, "yyyyMMdd");

        Date addDate = DateUtils.addTime(date, Calendar.DAY_OF_MONTH, 1);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long timeInMillis = cal.getTimeInMillis();

        Assert.assertEquals(addDate.getTime(), timeInMillis);
    }

    /**
     * 将日期字符串按指定格式转换成日期类型。
     */
    @Test
    public void testParseDatePattern() throws Exception {
        String source = "20121111";
        Date date = DateUtils.parseDatePattern(source, "yyyyMMdd");

        Date expectDate = DateUtils.buildDate(2012, 10, 11);
        Assert.assertEquals(date, expectDate);
    }

    @Test(expectedExceptions = Exception.class)
    public void testParseDatePatternFail() throws Exception {
        //给一个不带分隔符的日期,但是使用分隔符格式进行解析,会失败
        String source = "20170808";
        DateUtils.parseDatePattern(source, "yyyy-MM-dd");
    }

    @DataProvider
    public Object[][] p4testWeekStart() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //准备正常数据，获取第1天
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 14);
        objects[1] = DateUtils.buildDate(2015, 6, 13);
        objectsList.add(objects);

        //准备星期天，应该获取为星期一
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 19);
        objects[1] = DateUtils.buildDate(2015, 6, 13);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当周第一天 */
    @Test(dataProvider = "p4testWeekStart")
    public void testWeekStart(Date sourceDate, Date expectValue) throws Exception {
        Date value = DateUtils.weekStart(sourceDate);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue));
    }

    @DataProvider
    public Object[][] p4testWeekEnd() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //准备正常数据，返回星期日
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 14);
        objects[1] = DateUtils.buildDate(2015, 6, 19);
        objectsList.add(objects);

        //准备星期一，返回星期一
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 13);
        objects[1] = DateUtils.buildDate(2015, 6, 19);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当周最后一天 */
    @Test(dataProvider = "p4testWeekEnd")
    public void testWeekEnd(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.weekEnd(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue));
    }

    @DataProvider
    public Object[][] p4testMonthStart() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //准备正常数据，返回当月第1天

        //2月
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 1, 28);
        objects[1] = DateUtils.buildDate(2015, 1, 1);
        objectsList.add(objects);

        //3月
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 15);
        objects[1] = DateUtils.buildDate(2015, 6, 1);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当月第一天 */
    @Test(dataProvider = "p4testMonthStart")
    public void testMonthStart(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.monthStart(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue));
    }

    @DataProvider
    public Object[][] p4testMonthEnd() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //2月，非闰年
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 1, 15);
        objects[1] = DateUtils.buildDate(2015, 1, 28);
        objectsList.add(objects);

        //2月，闰年
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2012, 1, 1);
        objects[1] = DateUtils.buildDate(2012, 1, 29);
        objectsList.add(objects);

        //正常月 7月
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 15);
        objects[1] = DateUtils.buildDate(2015, 6, 31);
        objectsList.add(objects);

        //正常月 8月
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 7, 1);
        objects[1] = DateUtils.buildDate(2015, 7, 31);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当月最后一天 */
    @Test(dataProvider = "p4testMonthEnd")
    public void testMonthEnd(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.monthEnd(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue));
    }

    @DataProvider
    public Object[][] p4testQuarterStart() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //正常数据，返回季度第1天
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 15);
        objects[1] = DateUtils.buildDate(2015, 6, 1);
        objectsList.add(objects);

        //当季度第1天，仍返回当天
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 1);
        objects[1] = DateUtils.buildDate(2015, 6, 1);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当季第一天 */
    @Test(dataProvider = "p4testQuarterStart")
    public void testQuarterStart(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.quarterStart(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue), "数据不一致:" + value + "," + expectValue);
    }

    @DataProvider
    public Object[][] p4testQuarterEnd() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //正常数据，返回季度最后一天
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 15);
        objects[1] = DateUtils.buildDate(2015, 8, 30);
        objectsList.add(objects);

        //季度最后一天，返回当天
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 11, 31);
        objects[1] = DateUtils.buildDate(2015, 11, 31);
        objectsList.add(objects);

        //3月,季度最后一天为31号
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 1, 28);
        objects[1] = DateUtils.buildDate(2015, 2, 31);
        objectsList.add(objects);

        //6月,季度最后一天为30号
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 5, 29);
        objects[1] = DateUtils.buildDate(2015, 5, 30);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当报最后一天 */
    @Test(dataProvider = "p4testQuarterEnd")
    public void testQuarterEnd(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.quarterEnd(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue), "数据不一致:" + value + "," + expectValue);
    }

    @DataProvider
    public Object[][] p4testHalfYearStart() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //正常数据，返回半年的第1天 下半年
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 15);
        objects[1] = DateUtils.buildDate(2015, 6, 1);
        objectsList.add(objects);

        //上半年
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 5, 30);
        objects[1] = DateUtils.buildDate(2015, 0, 1);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当半年第一天 */
    @Test(dataProvider = "p4testHalfYearStart")
    public void testHalfYearStart(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.halfYearStart(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue), "数据不一致:" + value + "," + expectValue);
    }

    @DataProvider
    public Object[][] p4testHalfYearEnd() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //1 上半年
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 3, 3);
        objects[1] = DateUtils.buildDate(2015, 5, 30);
        objectsList.add(objects);

        //2 下半年
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 7, 15);
        objects[1] = DateUtils.buildDate(2015, 11, 31);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当半年最后一天 */
    @Test(dataProvider = "p4testHalfYearEnd")
    public void testHalfYearEnd(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.halfYearEnd(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue));
    }

    @DataProvider
    public Object[][] p4testYearStart() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //正常数据
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 6, 15);
        objects[1] = DateUtils.buildDate(2015, 0, 1);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当年第一天 */
    @Test(dataProvider = "p4testYearStart")
    public void testYearStart(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.yearStart(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue));
    }

    @DataProvider
    public Object[][] p4testYearEnd() {
        List<Object[]> objectsList = Lists.newArrayList();
        final int paramLength = 2;
        Object[] objects;

        //正常数据
        objects = new Object[paramLength];
        objects[0] = DateUtils.buildDate(2015, 5, 15);
        objects[1] = DateUtils.buildDate(2015, 11, 31);
        objectsList.add(objects);

        return objectsList.toArray(new Object[objectsList.size()][]);
    }

    /** 测试获取当年最后一天 */
    @Test(dataProvider = "p4testYearEnd")
    public void testYearEnd(Date source, Date expectValue) throws Exception {
        Date value = DateUtils.yearEnd(source);

        Assert.assertTrue(DateUtils.isSameDay(value, expectValue));
    }

    @DataProvider
    public Object[][] p4testFormatDateHM() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 2;
            Object[] objects;

            //1 正常数据
            objects = new Object[paramLength];
            objects[0] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);
            objects[1] = "2017-08-08 08:08";//仅有年月日时分,没有秒
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 测试格式化为年月日时分 */
    @Test(dataProvider = "p4testFormatDateHM")
    public void testFormatDateHM(Date source, String expectValue) throws Exception {
        String value = DateUtils.formatDateHM(source);

        Assert.assertEquals(value, expectValue);
    }

    @DataProvider
    public Object[][] p4testFormatDate() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 2;
            Object[] objects;

            //1 正常数据
            objects = new Object[paramLength];
            objects[0] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);
            objects[1] = "2017-08-08";
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 测试格式化为年月日 */
    @Test(dataProvider = "p4testFormatDate")
    public void testFormatDate(Date source, String expectValue) throws Exception {
        String value = DateUtils.formatDate(source);

        Assert.assertEquals(value, expectValue);
    }

    @DataProvider
    public Object[][] p4testFormatTime() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 2;
            Object[] objects;

            //1 正常数据
            objects = new Object[paramLength];
            objects[0] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);
            objects[1] = "08:08:08";
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 测试格式化为时间 */
    @Test(dataProvider = "p4testFormatTime")
    public void testFormatTime(Date source, String expectValue) throws Exception {
        String value = DateUtils.formatTime(source);

        Assert.assertEquals(value, expectValue);
    }

    /** 验证解析日期 */
    @Test
    public void testParseDate() throws Exception {
        //标准的日期格式
        String dateStr = "2017-08-08";
        Date date = DateUtils.parseDate(dateStr);

        Date expectDate = DateUtils.buildDate(2017, 7, 8);
        Assert.assertEquals(date, expectDate);
    }

    @DataProvider
    public Object[][] p4testParseDateFail() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 1;
            Object[] objects;

            //1 不是指定的格式
            objects = new Object[paramLength];
            objects[0] = "2017-08-08 08:08:08";
            objectsList.add(objects);

            //2 不是日期字符串
            objects = new Object[paramLength];
            objects[0] = "123456";
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证解析日期失败的场景 */
    @Test(dataProvider = "p4testParseDateFail", expectedExceptions = IllegalArgumentException.class)
    public void testParseDateFail(String dateStr) throws Exception {
        DateUtils.parseDate(dateStr);
    }

    @DataProvider
    public Object[][] p4testParseDateWithDefault() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 3;
            Object[] objects;

            //1 正确的解析,返回解析的结果
            objects = new Object[paramLength];
            objects[0] = "2017-08-08";
            objects[1] = DateUtils.buildDate(2017, 0, 1);//默认值与正确结果是不一样的
            objects[2] = DateUtils.buildDate(2017, 7, 8);
            objectsList.add(objects);

            //2 解析失败时,返回默认值
            objects = new Object[paramLength];
            objects[0] = "2017-08-08 08:08:08";
            objects[1] = DateUtils.buildDate(2017, 0, 1);
            objects[2] = DateUtils.buildDate(2017, 0, 1); //因为解析失败,因此返回默认值
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证解析日期并带失败时默认值的场景 */
    @Test(dataProvider = "p4testParseDateWithDefault")
    public void testParseDateWithDefault(String dateStr, Date defaultDate, Date expectDate) {
        Date value = DateUtils.parseDate(dateStr, defaultDate);

        Assert.assertEquals(value, expectDate);
    }

    @Test
    public void testParseTime() throws Exception {
        String timeStr = "08:08:08";
        Date time = DateUtils.parseTime(timeStr);

        Date expectTime = DateUtils.buildTime(8, 8, 8);
        Assert.assertEquals(time, expectTime);
    }

    @DataProvider
    public Object[][] p4testParseTimeFail() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 1;
            Object[] objects;

            //1 格式不正确
            objects = new Object[paramLength];
            objects[0] = "2017-08-08 08:08:08";
            objectsList.add(objects);

            //2 不是时间字符串
            objects = new Object[paramLength];
            objects[0] = "12345";
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证解析时间失败的场景 */
    @Test(expectedExceptions = Exception.class, dataProvider = "p4testParseTimeFail")
    public void testParseTimeFail(String timeStr) throws Exception {
        DateUtils.parseTime(timeStr);
    }

    @DataProvider
    public Object[][] p4testParseTimeWithDefault() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 3;
            Object[] objects;

            //1 正确的格式,能正确解析,并返回正确的值(非默认值)
            objects = new Object[paramLength];
            objects[0] = "07:07:07";
            objects[1] = DateUtils.buildTime(8, 8, 8);
            objects[2] = DateUtils.buildTime(7, 7, 7);//期望值为实际的正常结果
            objectsList.add(objects);

            //2 不正确的格式,不能解析,返回默认值
            objects = new Object[paramLength];
            objects[0] = "070707";
            objects[1] = DateUtils.buildTime(8, 8, 8);
            objects[2] = DateUtils.buildTime(8, 8, 8);//期望值即默认值
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证解析时间并带失败时默认值的场景 */
    @Test(dataProvider = "p4testParseTimeWithDefault")
    public void testParseTimeWithDefault(String timeStr, Date defaultTime, Date expectTime) {
        Date value = DateUtils.parseTime(timeStr, defaultTime);

        Assert.assertEquals(value, expectTime);
    }

    @Test
    public void testParseDateTime() throws Exception {
        String dateTimeStr = "2016-06-06 06:06:06";
        Date dateTime = DateUtils.parseDateTime(dateTimeStr);

        Date expectDateTime = DateUtils.buildDateTime(2016, 5, 6, 6, 6, 6);
        Assert.assertEquals(dateTime, expectDateTime);
    }

    @DataProvider
    public Object[][] p4testParseDateTimeFail() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 1;
            Object[] objects;

            //1 格式不正确
            //仅日期格式不行
            objects = new Object[paramLength];
            objects[0] = "2017-07-07";
            objectsList.add(objects);

            //仅时间格式不行
            objects = new Object[paramLength];
            objects[0] = "08:08:08";
            objectsList.add(objects);

            //2 本身不是日期时间
            objects = new Object[paramLength];
            objects[0] = "1234567890";
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证解析日期时间失败的场景 */
    @Test(expectedExceptions = Exception.class, dataProvider = "p4testParseDateTimeFail")
    public void testParseDateTimeFail(String dateTimeStr) throws Exception {
        DateUtils.parseDateTime(dateTimeStr);
    }

    @DataProvider
    public Object[][] p4testParseDateTimeWithDefault() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 3;
            Object[] objects;

            //1 正确的数据, 值为解析的结果(非默认值)
            objects = new Object[paramLength];
            objects[0] = "2017-08-08 08:08:08";
            objects[1] = DateUtils.buildDateTime(2017, 6, 7, 7, 7, 7);
            objects[2] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);//解析成功,值为解析值
            objectsList.add(objects);

            //2 不正确格式的
            objects = new Object[paramLength];
            objects[0] = "2017-08-0808-08-08";
            objects[1] = DateUtils.buildDateTime(2017, 6, 7, 7, 7, 7);
            objects[2] = DateUtils.buildDateTime(2017, 6, 7, 7, 7, 7);//解析不过, 最终值为期望值
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证解析日期时间并带失败时默认值的场景 */
    @Test(dataProvider = "p4testParseDateTimeWithDefault")
    public void testParseDateTimeWithDefault(String dateTimeStr, Date defaultDateTime, Date expectDateTime) {
        Date dateTime = DateUtils.parseDateTime(dateTimeStr, defaultDateTime);

        Assert.assertEquals(dateTime, expectDateTime);
    }

    @DataProvider
    public Object[][] p4testSwap() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 2;
            Object[] objects;

            //1 有1个值为空值,不作交换
            objects = new Object[paramLength];
            objects[0] = null;
            objects[1] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);
            objectsList.add(objects);

            objects = new Object[paramLength];
            objects[0] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);
            objects[1] = null;
            objectsList.add(objects);

            //2 原为前小后大,不作交换
            objects = new Object[paramLength];
            objects[0] = DateUtils.buildDateTime(2016, 7, 8, 8, 8, 8);
            objects[1] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);
            objectsList.add(objects);

            //3 原为前大后小, 进行交换
            objects = new Object[paramLength];
            objects[0] = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);
            objects[1] = DateUtils.buildDateTime(2016, 7, 8, 8, 8, 8);
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /** 验证交换时间 */
    @Test(dataProvider = "p4testSwap")
    public void testSwap(Date startDate, Date endDate) {
        DateUtils.swap(startDate, endDate);

        if(startDate != null && endDate != null) {
            Assert.assertTrue(startDate.equals(endDate) || startDate.before(endDate));
        }
    }

    @Test
    public void testTruncatToday() throws Exception {
        Date now = new Date();
        int year = DateUtils.getFullYear(now);
        int month = DateUtils.getMonth(now);
        int dayOfMonth = DateUtils.getDayOfMonth(now);

        Date expectDate = DateUtils.buildDate(year, month, dayOfMonth);

        val date = DateUtils.truncateToday();
        Assert.assertEquals(date, expectDate);
    }

    @Test
    public void testTruncatYesterday() throws Exception {
        Date now = new Date();
        int year = DateUtils.getFullYear(now);
        int month = DateUtils.getMonth(now);
        int dayOfMonth = DateUtils.getDayOfMonth(now) - 1;

        Date expectDate = DateUtils.buildDate(year, month, dayOfMonth);

        val date = DateUtils.truncateYesterday();
        Assert.assertEquals(date, expectDate);
    }

    @Test
    public void testDayStart() throws Exception {
        Date now = new Date();

        int year = DateUtils.getFullYear(now);
        int month = DateUtils.getMonth(now);
        int dayOfMonth = DateUtils.getDayOfMonth(now);

        Date expectDate = DateUtils.buildDate(year, month, dayOfMonth);

        val date = DateUtils.dayStart(now);
        Assert.assertEquals(date, expectDate);
    }

    @Test
    public void testDayEnd() throws Exception {
        Date now = new Date();

        int year = DateUtils.getFullYear(now);
        int month = DateUtils.getMonth(now);
        int dayOfMonth = DateUtils.getDayOfMonth(now);

        Date expectDate = DateUtils.buildDateTime(year, month, dayOfMonth, 23, 59, 59);

        val date = DateUtils.dayEnd(now);
        Assert.assertEquals(date, expectDate);
    }

    @Test
    public void testIsToday() {
        val now = new Date();

        Assert.assertTrue(DateUtils.isToday(now));
    }

    @DataProvider
    public Object[][] p4testFormatByPattern() throws Exception {
        try{
            List<Object[]> objectsList = Lists.newArrayList();
            final int paramLength = 3;
            Object[] objects;

            val date = DateUtils.buildDateTime(2017, 7, 8, 8, 8, 8);

            //1 按普通的日期格式
            objects = new Object[paramLength];
            objects[0] = date;
            objects[1] = "yyyy-MM-dd";
            objects[2] = "2017-08-08";
            objectsList.add(objects);

            //2 按普通的时间格式
            objects = new Object[paramLength];
            objects[0] = date;
            objects[1] = "HH:mm:ss";
            objects[2] = "08:08:08";
            objectsList.add(objects);

            //3 按普通的日期时间格式
            objects = new Object[paramLength];
            objects[0] = date;
            objects[1] = "yyyy-MM-dd HH:mm:ss";
            objects[2] = "2017-08-08 08:08:08";
            objectsList.add(objects);

            //4 按特定的格式
            objects = new Object[paramLength];
            objects[0] = date;
            objects[1] = "yyyyMMddHHmm";
            objects[2] = "201708080808";
            objectsList.add(objects);

            return objectsList.toArray(new Object[objectsList.size()][]);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Test(dataProvider = "p4testFormatByPattern")
    public void testFormatByPattern(Date date, String pattern, String expectValue) throws Exception {
        val result = DateUtils.format(date, pattern);

        Assert.assertEquals(result, expectValue);
    }
}
