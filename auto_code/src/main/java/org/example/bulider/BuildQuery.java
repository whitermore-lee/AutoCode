package org.example.bulider;

import org.apache.commons.lang3.ArrayUtils;
import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {
    private static Logger logger = LoggerFactory.getLogger(BuildQuery.class);
    private static String QueryName;
    public static void execute(TableInfo tableInfo){
        QueryName = tableInfo.getBeanName()+"Query";
        File file = new File(Constants.PATH_QUERY);
        if(!file.exists()){
            file.mkdirs();
        }

        File QueryFile = new File(file,QueryName+".java");

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        try{
            outputStream = new FileOutputStream(QueryFile);
            outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("package "+Constants.PACKAGE_QUERY+";");
            bufferedWriter.newLine();

            if(tableInfo.getHaveDate()||tableInfo.getHaveDateTime()){
                bufferedWriter.write("import java.util.Date;");
                bufferedWriter.newLine();
            }
            if(tableInfo.getHaveBigDecimal()){
                bufferedWriter.write("import java.math.BigDecimal;");
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.newLine();

            BuildComment.createClassComment(bufferedWriter,tableInfo.getComment()+"查询对象");//类注释
            bufferedWriter.write("public class " +QueryName+" extends BaseQuery"+"{");
            bufferedWriter.newLine();

//            /*list存放query新字段*/
//            List<FieldInfo> infoList = new ArrayList<FieldInfo>();
            /*class类字段生成*/
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                //字段描述
                bufferedWriter.write("\tprivate "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+" "+";" +" ");
                BuildComment.createFieldComment(bufferedWriter,fieldInfo.getComment());
                bufferedWriter.newLine();
                bufferedWriter.newLine();
//                String类型参数
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPE,fieldInfo.getSqlType())){
                    String propertyName = fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_FUZZY;
                    bufferedWriter.write("\tprivate "+fieldInfo.getJavaType()+" "+propertyName +";");
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();

                }
//                日期类型参数
                if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPE,fieldInfo.getSqlType())||ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPE,fieldInfo.getSqlType())){
                    bufferedWriter.write("\tprivate String "+fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_TIME_START+";");
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();


                    bufferedWriter.write("\tprivate String "+fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_TIME_END+";");
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();

                }
            }

            List<FieldInfo> fieldInfoList = tableInfo.getFieldsList();
//            fieldInfoList.addAll(infoList);
//            query调用setget
            buildGetSet(bufferedWriter,fieldInfoList);
            buildGetSet(bufferedWriter,tableInfo.getFieldExtendList());

            bufferedWriter.newLine();
            bufferedWriter.write("}");
            bufferedWriter.flush();

        }catch (Exception e){
            logger.error("创建Query文件错误");
        }finally {
            try {
                outputStreamWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }
    public static void buildGetSet(BufferedWriter bufferedWriter,List<FieldInfo> fieldInfoList) throws Exception{
        /*set和get方法*/
        for (FieldInfo field:fieldInfoList){
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
    }
}
