package org.example.bulider;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildService {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo){
        /*判断目录是否存在*/
        File folder = new File(Constants.PATH_SERVICE);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String serviceName = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()+"Service");
        File servicerFile = new File(folder,serviceName+".java");
        /*输入输出流*/
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter  = null;

        try {
            outputStream = new FileOutputStream(servicerFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            /*接口*/
            bufferedWriter.write("package "+Constants.PACKAGE_SERVICE+";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import java.util.List;");
            bufferedWriter.newLine();


            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" Service",tableInfo.getBeanName());//类注释
            bufferedWriter.write("public interface " +serviceName+"{");
            bufferedWriter.newLine();
            /*条件查询接口*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 根据条件查询列表",tableInfo.getBeanName());
            bufferedWriter.write("\tList<"+tableInfo.getBeanName()+"> findListByName("+tableInfo.getBeanParamName()+" query );");
            bufferedWriter.newLine();
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 根据条件查询数量",tableInfo.getBeanName());
            bufferedWriter.write("\tInteger findCountByName("+tableInfo.getBeanParamName()+" query);");
            bufferedWriter.newLine();
            /*分页查询接口*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 分页查询 ",tableInfo.getBeanName());
            bufferedWriter.write("\tPaginationResultVO<"+tableInfo.getBeanName()+"> findListByPage("+tableInfo.getBeanParamName()+" query);");
            bufferedWriter.newLine();
            /*新增*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 新增 ",tableInfo.getBeanName());
            bufferedWriter.write("\tInteger add("+tableInfo.getBeanName()+" bean);");
            bufferedWriter.newLine();
            /*批量新增*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 批量新增 ",tableInfo.getBeanName());
            bufferedWriter.write("\tInteger addBatch(List<"+tableInfo.getBeanName()+"> listbean);");
            bufferedWriter.newLine();
            /*批量新增和修改*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 批量新增 修改 ",tableInfo.getBeanName());
            bufferedWriter.write("\tInteger addOrUpdateBatch(List<"+tableInfo.getBeanName()+"> listbean);");
            bufferedWriter.newLine();
            /*根据指定条件查询、删除、更新*/
            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> keyFields = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuffer paramsNams = new StringBuffer();

                for (FieldInfo fieldInfo : keyFields) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramsNams.append(fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                    if (index < keyFields.size()) {
                        methodName.append("And");
                        paramsNams.append(".");
                    }
                }
                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"查询",tableInfo.getBeanName());
                bufferedWriter.write("\t"+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By"+methodName +"("+paramsNams+");");
                bufferedWriter.newLine();

                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"更新",tableInfo.getBeanName());
                bufferedWriter.write("\tInteger update"+tableInfo.getBeanName()+"By"+methodName +"("+tableInfo.getBeanName()+" bean, "+paramsNams+");");
                bufferedWriter.newLine();

                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"删除",tableInfo.getBeanName());
                bufferedWriter.write("\tInteger delete"+tableInfo.getBeanName()+"By"+methodName +"("+paramsNams+");");
                bufferedWriter.newLine();

            }
            bufferedWriter.newLine();
            bufferedWriter.write("}");
            bufferedWriter.flush();
        } catch (Exception e) {
            logger.error("创建service文件错误");
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
