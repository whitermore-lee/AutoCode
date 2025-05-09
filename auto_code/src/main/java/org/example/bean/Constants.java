package org.example.bean;

import org.example.utils.PropertiesUtiles;
/*
* 常量类*/
public class Constants {
    public static String AUTHOR_COMMENT; //生成作者名称
    public static Boolean IGNORE_TABLE_PERFIX;
    public static String SUFFIX_BEAN_PARAM;
//    文件路径
    public static String PATH_BASE;
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PATH_PO;
    public static String PATH_JAVA = "java";
    public static String PACKAGE_PARAM;
    public static String PATH_RESOURCES = "resources";

    public static String PACKAGE_UTILS;
    public static String PATH_UTILS;
//    需要忽略的属性
    public static String IGNORE_BEAN_TOJSON_FILED;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;
//    日期序列化
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;
//    日期反序列化
    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;

    static {
        /*配置文件获取*/
        IGNORE_TABLE_PERFIX =Boolean.valueOf( PropertiesUtiles.getProperty("ignore.table.perfix"));
        SUFFIX_BEAN_PARAM = String.valueOf(PropertiesUtiles.getProperty("suffix.bean.param"));

        AUTHOR_COMMENT = String.valueOf(PropertiesUtiles.getProperty("author.comment"));
        /*文件路径*/
        PACKAGE_PARAM = PropertiesUtiles.getProperty("package.param");
        PACKAGE_BASE = PropertiesUtiles.getProperty("package.base");

        PACKAGE_PO = PACKAGE_BASE +"." +PropertiesUtiles.getProperty("package.po");
        PACKAGE_UTILS = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.utils");

        PATH_BASE = PropertiesUtiles.getProperty("path.base");
        PATH_BASE = PATH_BASE + PATH_JAVA ;;

        PATH_PO = PATH_BASE + "/" +PACKAGE_PO.replace(".","/");
        /*工具类路径*/
        PATH_UTILS = PATH_BASE+"/"+PACKAGE_UTILS.replace(".","/");

        /*需要忽略的属性*/
        IGNORE_BEAN_TOJSON_FILED = PropertiesUtiles.getProperty("ignore.bean.tojson.filed");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtiles.getProperty("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtiles.getProperty("ignore.bean.tojson.class");

        /*日期的序列化和反序列化*/
        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtiles.getProperty("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtiles.getProperty("bean.date.format.class");
        BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtiles.getProperty("bean.date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = PropertiesUtiles.getProperty("bean.date.unformat.class");
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
