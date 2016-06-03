package com.jotunheim.mimir.web.utils;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

/**
 * @author : Sid Xiong
 * @version : 11/3/15.
 */
@Service
public class StringHelper {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern
            .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$");

    public static boolean isEmpty(String res) {
        return res == null || res.trim().isEmpty();
    }

    /**
     * 是否是手机号码
     * 
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        if (!isEmpty(str)) {
            return PHONE_NUMBER_PATTERN.matcher(str).find();
        }
        return false;
    }
}
