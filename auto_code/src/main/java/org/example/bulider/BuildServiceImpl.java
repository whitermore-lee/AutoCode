package org.example.bulider;

import org.example.bean.Constants;
import org.example.bean.FieldInfo;
import org.example.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo){
        /*判断目录是否存在*/
        File folder = new File(Constants.PATH_SERVICE_IMPL);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String mapperName = tableInfo.getBeanName()+Constants.SUFFIX_MAPPER;
        String mapperBeanName =StringUtils.lowerCaseFirstLetter(mapperName);
        String interfaceName = tableInfo.getBeanName()+"Service";
        String serviceImplName = StringUtils.uperCaseFirstLetter(tableInfo.getBeanName()+"ServiceImpl");

        File servicerFile = new File(folder,serviceImplName+".java");
        /*输入输出流*/
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter  = null;

        try {
            outputStream = new FileOutputStream(servicerFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            /*接口*/
            bufferedWriter.write("package "+Constants.PACKAGE_SERVICE_IMPL+";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanParamName()+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_QUERY+"."+"SimplePage;");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_ENUMS+"."+"PageSize;");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_MAPPER+"."+mapperName+";");
            bufferedWriter.newLine();
            bufferedWriter.write("import jakarta.annotation.Resource;\n");
            bufferedWriter.newLine();
            bufferedWriter.write("import org.springframework.stereotype.Service;\n");
            bufferedWriter.newLine();
            bufferedWriter.write("import java.util.List;\n");
            bufferedWriter.newLine();
            bufferedWriter.write("import "+Constants.PACKAGE_SERVICE+"."+interfaceName+";");
            bufferedWriter.newLine();


            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" ServiceImpl",tableInfo.getBeanName());//类注释
            bufferedWriter.write("@Service(\""+StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName())+"Service\")");
            bufferedWriter.newLine();
            bufferedWriter.write("public class " +serviceImplName+" implements "+interfaceName+"{");

            bufferedWriter.newLine();
            bufferedWriter.write("\t@Resource");
            bufferedWriter.newLine();

            bufferedWriter.write("\tprivate "+mapperName+" <"+ tableInfo.getBeanName()+","+tableInfo.getBeanParamName()+"> " + StringUtils.lowerCaseFirstLetter(mapperName)+";");
            bufferedWriter.newLine();

            /*条件查询接口*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 根据条件查询列表",tableInfo.getBeanName());
            bufferedWriter.write("\tpublic List<"+tableInfo.getBeanName()+"> findListByName("+tableInfo.getBeanParamName()+" query ){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn this."+mapperBeanName+".selectList(query);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");

            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 根据条件查询数量",tableInfo.getBeanName());
            bufferedWriter.write("\tpublic Integer findCountByName("+tableInfo.getBeanParamName()+" query){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn this."+mapperBeanName+".selectCount(query);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");

            /*分页查询接口*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 分页查询 ",tableInfo.getBeanName());
            bufferedWriter.write("\tpublic PaginationResultVO<"+tableInfo.getBeanName()+"> findListByPage("+tableInfo.getBeanParamName()+" query){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\tInteger count = this.findCountByName(query);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\tint pageSize = query.getPageSize()==null ? PageSize.SIZE15.getSize() : query.getPageSize();");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\tSimplePage page= new SimplePage(query.getPageNo(),count,pageSize); ");
            bufferedWriter.newLine();

            bufferedWriter.write("\t\tquery.setSimplePage(page);");
            bufferedWriter.newLine();

            bufferedWriter.write("\t\tList<"+tableInfo.getBeanName()+"> list = this.findListByName(query);");
            bufferedWriter.newLine();

            bufferedWriter.write("\t\tPaginationResultVO<"+tableInfo.getBeanName()+"> result = new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);");
            bufferedWriter.newLine();

            bufferedWriter.write("\t\treturn result;");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");

            /*新增*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 新增 ",tableInfo.getBeanName());
            bufferedWriter.write("\tpublic Integer add("+tableInfo.getBeanName()+" bean){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn this."+mapperBeanName+".insert(bean);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");

            /*批量新增*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 批量新增 ",tableInfo.getBeanName());
            bufferedWriter.write("\tpublic Integer addBatch(List<"+tableInfo.getBeanName()+"> listbean){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\tif(listbean == null || listbean.isEmpty()){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn 0;");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t}");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn this."+mapperBeanName+".insertBatch(listbean);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");

            /*批量新增和修改*/
            BuildComment.createFieldComment(bufferedWriter,tableInfo.getComment()+" 批量新增 修改 ",tableInfo.getBeanName());
            bufferedWriter.write("\tpublic Integer addOrUpdateBatch(List<"+tableInfo.getBeanName()+"> listbean){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\tif(listbean == null || listbean.isEmpty()){");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn 0;");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t}");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\treturn this."+mapperBeanName+".insertOrUpdateBatch(listbean);");
            bufferedWriter.newLine();
            bufferedWriter.write("\t}");

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
                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"查询",tableInfo.getBeanName());
                bufferedWriter.write("\tpublic "+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By"+methodName +"("+paramsNams+"){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\treturn this."+mapperBeanName+".selectBy"+methodName+"("+params+");");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");

                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"更新",tableInfo.getBeanName());
                bufferedWriter.write("\tpublic Integer update"+tableInfo.getBeanName()+"By"+methodName +"("+tableInfo.getBeanName()+" bean, "+paramsNams+"){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\treturn this."+mapperBeanName+".updateBy"+methodName+"(bean,"+params+");");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");

                BuildComment.createFieldComment(bufferedWriter,"根据"+methodName+"删除",tableInfo.getBeanName());
                bufferedWriter.write("\tpublic Integer delete"+tableInfo.getBeanName()+"By"+methodName +"("+paramsNams+"){");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\treturn this."+mapperBeanName+".deleteBy"+methodName+"("+params+");");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");

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
