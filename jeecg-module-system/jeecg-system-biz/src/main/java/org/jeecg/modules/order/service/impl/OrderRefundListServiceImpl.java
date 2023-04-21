package org.jeecg.modules.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.order.entity.OrderRefundList;
import org.jeecg.modules.order.mapper.OrderRefundListMapper;
import org.jeecg.modules.order.service.IOrderRefundListService;
import org.jeecg.utils.logistics.LogisticsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.yulichang.base.MPJBaseServiceImpl;

/**
 * @Description: order_refund_list
 * @Author: jeecg-boot
 * @Date:   2023-04-20
 * @Version: V1.0
 */
@Service
public class OrderRefundListServiceImpl extends MPJBaseServiceImpl<OrderRefundListMapper, OrderRefundList> implements IOrderRefundListService {

    @Autowired
    private LogisticsUtil logisticsUtil;

    @Override
    public OrderRefundList getOrderRefundListById(String id) {
        OrderRefundList orderRefundList = getById(id);
        if (orderRefundList == null) {
            throw new JeecgBootException("售后单数据不存在");
        }

        //获取物流数据JSON
        if (StrUtil.isAllNotBlank(orderRefundList.getBuyerLogisticsCompany(),orderRefundList.getBuyerTrackingNumber())) {
            String buyerLogisticsTracking = orderRefundList.getBuyerLogisticsTracking();
            if(StrUtil.isNotBlank(buyerLogisticsTracking)){
                JSONObject jsonObject = JSONObject.parseObject(buyerLogisticsTracking);
                if(jsonObject.get("status").equals("0")){
                    //已签收
                    JSONObject jsonObjectResult = JSONObject.parseObject(jsonObject.get("result").toString());

                    if(jsonObjectResult.get("issign").equals("1")){
                        //已签收，不做查询物流接口
                    }else{
                        //请求接口更新数据接口
                        String string = logisticsUtil.getLogisticsInfo(orderRefundList.getBuyerLogisticsCompany(),orderRefundList.getBuyerTrackingNumber());
                        orderRefundList.setBuyerLogisticsTracking(string);
                    }
                }else {
                    //请求接口更新物流数据接口
                    String string = logisticsUtil.getLogisticsInfo(orderRefundList.getBuyerLogisticsCompany(),orderRefundList.getBuyerTrackingNumber());
                    orderRefundList.setBuyerLogisticsTracking(string);
                }
            }else{
                String string = logisticsUtil.getLogisticsInfo(orderRefundList.getBuyerLogisticsCompany(),orderRefundList.getBuyerTrackingNumber());
                orderRefundList.setBuyerLogisticsTracking(string);
            }
            updateById(orderRefundList);
        }
        return orderRefundList;
    }
}
