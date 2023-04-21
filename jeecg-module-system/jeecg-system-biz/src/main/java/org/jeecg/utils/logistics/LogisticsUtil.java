package org.jeecg.utils.logistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.gongke.HttpUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张少林
 * @date 2023年04月22日 12:09 上午
 */
@Component
public class LogisticsUtil {

    @Autowired
    private ISysDictService sysDictService;

    /**
     * 查询物流信息
     *
     * @param logisticsCompany 物流公司
     * @param trackingNumber   物流单号
     * @return
     */
    public String getLogisticsInfo(String logisticsCompany, String trackingNumber) {
        //全国快递物流查询 - 快递查询接口：https://market.aliyun.com/products/57126001/cmapi021863.html#sku=yuncode1586300000
        String host = "https://wuliu.market.alicloudapi.com";
        String path = "/kdi";
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        List<DictModel> ls = sysDictService.queryDictItemsByCode("logistics_query_config");
        if (ls.size() > 0) {
            headers.put("Authorization", "APPCODE " + ls.get(0).getValue());//ls.get(0).getValue()
        }
        // headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        //送货上门
        if ("SHSM".equals(logisticsCompany)) {
            return "{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"无\",\"type\":\"SHSM\",\"list\":[{\"time\":\"无\",\"status\":\"送货上门\"}],\"deliverystatus\":\"3\",\"issign\":\"1\",\"expName\":\"送货上门\",\"expSite\":\"www.yto.net.cn \",\"expPhone\":\"95554\",\"logo\":\"http:\\/\\/img3.fegine.com\\/express\\/yto.jpg\",\"courier\":\"\",\"courierPhone\":\"\",\"updateTime\":\"无\",\"takeTime\":\"无\"}}";
        } else {
            String string = null;
            querys.put("no", trackingNumber);// !!! 请求参数 快递单号tracking_number orderProviderList.getTrackingNumber()
            querys.put("type", logisticsCompany);// !!! 请求参数 快递名称对应缩写 logistics_company orderProviderList.getLogisticsCompany()
            try {
                HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
                string = EntityUtils.toString(response.getEntity());
                JSONObject jsonObjectHttp = JSONObject.parseObject(string);

                if (jsonObjectHttp.get("status").equals("0")) {
                    //已签收
                    JSONObject jsonObjectResult = JSONObject.parseObject(jsonObjectHttp.get("result").toString());
                    if ("1".equals(jsonObjectResult.get("issign"))) {
//                        orderProviderList.setStatus("3");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //添加物流数据
            return string;
        }
    }
}
