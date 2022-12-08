package org.jeecg.common.util;

import org.apache.commons.lang3.RandomUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNoUtils {
    public static final SimpleDateFormat yyyymmddhhmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String getOrderNo(){
        return DateUtils.date2Str(new Date(),yyyymmddhhmmssSSS)+ RandomUtils.nextLong(100,999);
    }
}
