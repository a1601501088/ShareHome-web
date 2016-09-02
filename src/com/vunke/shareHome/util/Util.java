package com.vunke.shareHome.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Util {
    /**
     * 判断是否为手机号码
     * @param str
     * @return
     */
     public static boolean isPhone(String str){
         if (isEmpty(str)) {
             return false;
         }

         String regEx = "^(\\+86)?1[1,2,3,4,5,6,7,8,9](\\d{9})$";
         // 编译正则表达式
         Pattern pattern = Pattern.compile(regEx);
         // 忽略大小写的写法
         // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
         Matcher matcher = pattern.matcher(str);
         // 字符串是否与正则表达式相匹配
         boolean rs = matcher.matches();
         return rs;

     }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
