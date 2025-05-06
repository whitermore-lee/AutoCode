package org.example.bulider;

import org.example.bean.Constants;
import org.example.utils.DateUtils;

import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/*注解类*/
public class BuildComment {
    /*类注解*/
    public static void createClassComment(BufferedWriter bufferedWriter,String classComment) throws Exception{
        bufferedWriter.write("/**");
        bufferedWriter.newLine();
        bufferedWriter.write("* @Description: " + classComment);
        bufferedWriter.newLine();
        bufferedWriter.write("* @Date: " + DateUtils.format(new Date(),DateUtils.YYYY_MM_DD));
        bufferedWriter.newLine();
        bufferedWriter.write("* @Author: " + Constants.AUTHOR_COMMENT);
        bufferedWriter.newLine();
        bufferedWriter.write("**/");
        bufferedWriter.newLine();
    }
    /*字段注解*/
    public static void createFieldComment(BufferedWriter bufferedWriter,String fieldComment) throws Exception {

        bufferedWriter.write("//" +fieldComment==null?" ":"//"+fieldComment);
    }
    /*方法注解*/
    public static void createMethodComment(BufferedWriter bufferedWriter,String methodComment) throws Exception {

    }
}
