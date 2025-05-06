package org.example.bulider;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
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

            bufferedWriter.write("import java.io.Serializable;");
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

            BuildComment.createClassComment(bufferedWriter,tableInfo.getComment());//类注释
            bufferedWriter.write("public class " + tableInfo.getBeanName()+" implements Serializable {");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            /*class类字段生成*/
            for (FieldInfo fieldInfo:tableInfo.getFieldsList()){
                bufferedWriter.write("\tprivate "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+" "+";" +" ");
                BuildComment.createFieldComment(bufferedWriter,fieldInfo.getComment());
                bufferedWriter.newLine();
            }

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
