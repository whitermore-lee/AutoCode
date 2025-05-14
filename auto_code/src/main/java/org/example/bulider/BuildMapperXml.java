package org.example.bulider;

import org.apache.commons.lang3.ArrayUtils;
import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildMapperXml {
    private static Logger logger= LoggerFactory.getLogger(BuildMapper.class);

    private static final String  BASE_COLUMN_LIST = "base_column_list";

    private static final String  BASE_QUERY_CONDITION = "base_query_condition";

    private static final String  BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";


    private static final String QUERY_CONDITION = "query_condition";

    public static void excetus(TableInfo tableInfo){
        /*判断目录是否存在*/
        File folder = new File(Constants.PATH_MAPPER_XML);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String className = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()+Constants.SUFFIX_MAPPER);
        File mapperXmlFile = new File(folder,className+".xml");
        /*输入输出流*/
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw  = null;

        try {
            outputStream = new FileOutputStream(mapperXmlFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bw = new BufferedWriter(outputStreamWriter);


            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write(" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write("<mapper namespace=\""+Constants.PACKAGE_MAPPER+"."+className+"\">");
            bw.newLine();
            /*生成mapper映射*/
            bw.write("\t<!--实体映射-->");
            bw.newLine();

            String poMapClass = Constants.PACKAGE_PO+"."+tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\""+poMapClass+"\">");
            bw.newLine();

            /*property 查找主键id*/
            FieldInfo idFeild = null;
            Map<String,List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String ,List<FieldInfo>> entry:keyIndexMap.entrySet()){
                if("PRIMARY".equals(entry.getKey())){
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if(fieldInfoList.size()==1){//主键为1个的情况
                        idFeild = fieldInfoList.get(0);
                        break;
                    }
                }

            }
            for (FieldInfo fieldInfo : tableInfo.getFieldsList()){
                bw.write("\t\t<!--"+fieldInfo.getComment()+"-->");
                bw.newLine();
                String key = "";
                if (idFeild!=null && fieldInfo.getPropertyName().equals(idFeild.getPropertyName())){
                    key = "id";
                }else{
                    key = "result";
                }
                bw.write("\t\t<"+key+" column=\""+fieldInfo.getFieldName()+"\" property=\""+fieldInfo.getPropertyName()+"\"/>");
                bw.newLine();
            }
            bw.write("\t</resultMap>");
            bw.newLine();

            /*通用查询*/
            bw.write("\t<!--通用查询结果列-->");
            bw.newLine();
            StringBuilder column_builder = new StringBuilder();//拼接字段
            bw.write("\t<sql id=\""+BASE_COLUMN_LIST+"\">");
            bw.newLine();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                column_builder.append(fieldInfo.getFieldName()).append(",");
            }
            String columnBuilder = column_builder.substring(0,column_builder.lastIndexOf(","));
            bw.write("\t\t"+columnBuilder);
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();

            /*基础查询条件*/
            bw.write("\t<!--基础查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_QUERY_CONDITION+"\">");
            bw.newLine();
//            判断查询条件 String类型判断空
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                String stringQuery="";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE,fieldInfo.getSqlType())){
                    stringQuery = " and query."+fieldInfo.getPropertyName()+"!=''";
                }
                bw.write("\t\t<if test=\"query."+fieldInfo.getPropertyName() +"!= null"+stringQuery+"\">");
                bw.newLine();
                bw.write("\t\t\t and id = #{query."+fieldInfo.getPropertyName()+"};");
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();

            }
            bw.write("\t</sql>");
            bw.newLine();
            /*通用查询条件列 扩展查询属性*/
            bw.write("\t<!--扩展查询条件列-->");
            bw.newLine();
            bw.write("\t<sql id=\""+QUERY_CONDITION+"\">");
            bw.newLine();

            for (FieldInfo fieldInfo:tableInfo.getFieldExtendList()){
                String stringAndWhere="";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE,fieldInfo.getSqlType())){
                    stringAndWhere = " and "+fieldInfo.getFieldName()+" like concat('%',#{query."+fieldInfo.getPropertyName()+"},'%')";
                }else if(ArrayUtils.contains(Constants.SQL_DATA_TYPE,fieldInfo.getSqlType())||ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPE,fieldInfo.getSqlType())){
                 if(fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)){
                     stringAndWhere = "<![CDATA[ and "+fieldInfo.getFieldName()+">=str_to_date(#{query."+fieldInfo.getPropertyName()+"},'%Y-%m-%d') ]]>";

                 } else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_END)) {
                     stringAndWhere = "<![CDATA[ and "+fieldInfo.getFieldName()+"< date_sub(str_to_date(#{query."+fieldInfo.getPropertyName()+"},'%Y-%m-%d')," +"interval - 1 day) ]]>";

                 }
                }
                bw.write("\t\t<if test=\"query."+fieldInfo.getPropertyName() +" != null and query."+fieldInfo.getPropertyName()+"!=''\">");
                bw.newLine();
                bw.write("\t\t\t"+stringAndWhere);
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            /*通用查询条件*/
            bw.write("\t<!--通用查询条件列-->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_QUERY_CONDITION_EXTEND+"\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\""+BASE_QUERY_CONDITION+"\" />");
            bw.newLine();
            bw.write("\t\t\t<include refid=\""+BASE_QUERY_CONDITION_EXTEND+"\" />");
            bw.newLine();

            bw.write("\t\t</where>");
            bw.newLine();

            bw.write("\t</sql>");
            bw.newLine();
            /*查询列表*/
            bw.write("\t<!--查询列表-->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT <include refid=\""+BASE_COLUMN_LIST+"\" /> FROM "+tableInfo.getTableName()+" <include refid=\""+QUERY_CONDITION+"\"/>");
            bw.newLine();
            bw.write("\t\t<if test = \"query.orderBy!=null\"> order by $(query.orderBy)</if>");
            bw.newLine();
            bw.write("\t\t<if test = \"query.simplePage!=null\"> limit #{query.simplePage.start},#{query.simplePage.end}</if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            /*查询数量*/
            bw.write("\t<!--查询数量-->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tSELECT count(1) FROM "+tableInfo.getTableName()+" <include refid=\""+QUERY_CONDITION+"\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            /*插入*/
            bw.write("\t<!--插入 匹配有值字段-->");
            bw.newLine();
            bw.write("\t<insert id=\"insert\" parameterType=\""+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+"\">");
            bw.newLine();
            FieldInfo autoIncrementField = null;//自增长id
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                if (fieldInfo.getIsAutoIncrement()!=null && fieldInfo.getIsAutoIncrement()){
                    autoIncrementField = fieldInfo;
                    break;
                }
            }
            if (autoIncrementField!=null){
                bw.write("\t\t<selectKey keyProperty=\"bean."+autoIncrementField.getFieldName()+"\" resultType=\""+autoIncrementField.getJavaType()+"\" order=\"AFTER\">");
                bw.newLine();
                bw.write("\t\t\t"+"SELECT LAST_INSERT_ID()");
                bw.newLine();
                bw.write("\t\t</selectKey>");
                bw.newLine();

            }
            bw.write("\t\t INSERT INTO "+tableInfo.getTableName()+" ");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t"+fieldInfo.getFieldName()+",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();

            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean."+fieldInfo.getPropertyName()+"},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");

            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();

            /*插入或更新*/
            bw.write("\t<!--插入或更新-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\""+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+"\">");
            bw.newLine();
            bw.write("\t\t INSERT INTO "+tableInfo.getTableName()+" ");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t"+fieldInfo.getFieldName()+",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();

            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean."+fieldInfo.getPropertyName()+"},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();


            bw.write("\t\t on DUPLICATE KEY UPDATE ");
            bw.newLine();

            Map<String,String> KeyTempMap = new HashMap<>();
            for (Map.Entry<String ,List<FieldInfo>> entry:keyIndexMap.entrySet()){
                List<FieldInfo> fieldInfos = entry.getValue();
                for (FieldInfo item:fieldInfos){
                    KeyTempMap.put(item.getFieldName(),item.getFieldName());//过滤索引主键
                }
            }

            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                if(KeyTempMap.get(fieldInfo.getFieldName())!=null ){//保证唯一索引不会被更新
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t"+fieldInfo.getFieldName()+"= VALUES("+fieldInfo.getFieldName()+"),");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();


            bw.write("\t</insert>");
            bw.newLine();

            /*批量插入*/
            StringBuffer insertBuff = new StringBuffer();
            StringBuffer insertProBuff = new StringBuffer();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                insertBuff.append(fieldInfo.getFieldName()).append(",");//拿到所有数据库字段
            }
            String insertField = insertBuff.substring(0,insertBuff.lastIndexOf(","));
            bw.write("\t<!--批量插入-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertBatch\" parameterType=\""+poMapClass+"\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO "+tableInfo.getTableName()+"("+insertField+")VALUES");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
            for (final FieldInfo fieldInfo:tableInfo.getFieldsList()){
//                if (fieldInfo.getIsAutoIncrement()){
//                    continue;//id自增长不需要显示
//                }
                insertProBuff.append("#{item."+fieldInfo.getPropertyName()+"}").append(",");
            }
            String insertPro = insertProBuff.substring(0,insertProBuff.lastIndexOf(","));
            bw.write("\t\t("+insertPro+")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();


            /*批量插入或更新*/
            bw.write("\t<!--批量插入或更新-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\""+poMapClass+"\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO "+tableInfo.getTableName()+"("+insertField+")VALUES");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\" >");
            bw.newLine();
            bw.write("\t\t("+insertPro+")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t\tON DUPLICATE KEY UPDATE ");
            bw.newLine();
            StringBuffer insertBatchUpdateBuff = new StringBuffer();
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                insertBatchUpdateBuff.append(fieldInfo.getPropertyName()+"= VALUES("+fieldInfo.getFieldName()+"),");
            }
            String insertBatchUpdateStr = insertBatchUpdateBuff.substring(0,insertBatchUpdateBuff.lastIndexOf(","));
            bw.write("\t\t"+insertBatchUpdateStr);
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();

            /*根据指定条件查询、删除、更新*/
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFields = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuffer paramsNams = new StringBuffer();

                for (FieldInfo fieldInfo : keyFields) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramsNams.append(fieldInfo.getFieldName()+"=#{"+fieldInfo.getPropertyName()+"}");
                    if (index < keyFields.size()) {
                        methodName.append("and");
                        paramsNams.append(" and ");
                    }
                }
                bw.write("\t<!--根据"+methodName+"查询-->");
                bw.newLine();
                bw.write("\t<select id=\"selectBy" + methodName + "\" resultMap=\"base_result_map\">");
                bw.newLine();
                bw.write("\t\tselect <include refid=\""+BASE_COLUMN_LIST+"\"/> from "+tableInfo.getTableName()+" where " +paramsNams);
                bw.newLine();
                bw.write("\t</select>");
                bw.newLine();

                /*根据指定条件删除*/
                bw.write("\t<!--根据"+methodName+"删除-->");
                bw.newLine();
                bw.write("\t<delete id=\"deleteBy" + methodName + "\">");
                bw.newLine();
                bw.write("\t\tDELETE FROM "+tableInfo.getTableName()+" where "+paramsNams);
                bw.newLine();
                bw.write("\t</delete>");
                bw.newLine();

                /*根据指定条件更新*/
                bw.write("\t<!--根据"+methodName+"更新-->");
                bw.newLine();
                bw.write("\t<update id=\"updateBy" + methodName + "\""+" parameterType=\""+poMapClass+"\">");
                bw.newLine();
                bw.write("\t\tUPDATE "+tableInfo.getTableName());
                bw.newLine();
                bw.write("\t\t\t<set>");
                bw.newLine();
                for (FieldInfo fieldInfo : tableInfo.getFieldsList()) {
                    bw.write("\t\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                    bw.newLine();
                    bw.write("\t\t\t\t"+fieldInfo.getFieldName()+"= #{bean."+fieldInfo.getPropertyName()+"},");
                    bw.newLine();
                    bw.write("\t\t\t\t</if>");
                    bw.newLine();
                }
                bw.write("\t\t\t</set>");
                bw.write("  where "+ paramsNams);
                bw.newLine();
                bw.write("\t</update>");
                bw.newLine();

            }

            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建mapper.xml文件错误");
        }finally {
            if (bw!= null){
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStreamWriter!=null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }

}
