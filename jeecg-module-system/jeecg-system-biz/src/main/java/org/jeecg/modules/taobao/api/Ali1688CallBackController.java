package org.jeecg.modules.taobao.api;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private IGoodSpecificationService goodSpecificationService;


    /**
     * 1688消息回调
     *
     * @param body
     * @return
     */
    @RequestMapping("goodInformation")
    @ResponseBody
    public String goodInformation(@RequestBody String body) {
        log.info("1688消息回调：" + body);
        try {
            JSONObject jsonObject = JSON.parseObject(StringUtils.substringBetween(URLDecoder.decode(body, "UTF-8"), "message=", "&"));
            log.info("解析后的1688回调消息：" + JSON.toJSONString(jsonObject));
            //物流信息回调：1688物流单状态变更消息，包括发货、揽收、运输、派送、签收五个节点首次触发时的消息，仅买家或买家授权的用户能接收到
            if (jsonObject.getString("type").equals("LOGISTICS_BUYER_VIEW_TRACE")) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                log.info("物流信息回调：" + JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog = new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                JSONObject orderLogisticsTracingModelObject = dataObject.getJSONObject("OrderLogisticsTracingModel");//订单信息
                //发货
                if (orderLogisticsTracingModelObject.getString("statusChanged").equals("CONSIGN")) {
                    //订单信息
                    JSONArray orderLogsItemsArray = orderLogisticsTracingModelObject.getJSONArray("orderLogsItems");
                    JSONObject orderLogsItems = orderLogsItemsArray.getJSONObject(0);
                    //发货
                    iOrderProviderListService.taobaoShipments(orderLogsItems.getString("orderId"));
                }
                //揽收
                if (orderLogisticsTracingModelObject.getString("statusChanged").equals("ACCEPT")) {

                }

                //运输
                if (orderLogisticsTracingModelObject.getString("statusChanged").equals("TRANSPORT")) {

                }

                //派送
                if (orderLogisticsTracingModelObject.getString("statusChanged").equals("DELIVERING")) {

                }

                //签收
                if (orderLogisticsTracingModelObject.getString("statusChanged").equals("SIGN")) {

                }


            }
            //1688产品删除（关系用户视角）
            if (jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_DELETE")) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                log.info("1688产品删除：" + JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog = new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //删除平台商品
                List<String> productIds = StrUtil.split(dataObject.getString("productIds"), StrUtil.C_COMMA);
                if (CollUtil.isNotEmpty(productIds)) {
                    LambdaQueryWrapper<GoodList> goodListLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    goodListLambdaQueryWrapper.in(GoodList::getGoodNo, productIds);
                    goodListService.list(goodListLambdaQueryWrapper).stream().map(GoodList::getId).forEach(goodId -> goodListService.deleteAndDelExplain(goodId, "1688 产品删除（关系用户视角）"));
                }
            }
            //1688产品下架（关系用户视角）
            if (jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_EXPIRE")) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                log.info("1688产品下架：" + JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog = new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);

                //下架平台商品
                List<String> productIds = StrUtil.split(dataObject.getString("productIds"), StrUtil.C_COMMA);
                if (CollUtil.isNotEmpty(productIds)) {
                    LambdaQueryWrapper<GoodList> goodListLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    goodListLambdaQueryWrapper.in(GoodList::getGoodNo, productIds);
                    String goodIds = goodListService.list(goodListLambdaQueryWrapper).stream().map(GoodList::getId).collect(Collectors.joining(","));
                    goodListService.updateFrameStatus(goodIds, "0", "1688 产品下架（关系用户视角）");
                }
            }
            //1688产品新增或修改（关系用户视角）
            if (jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_NEW_OR_MODIFY")) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                log.info("1688产品新增或修改：" + JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog = new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //平台商品信息或修改
                StrUtil.split(dataObject.getString("productIds"), StrUtil.C_COMMA).forEach(goodId -> ali1688Service.addShop(Convert.toLong(goodId), null));
            }
            //1688产品上架（关系用户视角）
            if (jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_REPOST")) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                log.info("1688产品上架：" + JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog = new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //上架平台商品
                List<String> productIds = StrUtil.split(dataObject.getString("productIds"), StrUtil.C_COMMA);
                if (CollUtil.isNotEmpty(productIds)) {
                    LambdaQueryWrapper<GoodList> goodListLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    goodListLambdaQueryWrapper.in(GoodList::getGoodNo, productIds);
                    String goodIds = goodListService.list(goodListLambdaQueryWrapper).stream().map(GoodList::getId).collect(Collectors.joining(","));
                    goodListService.updateFrameStatus(goodIds, "1", "1688 产品上架（关系用户视角）");
                }
            }
            //1688商品库存变更消息（关系用户视角）
            if (jsonObject.getString("type").equals("PRODUCT_PRODUCT_INVENTORY_CHANGE")) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                log.info("1688商品库存变更消息：" + JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog = new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);

                //更新库存
                JSONArray offerInventoryChangeList = dataObject.getJSONArray("OfferInventoryChangeList");
                for (Object obj : offerInventoryChangeList) {
                    JSONObject offerInventoryChange = (JSONObject) obj;
                    String offerId = offerInventoryChange.getString("offerId");
                    Long offerOnSale = offerInventoryChange.getLong("offerOnSale");
                    String skuId = offerInventoryChange.getString("skuId");
                    Long skuOnSale = offerInventoryChange.getLong("skuOnSale");

                    LambdaQueryWrapper<GoodList> goodListLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    goodListLambdaQueryWrapper.eq(GoodList::getGoodNo, offerId).eq(GoodList::getDelFlag, "0");
                    GoodList goodList = goodListService.getOne(goodListLambdaQueryWrapper, false);
                    if (goodList == null) {
                        continue;
                    }
                    if (StrUtil.equals(goodList.getIsSpecification(), "1") && StrUtil.isNotBlank(skuId)) {
                        LambdaQueryWrapper<GoodSpecification> goodSpecificationLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        goodSpecificationLambdaQueryWrapper.eq(GoodSpecification::getGoodListId, goodList.getId()).eq(GoodSpecification::getSkuId, skuId).eq(GoodSpecification::getDelFlag, "0");
                        GoodSpecification goodSpecification = goodSpecificationService.getOne(goodSpecificationLambdaQueryWrapper, false);
                        if (goodSpecification != null) {
                            goodSpecification.setRepertory(new BigDecimal(skuOnSale));
                            goodSpecificationService.updateById(goodSpecification);
                        }
                    } else if (StrUtil.equals(goodList.getIsSpecification(), "0")) {
                        LambdaQueryWrapper<GoodSpecification> goodSpecificationLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        goodSpecificationLambdaQueryWrapper.eq(GoodSpecification::getGoodListId, goodList.getId()).eq(GoodSpecification::getDelFlag, "0");
                        GoodSpecification goodSpecification = goodSpecificationService.getOne(goodSpecificationLambdaQueryWrapper, false);
                        if (goodSpecification != null) {
                            goodSpecification.setRepertory(new BigDecimal(offerOnSale));
                            goodSpecificationService.updateById(goodSpecification);
                        }
                    }
                }
            }
            //1688产品审核下架（关系用户视角）
            if (jsonObject.getString("type").equals("PRODUCT_RELATION_VIEW_PRODUCT_AUDIT")) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                log.info("1688产品审核下架消息：" + JSON.toJSONString(dataObject));
                //记录日志
                OrderTaobaoLog orderTaobaoLog = new OrderTaobaoLog();
                orderTaobaoLog.setTradeType(jsonObject.getString("type"));
                orderTaobaoLog.setJsonData(JSON.toJSONString(dataObject));
                iOrderTaobaoLogService.save(orderTaobaoLog);
                //下架平台商品
                List<String> productIds = StrUtil.split(dataObject.getString("productIds"), StrUtil.C_COMMA);
                if (CollUtil.isNotEmpty(productIds)) {
                    LambdaQueryWrapper<GoodList> goodListLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    goodListLambdaQueryWrapper.in(GoodList::getGoodNo, productIds);
                    String goodIds = goodListService.list(goodListLambdaQueryWrapper).stream().map(GoodList::getId).collect(Collectors.joining(","));
                    goodListService.updateFrameStatus(goodIds, "0", "1688 产品审核下架（关系用户视角）");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    @RequestMapping("/syncSkuId")
    @ResponseBody
    public Result<?> syncSkuId() {
        ThreadUtil.execute(() -> ali1688Service.syncSkuId());
        return Result.ok("操作成功");
    }

    public static void main(String[] args) {
        String data = "{\"OfferInventoryChangeList\":[{\"offerOnSale\":195450568,\"quantity\":-10,\"offerId\":664826856043,\"skuOnSale\":8878934,\"skuId\":5063737031267}]}";
        JSONObject dataObject = JSON.parseObject(data);

//        StrUtil.split(jsonObject.getString("productIds"),StrUtil.C_COMMA).forEach(goodId -> System.out.println(goodId));
    }
}