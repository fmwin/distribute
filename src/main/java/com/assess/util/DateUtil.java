package com.assess.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static String getToday(){
        Date dt = new Date();
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        return sdf.format(dt);
    }

    public static Date getMonthAgo(int month){
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -month);
        Date monthAgo = c.getTime();

        return monthAgo;
    }
}
