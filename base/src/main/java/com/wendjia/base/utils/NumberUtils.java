package com.wendjia.base.utils;

import android.text.TextUtils;

public class NumberUtils {
    public static String protectFormatPhoneNumber(String phoneNumber){
        if(TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 9) return phoneNumber;
        StringBuilder builder = new StringBuilder(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.substring(7));
        return builder.toString();
    }
}
