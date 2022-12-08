package com.alibaba.ocean.rawsdk.client.imp.serialize;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.serializer.DateCodec;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xingming.zhaoxm
 * @date 2018/10/26
 */
public class OpenPlatformDateCodec extends DateCodec {

    public static final OpenPlatformDateCodec instance = new OpenPlatformDateCodec();

    public OpenPlatformDateCodec() {
        super();
    }

    @Override
    public <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof Date) {
            return (T)val;
        } else if (val instanceof Number) {
            return (T)new Date(((Number)val).longValue());
        } else if (!(val instanceof String)) {
            throw new JSONException("parse error");
        } else {
            String strVal = (String)val;
            if (strVal.length() == 0) {
                return null;
            } else {
                JSONScanner dateLexer = new JSONScanner(strVal);

                label346: {
                    Date var8;
                    try {
                        if (!dateLexer.scanISO8601DateIfMatch(false)) {
                            break label346;
                        }

                        Calendar calendar = dateLexer.getCalendar();
                        if (clazz == Calendar.class) {
                            Calendar var28 = calendar;
                            return (T)var28;
                        }

                        var8 = calendar.getTime();
                    } finally {
                        dateLexer.close();
                    }

                    return (T)var8;
                }

                if (strVal.length() == parser.getDateFomartPattern().length() || strVal.length() == 22 && parser.getDateFomartPattern().equals("yyyyMMddHHmmssSSSZ")) {
                    DateFormat dateFormat = parser.getDateFormat();

                    try {
                        return (T)dateFormat.parse(strVal);
                    } catch (ParseException var21) {
                        ;
                    }
                }

                if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
                    String dotnetDateStr = strVal.substring(6, strVal.length() - 2);
                    strVal = dotnetDateStr;
                }

                if (!"0000-00-00".equals(strVal) && !"0000-00-00T00:00:00".equalsIgnoreCase(strVal) && !"0001-01-01T00:00:00+08:00".equalsIgnoreCase(strVal)) {
                    int index = strVal.lastIndexOf(124);
                    if (index > 20) {
                        String tzStr = strVal.substring(index + 1);
                        TimeZone timeZone = TimeZone.getTimeZone(tzStr);
                        if (!"GMT".equals(timeZone.getID())) {
                            String subStr = strVal.substring(0, index);
                            dateLexer = new JSONScanner(subStr);

                            try {
                                if (dateLexer.scanISO8601DateIfMatch(false)) {
                                    Calendar calendar = dateLexer.getCalendar();
                                    calendar.setTimeZone(timeZone);
                                    if (clazz == Calendar.class) {
                                        Calendar var30 = calendar;
                                        return (T)var30;
                                    }

                                    Date var12 = calendar.getTime();
                                    return (T)var12;
                                }
                            } finally {
                                dateLexer.close();
                            }
                        }
                    }
                    //---support openplatform Date format:20190424145905000+0800
                    if(22==strVal.length() && (strVal.lastIndexOf("-")>0||strVal.lastIndexOf("+")>0)){
                        index = strVal.lastIndexOf("-");
                        if(index<0) {
                            index=strVal.lastIndexOf("+");
                        }
                        String tzStr = strVal.substring(index);
                        String tmStr = strVal.substring(0,index);
                        TimeZone timeZone = TimeZone.getTimeZone("GMT"+tzStr);
                        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmssSSS");
                        format.setTimeZone(timeZone);
                        try {
                            Date d=format.parse(tmStr);
                            return (T)d;
                        } catch (ParseException e) {
                            throw new JSONException("parse error",e);
                        }
                    }
                    long longVal = Long.parseLong(strVal);
                    return (T)new Date(longVal);
                } else {
                    return null;
                }
            }
        }
    }


}
