package org.example.bean;

import org.example.utils.PropertiesUtiles;

public class Constants {
    public static Boolean IGNORE_TABLE_PERFIX;
    public static String SUFFIX_BEAN_PARAM;
    static {
        IGNORE_TABLE_PERFIX =Boolean.valueOf( PropertiesUtiles.getProperty("ignore.table.perfix"));
        SUFFIX_BEAN_PARAM = String.valueOf(PropertiesUtiles.getProperty("suffix.bean.param"));

    }

    public static String[] SQL_DATA_TIME_TYPE = new String[]{"datetime","timestamp"};
    public static String[] SQL_DATA_TYPE = new String[]{"date"};
    public static String[] SQL_DECIMAL_TYPE = new String[]{"decimal","double","float"};

    public static String[] SQL_STRING_TYPE = new String[]{"char","varchar","text","mediumtext","longtext"};

    public static String[] SQL_INTEGER_TYPE = new String[]{"int","tinyint"};

    public static String[] SQL_LONG_TYPE = new String[]{"bigint"};

}
