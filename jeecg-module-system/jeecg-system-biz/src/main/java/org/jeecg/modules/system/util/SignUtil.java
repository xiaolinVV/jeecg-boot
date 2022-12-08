package org.jeecg.modules.system.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.Md5Util;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class SignUtil {
    // 与接口配置信息中的Token要一致
    private static String token = "jihuoma";

    //按键名排序
    public static void Ksort(Map<String, Object> map){
        String sb = "";
        String[] key = new String[map.size()];
        int index = 0;
        for (String k : map.keySet()) {
            key[index] = k;
            index++;
        }
        Arrays.sort(key);
        for (String s : key) {
            sb += s + "=" + map.get(s) + "&";
        }
        sb = sb.substring(0, sb.length() - 1);
        // 将得到的字符串进行处理得到目标格式的字符串
        try {
            sb = URLEncoder.encode(sb, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 使用常见的UTF-8编码
    sb = sb.replace("%3D", "=").replace("%26", "&");
  }

    /**
     * 将map转换成url参数
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }
    /**
     * 将map转换成url参数
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap1(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

     /**
        * 将一段错误解码的字符串重新解码
        */
             public static String convertEncodingFormat(String str, String formatFrom, String FormatTo) {
                 String result = null;
                 if (!(str == null || str.length() == 0)) {
                 try {
                 result = new String(str.getBytes(formatFrom), FormatTo);
                 } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
                 }
                 }
                 return result;
                 }

    private static String hexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    private static String kSort(Map<String, Object> map) {
        String sb = "";
        String[] key = new String[map.size()];
        int index = 0;
        for (String k : map.keySet()) {
            key[index] = k;
            index++;
        }
        Arrays.sort(key);
        for (String s : key) {
            sb += s + "=" + map.get(s) + "&";
        }
        sb = sb.substring(0, sb.length() - 1);
        // 将得到的字符串进行处理得到目标格式的字符串
        try {
            sb = URLEncoder.encode(sb, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 使用常见的UTF-8编码
        sb = sb.replace("%3D", "=").replace("%26", "&");

        return sb;
    }

    /**
     * string转二进制
     * @param str
     * @return
     */
    public static String toBinary(String str){
        //把字符串转成字符数组
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            //toBinaryString(int i)返回变量的二进制表示的字符串
            //toHexString(int i) 八进制
            //toOctalString(int i) 十六进制
            result +=Integer.toBinaryString(strChar[i])+ " ";
        }
        return result;
    }

    /**
     *java实现 pack("H*", $str)
     * @param str
     * @return
     */
    public static byte[] str2pack(String str) {
        int nibbleshift = 4;
        int position = 0;
        int len = str.length()/2 + str.length()%2;

        byte[] output = new byte[len];
        for (char v : str.toCharArray()) {
            byte n = (byte) v;
            if (n >= '0' && n <= '9') {
                n -= '0';
            } else if (n >= 'A' && n <= 'F') {
                n -= ('A' - 10);
            } else if (n >= 'a' && n <= 'f') {
                n -= ('a' - 10);
            } else {
                continue;
            }

            output[position] |= (n << nibbleshift);

            if (nibbleshift == 0) {
                position++;
            }
            nibbleshift = (nibbleshift + 4) & 7;
        }

        return output;
    }



    /**
     * 验证签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[] { token, timestamp, nonce };
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 sign
     */
    @SuppressWarnings("rawtypes")
    public static String createSign(Map<String, Object> map,String partnerkey) {
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        for (Map.Entry<String, Object> m : map.entrySet()) {
            packageParams.put(m.getKey(), m.getValue().toString());
        }

        StringBuffer sb = new StringBuffer();
        Set<?> es = packageParams.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!org.apache.commons.lang3.StringUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + partnerkey);


        String sign = Md5Util.md5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }



    /**
     * 返回客户端ip
     *
     * @param request
     * @return
     */
    public static String getRemoteAddrIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static String createXML(Map<String, Object> map){
        String xml = "<xml>";
        Set<String> set = map.keySet();
        Iterator<String> i = set.iterator();
        while(i.hasNext()){
            String str = i.next();
            xml+="<"+str+">"+"<![CDATA["+map.get(str)+"]]>"+"</"+str+">";
        }
        xml+="</xml>";
        return xml;
    }

    /**
     * 冒泡排序
     * @param a
     */
    public  BigDecimal[] maopaoSort(BigDecimal[] a){
        //外层循环，是需要进行比较的轮数，一共进行5次即可
        for(int i=0;i<a.length-1;i++){
            //内存循环，是每一轮中进行的两两比较
            for(int j=0;j<a.length-1;j++)
            {
                if(a[j].compareTo(a[j+1])  > 0)
                {
                    BigDecimal temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }
            //System.out.println("第"+(i+1)+"轮排序后的数组为: "+Arrays.toString(a));

        }
        return a;
    }

    /**
     * 随机16为数值(随机字符串)
     *
     * @return
     */
    public  String buildRandom() {
        String currTime = getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < 4; i++) {
            num = num * 10;
        }
        return (int) ((random * num)) + strTime;
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }
}
