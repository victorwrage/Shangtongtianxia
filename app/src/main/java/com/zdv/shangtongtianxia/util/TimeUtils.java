package com.zdv.shangtongtianxia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hello_world on 2017/9/23.
 */

public class TimeUtils {
    /**
     * 与当前时间比较早晚
     *
     * @param time 需要比较的时间
     * @return 输入的时间比现在时间晚则返回true
     */
    public static boolean compareNowTime(String time) {
        boolean isDayu = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date parse = dateFormat.parse(time);
            Date parse1 = dateFormat.parse(getDate());

            long diff = parse1.getTime() - parse.getTime();
            if (diff <= 0) {
                isDayu = true;
            } else {
                isDayu = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isDayu;
    }

    /**
     * 获取当前时间
     *
     * @return
     */

    public static String getDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    public static String getDate2() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

}
