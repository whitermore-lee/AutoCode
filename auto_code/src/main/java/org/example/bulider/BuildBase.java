package org.example.bulider;

import ch.qos.logback.classic.Logger;
import org.example.bean.Constants;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildBase {
    public static void main(String[] args) {
        System.out.println(Constants.PACKAGE_UTILS);
    }
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BuildBase.class);
    public static void execute(){
        List<String> headerInfoList = new ArrayList<String>();
        /*生成date枚举*/
        headerInfoList.add("package " +Constants.PACKAGE_ENUMS+";");
        build(headerInfoList,"DateTimePatternEnum",Constants.PATH_ENUMS);
        headerInfoList.clear();
        headerInfoList.add("package "+Constants.PACKAGE_UTILS+";");
        build(headerInfoList,"DateUtils",Constants.PATH_UTILS);
        headerInfoList.clear();

        /*BaseMapper生成*/
        headerInfoList.add("package "+Constants.PACKAGE_MAPPER+";");
        build(headerInfoList,"BaseMapper",Constants.PATH_MAPPER);
        headerInfoList.clear();

        /*生成pageSize枚举*/
        headerInfoList.add("package "+Constants.PACKAGE_ENUMS+";");
        build(headerInfoList,"PageSize",Constants.PATH_ENUMS);
        headerInfoList.clear();

        /*生成分页信息*/
        headerInfoList.add("package "+Constants.PACKAGE_QUERY+";");
        headerInfoList.add("import "+Constants.PACKAGE_ENUMS+".PageSize;");
        build(headerInfoList,"SimplePage",Constants.PATH_QUERY);
        headerInfoList.clear();

        /*生成pageSize枚举*/
        headerInfoList.add("package "+Constants.PACKAGE_QUERY+";");
        build(headerInfoList,"BaseQuery",Constants.PATH_QUERY);
        headerInfoList.clear();

        /*分页查询方法*/
        headerInfoList.add("package "+Constants.PACKAGE_VO+";");
        build(headerInfoList,"PaginationResultVO",Constants.PATH_VO);
        headerInfoList.clear();

        /*状态码*/
        headerInfoList.add("package "+Constants.PACKAGE_ENUMS+";");
        build(headerInfoList,"ResponseCodeEnum",Constants.PATH_ENUMS);
        headerInfoList.clear();

        /*ResponseVO*/
        headerInfoList.add("package "+Constants.PACKAGE_VO+";");
        build(headerInfoList,"ResponseVO",Constants.PATH_VO);
        headerInfoList.clear();

        /*生成exception*/
        headerInfoList.add("package "+Constants.PACKAGE_EXCEPTION+";");
        headerInfoList.add(("import "+Constants.PACKAGE_ENUMS+"."+"ResponseCodeEnum;"));

        build(headerInfoList,"BusinessException",Constants.PATH_EXCEPTION);
        headerInfoList.clear();

        /*生成BaseController*/
        headerInfoList.add("package "+Constants.PACKAGE_CONTROLLER+";");
        headerInfoList.add(("import "+Constants.PACKAGE_ENUMS+"."+"ResponseCodeEnum;"));
        headerInfoList.add(("import "+Constants.PACKAGE_VO+"."+"ResponseVO;"));

        build(headerInfoList,"BaseController",Constants.PATH_CONTROLLER);
        headerInfoList.clear();

        /*生成GlobalExceptionHandlerController*/
        headerInfoList.add("package "+Constants.PACKAGE_CONTROLLER+";");
        headerInfoList.add(("import "+Constants.PACKAGE_ENUMS+"."+"ResponseCodeEnum;"));
        headerInfoList.add(("import "+Constants.PACKAGE_VO+"."+"ResponseVO;"));
        headerInfoList.add(("import "+Constants.PACKAGE_EXCEPTION+"."+"BusinessException;"));

        build(headerInfoList,"GlobalExceptionHandlerController",Constants.PATH_CONTROLLER);
        headerInfoList.clear();

    }
    private static void build(List<String> headerInfoList,String fileName,String outPutPath){
        File file = new File(outPutPath);
        if(!file.exists()){
            file.mkdirs();
        }
        //输出内容
        File javaFile = new File(outPutPath,fileName+".java");
        OutputStream outputStream  = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try{
            outputStream = new FileOutputStream(javaFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            String templatePath = BuildBase.class.getClassLoader().getResource("template/"+fileName+".txt").getPath();
            inputStream = new FileInputStream(templatePath);
            inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            /*导入包*/
            for (String header : headerInfoList){
                bufferedWriter.write(header);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }
            String line = null;
            while ((line=bufferedReader.readLine())!=null){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        }catch (Exception e){
            logger.error("创建基础类文件错误",fileName,e);
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(inputStreamReader!=null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(outputStreamWriter!=null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(bufferedWriter!=null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
