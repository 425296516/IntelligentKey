package com.aigo.router.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangcirui on 2017/2/15.
 */

public class TimeUtil {

    public static int getNowYear() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(currentTime);

        return Integer.parseInt(dateString);
    }

    public static int getNowMonth() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String dateString = formatter.format(currentTime);

        return Integer.parseInt(dateString);
    }

    //根据日期取得星期几
    public static String getDayAndWeek(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String day = formatter.format(date);

        return day + "日" + "-" + week;
    }

    //根据日期取得星期几
    public static int getDay(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String day = formatter.format(date);

        return Integer.parseInt(day);
    }

    public static String getHourMinute(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String dateString = formatter.format(date);

            return dateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
