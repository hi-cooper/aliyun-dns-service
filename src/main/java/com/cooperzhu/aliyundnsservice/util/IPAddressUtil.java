package com.cooperzhu.aliyundnsservice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressUtil {
    public static boolean isValid(String ip) {
        if (null == ip) {
            return false;
        }

        String zeroTo255 = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        return m.matches();
    }
}
