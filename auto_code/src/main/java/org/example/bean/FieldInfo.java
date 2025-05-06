package org.example.bean;

import java.util.List;

public class FieldInfo {
    private String fieldName;
    private String propertyName;
    private String sqlType;

    private String javaType;
    private String comment;
    /*字段是否自增长*/

    private Boolean isAutoIncrement;
    public  String getFieldName(){
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    public String getSqlType() {
        return sqlType;
    }
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }
    public String getJavaType() {
        return javaType;
    }
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Boolean getIsAutoIncrement() {
        return isAutoIncrement;
    }
    public void setIsAutoIncrement(Boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

}
