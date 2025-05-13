package org.example.bulider;

public class StringUtils {
    /*第一个字母转大写*/
    public static String uperCaseFirstLetter(String filed) {
        if(org.apache.commons.lang3.StringUtils.isEmpty(filed)) {
            return filed;
        }
        return filed.substring(0, 1).toUpperCase() + filed.substring(1);
    }

    public static String lowerCaseFirstLetter(String filed){
        if(org.apache.commons.lang3.StringUtils.isEmpty(filed)) {
            return filed;
        }
        return filed.substring(0, 1).toLowerCase() + filed.substring(1);
    }

    public static void main(String[] args) {
        System.out.println(lowerCaseFirstLetter("HowAreYou"));
    }

}

