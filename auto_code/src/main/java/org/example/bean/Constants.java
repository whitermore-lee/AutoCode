package org.example.bean;

import org.example.utils.PropertiesUtiles;
/*
* 常量类*/
public class Constants {
    public static String AUTHOR_COMMENT; //生成作者名称
    public static Boolean IGNORE_TABLE_PERFIX;
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_MAPPER;//mapper后缀
//    文件路径
    public static String PATH_BASE;
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PATH_PO;
    public static String PATH_JAVA = "java";
    public static String PATH_QUERY;
    public static String PACKAGE_QUERY;
    public static String PATH_RESOURCES = "resources";
    public static String PACKAGE_SERVICE;
    public static String PACKAGE_SERVICE_IMPL;
    public static String PATH_SERVICE;
    public static String PATH_SERVICE_IMPL;
    public static String PACKAGE_VO;//分页
    public static String PATH_VO;
    public static String PACKAGE_EXCEPTION;
    public static String PATH_EXCEPTION;
    public static String PACKAGE_CONTROLLER;
    public static String PATH_CONTROLLER;

//    工具类位置
    public static String PACKAGE_UTILS;
    public static String PATH_UTILS;
//    枚举
    public static String PACKAGE_ENUMS;
    public static String PATH_ENUMS;
//  Basemapper
    public static String PACKAGE_MAPPER;
    public static String PATH_MAPPER;
    public static String PATH_MAPPER_XML;
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
//    query相关配置
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;

    static {
        /*配置文件获取*/
        IGNORE_TABLE_PERFIX =Boolean.valueOf( PropertiesUtiles.getProperty("ignore.table.perfix"));
        /*query文件配置*/
        SUFFIX_BEAN_QUERY = String.valueOf(PropertiesUtiles.getProperty("suffix.bean.query"));
        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtiles.getProperty("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtiles.getProperty("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtiles.getProperty("suffix.bean.query.time.end");
        SUFFIX_MAPPER = PropertiesUtiles.getProperty("suffix.mapper");
        /*作者信息*/
        AUTHOR_COMMENT = String.valueOf(PropertiesUtiles.getProperty("author.comment"));
        /*文件路径*/
        PACKAGE_BASE = PropertiesUtiles.getProperty("package.base");

        PACKAGE_PO = PACKAGE_BASE +"." +PropertiesUtiles.getProperty("package.po");//实体类po层
        PACKAGE_UTILS = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.utils");//工具类
        PACKAGE_ENUMS = PACKAGE_BASE +"."+PropertiesUtiles.getProperty("packgae.enum");//日期枚举
        PACKAGE_QUERY = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.query");//Query
        PACKAGE_MAPPER = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.mapper");//Mapper
        PACKAGE_SERVICE =PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.service");
        PACKAGE_SERVICE_IMPL = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.service.impl");
        PACKAGE_VO = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.vo");
        PACKAGE_EXCEPTION = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.exception");
        PACKAGE_CONTROLLER = PACKAGE_BASE+"."+PropertiesUtiles.getProperty("package.controller");

        PATH_BASE = PropertiesUtiles.getProperty("path.base");//import包地址
        PATH_BASE = PATH_BASE + PATH_JAVA ;;

        PATH_PO = PATH_BASE + "/" +PACKAGE_PO.replace(".","/");//po文件地址
        /*工具类路径*/
        PATH_UTILS = PATH_BASE+"/"+PACKAGE_UTILS.replace(".","/");//工具类path
        PATH_ENUMS = PATH_BASE+"/"+PACKAGE_ENUMS.replace(".","/");//枚举path
        PATH_QUERY = PATH_BASE+"/"+PACKAGE_QUERY.replace(".","/");
        PATH_MAPPER = PATH_BASE+"/"+PACKAGE_MAPPER.replace(".","/");
        PATH_SERVICE = PATH_BASE+"/"+PACKAGE_SERVICE.replace(".","/");
        PATH_SERVICE_IMPL = PATH_BASE+"/"+PACKAGE_SERVICE_IMPL.replace(".","/");
        PATH_VO = PATH_BASE+"/"+PACKAGE_VO.replace(".","/");
        PATH_EXCEPTION = PATH_BASE+"/"+PACKAGE_EXCEPTION.replace(".","/");
        PATH_CONTROLLER = PATH_BASE+"/"+PACKAGE_CONTROLLER.replace(".","/");

        PATH_MAPPER_XML = PropertiesUtiles.getProperty("path.base")+PATH_RESOURCES+"/"+PACKAGE_MAPPER.replace(".","/");


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
        System.out.println(PATH_SERVICE_IMPL);

    }


}
