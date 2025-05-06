package org.example.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static String format(Date date, String patten){
        return new SimpleDateFormat(patten).format(date);
    }
    public static String parse(String date,String patten){
        new SimpleDateFormat(patten).format(date);
        return null;
    }
}
