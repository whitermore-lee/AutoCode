package org.example.bulider;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo){
        /*判断目录是否存在*/
        File folder = new File(Constants.PATH_CONTROLLER);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String className = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()+"Controller");
        String serviceName = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()+"Service");
        String serviceBeanName = StringUtils.lowerCaseFirstLetter(serviceName);
        File controllerFile = new File(folder,className+".java");
        /*输入输出流*/
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter  = null;

        try {
            outputStream = new FileOutputStream(controllerFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            /*接口*/
            bufferedWriter.write("package "+Constants.PACKAGE_CONTROLLER+";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_SERVICE+"."+serviceName+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_VO+".ResponseVO;");
            bufferedWriter.newLine();
            bufferedWriter.write("import org.springframework.web.bind.annotation.RequestBody;\n");
            bufferedWriter.newLine();
            bufferedWriter.write("import org.springframework.web.bind.annotation.RestController;\n");
            bufferedWriter.newLine();
            bufferedWriter.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
            bufferedWriter.newLine();
            bufferedWriter.write("import jakarta.annotation.Resource;\n");
            bufferedWriter.newLine();
            bufferedWriter.write("import java.util.List;\n");
            bufferedWriter.newLine();

            BuildComment.createClassComment(bufferedWriter,tableInfo.getComment()+" Controller");//类注释
            bufferedWriter.newLine();
            bufferedWriter.write("@RestController");
            bufferedWriter.newLine();

            bufferedWriter.write("@RequestMapping(\"/"+StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName())+"\")");
            bufferedWriter.newLine();

            bufferedWriter.write("public class " +className+" extends BaseController {");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("@Resource");
            bufferedWriter.newLine();
            bufferedWriter.write("private "+serviceName +" " +serviceBeanName+";");
            bufferedWriter.newLine();
            /*class类字段生成*/

            /*查询方法*/
            bufferedWriter.write("\t@RequestMapping(\"loadDataList\")");
            bufferedWriter.newLine();
            bufferedWriter.write("\tpublic ResponseVO loadDataList("+tableInfo.getBeanParamName()+" query) {");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn getSuccessResponseVO("+serviceBeanName+".findListByPage(query));");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");
            bufferedWriter.newLine();

            /*新增*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 新增 ",tableInfo.getBeanName());
            bufferedWriter.newLine();
            bufferedWriter.write("\t@RequestMapping(\"add\")");
            bufferedWriter.newLine();
            bufferedWriter.write("\tpublic ResponseVO add("+tableInfo.getBeanName()+" bean){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\tthis."+serviceBeanName+".add(bean);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn getSuccessResponseVO(null);");

            bufferedWriter.newLine();
            bufferedWriter.write("\t}");
            bufferedWriter.newLine();

            /*批量新增*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 批量新增 ",tableInfo.getBeanName());
            bufferedWriter.newLine();
            bufferedWriter.write("\t@RequestMapping(\"addBatch\")");
            bufferedWriter.newLine();
            bufferedWriter.write("\tpublic ResponseVO addBatch(@RequestBody List<"+tableInfo.getBeanName()+"> listbean){");

            bufferedWriter.newLine();
            bufferedWriter.write("\t\tthis."+serviceBeanName+".addBatch(listbean);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn getSuccessResponseVO(null);");

            bufferedWriter.newLine();
            bufferedWriter.write("\t}");
            bufferedWriter.newLine();


            /*批量新增和修改*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 批量新增 修改 ",tableInfo.getBeanName());
            bufferedWriter.newLine();
            bufferedWriter.write("\t@RequestMapping(\"addBatchOrUpdate\")");
            bufferedWriter.newLine();
            bufferedWriter.write("\tpublic ResponseVO addOrUpdateBatch(@RequestBody List<"+tableInfo.getBeanName()+"> listbean){");
            bufferedWriter.newLine();

            bufferedWriter.write("\t\tthis."+serviceBeanName+".addOrUpdateBatch(listbean);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn getSuccessResponseVO(null);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");
            bufferedWriter.newLine();

            /*根据指定条件查询、删除、更新*/
            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> keyFields = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuffer paramsNams = new StringBuffer();
                StringBuffer params = new StringBuffer();//没有类型的params
                for (FieldInfo fieldInfo : keyFields) {
                    index++;
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramsNams.append(fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                    params.append(fieldInfo.getPropertyName());
                    if (index < keyFields.size()) {
                        methodName.append("And");
                        paramsNams.append(",");
                        params.append(",");
                    }
                }
                bufferedWriter.newLine();

                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"查询",tableInfo.getBeanName());
                bufferedWriter.newLine();
                bufferedWriter.write("\t@RequestMapping(\"get"+tableInfo.getBeanName()+"By"+methodName +"\")");
                bufferedWriter.newLine();
                bufferedWriter.write("\tpublic ResponseVO  get"+tableInfo.getBeanName()+"By"+methodName +"("+paramsNams+"){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\treturn getSuccessResponseVO(this."+serviceBeanName+".get"+tableInfo.getBeanName()+"By"+methodName+"("+params+"));");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");
                bufferedWriter.newLine();


                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"更新",tableInfo.getBeanName());
                bufferedWriter.newLine();
                bufferedWriter.write("\t@RequestMapping(\"update"+tableInfo.getBeanName()+"By"+methodName +"\")");
                bufferedWriter.newLine();
                bufferedWriter.write("\tpublic ResponseVO update"+tableInfo.getBeanName()+"By"+methodName +"("+tableInfo.getBeanName()+" bean, "+paramsNams+"){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\tthis."+serviceBeanName+".update"+tableInfo.getBeanName()+"By"+methodName +"(bean,"+params+");");
                bufferedWriter.newLine();

                bufferedWriter.write("\t\treturn getSuccessResponseVO(null);");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");
                bufferedWriter.newLine();


                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"删除",tableInfo.getBeanName());
                bufferedWriter.newLine();
                bufferedWriter.write("\t@RequestMapping(\"delete"+tableInfo.getBeanName()+"By"+methodName +"\")");
                bufferedWriter.newLine();
                bufferedWriter.write("\tpublic ResponseVO delete"+tableInfo.getBeanName()+"By"+methodName +"("+paramsNams+"){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\tthis."+serviceBeanName+".delete"+tableInfo.getBeanName()+"By"+methodName +"("+params+");");
                bufferedWriter.newLine();

                bufferedWriter.write("\t\treturn getSuccessResponseVO(null);");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");
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
