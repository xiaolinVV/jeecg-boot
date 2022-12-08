package org.jeecg.config.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class HttpClientUtil {

    //证书到商户系统下载
    @Value("${jeecg.path.cert}")
    private  String cert;
    @Autowired
    @Lazy
    private ISysDictService iSysDictService;
   /* final static String KEYSTORE_FILE = "C:/zhengshu/apiclient_cert.p12";
    final static String KEYSTORE_PASSWORD = "1501259041";//默认证书密码是商户号*/

public static String doGet(String url, Map<String, String> param) {

    // 创建Httpclient对象

    CloseableHttpClient httpclient = HttpClients.createDefault();

    String resultString = "";

    CloseableHttpResponse response = null;

    try {

            // 创建uri
    URIBuilder builder = new URIBuilder(url);
   if (param != null) {
    for (String key : param.keySet()) {
    builder.addParameter(key, param.get(key));
   }
   }
    URI uri = builder.build();
   // 创建http GET请求
    HttpGet httpGet = new HttpGet(uri);
    // 执行请求
   response = httpclient.execute(httpGet);
   // 判断返回状态是否为200
    if (response.getStatusLine().getStatusCode() == 200) {
    resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
    }
   } catch (Exception e) {
        e.printStackTrace();
    } finally {
    try {
    if (response != null) {
    response.close();
    }
   httpclient.close();
   } catch (IOException e) {
   e.printStackTrace();
   }
   }
   return resultString;
   }
   public static String doGet(String url) {
   return doGet(url, null);
    }

    public static String doPost(String url, Map<String, String> param) {
   // 创建Httpclient对象
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = null;
   String resultString = "";
    try {
    // 创建Http Post请求
    HttpPost httpPost = new HttpPost(url);
    // 创建参数列表
   if (param != null) {
   List<NameValuePair> paramList = new ArrayList<NameValuePair>();
   for (String key : param.keySet()) {
    paramList.add(new BasicNameValuePair(key, param.get(key)));
    }
   // 模拟表单
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
    httpPost.setEntity(entity);
   }
    // 执行http请求
   response = httpClient.execute(httpPost);
   resultString = EntityUtils.toString(response.getEntity(), "utf-8");
   } catch (Exception e) {
    e.printStackTrace();
    } finally {
   try {
   response.close();
    } catch (IOException e) {
    e.printStackTrace();
   }
    }
   return resultString;
    }

    public static String doPost(String url) {
   return doPost(url, null);
    }

    public static String doPostJson(String url, String json) {
    // 创建Httpclient对象
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = null;
    String resultString = "";
   try {
    // 创建Http Post请求
    HttpPost httpPost = new HttpPost(url);
    // 创建请求内容
    StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
    httpPost.setEntity(entity);
   // 执行http请求
    response = httpClient.execute(httpPost);
    resultString = EntityUtils.toString(response.getEntity(), "utf-8");
   } catch (Exception e) {
   e.printStackTrace();
    } finally {
   try {

        response.close();

    } catch (IOException e) {

        e.printStackTrace();

    }

    }

    return resultString;

}



    public static String httpPost(String url, Map<String, String> params, String encoding) throws Exception {

       // log.debug("收到HTTP POST请求");



        String result = "";

        // 创建默认的httpClient实例.

        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建httppost

        HttpPost httppost = new HttpPost(url);



        //参数

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        if (params != null) {

        //    log.debug("发送post参数");

            Set<String> keys = params.keySet();



            for (String key : keys) {

             //   log.debug("param:" + key);

                formparams.add(new BasicNameValuePair(key, params.get(key)));

            }



        }



        UrlEncodedFormEntity uefEntity;

        try {

            uefEntity = new UrlEncodedFormEntity(formparams, encoding);

            httppost.setEntity(uefEntity);



          //  log.debug("executing request " + httppost.getURI());



            CloseableHttpResponse response = httpclient.execute(httppost);



            try {

            //    log.debug("返回HTTP状态:" + response.getStatusLine());



                Header[] headers = response.getAllHeaders();



            //    log.debug("返回HTTP头");

            //    log.debug("--------------------------------------");

                for (Header header : headers) {

          //          log.debug(header.getName() + "-->" + header.getValue());

                }

          //      log.debug("--------------------------------------");



                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    result = EntityUtils.toString(entity, encoding);

          //          log.debug("--------------------------------------");

          //          log.debug("Response content: " + result);

           //         log.debug("--------------------------------------");

                }



            } finally {

                response.close();

            }

        } catch (IOException e) {

            throw e;

        } finally {

            // 关闭连接,释放资源

            try {

                httpclient.close();

            } catch (IOException e) {

            }

        }



        return result;

    }
    public static String httpRequestToString(String url,
                                             Map<String,String> params) {
        String result = null;
        try {
            InputStream is = httpRequestToStream(url,  params);
            BufferedReader in = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    private static InputStream httpRequestToStream(String url,
                                                   Map<String, String> params) {
        InputStream is = null;
        try {
            String parameters = "";
            boolean hasParams = false;
            for(String key : params.keySet()){
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                parameters += key +"="+ value +"&";
                hasParams = true;
            }
            if(hasParams){
                parameters = parameters.substring(0, parameters.length()-1);
            }


            url += "?"+ parameters;

            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.setDoInput(true);
            //设置请求方式，默认为GET
            conn.setRequestMethod("GET");


            is = conn.getInputStream();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
    public  String doSendMoney(String url, String data) throws Exception {
        //商户号
        String  mchId =iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "mchId");

        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(cert + "/apiclient_cert.p12"));//P12文件目录
        //InputStream instream = MoneyUtils.class.getResourceAsStream("/apiclient_cert.p12");
        try {
            keyStore.load(instream, mchId.toCharArray());//这里写密码..默认是你的MCHID
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mchId.toCharArray())//这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=GBK");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                //String jsonStr = toStringInfo(response.getEntity(),"UTF-8");

                //微信返回的报文时GBK，直接使用httpcore解析乱码
                String jsonStr = EntityUtils.toString(response.getEntity(),"UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
