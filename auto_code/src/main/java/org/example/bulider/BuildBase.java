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
                if(header.contains("package")){
                    bufferedWriter.newLine();
                }
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
