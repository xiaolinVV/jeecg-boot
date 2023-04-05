package org.jeecg.modules.taobao.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.logistics.param.AlibabaLogisticsOpenPlatformLogisticsStep;
import com.alibaba.logistics.param.AlibabaTradeGetLogisticsTraceInfoBuyerViewParam;
import com.alibaba.logistics.param.AlibabaTradeGetLogisticsTraceInfoBuyerViewResult;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.alibaba.p4p.param.*;
import com.alibaba.product.param.AlibabaProductFollowParam;
import com.alibaba.trade.param.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Log
public class Ali1688Utils {

    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 获取ApiExecutor
     *
     * @return
     */
    public ApiExecutor getApiExecutor(){
        String appkey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "1688appkey");
        String seckey = iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "1688seckey");
        return new ApiExecutor(appkey,seckey);
    }


    /**
     * 获取token
     * @return
     */
    public String createToken(){
        String token = null;
        token = (String) redisTemplate.boundValueOps("taobao-1688-token").get();
        if (token == null) {
            ApiExecutor apiExecutor = getApiExecutor();
            String refreshToken=iSysDictService.queryTableDictTextByKey("sys_dict_item", "item_value", "item_text", "1688refresh_token");
            token=apiExecutor.refreshToken(refreshToken).getAccess_token();
            //存储到redis并设置过期时间
            redisTemplate.boundValueOps("taobao-1688-token").set(token, 5, TimeUnit.HOURS);

            log.info("重新创建token："+token);
        }

        log.info("token的值为："+token);

        return token;
    }

    /**
     * 获取 选品库列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Map<String,Object>> getUserGroups(String pageNo,String pageSize){
        AlibabaCpsOpListCybUserGroupParam alibabaCpsOpListCybUserGroupParam=new AlibabaCpsOpListCybUserGroupParam();
        alibabaCpsOpListCybUserGroupParam.setPageNo(pageNo);
        alibabaCpsOpListCybUserGroupParam.setPageSize(pageSize);
        List<Map<String,Object>> resultList= Lists.newArrayList();
        SDKResult<AlibabaCpsOpListCybUserGroupResult> result = getApiExecutor().execute(alibabaCpsOpListCybUserGroupParam, this.createToken());
        if(result.getResult().getResult().getSuccess()){
            AlibabaPpOpenClientDtoOpenUnionOpGroupDTO[] alibabaPpOpenClientDtoOpenUnionOpGroupDTOS=  result.getResult().getResult().getResult();
            for (AlibabaPpOpenClientDtoOpenUnionOpGroupDTO a:alibabaPpOpenClientDtoOpenUnionOpGroupDTOS) {
                Map<String,Object> resultMap= Maps.newHashMap();
                resultMap.put("userGroupId",a.getId());
                resultMap.put("title",a.getTitle());
                resultList.add(resultMap);
            }
        }
        return resultList;
    }


    /**
     * 获取选品分组下的商品id
     *
     * @param groupId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Long> getUserGroupFeed(Long groupId,Integer pageNo,Integer pageSize){

        List<Long> resultList=Lists.newArrayList();
        AlibabaCpsOpListCybUserGroupFeedParam alibabaCpsOpListCybUserGroupFeedParam=new AlibabaCpsOpListCybUserGroupFeedParam();
        alibabaCpsOpListCybUserGroupFeedParam.setGroupId(groupId);
        alibabaCpsOpListCybUserGroupFeedParam.setPageNo(pageNo);
        alibabaCpsOpListCybUserGroupFeedParam.setPageSize(pageSize);
        SDKResult<AlibabaCpsOpListCybUserGroupFeedResult> result=getApiExecutor().execute(alibabaCpsOpListCybUserGroupFeedParam, this.createToken());
        if(result.getResult().getResult().getSuccess()){
            ComAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO[] comAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTOS=result.getResult().getResult().getResultList();
            for (ComAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO comAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO:comAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTOS) {
                resultList.add(comAlibabaPpOpenClientDtoOpenUnionOpGroupFeedDTO.getFeedId());
            }
        }
        return resultList;
    }


    /**
     * 1688下单并支付
     *
     * @param orderProviderListMap
     * @param goodListMap
     * @return
     */
    public Map<String,Object> placeOrder(Map<String,Object> orderProviderListMap,Map<String,Object> goodListMap){
        Map<String,Object> resultMap=Maps.newHashMap();

        //整合参数
        Map<String, Object> outerOrdeMap = Maps.newLinkedHashMap();
        outerOrdeMap.put("mediaOrderId", orderProviderListMap.get("id"));
        outerOrdeMap.put("phone", orderProviderListMap.get("contactNumber"));
        List<Map<String, Object>> offersList = Lists.newArrayList();
        outerOrdeMap.put("offers", offersList);

        //1688订单的创建
        AlibabaTradeCreateOrderCybMediaParam alibabaTradeCreateOrderCybMediaParam = new AlibabaTradeCreateOrderCybMediaParam();
        //设置留言
        if(orderProviderListMap.get("message")!=null) {
            alibabaTradeCreateOrderCybMediaParam.setMessage(orderProviderListMap.get("message").toString());
        }

        AlibabaTradeFastAddress alibabaTradeFastAddress = new AlibabaTradeFastAddress();
        //地址数据的整合

        //地址编码的解析
        AlibabaTradeAddresscodeParseParam alibabaTradeAddresscodeParseParam = new AlibabaTradeAddresscodeParseParam();

        //拼接地址
        String allAddress = orderProviderListMap.get("shippingAddress").toString() + StringUtils.defaultIfBlank(String.valueOf(orderProviderListMap.get("houseNumber")),"");

        alibabaTradeAddresscodeParseParam.setAddressInfo(allAddress);
        SDKResult<AlibabaTradeAddresscodeParseResult> addresscodeResult = getApiExecutor().execute(alibabaTradeAddresscodeParseParam, this.createToken());

        AlibabaTradeReceiveAddress alibabaTradeReceiveAddress = addresscodeResult.getResult().getResult();

        log.info("地址码：" + alibabaTradeReceiveAddress.getAddressCode());

        List<Map<String, String>> addressResolutionList = AddressResolutionUtil.addressResolution(allAddress);
        if (addressResolutionList.size() <= 0) {
            addressResolutionList = AddressResolutionUtil.addressResolutionCity(allAddress);
        }

        Map<String, String> addressResolutionMap = addressResolutionList.get(0);

        alibabaTradeFastAddress.setFullName(StringUtils.defaultIfBlank(String.valueOf(orderProviderListMap.get("consignee")),""));
        alibabaTradeFastAddress.setMobile(StringUtils.defaultIfBlank(String.valueOf(orderProviderListMap.get("contactNumber")),""));
        alibabaTradeFastAddress.setPhone(StringUtils.defaultIfBlank(String.valueOf(orderProviderListMap.get("contactNumber")),""));
        alibabaTradeFastAddress.setPostCode(alibabaTradeReceiveAddress.getPostCode());
        alibabaTradeFastAddress.setCityText(addressResolutionMap.get("city"));
        alibabaTradeFastAddress.setProvinceText(StringUtils.defaultIfBlank(String.valueOf(orderProviderListMap.get("provice")),""));
        alibabaTradeFastAddress.setAreaText(addressResolutionMap.get("county"));
        alibabaTradeFastAddress.setTownText(addressResolutionMap.get("town"));
        alibabaTradeFastAddress.setAddress(addressResolutionMap.get("county") + addressResolutionMap.get("village"));
        alibabaTradeFastAddress.setDistrictCode(alibabaTradeReceiveAddress.getAddressCode());
        alibabaTradeCreateOrderCybMediaParam.setAddressParam(alibabaTradeFastAddress);


        AlibabaTradeFastCargo[] alibabaTradeFastCargos = new AlibabaTradeFastCargo[1];

        //商品信息列表
        log.info("商品信息列表：" + JSON.toJSON(goodListMap));

        //组织参数
        Map<String, Object> offersMap = Maps.newLinkedHashMap();
        offersMap.put("id", Long.parseLong(goodListMap.get("goodNo").toString()));
        if (!String.valueOf(goodListMap.get("skuNo")).equals("无")) {
            offersMap.put("specId", String.valueOf(goodListMap.get("skuNo")));
        }
        offersMap.put("price", new BigDecimal(String.valueOf(goodListMap.get("price"))).multiply(new BigDecimal(100)).setScale(0));
        offersMap.put("num", Long.parseLong(goodListMap.get("amount").toString()));

        offersList.add(offersMap);


        //创建商品数据
        AlibabaTradeFastCargo alibabaTradeFastCargo = new AlibabaTradeFastCargo();
        alibabaTradeFastCargo.setOfferId(Long.parseLong(goodListMap.get("goodNo").toString()));
        if (!String.valueOf(goodListMap.get("skuNo")).equals("无")) {
            alibabaTradeFastCargo.setSpecId(String.valueOf(goodListMap.get("skuNo")));
        }
        alibabaTradeFastCargo.setQuantity(Double.parseDouble(goodListMap.get("amount").toString()));
        alibabaTradeFastCargos[0] = alibabaTradeFastCargo;

        log.info(JSON.toJSONString(alibabaTradeFastCargos));
        alibabaTradeCreateOrderCybMediaParam.setCargoParamList(alibabaTradeFastCargos);


        alibabaTradeCreateOrderCybMediaParam.setOuterOrderInfo(JSON.toJSONString(outerOrdeMap));
        log.info("OuterOrderInfo需要的参数：" + JSON.toJSONString(outerOrdeMap));

        //1688下单
        SDKResult<AlibabaTradeCreateOrderCybMediaResult> createOrderResult = getApiExecutor().execute(alibabaTradeCreateOrderCybMediaParam, this.createToken());
        if (createOrderResult.getResult().getSuccess()) {
            //调用系统下单接口进行下单
            String result=payOrderSuccess(Long.parseLong(createOrderResult.getResult().getResult().getOrderId()));
            if(result.equals("SUCCESS")){
                resultMap.put("orderProviderListId", orderProviderListMap.get("id"));
                resultMap.put("taoOrderId", createOrderResult.getResult().getResult().getOrderId());
                resultMap.put("code","1");
                resultMap.put("message","下单成功");
            }else{
                resultMap.put("orderProviderListId", orderProviderListMap.get("id"));
                resultMap.put("code","0");
                resultMap.put("message",result);
            }
        } else {
            resultMap.put("orderProviderListId", orderProviderListMap.get("id"));
            resultMap.put("code","0");
            resultMap.put("message","失败编码：" + createOrderResult.getResult().getErrorCode() + "；失败原因：" + createOrderResult.getResult().getErrorMsg());
            log.info("1688下单失败，失败单号：" + orderProviderListMap.get("id") + "；失败编码：" + createOrderResult.getResult().getErrorCode() + "；失败原因：" + createOrderResult.getResult().getErrorMsg());
        }
        return resultMap;
    }


    /**
     * 1688订单支付
     */
    private String payOrderSuccess(Long orderId){
        //调用支付冲账
        AlibabaTradePayProtocolPayParam alibabaTradePayProtocolPayParam=new AlibabaTradePayProtocolPayParam();
        alibabaTradePayProtocolPayParam.setOrderId(orderId);
        SDKResult<AlibabaTradePayProtocolPayResult> payOrderResult = getApiExecutor().execute(alibabaTradePayProtocolPayParam, this.createToken());
        if(payOrderResult.getResult().getSuccess()){
            return "SUCCESS";
        }else{
            log.info("订单冲账失败，失败编号："+payOrderResult.getErrorCode()+"；失败原因："+payOrderResult.getErrorMessage());
            return "订单冲账失败，失败编号："+payOrderResult.getErrorCode()+"；失败原因："+payOrderResult.getErrorMessage();
        }
    }


    /**
     * 关注订阅商品
     *
     * @param productId
     */
    public void followGood(Long productId){
        AlibabaProductFollowParam alibabaProductFollowParam=new AlibabaProductFollowParam();
        alibabaProductFollowParam.setProductId(productId);
        this.getApiExecutor().execute(alibabaProductFollowParam, this.createToken());
    }


    /**
     * 获取物流信息
     *
     * @param orderId
     * @return
     */
    public Map<String,Object> getLogisticsTraceInfo(Long orderId){
        Map<String,Object> result=Maps.newHashMap();
        result.put("status","0");
        result.put("msg","ok");
        Map<String,Object> resultMap=Maps.newHashMap();
        result.put("result",resultMap);
        List<Map<String,Object>> resultMapList=Lists.newArrayList();
        resultMap.put("list",resultMapList);
        AlibabaTradeGetLogisticsTraceInfoBuyerViewParam alibabaTradeGetLogisticsTraceInfoBuyerViewParam=new AlibabaTradeGetLogisticsTraceInfoBuyerViewParam();
        alibabaTradeGetLogisticsTraceInfoBuyerViewParam.setWebSite("1688");
        alibabaTradeGetLogisticsTraceInfoBuyerViewParam.setOrderId(orderId);
        SDKResult<AlibabaTradeGetLogisticsTraceInfoBuyerViewResult> sdkResult=this.getApiExecutor().execute(alibabaTradeGetLogisticsTraceInfoBuyerViewParam, this.createToken());
        if(sdkResult.getResult().getSuccess()&&sdkResult.getResult().getLogisticsTrace()!=null){

            AlibabaLogisticsOpenPlatformLogisticsStep[] alibabaLogisticsOpenPlatformLogisticsSteps=sdkResult.getResult().getLogisticsTrace()[0].getLogisticsSteps();
            Arrays.asList(alibabaLogisticsOpenPlatformLogisticsSteps).forEach(a->{
                Map<String,Object> logisticsStepsMap=Maps.newHashMap();
                logisticsStepsMap.put("time",a.getAcceptTime());
                logisticsStepsMap.put("status",a.getRemark());
                resultMapList.add(logisticsStepsMap);
            });
        }
        return result;
    }

}
