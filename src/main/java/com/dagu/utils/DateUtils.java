package com.dagu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date getDate(String date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(true);
        Date d = null;
        try {
             d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public static String toString(Date date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }
}
