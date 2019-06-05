/*
 * renren-toro-tango-service - com.renren.toro.tango.util.TimeUtils.java
 * 2015年4月17日:上午10:59:22
 * Keen
 *
 * jacks808@163.com
 *
 * KKKKKKKKK    KKKKKKK
 * K:::::::K    K:::::K
 * K:::::::K    K:::::K
 * K:::::::K   K::::::K
 * KK::::::K  K:::::KKK    eeeeeeeeeeee        eeeeeeeeeeee    nnnn  nnnnnnnn
 *   K:::::K K:::::K     ee::::::::::::ee    ee::::::::::::ee  n:::nn::::::::nn
 *   K::::::K:::::K     e::::::eeeee:::::ee e::::::eeeee:::::een::::::::::::::nn
 *   K:::::::::::K     e::::::e     e:::::ee::::::e     e:::::enn:::::::::::::::n
 *   K:::::::::::K     e:::::::eeeee::::::ee:::::::eeeee::::::e  n:::::nnnn:::::n
 *   K::::::K:::::K    e:::::::::::::::::e e:::::::::::::::::e   n::::n    n::::n
 *   K:::::K K:::::K   e::::::eeeeeeeeeee  e::::::eeeeeeeeeee    n::::n    n::::n
 * KK::::::K  K:::::KKKe:::::::e           e:::::::e             n::::n    n::::n
 * K:::::::K   K::::::Ke::::::::e          e::::::::e            n::::n    n::::n
 * K:::::::K    K:::::K e::::::::eeeeeeee   e::::::::eeeeeeee    n::::n    n::::n
 * K:::::::K    K:::::K  ee:::::::::::::e    ee:::::::::::::e    n::::n    n::::n
 * KKKKKKKKK    KKKKKKK    eeeeeeeeeeeeee      eeeeeeeeeeeeee    nnnnnn    nnnnnn
 *
 */
package com.assess.util;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 时间工具 <br>
 * 2015年4月17日:上午10:59:22
 *
 * @author Keen | jacks808@163.com | ziyi.wang@renren-inc.com<br>
 */
public class TimeUtils {

    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM = "yyyy-MM";
    public static final String yyyy_MM_CHINESE = "yyyy年MM月";
    public static final String MM_dd_HH_mm_CHINESE = "MM月dd日 HH:mm";
    public static final String MM_dd = "MM-dd";
    public static final String yyyyMMdd_HHmmss = "yyyyMMdd HHmmss";
    public static final String HHmmss = "HHmmss";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_SPACE_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String yyyy_MM_dd_HH_mm_ssS = "yyyy-MM-dd HH:mm:ss.S";
    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd/HH:mm";
    public static final String MMdd = "MM/dd";
    public static final String yyyy_MM_dd_Chinese = "yyyy年MM月dd日";
    public static final String yyyyDotMMDotdd = "yyyy.MM.dd";
    public static final String yyyyMMddReport = "yyyy/MM/dd";
    public static final String yyyy = "yyyy";

    /**
     * 使用指定的时间创建一个Date对象 <br>
     * 2015年6月19日:下午5:44:09<br>
     * <br>
     *
     * @param year
     * @param month
     *            从0开始
     * @param date
     * @return <pre>
     * </pre>
     */
    public static Date build(
                    int year,
                    int month,
                    int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getTime();
    }

    public static Date build(
                    int year,
                    int month,
                    int date,
                    int hourOfDay,
                    int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute);
        return calendar.getTime();
    }

    /**
     * 获取今天 <br>
     * 2015年6月23日:下午5:05:30<br>
     * <br>
     *
     * @return
     */
    public static Date buildToday() {
        try {
            return TimeUtils.translate(new Date(), TimeUtils.yyyy_MM_dd);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal = Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
    
    /**
     * 获取某年当前时间
     * @param year
     * @return
     */
    public static Date getCurrYearFirst(Date currentDate, int year){
    	Calendar currCal = Calendar.getInstance();
    	currCal.setTime(currentDate);
        currCal.add(Calendar.YEAR, -year);
        return currCal.getTime();
    }
    
    /**
     * 获取去年的第一天
     * @return
     */
    public static Date getBeforeYearFirst(){
        Calendar currCal = Calendar.getInstance();  
        currCal.add(Calendar.YEAR,-1);	//日期减1年
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
    
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
    
    /**
     * 根据格式返回当前时间的字符串 <br>
     * 2015年7月24日:下午4:03:49<br>
     * <br>
     *
     * @param format
     * @return
     */
    public static String buildByFormat(
                    String format) {
        return TimeUtils.format(new Date(), format);
    }

    /**
     * 获取两天之间的日期list <br>
     * 2015年6月23日:下午3:54:40<br>
     * <br>
     *
     * @param startDate
     * @param containsStartDay
     * @param endDate
     * @param containsEndDay
     * @return 早的日期排在前
     */
    public static List<Date> getDateListBetweenDays(
                    Date startDate,
                    boolean containsStartDay,
                    Date endDate,
                    boolean containsEndDay) {
        List<Date> result = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        if (containsStartDay) {
            Date time = calendar.getTime();
            result.add(time);
        }
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date today = calendar.getTime();
        try {
        	String todeyStr = format(today, yyyy_MM_dd);
			today = parse(todeyStr, yyyy_MM_dd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        while (today.compareTo(endDate) < 0) {
            result.add(today);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            today = calendar.getTime();
            try {
            	String todeyStr = format(today, yyyy_MM_dd);
    			today = parse(todeyStr, yyyy_MM_dd);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
        }
        if (containsEndDay) {
            result.add(endDate);
        }
        return result;
    }

    /**
     * 格式化date <br>
     * 2015年4月17日:上午11:17:29<br>
     * <br>
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(
                    Date date,
                    String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String string = sdf.format(date);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 批量格式化 <br>
     * 2015年6月5日:下午1:35:29<br>
     * <br>
     *
     * @param dateList
     * @param format
     * @return
     */
    public static List<String> batchFormat(
                    List<Date> dateList,
                    String format) {
        List<String> result = Lists.newArrayList();
        for (Date date : dateList) {
            result.add(TimeUtils.format(date, format));
        }
        return result;
    }

    /**
     * 把一个list的数据转成long类型 <br>
     * 2015年7月31日:下午5:39:01<br>
     * <br>
     *
     * @param dates
     * @return
     */
    public static List<Long> batchToLong(
                    List<Date> dates) {
        List<Long> result = Lists.newArrayListWithExpectedSize(dates.size());
        for (Date date : dates) {
            result.add(date.getTime());
        }
        return result;
    }

    /**
     * 按照日期格式转化成date <br>
     * 2015年5月14日:下午3:18:42<br>
     * <br>
     *
     * @param dateString
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parse(
                    String dateString,
                    String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(dateString);
        return date;
    }

    /**
     * 转化日期格式 <br>
     * 2015年5月14日:下午3:52:17<br>
     * <br>
     *
     * @param src
     * @param srcFormate
     * @param targetFormate
     * @return
     * @throws ParseException
     */
    public static String translate(
                    String src,
                    String srcFormate,
                    String targetFormate) throws ParseException {
        if (src == null)
            return "";
        Date parse = TimeUtils.parse(src, srcFormate);
        String format = TimeUtils.format(parse, targetFormate);
        return format;
    }

    /**
     * 转化日期格式 <br>
     * 2015年5月14日:下午3:52:17<br>
     * <br>
     *
     * @param src
     * @param targetFormate
     * @return
     * @throws ParseException
     */
    public static Date translate(
                    Date src,
                    String targetFormate) throws ParseException {
        return TimeUtils.parse(TimeUtils.format(src, targetFormate), targetFormate);
    }

    /**
     * 将时间戳转换为时间 <br>
     * 2015年4月22日 下午4:52:19 <br>
     * <br>
     *
     * @param timeStamp
     * @return
     */
    public static String converTimeStampToDate(
                    String timeStamp) {
        try {
            long timeMillis = Long.parseLong(timeStamp) * 1000;
            Date date = new Date(timeMillis);
            return format(date, "yyyy-MM-dd HH:mm:ss");
        } catch (NumberFormatException e) {
            return timeStamp;
        }
    }

    /**
     * 获取上月同一时期<br>
     * 2015年5月15日 上午11:10:39 <br>
     * <br>
     *
     * @param currentDate
     * @return
     */
    public static Date getLastMonthDate(
                    Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 取得某一月的同一天date <br>
     * 2015年6月5日:上午11:40:42<br>
     * <br>
     *
     * @param currentDate
     * @param month
     *            负数向前,正数向后
     * @return
     */
    public static Date getLastMonthDate(
                    Date currentDate,
                    int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 获取下月同一时期<br>
     * 2015年5月15日 上午11:10:39 <br>
     * <br>
     *
     * @param currentDate
     * @return
     */
    public static Date getNextMonthDate(
                    Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 根据当前日期获取昨日日期<br>
     * 2015年5月18日 下午3:47:47 <br>
     * <br>
     *
     * @param currentDate
     * @return
     */
    public static Date getYesterday(
                    Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
    
    /**
     * 获取过去时间按月份
     * 
     * @param currentDate
     * @param moth
     * @return
     */
    public static Date getBeforeMoth(Date currentDate, int moth) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        
        calendar.add(Calendar.MONTH, -moth);
        return calendar.getTime();
    }

    /**
     * 根据当前日期获取昨日日期<br>
     * 2015年5月18日 下午3:47:47 <br>
     * <br>
     *
     * @param currentDate
     * @return
     */
    public static Date getTomorrow(
                    Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * FIXME 到账日期时间：T+1<br>
     * 2015年5月23日 下午3:04:16 <br>
     * <br>
     *
     * @param date
     * @return
     */
    public static Date getArriveTime(
                    Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        c2.set(Calendar.HOUR_OF_DAY, 15);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);

        if (date.after(c2.getTime())) {
            c.add(Calendar.DATE, 2);
            c.set(Calendar.HOUR_OF_DAY, 15);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
        } else {
            c.add(Calendar.DATE, 1);
        }

        return c.getTime();
    }

    /**
     * 预计调仓完成时间
     *
     * @param date
     * @return
     */
    public static Date getTransferCompleteTime(
                    Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 7);
        return c.getTime();
    }

    /**
     * 计算相差天数<br>
     * 2015年5月23日 下午4:02:34 <br>
     * <br>
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getBetweenDays(
                    Date startDate,
                    Date endDate) {
        long beginTime = startDate.getTime();
        long endTime = endDate.getTime();
        int betweenDays = (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24) + 0.5);

        return betweenDays;
    }

    /**
     * 计算相差天数<br>
     * 2015年5月23日 下午4:02:34 <br>
     * <br>
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getSeconsBetweenDays(
            Date startDate,
            Date endDate) {
        long beginTime = startDate.getTime();
        long endTime = endDate.getTime();
        int betweenSeconds = (int) ((endTime - beginTime) / 1000);

        return betweenSeconds;
    }

    /**
     * 判断是否是同一天<br>
     * 2015年5月25日 下午6:13:55 <br>
     * <br>
     *
     * @param dateA
     * @param dateB
     * @return
     */
    public static boolean areSameDay(
                    Date dateA,
                    Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                        && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                        && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取days天之后的date对象 <br>
     * 2015年6月3日:下午8:53:05<br>
     * <br>
     *
     * @param startDate
     * @param days
     *            正数向后,负数向前
     * @return
     */
    public static Date getBeforeDate(
                    Date startDate,
                    int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static String getMMdd(
                    String dateString) {
        try {
            Date date = parse(dateString, yyyy_MM_dd);
            return format(date, MMdd);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    /**
     * 计算当天剩余的秒数
     *
     * @param date
     * @return
     */
    public static long getDayLastSeconds(
                    Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long lastSeconds = (calendar.getTime().getTime() - date.getTime()) / 1000;
        return lastSeconds;
    }

    // 获得当天24点时间
    public static Date getFastArriveTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static Date getNormalArriveTime(
                    Date date) {
        return getArriveTime(date);
    }

    /**
     * 返回规定新鲜事时间戳 <br>
     * 小于等于60秒 显示“刚刚更新”<br>
     * 大于60秒小于等于120秒 显示“2分钟前”<br>
     * 大于120秒小于等于180秒 显示“3分钟前”<br>
     * 以此类推一个小时内发布的 显示“4分钟前”……“59分钟前”<br>
     * 大于1小时且是当天发送的 显示发布时的具体时间“15:36”hh:mm<br>
     * 大于1小时且是昨天或者昨天之前（今年内）发送的 显示发布时的具体日期和具体时间 mm-dd hh-mm“05-10 22:00”<br>
     * 大于1小时且是去年及去年之前发送的 显示发布时的具体日期 “2013-10-10”yyyy-mm-dd <br>
     *
     * @param createTime
     * @return
     */
    public static String getTimeStamp(
                    String createTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createDate = formatter.parse(createTime);
            Date currDate = new Date();
            long minusSecond = (currDate.getTime() - createDate.getTime()) / 1000;
            if (minusSecond <= 60) {
                // System.out.println("刚刚更新");
                return "刚刚更新";
            }

            // 一小时以内
            if (minusSecond < 60 * 60) {
                // System.out.println(minusSecond / 60 + "分钟前");
                return minusSecond / 60 + "分钟前";
            }

            Calendar currCalendar = Calendar.getInstance();
            currCalendar.setTime(currDate);

            Calendar createCalendar = Calendar.getInstance();
            createCalendar.setTime(createDate);

            // 当天内，一小时前
            if (minusSecond < 60 * 60 * 24 && currCalendar.get(Calendar.DATE) == createCalendar.get(Calendar.DATE)) {
                // System.out.println(format(createDate, "HH:mm"));
                return format(createDate, "HH:mm");
            }

            // 当年内，一天前
            if (minusSecond < 60 * 60 * 24 * 365
                            && currCalendar.get(Calendar.YEAR) == createCalendar.get(Calendar.YEAR)) {
                // System.out.println(format(createDate, "MM-dd HH:mm"));
                return format(createDate, "MM-dd HH:mm");
            }

            // 一年前
            // System.out.println(format(createDate, "yyyy-MM-dd"));
            return format(createDate, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
            return createTime;
        }
    }

    /**
     * 根据日期得到对应的季度名称
     *
     * 结果形如 "2015第1季度"
     *
     * @Author Elvis Wang <bo.wang35@renren-inc.com>
     *
     * @param _date
     * @return
     */
    private static String getSeasonName(
                    final Date _date) {

        final int SPRING = 1;
        final int SUMMER = 2;
        final int AUTUMN = 3;
        final int WINTER = 4;

        final Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTime(_date);

        int season_idx = SPRING;

        final int month_idx = cur_cal.get(Calendar.MONTH);
        switch (month_idx) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season_idx = SPRING;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season_idx = SUMMER;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season_idx = AUTUMN;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season_idx = WINTER;
                break;
            default:
                assert false;
                return "未知季度";
        }

        return Joiner.on("").join(cur_cal.get(Calendar.YEAR), "第", season_idx, "季度");
    }

    /**
     * 根据起始日期得到涵盖的季度名称列表
     *
     * 结果形如 "2015第1季度"
     *
     * @Author Elvis Wang <bo.wang35@renren-inc.com>
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getSeasonNames(
                    final Date startDate,
                    final Date endDate) {

        final List<String> season_names = Lists.newArrayList();

        if (null == startDate || null == endDate)
            // return empty list
            return season_names;

        Date cur_date = getFirstDateOfSeason(startDate);
        while (!cur_date.after(endDate)) {
            final String cur_season_name = getSeasonName(cur_date);
            season_names.add(cur_season_name);

            // next season
            cur_date = getFirstDateOfNextSeason(cur_date);
        }

        return season_names;
    }

    /**
     * 返回指定日期所在季度的下个季度的第一天
     *
     * @Author Elvis Wang <bo.wang35@renren-inc.com>
     * @param _date
     * @return
     */
    private static Date getFirstDateOfNextSeason(
                    final Date _date) {
        final Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTime(_date);

        // 下个季度的年序号和第一个月序号
        int year_idx = cur_cal.get(Calendar.YEAR);
        int month_idx_of_season = -1;

        final int month_idx = cur_cal.get(Calendar.MONTH);
        switch (month_idx) {
            case Calendar.JANUARY: // fall through
            case Calendar.FEBRUARY: // fall through
            case Calendar.MARCH: // fall through
                month_idx_of_season = Calendar.APRIL;
                break;
            case Calendar.APRIL: // fall through
            case Calendar.MAY: // fall through
            case Calendar.JUNE: // fall through
                month_idx_of_season = Calendar.JULY;
                break;
            case Calendar.JULY: // fall through
            case Calendar.AUGUST: // fall through
            case Calendar.SEPTEMBER: // fall through
                month_idx_of_season = Calendar.OCTOBER;
                break;
            case Calendar.OCTOBER: // fall through
            case Calendar.NOVEMBER: // fall through
            case Calendar.DECEMBER: // fall through
                year_idx++; // step to next year
                month_idx_of_season = Calendar.JANUARY;
                break;
            default:
                assert false;
                break;
        }

        // 获取下个季度的第一天
        final Calendar tmp_cal = Calendar.getInstance();
        tmp_cal.setTime(_date);
        tmp_cal.set(Calendar.YEAR, year_idx);
        tmp_cal.set(Calendar.MONTH, month_idx_of_season);
        tmp_cal.set(Calendar.DAY_OF_MONTH, tmp_cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        return tmp_cal.getTime();
    }

    /**
     * 返回指定日期所在季度的第一天
     *
     * @Author Elvis Wang <bo.wang35@renren-inc.com>
     * @param _date
     * @return
     */
    private static Date getFirstDateOfSeason(
                    final Date _date) {

        final Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTime(_date);

        // 季度的第一个月序号
        int first_month_idx_of_season = -1;

        final int month_idx = cur_cal.get(Calendar.MONTH);
        switch (month_idx) {
            case Calendar.JANUARY: // fall through
            case Calendar.FEBRUARY: // fall through
            case Calendar.MARCH: // fall through
                first_month_idx_of_season = Calendar.JANUARY;
                break;
            case Calendar.APRIL: // fall through
            case Calendar.MAY: // fall through
            case Calendar.JUNE: // fall through
                first_month_idx_of_season = Calendar.APRIL;
                break;
            case Calendar.JULY: // fall through
            case Calendar.AUGUST: // fall through
            case Calendar.SEPTEMBER: // fall through
                first_month_idx_of_season = Calendar.JULY;
                break;
            case Calendar.OCTOBER: // fall through
            case Calendar.NOVEMBER: // fall through
            case Calendar.DECEMBER: // fall through
                first_month_idx_of_season = Calendar.OCTOBER;
                break;
            default:
                assert false;
                break;
        }

        // 获取所在季度的第一天
        final Calendar tmp_cal = Calendar.getInstance();
        tmp_cal.setTime(_date);
        tmp_cal.set(Calendar.MONTH, first_month_idx_of_season);
        tmp_cal.set(Calendar.DAY_OF_MONTH, tmp_cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        return tmp_cal.getTime();
    }

    /**
     * 返回指定日期所在季度的最后一天
     *
     * @Author Elvis Wang <bo.wang35@renren-inc.com>
     * @param _date
     * @return
     */
    private static Date getLastDateOfSeason(
                    final Date _date) {

        final Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTime(_date);

        // 季度的最后一个月序号
        int end_month_idx_of_season = -1;

        final int month_idx = cur_cal.get(Calendar.MONTH);
        switch (month_idx) {
            case Calendar.JANUARY: // fall through
            case Calendar.FEBRUARY: // fall through
            case Calendar.MARCH: // fall through
                end_month_idx_of_season = Calendar.MARCH;
                break;
            case Calendar.APRIL: // fall through
            case Calendar.MAY: // fall through
            case Calendar.JUNE: // fall through
                end_month_idx_of_season = Calendar.JUNE;
                break;
            case Calendar.JULY: // fall through
            case Calendar.AUGUST: // fall through
            case Calendar.SEPTEMBER: // fall through
                end_month_idx_of_season = Calendar.SEPTEMBER;
                break;
            case Calendar.OCTOBER: // fall through
            case Calendar.NOVEMBER: // fall through
            case Calendar.DECEMBER: // fall through
                end_month_idx_of_season = Calendar.DECEMBER;
                break;
            default:
                assert false;
                break;
        }

        // 获取所在季度的最后一天
        final Calendar end_cal = Calendar.getInstance();
        end_cal.setTime(_date);
        end_cal.set(Calendar.MONTH, end_month_idx_of_season);
        end_cal.set(Calendar.DAY_OF_MONTH, end_cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return end_cal.getTime();
    }

    /**
     * 获取几年以来的季度列表
     * @param yearCount
     * @return
     */
    public static List<String> getQuarList(int yearCount) {
        if (yearCount <= 0) {
            return Lists.newArrayList();
        }

        List<String> quarterList = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        //1、今年以前的
        if (yearCount > 1) {
            for (int i = yearCount - 1; i > 0; i--) {
                int preYear = year - i;
                int prePreYear = preYear -1;
                if (month < 4) {
                    //去年的Q
                    //quarterList.add(prePreYear + "Q1");
                    quarterList.add(prePreYear + "Q2");
                    quarterList.add(prePreYear + "Q3");
                    quarterList.add(prePreYear + "Q4");
                    quarterList.add(preYear + "Q1");
                } else if (month < 7) {
                    //quarterList.add(prePreYear + "Q2");
                    quarterList.add(prePreYear + "Q3");
                    quarterList.add(prePreYear + "Q4");
                    quarterList.add(preYear + "Q1");
                    quarterList.add(preYear + "Q2");
                } else if (month < 10) {
                    //quarterList.add(prePreYear + "Q3");
                    quarterList.add(prePreYear + "Q4");
                    quarterList.add(preYear + "Q1");
                    quarterList.add(preYear + "Q2");
                    quarterList.add(preYear + "Q3");
                } else {
                    //quarterList.add(prePreYear + "Q4");
                    quarterList.add(preYear + "Q1");
                    quarterList.add(preYear + "Q2");
                    quarterList.add(preYear + "Q3");
                    quarterList.add(preYear + "Q4");
                }
            }
        }

        //2、近1年
        int lastYear = year - 1;
        if (month < 4) {
            //去年的Q
            //quarterList.add(lastYear + "Q1");
            quarterList.add(lastYear + "Q2");
            quarterList.add(lastYear + "Q3");
            quarterList.add(lastYear + "Q4");
            quarterList.add(year + "Q1");
        } else if (month < 7) {
            //quarterList.add(lastYear + "Q2");
            quarterList.add(lastYear + "Q3");
            quarterList.add(lastYear + "Q4");
            quarterList.add(year + "Q1");
            quarterList.add(year + "Q2");
        } else if (month < 10) {
            //quarterList.add(lastYear + "Q3");
            quarterList.add(lastYear + "Q4");
            quarterList.add(year + "Q1");
            quarterList.add(year + "Q2");
            quarterList.add(year + "Q3");
        } else {
            //quarterList.add(lastYear + "Q4");
            quarterList.add(year + "Q1");
            quarterList.add(year + "Q2");
            quarterList.add(year + "Q3");
            quarterList.add(year + "Q4");
        }

        return quarterList;
    }
    
    /**
     * 获取几年以来的季度列表(不算当季度)
     * @param yearCount
     * @return
     */
    public static List<String> getQuarList2(int yearCount) {
        if (yearCount <= 0) {
            return Lists.newArrayList();
        }

        List<String> quarterList = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        //1、今年以前的
        if (yearCount > 1) {
            for (int i = yearCount - 1; i > 0; i--) {
                int preYear = year - i;
                int prePreYear = preYear -1;
                if (month < 4) {
                    //去年的Q
                    quarterList.add(prePreYear + "Q1");
                    quarterList.add(prePreYear + "Q2");
                    quarterList.add(prePreYear + "Q3");
                    quarterList.add(prePreYear + "Q4");
                   
                } else if (month < 7) {
                    quarterList.add(prePreYear + "Q2");
                    quarterList.add(prePreYear + "Q3");
                    quarterList.add(prePreYear + "Q4");
                    quarterList.add(preYear + "Q1");
                } else if (month < 10) {
                    quarterList.add(prePreYear + "Q3");
                    quarterList.add(prePreYear + "Q4");
                    quarterList.add(preYear + "Q1");
                    quarterList.add(preYear + "Q2");
//                    quarterList.add(preYear + "Q3");
                } else {
                    quarterList.add(prePreYear + "Q4");
                    quarterList.add(preYear + "Q1");
                    quarterList.add(preYear + "Q2");
                    quarterList.add(preYear + "Q3");
//                    quarterList.add(preYear + "Q4");
                }
            }
        }

        //2、近1年
        int lastYear = year - 1;
        if (month < 4) {
            //去年的Q
            quarterList.add(lastYear + "Q1");
            quarterList.add(lastYear + "Q2");
            quarterList.add(lastYear + "Q3");
            quarterList.add(lastYear + "Q4");
//            quarterList.add(year + "Q1");
        } else if (month < 7) {
            quarterList.add(lastYear + "Q2");
            quarterList.add(lastYear + "Q3");
            quarterList.add(lastYear + "Q4");
            quarterList.add(year + "Q1");
//            quarterList.add(year + "Q2");
        } else if (month < 10) {
            quarterList.add(lastYear + "Q3");
            quarterList.add(lastYear + "Q4");
            quarterList.add(year + "Q1");
            quarterList.add(year + "Q2");
//            quarterList.add(year + "Q3");
        } else {
            quarterList.add(lastYear + "Q4");
            quarterList.add(year + "Q1");
            quarterList.add(year + "Q2");
            quarterList.add(year + "Q3");
//            quarterList.add(year + "Q4");
        }

        return quarterList;
    }
    
    
    /**
     * 获取几年以来的季度列表
     * @param yearCount
     * @return
     */
    public static List<String> getYearList(int yearCount) {
    	
    	List<String> yearList = Lists.newArrayListWithCapacity(yearCount);
    	
    	
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        
        for(int i=1;i<=yearCount;i++){
        	calendar.add(Calendar.YEAR, -1);
        	yearList.add(TimeUtils.format(calendar.getTime(), TimeUtils.yyyy));
        }
    	
    	return yearList;
    	
    	
    	
    }

    /**
     * 获取最新的quarter
     * 
     * @return
     */
    public static String getLastestQuarter() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(date);
        int year = Integer.parseInt(currentDate.split("-")[0]);
        int month = Integer.parseInt(currentDate.split("-")[1]);
        String quarter = null;
        if (month < 4) {
            year = year - 1;
            quarter = Integer.toString(year) + "Q4";
        } else if (month < 7) {
            quarter = Integer.toString(year) + "Q1";
        } else if (month < 10) {
            quarter = Integer.toString(year) + "Q2";
        } else {
            quarter = Integer.toString(year) + "Q3";
        }
        return quarter;
    }
    
    /**
     * 根据日期得到对应的季度名称
     *
     * 结果形如 "2015第1季度"
     *
     * @Author Elvis Wang <bo.wang35@renren-inc.com>
     *
     * @param _date
     * @return
     */
    public static String getSeasonQuaterName(
                    final Date _date) {

        final int SPRING = 1;
        final int SUMMER = 2;
        final int AUTUMN = 3;
        final int WINTER = 4;

        final Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTime(_date);

        int season_idx = SPRING;

        final int month_idx = cur_cal.get(Calendar.MONTH);
        switch (month_idx) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season_idx = SPRING;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season_idx = SUMMER;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season_idx = AUTUMN;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season_idx = WINTER;
                break;
            default:
                assert false;
                return "未知季度";
        }

        return Joiner.on("").join(cur_cal.get(Calendar.YEAR), "Q", season_idx);
    }

    //获取两时间端相差的天数,返回格式XX年又XX天
    public static String getTotalCountBetweenTwoDays(
            final Date date1, final Date date2
    ){
        int days = getBetweenDays(date1, date2);
        int yieldOfYear = 0;
        while(days >= 365){
            days -= 365;
            yieldOfYear++;
        }

        StringBuffer sb = new StringBuffer();
        if(yieldOfYear > 0 && days > 0){
            sb.append(yieldOfYear)
                    .append("年")
                    .append("又")
                    .append(days)
                    .append("天");
        }else{
            if(yieldOfYear > 0){
                sb.append(yieldOfYear).append("年");
            }else{
                sb.append(days).append("天");
            }
        }
        return sb.toString();
    }

    public static boolean equalByDay(Date date1, Date date2){
        String time1 = format(date1, yyyy_MM_dd);
        String time2 = format(date2, yyyy_MM_dd);
        return time1.equals(time2);
    }

    public static String transforTimeToQuater(Date date){
        String year = format(date, yyyy);
        int month = date.getMonth();
        return year + " " + "Q" +(month/3 + 1);
    }

    //time
    public static String transforStringToQuater(String time, String timeFormat) {
        try {
            Date date = parse(time, timeFormat);
            return transforTimeToQuater(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //校验某对象的字段是否包含指定数据,如果包含返回包含时间的对象的索引
    //by hehan
    public static <T> int isDayExist(String date, List<T> list, String col){
        try {
            String firstLetter = col.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + col.substring(1);

            for (int i = 0; i < list.size(); i++) {
                if (null == list.get(i)) {
                    continue;
                }
                Method method = list.get(i).getClass().getMethod(getter, new Class[]{});

                Object time = method.invoke(list.get(i), new Object[]{});
                String eachDateStr = "";
                if(time instanceof Date){
                    eachDateStr = TimeUtils.format((Date)time, TimeUtils.yyyy_MM_dd);
                }else {
                    eachDateStr = time.toString();
                }
                if (null == eachDateStr) {
                    continue;
                }
                if (eachDateStr.contains(date)) {
                    return i;
                }
            }
        }catch (Exception e){
            return -1;
        }
        return -1;
    }

    //将map<Date, T>转为map<string, T>
    public static <T> Map<String, T> transferDateMapToStringMap(Map<Date, T> map){
        Map<String, T> result = Maps.newHashMap();
        for(Date time : map.keySet()){
            result.put(format(time, yyyy_MM_dd), map.get(time));
        }
        return result;
    }


    /**
     * 获取几年以来的季度列表,代码尽量写通用点,大家都方便
     * @param yearCount
     * @param space 中间是否有空格
     * @param numberHead 结果是否数字在前面
     * @return
     *
     */
    public static List<String> getLastestQuartersByTime(int yearCount, String space, boolean numberHead) {
        if (yearCount <= 0) {
            return Lists.newArrayList();
        }
        List<String> result = Lists.newArrayList();

        Calendar now = Calendar.getInstance();

        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH) + 1;

        while(yearCount >= 0){
            int eachYear = currentYear - yearCount;
            int eachQuarterSize = 4; //默认一年4个季度
            if(eachYear == currentYear){//如果是当年的话 获取当年可以展示的季度数量
                eachQuarterSize = currentMonth / 3;
            }
            if(eachQuarterSize > 0) {
                int eachQuarter = 1;
                while(eachQuarter <= eachQuarterSize) {
                    result.add(eachYear + space + "Q" + eachQuarter);
                    eachQuarter++;
                }
            }
            yearCount--;
        }

        if(!numberHead){//如果数字不在前面,需要翻转数据
            List<String> result_reverse = Lists.newArrayList();
            for(String data : result) {
                char[] charAry = data.toCharArray();
                reverseData(charAry, 0, 3); //年反转
                reverseData(charAry, space.length() + 4, charAry.length - 1); //季度反转
                reverseData(charAry, 0, charAry.length - 1); //反转整体

                result_reverse.add(new String(charAry));
            }
            result.clear();
            result.addAll(result_reverse);
        }
        return  result;
    }

    //数据倒置
    public static  void reverseData(char[] arr, int start, int end){
        while(start < end){
            char tem = arr[start];
            arr[start] = arr[end];
            arr[end] = tem;

            start++;
            end--;
        }
    }

    public static void main(
            String[] args) throws ParseException {
//        Date startDate = TimeUtils.parse("2015-06-01", TimeUtils.yyyy_MM_dd);
//        Date endDate = TimeUtils.parse("2015-06-10", TimeUtils.yyyy_MM_dd);
//        List<Date> dateListBetweenDays = TimeUtils.getDateListBetweenDays(startDate, true, endDate, true);
//        System.out.println(TimeUtils.batchFormat(dateListBetweenDays, TimeUtils.yyyy_MM_dd_Chinese));

        /*String a = "123456";
        char[] arr = a.toCharArray();
        reverseData(arr, 0, 5);
        System.out.println(new String(arr));*/

        System.out.println(getLastestQuartersByTime(5, " ", false));

    }
}
