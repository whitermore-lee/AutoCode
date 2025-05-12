package org.example.bulider;

import org.apache.commons.lang3.ArrayUtils;
import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.example.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo){
        /*判断目录是否存在*/
        File folder = new File(Constants.PATH_MAPPER);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String className = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()+Constants.SUFFIX_MAPPER);
        File mapperFile = new File(folder,className+".java");
        /*输入输出流*/
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter  = null;

        try {
            outputStream = new FileOutputStream(mapperFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            /*接口*/
            bufferedWriter.write("package "+Constants.PACKAGE_MAPPER+";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("import org.apache.ibatis.annotations.Param;\n");

            BuildComment.createClassComment(bufferedWriter,tableInfo.getComment()+" Mapper");//类注释
            bufferedWriter.write("public interface " +className+"<T,P> extends BaseMapper {");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            /*class类字段生成*/
            Map<String, List<FieldInfo>> keyIndexMap =  tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFields = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                for (FieldInfo fieldInfo: keyFields) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index<keyFields.size()){
                        methodName.append("And");
                    }
                    methodParams.append("(@Param(\""+fieldInfo.getPropertyName()+"\") "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+")");
                    if (index<keyFields.size()){
                        methodName.append(", ");
                    }
                }
                BuildComment.createFieldComment(bufferedWriter,"\t根据"+methodName+"查询");
                bufferedWriter.newLine();
                bufferedWriter.write("\tT selectBy" +methodName + methodParams+";");
                bufferedWriter.newLine();
                bufferedWriter.newLine();

                BuildComment.createFieldComment(bufferedWriter,"\t根据"+methodName+"删除");
                bufferedWriter.newLine();
                bufferedWriter.write("\tInteger deleteBy" +methodName + methodParams+";");
                bufferedWriter.newLine();
                bufferedWriter.newLine();

                BuildComment.createFieldComment(bufferedWriter,"\t根据"+methodName+"更新");
                bufferedWriter.newLine();
                bufferedWriter.write("\tInteger updateBy" +methodName+"(@Param(\"bean\")T t,"+methodParams.delete(0,1)+";");
                bufferedWriter.newLine();
                bufferedWriter.newLine();

            }
            bufferedWriter.newLine();
            bufferedWriter.write("}");
            bufferedWriter.flush();
        } catch (Exception e) {
            logger.error("创建mapper文件错误");
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
