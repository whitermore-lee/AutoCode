package org.example.bulider;

import org.apache.commons.lang3.ArrayUtils;
import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.example.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/*生成相关的目标文件*/
public class BuildPo{
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute (TableInfo tableInfo) {
        /*判断目录是否存在*/
        File folder = new File(Constants.PATH_PO);
        if(!folder.exists()){
            folder.mkdirs();
        }

        File poFile = new File(folder,tableInfo.getBeanName()+".java");
        /*输入输出流*/
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter  = null;

        try {
            outputStream = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            /*接口*/
            bufferedWriter.write("package "+Constants.PACKAGE_PO+";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            /*导包*/
            bufferedWriter.write("import java.io.Serializable;");
            bufferedWriter.newLine();
            //忽略属性
            Boolean TOJSON_FILED = false;
            for (FieldInfo fieldInfo : tableInfo.getFieldsList()){
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","),fieldInfo.getPropertyName())){
                    TOJSON_FILED = true;
                    break;
                }
            }
            if(TOJSON_FILED){
                bufferedWriter.write(Constants.IGNORE_BEAN_TOJSON_CLASS+";");
                bufferedWriter.newLine();
            }

            if(tableInfo.getHaveDate()||tableInfo.getHaveDateTime()){
                bufferedWriter.write("import java.util.Date;");
                bufferedWriter.newLine();
                //日期序列化和反序列化
                bufferedWriter.write(Constants.BEAN_DATE_FORMAT_CLASS+";");
                bufferedWriter.newLine();
                bufferedWriter.write(Constants.BEAN_DATE_UNFORMAT_CLASS+";");
                bufferedWriter.newLine();
            }
            if(tableInfo.getHaveBigDecimal()){
                bufferedWriter.write("import java.math.BigDecimal;");
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.newLine();

            BuildComment.createClassComment(bufferedWriter,tableInfo.getComment());//类注释
            bufferedWriter.write("public class " + tableInfo.getBeanName()+" implements Serializable {");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            /*class类字段生成*/
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                //日期时间序列化
                if (ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPE,fieldInfo.getSqlType())){
                    bufferedWriter.write("\t"+String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bufferedWriter.newLine();
                    bufferedWriter.write("\t"+String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bufferedWriter.newLine();
                }
                if(ArrayUtils.contains(Constants.SQL_DATA_TYPE,fieldInfo.getSqlType())){
                    bufferedWriter.write("\t"+String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION,DateUtils.YYYY_MM_DD));
                    bufferedWriter.newLine();
                    bufferedWriter.write("\t"+String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bufferedWriter.newLine();
                }
                //忽略属性注解
                if(ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","),fieldInfo.getPropertyName())){
                    bufferedWriter.write("\t"+String.format(Constants.IGNORE_BEAN_TOJSON_EXPRESSION));
                    bufferedWriter.newLine();
                }
                //字段描述
                bufferedWriter.write("\tprivate "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+" "+";" +" ");
                BuildComment.createFieldComment(bufferedWriter,fieldInfo.getComment());
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            /*set和get方法*/
            for (FieldInfo field:tableInfo.getFieldsList()){
                String tempField = StringUtils.uperCaseFirstLetter(field.getPropertyName());
                bufferedWriter.write("\t"+"public void set"+tempField+"("+field.getJavaType()+ " "+field.getPropertyName()+"){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\tthis."+field.getPropertyName()+"="+field.getPropertyName()+";");
                bufferedWriter.newLine();
                bufferedWriter.write("\t"+"}");
                bufferedWriter.newLine();
                bufferedWriter.newLine();

                bufferedWriter.write("\t"+"public "+field.getJavaType()+" get"+tempField+"(){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\treturn this."+field.getPropertyName()+";");
                bufferedWriter.newLine();
                bufferedWriter.write("\t"+"}");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            /*重写toString方法*/
            StringBuffer toStringBuffer = new StringBuffer();
            for (final FieldInfo fieldInfo:tableInfo.getFieldsList()){
                toStringBuffer.append("\""+fieldInfo.getComment()+":\"+("+fieldInfo.getPropertyName()+"== null? \"空\" :"+fieldInfo.getPropertyName()+")+");
                toStringBuffer.append("\n");
                toStringBuffer.append("\t\t\t\t");
            }
            toStringBuffer.deleteCharAt(toStringBuffer.length()-6).append(";");
            bufferedWriter.write("\t@Override");
            bufferedWriter.newLine();
            bufferedWriter.write("\tpublic String toString (){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn "+toStringBuffer);
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");


            bufferedWriter.write("}");
            bufferedWriter.flush();
        } catch (Exception e) {
            logger.error("创建po文件错误");
        }finally {
            if (bufferedWriter!= null){
                try {
                    bufferedWriter.close();
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
