package org.jeecg.modules.taobao.api;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.order.entity.OrderTaobaoLog;
import org.jeecg.modules.order.service.IOrderProviderListService;
import org.jeecg.modules.order.service.IOrderTaobaoLogService;
import org.jeecg.modules.taobao.service.IAli1688Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RequestMapping("front/ali1688CallBack")
@Controller
@Log
public class Ali1688CallBackController {


    @Autowired
    private IOrderTaobaoLogService iOrderTaobaoLogService;

    @Autowired
    private IOrderProviderListService iOrderProviderListService;

    @Autowired
    private IGoodListService goodListService;

    @Autowired
    private IAli1688Service ali1688Service;


    /**
     * 1688消息回调
     *
     * @param body
     * @return
     */
    @RequestMapping("goodInformation")
    @ResponseBody
    public String goodInformation(@RequestBody String body){
        log.info("1688消息回调："+body);
        try {
            JSONObject jsonObject=JSON.parseObject(StringUtils.substringBetween(URLDecoder.decode(body,"UTF-8"),"message=","&"));
            log.info("解析后的1688回调消息："+JSON.toJSONString(jsonObject));
            //物流信息回调：1688物流单状态变更消息，包括发货、揽收、运输、派送、签收五个节点首次触发时的消息，仅买家或买家授权的用户能接收到
            if(jsonObject.getString("type").equals("LOGISTICS_BUYER_VIEW_TRACE")){
                JSONObject dataObject=jsonObject.getJSONObject("data");
                log.info("物流信息回调："+JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog=new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                JSONObject orderLogisticsTracingModelObject=dataObject.getJSONObject("OrderLogisticsTracingModel");//订单信息
                //发货
                if(orderLogisticsTracingModelObject.getString("statusChanged").equals("CONSIGN")){
                    //订单信息
                    JSONArray orderLogsItemsArray=orderLogisticsTracingModelObject.getJSONArray("orderLogsItems");
                    JSONObject orderLogsItems=orderLogsItemsArray.getJSONObject(0);
                    //发货
                    iOrderProviderListService.taobaoShipments(orderLogsItems.getString("orderId"));
                }
                //揽收
                if(orderLogisticsTracingModelObject.getString("statusChanged").equals("ACCEPT")){

                }

                //运输
                if(orderLogisticsTracingModelObject.getString("statusChanged").equals("TRANSPORT")){

                }

                //派送
                if(orderLogisticsTracingModelObject.getString("statusChanged").equals("DELIVERING")){

                }

                //签收
                if(orderLogisticsTracingModelObject.getString("statusChanged").equals("SIGN")){

                }


            }
            //1688产品删除（关系用户视角）
            if(jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_DELETE")){
                JSONObject dataObject=jsonObject.getJSONObject("data");
                log.info("1688产品删除："+JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog=new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //删除平台商品
                StrUtil.split(dataObject.getString("productIds"),StrUtil.C_COMMA).forEach(goodId -> goodListService.deleteAndDelExplain(goodId,"1688 产品删除（关系用户视角）"));
            }
            //1688产品下架（关系用户视角）
            if(jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_EXPIRE")){
                JSONObject dataObject=jsonObject.getJSONObject("data");
                log.info("1688产品下架："+JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog=new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //下架平台商品
                StrUtil.split(dataObject.getString("productIds"),StrUtil.C_COMMA).forEach(goodId -> goodListService.updateFrameStatus(goodId,"0","1688 产品下架（关系用户视角）"));
            }
            //1688产品新增或修改（关系用户视角）
            if(jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_NEW_OR_MODIFY")){
                JSONObject dataObject=jsonObject.getJSONObject("data");
                log.info("1688产品新增或修改："+JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog=new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //平台商品信息或修改
                StrUtil.split(dataObject.getString("productIds"),StrUtil.C_COMMA).forEach(goodId -> ali1688Service.addShop(Convert.toLong(goodId),null));
            }
            //1688产品上架（关系用户视角）
            if(jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_REPOST")){
                JSONObject dataObject=jsonObject.getJSONObject("data");
                log.info("1688产品上架："+JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog=new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //上架平台商品
                StrUtil.split(dataObject.getString("productIds"),StrUtil.C_COMMA).forEach(goodId -> goodListService.updateFrameStatus(goodId,"1","1688 产品上架（关系用户视角）"));
            }
            //1688商品库存变更消息（关系用户视角）
            if(jsonObject.getString("type").equals("PRODUCT_PRODUCT_INVENTORY_CHANGE")){
                JSONObject dataObject=jsonObject.getJSONObject("data");
                log.info("1688商品库存变更消息："+JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog=new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
            }
            //1688产品审核下架（关系用户视角）
            if(jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_AUDIT")){
                JSONObject dataObject=jsonObject.getJSONObject("data");
                log.info("1688产品审核下架消息："+JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog=new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //下架平台商品
                StrUtil.split(dataObject.getString("productIds"),StrUtil.C_COMMA).forEach(goodId -> goodListService.updateFrameStatus(goodId,"0","1688 产品审核下架（关系用户视角）"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }
}