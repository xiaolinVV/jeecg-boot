package org.jeecg.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.config.util.HttpTool;
import org.jeecg.modules.order.dto.OrderStoreSubListDTO;
import org.jeecg.modules.order.entity.OrderStoreSubList;
import org.jeecg.modules.order.mapper.OrderStoreSubListMapper;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.jeecg.modules.order.service.IOrderStoreSubListService;
import org.jeecg.modules.store.service.IStoreAddressService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺包裹订单列表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Service
public class OrderStoreSubListServiceImpl extends ServiceImpl<OrderStoreSubListMapper, OrderStoreSubList> implements IOrderStoreSubListService {
    @Autowired(required = false)
    private OrderStoreSubListMapper orderStoreSubListMapper;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IOrderStoreGoodRecordService orderStoreGoodRecordService;
    @Autowired
    private IStoreAddressService storeAddressService;

    /**
     * 根据orderStoreListId查询列表
     * sysUserId 数据权限使用
     * @param orderStoreListId
     * @param sysUserId
     * @return
     */
    @Override
    public List<OrderStoreSubListDTO> selectorderStoreListId(String orderStoreListId, String sysUserId){
        return orderStoreSubListMapper.selectorderStoreListId(orderStoreListId,sysUserId);
    };
     /**
     *  * 根据orderStoreListId查询列表
     * sysUserId 数据权限使用
     * @param orderStoreListId
     * @param sysUserId
     * @return
             */
    @Override
    public List<OrderStoreSubListDTO> selectorderStoreListId(String orderStoreListId, String sysUserId,String parentId,String notParentId){
        return orderStoreSubListMapper.selectorderStoreListId(orderStoreListId,sysUserId,parentId,notParentId);
    };
    @Override
    public String listSkip(String orderId) {
        OrderStoreSubList orderStoreSubList = baseMapper.selectById(orderId);// getById();
        if(orderStoreSubList!=null){
            String logisticsCompany = orderStoreSubList.getLogisticsCompany();
            String host = "https://wuliu.market.alicloudapi.com";
            String path = "/kdi";
            String method = "GET";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            List<DictModel> ls = sysDictService.queryDictItemsByCode("logistics_query_config");
            if(ls.size()>0){
                headers.put("Authorization", "APPCODE " + ls.get(0).getValue());//ls.get(0).getValue()
            }

            Map<String, String> querys = new HashMap<String, String>();
            //送货上门
            if("SHSM".equals(orderStoreSubList.getLogisticsCompany())){
                return "{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"无\",\"type\":\"SHSM\",\"list\":[{\"time\":\"无\",\"status\":\"送货上门\"}],\"deliverystatus\":\"3\",\"issign\":\"1\",\"expName\":\"送货上门\",\"expSite\":\"www.yto.net.cn \",\"expPhone\":\"95554\",\"logo\":\"http:\\/\\/img3.fegine.com\\/express\\/yto.jpg\",\"courier\":\"\",\"courierPhone\":\"\",\"updateTime\":\"无\",\"takeTime\":\"无\"}}";
            }else{
            querys.put("no",orderStoreSubList.getTrackingNumber());// !!! 请求参数 快递单号tracking_number orderStoreSubList.getTrackingNumber()
            querys.put("type",orderStoreSubList.getLogisticsCompany() );// !!! 请求参数 快递名称对应缩写 logistics_company orderStoreSubList.getLogisticsCompany()
            try {
                HttpResponse response = HttpTool.doGet(host, path, method, headers, querys);
                String string = EntityUtils.toString(response.getEntity());
                JSONObject jsonObjectHttp = JSONObject.parseObject(string);

                if(jsonObjectHttp.get("status").equals("0")){
                    //已签收
                    JSONObject jsonObjectResult = JSONObject.parseObject(jsonObjectHttp.get("result").toString());
                    if("1".equals(jsonObjectResult.get("issign"))){
                        orderStoreSubList.setStatus("3");
                    }
                    //添加物流数据
                    orderStoreSubList.setLogisticsTracking(string);
                    baseMapper.updateById(orderStoreSubList);
                }
                return string;
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        }

        return null;
    };
}
