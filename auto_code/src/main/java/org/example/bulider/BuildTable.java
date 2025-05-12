package org.example.bulider;

import org.apache.commons.lang3.ArrayUtils;
import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.example.utils.JsonUtils;
import org.example.utils.PropertiesUtiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTable {
    private  static  String SQL_SHOW_INDEX_FROM = "show index from %s";
    private static String SQL_SHOW_FULL_FIELD_FROM = "show full fields from %s";
    private static String SQL_SHOW_TABLE_STATUS = "show table status";
    private static Connection connection = null;
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);

    /*连接数据库*/
    static {
        String drivername = PropertiesUtiles.getProperty("db.driver.name");
        String url = PropertiesUtiles.getProperty("db.url");
        String user = PropertiesUtiles.getProperty("db.username");
        String password = PropertiesUtiles.getProperty("db.userpassword");
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("数据库连接错误", e);
        }
    }

    //获取表
    public static List<TableInfo> getTables() {
        List<TableInfo> tableInfoList = new ArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_SHOW_TABLE_STATUS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String tableName = resultSet.getString("name");
                String comment = resultSet.getString("comment");
                logger.info("tableName:" + tableName + " comment:" + comment);

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PERFIX) {
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }
                beanName = processFiled(beanName, true);
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);
                readFieldInfo(tableInfo);
//                logger.info("表格转换json:{}" ,JsonUtils.convertObjectToJson(tableInfo));
//                logger.info("字段转换json:{}",JsonUtils.convertObjectToJson(fieldInfoLIst));
                getKeyIndexInfo(tableInfo);//获取索引
                logger.info("tableinfo:{}",JsonUtils.convertObjectToJson(tableInfo));
//                readFieldInfo(tableInfo);
//                logger.info("表：{},备注:{},javabean:{},javaparambean:{}", tableInfo.getTableName(), tableInfo.getComment(), tableInfo.getBeanName(), tableInfo.getBeanParamName());
                tableInfoList.add(tableInfo);
            }
        } catch (Exception e) {
            logger.error("读取表失败");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return tableInfoList;
    }

    /*读取表字段*/
    private static void readFieldInfo(TableInfo tableInfo) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        List<FieldInfo> fieldExtendList = new  ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet filedResultSet = null;
        try {
            Boolean haveDataTime = false;
            Boolean haveBigDecimal = false;
            Boolean haveData = false;
            preparedStatement = connection.prepareStatement(String.format(SQL_SHOW_FULL_FIELD_FROM,tableInfo.getTableName()));
            filedResultSet = preparedStatement.executeQuery();
            while (filedResultSet.next()) {
                String filed = filedResultSet.getString("field");
                String type = filedResultSet.getString("type");
                String extra = filedResultSet.getString("extra");
                String comment = filedResultSet.getString("comment");
                if (type.indexOf("(")>0){
                    type = type.substring(0,type.indexOf("("));
                }
                String  propertyName = processFiled(filed, false);

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfoList.add(fieldInfo);
                fieldInfo.setFieldName(filed);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setIsAutoIncrement("auto_increment".equalsIgnoreCase(extra)?true:false);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));

                if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPE,type)){
                    haveDataTime = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DATA_TYPE,type)){
                    haveData = true;
                }
                if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE,type)){
                    haveBigDecimal = true;
                }
                //扩展属性
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPE,type)){
                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setPropertyName(fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setSqlType(type);
                    fieldExtendList.add(fuzzyField);
                }
                if (ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPE,type)||ArrayUtils.contains(Constants.SQL_DATA_TYPE,type)){
                    FieldInfo timeStartField = new FieldInfo();
                    timeStartField.setJavaType("String");
                    timeStartField.setPropertyName(fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_TIME_START);
                    timeStartField.setFieldName(fieldInfo.getFieldName());
                    timeStartField.setSqlType(type);
                    fieldExtendList.add(timeStartField);

                    FieldInfo timeEndtField = new FieldInfo();
                    timeEndtField.setJavaType("String");
                    timeEndtField.setPropertyName(fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_TIME_END);
                    timeEndtField.setFieldName(fieldInfo.getFieldName());
                    timeEndtField.setSqlType(type);
                    fieldExtendList.add(timeEndtField);

                }
            }
            tableInfo.setHaveBigDecimal(haveBigDecimal);
            tableInfo.setHaveDate(haveData);
            tableInfo.setHaveDateTime(haveDataTime);
            tableInfo.setFieldsList(fieldInfoList);//设置索引
            tableInfo.setFieldExtendList(fieldExtendList);
        } catch (Exception e) {
            logger.info("读取字段失败",e);
        } finally {
            if (filedResultSet != null) {
                try {
                    filedResultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*唯一索引*/
    private static void getKeyIndexInfo(TableInfo tableInfo) {
        PreparedStatement preparedstatement = null;
        ResultSet resultSet = null;

        try {
            Map<String,FieldInfo> fieldInfoMap = new HashMap<>();
            for(FieldInfo fieldInfo:tableInfo.getFieldsList()){
                fieldInfoMap.put(fieldInfo.getFieldName(),fieldInfo);
            }
            preparedstatement = connection.prepareStatement(String.format(SQL_SHOW_INDEX_FROM, tableInfo.getTableName()));
            resultSet = preparedstatement.executeQuery();
            while (resultSet.next()) {
                String keyName = resultSet.getString("key_name");
                String cloumnName = resultSet.getString("column_name");
                Integer nonUnique = resultSet.getInt("non_unique");//判断是否是索引的标志0：否，1：是
                if (1 == nonUnique) {
                    continue;
                }
                List<FieldInfo> keyFieldInfoList = tableInfo.getKeyIndexMap().get(keyName);
                if (null == keyFieldInfoList) {
                    keyFieldInfoList = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldInfoList);
                }
                keyFieldInfoList.add(fieldInfoMap.get(cloumnName));
            }
        } catch (SQLException e) {
            logger.error("读取索引失败", e);
        } finally {
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /*判断类型*/
    private static String processJavaType(String type){
        if(ArrayUtils.contains(Constants.SQL_INTEGER_TYPE,type)){
            return "Integer";
        }else if(ArrayUtils.contains(Constants.SQL_LONG_TYPE,type)){
            return "Long";
        }else if (ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPE,type)){
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE,type)) {
            return "String";
        }else if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE,type)){
            return "BigDecimal";
        }else {
            throw new RuntimeException("无法识别类型"+type);
        }
    }

    /*下划线转成大写*/
    private static String processFiled(String filed, Boolean uperCaseFirstLetter) {

        StringBuilder stringBuilder = new StringBuilder();
        String[] fileds = filed.split("_");
        stringBuilder.append(uperCaseFirstLetter ? StringUtils.uperCaseFirstLetter(fileds[0]) : fileds[0]);
        for (int i = 1; i < fileds.length; i++) {
            stringBuilder.append(StringUtils.uperCaseFirstLetter(fileds[i]));
        }
        return stringBuilder.toString();
    }
}
