package org.example.utils;

import java.text.SimpleDateFormat;
import java.util.Date;



public class DateUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String format(Date date, String patten){
        return new SimpleDateFormat(patten).format(date);
    }
    public static String parse(String date,String patten){
        new SimpleDateFormat(patten).format(date);
        return null;
    }
}

//public class DateUtils {
//    public static final Object object = new Object();
//    public static Map<String,ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String,ThreadLocal<SimpleDateFormat>>();
//    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
//        ThreadLocal<SimpleDateFormat> threadLocal = sdfMap.get(pattern);
//        if (threadLocal == null) {
//            synchronized (object) {
//                threadLocal = sdfMap.get(pattern);
//                if (threadLocal == null) {
//                    threadLocal = new ThreadLocal<SimpleDateFormat>(){
//                        @Override
//                        protected SimpleDateFormat initialValue() {
//                            return new SimpleDateFormat(pattern);
//                        }
//                    };
//                    sdfMap.put(pattern, threadLocal);
//                }
//            }
//        }
//        return threadLocal.get();
//    }
//    public static String formatDate(Date date,String pattern){
//        return getSimpleDateFormat(pattern).format(date);
//    }
//    public static Date parseDate(String date,String pattern){
//        try{
//            return getSimpleDateFormat(pattern).parse(date);
//
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//}