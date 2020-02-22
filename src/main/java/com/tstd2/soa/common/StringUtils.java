package com.tstd2.soa.common;

/**
 * @author yancey
 * @date 2020/2/19 17:44
 */
public class StringUtils {

    public static String lowerFirstCapse(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
