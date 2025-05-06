package org.example.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {
    private String tableName;//表名

    private String beanName;//bean名称
    private String beanParamName;//参数名称
    private String Comment;//表注释
    private List<FieldInfo> fieldsList;//字段名
    private Map<String,List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();//唯一索引集合
    /*是否有相关类型*/
    private Boolean haveDate;
    private Boolean haveTime;
    private Boolean bigDecimal;
    private Boolean haveDateTime;
    private Boolean haveBigDecimal;
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getBeanName() {
        return beanName;
    }
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    public String getBeanParamName() {
        return beanParamName;
    }
    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }
    public String getComment() {
        return Comment;
    }
    public void setComment(String comment) {
        Comment = comment;
    }
    public List<FieldInfo> getFieldsList() {
        return fieldsList;
    }
    public void setFieldsList(List<FieldInfo> fieldsList) {
        this.fieldsList = fieldsList;
    }
    public Boolean getHaveDate() {
        return haveDate;
    }
    public void setHaveDate(Boolean haveDate) {
        this.haveDate = haveDate;
    }
    public Boolean getHaveTime() {
        return haveTime;
    }
    public void setHaveTime(Boolean haveTime) {
        this.haveTime = haveTime;
    }
    public Boolean getBigDecimal() {
        return bigDecimal;
    }
    public void setBigDecimal(Boolean bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
    public Map<String,List<FieldInfo>> getKeyIndexMap() {
        return keyIndexMap;
    }
    public void setKeyIndexMap(Map<String,List<FieldInfo>> keyIndexMap) {
        this.keyIndexMap = keyIndexMap;
    }
    public Boolean getHaveDateTime() {
        return haveDateTime;
    }
    public void setHaveDateTime(Boolean haveDateTime) {
        this.haveDateTime = haveDateTime;
    }
    public Boolean getHaveBigDecimal() {
        return haveBigDecimal;
    }
    public void setHaveBigDecimal(Boolean haveBigDecimal) {
        this.haveBigDecimal = haveBigDecimal;
    }

}
