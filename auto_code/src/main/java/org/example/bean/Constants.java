package org.example.bean;

import org.example.utils.PropertiesUtiles;
/*
* 常量类*/
public class Constants {
    public static String AUTHOR_COMMENT;

    public static Boolean IGNORE_TABLE_PERFIX;
    public static String SUFFIX_BEAN_PARAM;
    public static String PATH_BASE;
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PATH_PO;
    public static String PATH_JAVA = "java";
    public static String PATH_RESOURCES = "resources";
    static {
        /*配置文件获取*/
        IGNORE_TABLE_PERFIX =Boolean.valueOf( PropertiesUtiles.getProperty("ignore.table.perfix"));
        SUFFIX_BEAN_PARAM = String.valueOf(PropertiesUtiles.getProperty("suffix.bean.param"));

        AUTHOR_COMMENT = String.valueOf(PropertiesUtiles.getProperty("author.comment"));
        /*文件路径*/
        PACKAGE_BASE=PropertiesUtiles.getProperty("package.base");

        PACKAGE_PO = PACKAGE_BASE +"." +PropertiesUtiles.getProperty("package.po");

        PATH_BASE = PropertiesUtiles.getProperty("path.base");
        PATH_BASE = PATH_BASE + PATH_JAVA ;;

        PATH_PO = PATH_BASE + "/" +PACKAGE_PO.replace(".","/");


    }

    /*数据类型*/
    public static String[] SQL_DATA_TIME_TYPE = new String[]{"datetime","timestamp"};
    public static String[] SQL_DATA_TYPE = new String[]{"date"};
    public static String[] SQL_DECIMAL_TYPE = new String[]{"decimal","double","float"};
    public static String[] SQL_STRING_TYPE = new String[]{"char","varchar","text","mediumtext","longtext"};
    public static String[] SQL_INTEGER_TYPE = new String[]{"int","tinyint"};
    public static String[] SQL_LONG_TYPE = new String[]{"bigint"};

    public static void main(String[] args) {
        System.out.println(PATH_PO);
        System.out.println(PACKAGE_PO);

    }


}
