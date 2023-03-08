package org.jeecg.modules.order.api;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.dto.MarketingDisountGoodDTO;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.marketing.store.prefecture.service.IMarketingStorePrefectureGiveService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.entity.MemberShoppingCart;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.member.service.IMemberShoppingCartService;
import org.jeecg.modules.order.entity.OrderList;
import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.order.service.IOrderListService;
import org.jeecg.modules.order.service.IOrderProviderGoodRecordService;
import org.jeecg.modules.order.service.IOrderStoreGoodRecordService;
import org.jeecg.modules.order.service.IOrderStoreListService;
import org.jeecg.modules.order.utils.TotalPayUtils;
import org.jeecg.modules.pay.entity.PayOrderCarLog;
import org.jeecg.modules.pay.service.IPayOrderCarLogService;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.jeecg.modules.store.service.IStoreTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单api控制器
 */
@RequestMapping("after/order")
@Controller
@Slf4j
public class AfterOrderController {

    @Autowired
    private IMemberShoppingCartService iMemberShoppingCartService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;

    @Autowired
    private IOrderListService iOrderListService;

    @Autowired
    private IOrderStoreListService iOrderStoreListService;

    @Autowired
    private IOrderProviderGoodRecordService iOrderProviderGoodRecordService;

    @Autowired
    private IOrderStoreGoodRecordService iOrderStoreGoodRecordService;

    @Autowired
    private IStoreTemplateService iStoreTemplateService;

    @Autowired
    private IProviderTemplateService iProviderTemplateService;

    @Autowired
    private IMarketingDiscountCouponService iMarketingDiscountCouponService;

    @Autowired
    private IMarketingPrefectureGoodService iMarketingPrefectureGoodService;

    @Autowired
    private IMarketingPrefectureGoodSpecificationService iMarketingPrefectureGoodSpecificationService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IMarketingCertificateRecordService iMarketingCertificateRecordServicel;

    @Autowired
    private IMarketingCertificateGoodService iMarketingCertificateGoodService;

    @Autowired
    private TotalPayUtils totalPayUtils;

    @Autowired
    private IMarketingWelfarePaymentsSettingService iMarketingWelfarePaymentsSettingService;

    @Autowired
    private IMarketingStoreGiftCardMemberListService iMarketingStoreGiftCardMemberListService;

    @Autowired
    private IPayOrderCarLogService iPayOrderCarLogService;


    @Autowired
    private IMarketingGiftBagRecordService iMarketingGiftBagRecordService;


    @Autowired
    private IMarketingStorePrefectureGiveService iMarketingStorePrefectureGiveService;

    @Autowired
    private IMarketingDiscountGoodService marketingDiscountGoodService;

    /**
     * 兑换券线上兑换
     *
     * @param marketingCertificateRecordId
     * @return
     */
    @RequestMapping("submitCertificate")
    @ResponseBody
    public Result<?> submitCertificate(String marketingCertificateRecordId,
                                                        String marketingCertificateGoodIds,
                                                        @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<Map<String,Object>> result=new Result<>();

//        return Result.error("功能升级中,请选择线下核销!");

        //检测参数不能为空
        if(StringUtils.isBlank(marketingCertificateRecordId)){
            result.error500("兑换券id不能为空");
            return result;
        }

        //兑换券记录状态必须正确
        MarketingCertificateRecord marketingCertificateRecord= iMarketingCertificateRecordServicel.getById(marketingCertificateRecordId);
        if(marketingCertificateRecord==null){
            result.error500("兑换券在系统中不存在！！！");
            return result;
        }

        if(!marketingCertificateRecord.getStatus().equals("1")){
            result.error500("兑换券的状态不是未使用状态！！！");
            return result;
        }

        //查询兑换券商品

        if(StringUtils.isBlank(marketingCertificateGoodIds)){
            result.error500("兑换券商品必须提交！！！");
            return result;
        }
        iMarketingCertificateRecordServicel.saveOrUpdate(marketingCertificateRecord.setUserTime(new Date()));
        List<String> marketingCertificateGoodList=Arrays.asList(StringUtils.split(marketingCertificateGoodIds,","));

        //购物车的列表信息
        List<String> shopCarIds=Lists.newArrayList();

        for (String id:marketingCertificateGoodList) {

            MarketingCertificateGood mcg=iMarketingCertificateGoodService.getById(id);

            //兑换券商品加入购物车
            String backResult=iMemberShoppingCartService.addGoodToShoppingCartCertificate(Integer.parseInt(mcg.getIsPlatform()),mcg.getGoodSpecificationId(),memberId,mcg.getQuantity().intValue(),"0",marketingCertificateRecordId);

            if(backResult.indexOf("SUCCESS")==-1){
                result.error500(backResult);
                return result;
            }else{
                shopCarIds.add(StringUtils.substringAfter(backResult,"="));
            }
        }

        return affirmOrder(StringUtils.join(shopCarIds,","),null,null,memberId);
    }


    /**
     * 确认收货
     * @param isPlatform
     * @param id
     * @param memberId
     * @return
     */
    @RequestMapping("affirmOrderDelivery")
    @ResponseBody
    public Result<String> affirmOrderDelivery(Integer isPlatform,String id,@RequestAttribute(value = "memberId",required = false) String memberId){
        Result<String> result=new Result<>();

        //参数校验
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }


        if(isPlatform.intValue()==0){
            //店铺订单
            OrderStoreList orderStoreList=iOrderStoreListService.getById(id);
            if(!orderStoreList.getMemberListId().equals(memberId)){
                result.error500("订单不是用户的订单！！！");
                return result;
            }
            iOrderStoreListService.affirmOrder(id);
        }else

        if(isPlatform.intValue()==1){
            //平台订单
            OrderList orderStoreList=iOrderListService.getById(id);
            if(!orderStoreList.getMemberListId().equals(memberId)){
                result.error500("订单不是用户的订单！！！");
                return result;
            }
            iOrderListService.affirmOrder(id);
        }else{
            result.error500("isPlatform参数不正确请联系平台管理员！！！");
            return result;
        }
        result.success("订单确认收货成功");
        return result;
    }

    /**
     * 取消订单
     *
     * @param isPlatform
     * @param id
     * @return
     */
    @RequestMapping("abrogateOrder")
    @ResponseBody
    public  Result<String> abrogateOrder(Integer isPlatform,String id,String value,@RequestAttribute(value = "memberId",required = false) String memberId){
        Result<String> result=new Result<>();

        //参数校验
        if(isPlatform==null){

            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(value)){
            result.error500("value值不能为空！！！");
            return result;
        }

        if(isPlatform.intValue()==0){
            //店铺订单
            OrderStoreList orderStoreList=iOrderStoreListService.getById(id);
            if(!orderStoreList.getMemberListId().equals(memberId)){
                result.error500("订单不是用户的订单！！！");
                return result;
            }
            iOrderStoreListService.abrogateOrder(id,value,"1");
        }else

            if(isPlatform.intValue()==1){
                //平台订单
                OrderList orderStoreList=iOrderListService.getById(id);
                if(!orderStoreList.getMemberListId().equals(memberId)){
                    result.error500("订单不是用户的订单！！！");
                    return result;
                }
                iOrderListService.abrogateOrder(id,value,"1");
            }else{
                result.error500("isPlatform参数不正确请联系平台管理员！！！");
                return result;
            }
        result.success("订单取消成功");
        return result;
    }


    /**
     * 查询订单详情
     *
     * @param id
     * @param isPlatform
     * @return
     */
    @RequestMapping("viewOrderInfo")
    @ResponseBody
    public Result<Map<String,Object>> viewOrderInfo(String id,Integer isPlatform){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //参数判断
        if(StringUtils.isBlank(id)){
            result.error500("订单id不能为空！！！");
            return result;
        }
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        //店铺订单
        if(isPlatform.intValue()==0){
            OrderStoreList orderStoreList=iOrderStoreListService.getById(id);
            //收货地址信息
            objectMap.put("consignee",orderStoreList.getConsignee());
            objectMap.put("contactNumber",orderStoreList.getContactNumber());
            objectMap.put("shippingAddress",orderStoreList.getShippingAddress());
            objectMap.put("houseNumber",orderStoreList.getHouseNumber());
            //留言
            objectMap.put("message",orderStoreList.getMessage());
            //订单价格
            objectMap.put("goodsTotal",orderStoreList.getGoodsTotal());
            objectMap.put("shipFee",orderStoreList.getShipFee());
            objectMap.put("coupon",orderStoreList.getCoupon());
            objectMap.put("actualPayment",orderStoreList.getActualPayment());


            objectMap.put("balance",orderStoreList.getBalance());
            objectMap.put("payWelfarePayments",orderStoreList.getPayWelfarePayments());//支付的积分
            objectMap.put("payWelfarePaymentsPrice",orderStoreList.getPayWelfarePaymentsPrice());//约等于金额
            objectMap.put("modePayment",orderStoreList.getModePayment());//0:微信；1：支付宝；2：余额
            objectMap.put("discount",orderStoreList.getDiscount());//折扣
            objectMap.put("payPrice",orderStoreList.getPayPrice());//支付金额
            objectMap.put("status",orderStoreList.getStatus());
            //订单商品
            objectMap.put("goods",iOrderStoreGoodRecordService.getOrderStoreGoodRecordByOrderId(orderStoreList.getId()));

            //订单基础信息
            objectMap.put("id",orderStoreList.getId());
            objectMap.put("isPlatform",isPlatform);
            objectMap.put("orderNo",orderStoreList.getOrderNo());

            objectMap.put("createTime", DateUtil.formatDateTime(orderStoreList.getCreateTime()));

            if(orderStoreList.getPayTime()!=null){
                objectMap.put("payTime", DateUtil.formatDateTime(orderStoreList.getPayTime()));
            }else{
                objectMap.put("payTime", "");
            }

            if(orderStoreList.getShipmentsTime()!=null){
                objectMap.put("shipmentsTime", DateUtil.formatDateTime(orderStoreList.getShipmentsTime()));
            }else{
                objectMap.put("shipmentsTime", "");
            }


            if(orderStoreList.getDeliveryTime()!=null){
                objectMap.put("deliveryTime", DateUtil.formatDateTime(orderStoreList.getDeliveryTime()));
            }else{
                objectMap.put("deliveryTime", "");
            }

            if(orderStoreList.getCloseTime()!=null){
                objectMap.put("closeTime", DateUtil.formatDateTime(orderStoreList.getCloseTime()));
            }else{
                objectMap.put("closeTime", "");
            }

            if(orderStoreList.getCompletionTime()!=null){
                objectMap.put("completionTime", DateUtil.formatDateTime(orderStoreList.getCompletionTime()));
            }else{
                objectMap.put("completionTime", "");
            }


            if(orderStoreList.getEvaluateTime()!=null){
                objectMap.put("evaluateTime", DateUtil.formatDateTime(orderStoreList.getEvaluateTime()));
            }else{
                objectMap.put("evaluateTime", "");
            }
        }else
            //平台订单
            if(isPlatform.intValue()==1){
                OrderList orderList=iOrderListService.getById(id);
                //收货地址信息
                objectMap.put("consignee",orderList.getConsignee());
                objectMap.put("contactNumber",orderList.getContactNumber());
                objectMap.put("shippingAddress",orderList.getShippingAddress());
                objectMap.put("houseNumber",orderList.getHouseNumber());
                 //留言
                objectMap.put("message",orderList.getMessage());
                //订单价格
                objectMap.put("goodsTotal",orderList.getGoodsTotal());//商品总价
                objectMap.put("shipFee",orderList.getShipFee());//运费
                objectMap.put("coupon",orderList.getDiscountOuponPrice());//优惠金额

                objectMap.put("memberDiscountPriceTotal",orderList.getMemberDiscountPriceTotal());
                objectMap.put("welfarePayments",orderList.getWelfarePayments());
                objectMap.put("welfarePaymentsPrice",orderList.getWelfarePaymentsPrice());
                objectMap.put("balance",orderList.getBalance());
                objectMap.put("payWelfarePayments",orderList.getPayWelfarePayments());//支付的积分
                objectMap.put("payWelfarePaymentsPrice",orderList.getPayWelfarePaymentsPrice());//约等于金额
                objectMap.put("giveWelfarePayments",orderList.getGiveWelfarePayments());
                objectMap.put("vipLowerTotal",orderList.getVipLowerTotal());
                objectMap.put("actualPayment",orderList.getActualPayment());
                objectMap.put("modePayment",orderList.getModePayment());//0:微信；1：支付宝；2：余额

                objectMap.put("status",orderList.getStatus());
                //获取支付记录
                if(StringUtils.isNotBlank(orderList.getSerialNumber())) {
                    PayOrderCarLog payOrderCarLog=iPayOrderCarLogService.getById(orderList.getSerialNumber());
                    objectMap.put("integral", payOrderCarLog.getIntegral());//积分数量
                    objectMap.put("integralPrice", payOrderCarLog.getIntegralPrice());//积分价值
                    objectMap.put("payPrice", payOrderCarLog.getPayPrice());//支付金额
                }

                //订单商品
                objectMap.put("goods",iOrderProviderGoodRecordService.getOrderProviderGoodRecordByOrderId(orderList.getId()));

                //订单基础信息
                objectMap.put("id",orderList.getId());
                objectMap.put("isPlatform",isPlatform);
                objectMap.put("orderNo",orderList.getOrderNo());
                objectMap.put("createTime", DateUtil.formatDateTime(orderList.getCreateTime()));
                //付款时间
                if(orderList.getPayTime()!=null){
                    objectMap.put("payTime", DateUtil.formatDateTime(orderList.getPayTime()));
                }else{
                    objectMap.put("payTime", "");
                }
                //发货时间
                if(orderList.getShipmentsTime()!=null){
                    objectMap.put("shipmentsTime", DateUtil.formatDateTime(orderList.getShipmentsTime()));
                }else{
                    objectMap.put("shipmentsTime", "");
                }

                if(orderList.getDeliveryTime()!=null){
                    objectMap.put("deliveryTime", DateUtil.formatDateTime(orderList.getDeliveryTime()));
                }else{
                    objectMap.put("deliveryTime", "");
                }

                if(orderList.getCloseTime()!=null){
                    objectMap.put("closeTime", DateUtil.formatDateTime(orderList.getCloseTime()));
                }else{
                    objectMap.put("closeTime", "");
                }

                if(orderList.getCompletionTime()!=null){
                    objectMap.put("completionTime", DateUtil.formatDateTime(orderList.getCompletionTime()));
                }else{
                    objectMap.put("completionTime", "");
                }


                if(orderList.getEvaluateTime()!=null){
                    objectMap.put("evaluateTime", DateUtil.formatDateTime(orderList.getEvaluateTime()));
                }else{
                    objectMap.put("evaluateTime", "");
                }

                //免单专区商品是否免单



            }else{
                result.error500("isPlatform参数不正确请联系平台管理员！！！");
                return result;
            }

        result.setResult(objectMap);
        result.success("查询订单详情成功");
        return result;
    }



    /**
     * 待支付订单支付
     *
     * @param id
     * @param isPlatform
     * @param request
     * @return
     */
    @RequestMapping("unpaidOrderSubmit")
    @ResponseBody
    public Result<Map<String,Object>>  unpaidOrderSubmit(String id,Integer isPlatform,
                                                         @RequestAttribute(value = "memberId",required = false) String memberId,
                                                         @RequestHeader(name = "softModel",required = false,defaultValue = "") String softModel,
                                                         HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
       //参数判断
        if(StringUtils.isBlank(id)){
            result.error500("订单id不能为空！！！");
            return result;
        }
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }


        //价格
        BigDecimal allTotalPrice=new BigDecimal(0);

        //将所有订单信息记录到支付日志
        Map<String,Object> payOrderLog=Maps.newHashMap();

        //店铺订单
        if(isPlatform.intValue()==0){
            List<String> storeGoodsList=Lists.newArrayList();
            OrderStoreList orderStoreList=iOrderStoreListService.getById(id);
            allTotalPrice=orderStoreList.getActualPayment();
            storeGoodsList.add(orderStoreList.getId());
            payOrderLog.put("storeGoods",storeGoodsList);
        }else
            //平台订单
            if(isPlatform.intValue()==1){

                OrderList orderList=iOrderListService.getById(id);
                allTotalPrice=orderList.getActualPayment();
                String orderType=orderList.getOrderType();
                if(!orderType.equals("3")){
                    payOrderLog.put("goods",orderList.getId());
                }
                //免单专区
                if(orderType.equals("3")){
                    payOrderLog.put("marketingFreeGoods",orderList.getId());
                }
            }else{
                result.error500("isPlatform参数不正确请联系平台管理员！！！");
                return result;
            }
            //支付
        Map<String,Object> objectMap= totalPayUtils.payOrder(allTotalPrice,payOrderLog,memberId,request,softModel);

        log.info("订单支付信息汇总："+JSON.toJSONString(objectMap));
        result.setResult(objectMap);
        result.success("生成待支付订单成功");
        return result;
    }

    /**
     * 订单查询接口
     * @param status
     * @param pageNo
     * @param pageSize
     * @param memberId
     * @return
     */
    @RequestMapping("orderList")
    @ResponseBody
    public Result<IPage<Map<String,Object>>> orderList(Integer status,
                                                       @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<IPage<Map<String,Object>>> result=new Result<>();
        //参数判断
        if(status==null){
            result.error500("status状态值不能为空！！！");
            return result;

        }
        //组织查询参数
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        Map<String,Object> paramObjectMap= Maps.newHashMap();
        paramObjectMap.put("memberId",memberId);
        paramObjectMap.put("status",status);
        //查询订单数据
        IPage<Map<String,Object>> orderListMapIPage=iOrderListService.getOrderListByMemberIdAndStatus(page,paramObjectMap);
        orderListMapIPage.getRecords().stream().forEach(o->{
            //给订单查询商品数据
            //平台商品
            if(o.get("isPlatform").toString().equals("1")) {
                o.put("goods", iOrderProviderGoodRecordService.getOrderProviderGoodRecordByOrderId(o.get("id").toString()));
            }
            //店铺商品
            if(o.get("isPlatform").toString().equals("0")) {
                o.put("goods", iOrderStoreGoodRecordService.getOrderStoreGoodRecordByOrderId(o.get("id").toString()));
            }
        });
        result.setResult(orderListMapIPage);
        result.success("订单列表查询成功");
        return result;
    }


    /**
     *  立即购买到确认订单
     *
     * @param goodId
     * @param specification
     * @param isPlatform
     * @param quantity
     * @return
     */
    @RequestMapping("promptlyAffirmOrder")
    @ResponseBody
    public Result<?> promptlyAffirmOrder(String goodId,
                                         @RequestParam(required = false) String orderJson,
                                                          String specification,
                                                          Integer isPlatform,
                                                          Integer quantity,
                                                          @RequestParam(value = "marketingGroupRecordId",defaultValue = "",required = false) String marketingGroupRecordId,
                                                          @RequestParam(value = "marketingPrefectureId",defaultValue = "",required = false) String marketingPrefectureId,
                                                          @RequestParam(value = "marketingFreeGoodListId",defaultValue = "",required = false) String marketingFreeGoodListId,
                                                          @RequestParam(value = "marketingStoreGiftCardMemberListId",defaultValue = "",required = false) String marketingStoreGiftCardMemberListId,
                                                          @RequestParam(value = "marketingRushGroupId",defaultValue = "",required = false) String marketingRushGroupId,
                                                          @RequestParam(name = "marketingLeagueGoodListId",defaultValue = "" ,required = false) String marketingLeagueGoodListId,
                                                         @RequestParam(name = "marketingStorePrefectureGoodId",defaultValue = "" ,required = false) String marketingStorePrefectureGoodId,
                                                         @RequestParam(required = false) String memberShippingAddressId,
                                                          @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<Map<String,Object>> result=new Result<>();

        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(goodId)){
            result.error500("goodId商品id不能为空！！！");
            return result;
        }

        if(quantity==null){
            result.error500("quantity不能为空！！！");
            return result;
        }
        if(quantity.intValue()<1){
            result.error500("购买商品数必须大于等于1！！！");
            return result;
        }
        if(specification==null){
            result.error500("specification不能为空！！！");
            return result;
        }
        if(StringUtils.isNotBlank(marketingPrefectureId)){

            MemberList memberList=iMemberListService.getById(memberId);

            MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(marketingPrefectureId);
            if(StringUtils.indexOf(marketingPrefecture.getBuyerLimit(),memberList.getMemberType())==-1){
                if(memberList.getMemberType().equals("0")){
                    result.error500("本商品普通会员不可购买");
                    return result;
                }
                if(memberList.getMemberType().equals("1")){
                    result.error500("本商品VIP会员不可购买");
                    return result;
                }
            }
        }


        /*店铺专区*/
        if(StringUtils.isNotBlank(marketingStorePrefectureGoodId)){
            if(!iMarketingStorePrefectureGiveService.ifBuy(memberId,marketingStorePrefectureGoodId)){
                result.error500("本限购专区购买条件不达标或者周期内已经购买");
                return result;
            }
        }

      String backResult=iMemberShoppingCartService.addGoodToShoppingCart(isPlatform,goodId,specification,memberId,quantity,"0",marketingPrefectureId,marketingFreeGoodListId,marketingGroupRecordId,marketingStoreGiftCardMemberListId,marketingRushGroupId,marketingLeagueGoodListId,marketingStorePrefectureGoodId);

        if(backResult.indexOf("SUCCESS")==-1){
            result.error500(backResult);
            return result;
        }
        return affirmOrder(StringUtils.substringAfter(backResult,"="),memberShippingAddressId,orderJson,memberId);
    }



    /**
     * 确认订单接口
     * @return
     */
    @RequestMapping("affirmOrder")
    @ResponseBody
    public Result<?> affirmOrder(String ids,
                                                  String memberShippingAddressId,
                                                  @RequestParam(required = false) String orderJson,
                                                  @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();

        //总运费
        BigDecimal allFreight=new BigDecimal(0);
        //件数
        BigDecimal allNumberUnits=new BigDecimal(0);
        //价格
        BigDecimal allTotalPrice=new BigDecimal(0);

        if(StringUtils.isBlank(ids)){
            result.error500("ids不能为空！！！");
            return result;
        }


        //查询购物车商品
        List<MemberShoppingCart> memberShoppingCarts=Lists.newArrayList();

        Arrays.asList(StringUtils.split(ids,",")).stream().forEach(id->{
            MemberShoppingCart memberShoppingCart= iMemberShoppingCartService.getById(id);
            if(memberShoppingCart!=null){
                memberShoppingCarts.add(memberShoppingCart);
            }
        });


        //商品归类处理
        Map<String,Object> stringObjectMap= iGoodListService.getCarGoodByMemberId(memberShoppingCarts,memberId);

        //店铺商品
        List<Map<String,Object>> storeGoods=(List<Map<String,Object>>) stringObjectMap.get("storeGoods");
        //平台商品
        List<Map<String,Object>> goods= (List<Map<String,Object>> )stringObjectMap.get("goods");
        //自选商品
        List<Map<String,Object>> marketingFreeGoods= ( List<Map<String,Object>>)stringObjectMap.get("marketingFreeGoods");
        //无效商品
        List<Map<String,Object>> disableGoods= (List<Map<String,Object>>)stringObjectMap.get("disableGoods");

        if(disableGoods.size()>0){
            objectMap.put("disableGoods",disableGoods);
            result.setResult(objectMap);
            result.error500("您提交的订单商品有问题，请查验!!!");
            return result;
        }

        MemberShippingAddress memberShippingAddress=null;

        //收货地址写入
        if(StringUtils.isBlank(memberShippingAddressId)){
            memberShippingAddress= iMemberShippingAddressService.getOne(new LambdaQueryWrapper<MemberShippingAddress>()
                    .eq(MemberShippingAddress::getMemberListId,memberId)
                    .eq(MemberShippingAddress::getIsDefault,"1"));
        }else{
            memberShippingAddress=iMemberShippingAddressService.getById(memberShippingAddressId);
        }

        //收货地址数据
        Map<String, Object> memberShippingAddressMaps = Maps.newHashMap();
        if(memberShippingAddress!=null) {
            memberShippingAddressMaps.put("isDefault",memberShippingAddress.getIsDefault());
            memberShippingAddressMaps.put("phone",memberShippingAddress.getPhone());
            memberShippingAddressMaps.put("houseNumber",memberShippingAddress.getHouseNumber());
            memberShippingAddressMaps.put("areaAddress",memberShippingAddress.getAreaAddress());
            memberShippingAddressMaps.put("id",memberShippingAddress.getId());
            memberShippingAddressMaps.put("linkman",memberShippingAddress.getLinkman());
            memberShippingAddressMaps.put("sysAreaId",memberShippingAddress.getSysAreaId());
            memberShippingAddressMaps.put("areaExplan",memberShippingAddress.getAreaExplan());
            memberShippingAddressMaps.put("areaExplanIds",memberShippingAddress.getAreaExplanIds());
        }

        objectMap.put("memberShippingAddress",memberShippingAddressMaps);


        //商品礼品卡金额
        BigDecimal goodGiftCardtotal=new BigDecimal(0);

        String marketingStoreGiftCardMemberListId=null;

        //生成确认订单数据
        //店铺订单
        if(storeGoods.size()>0){
            for (Map<String,Object> s:storeGoods) {
                String storeId = Convert.toStr(s.get("id")); //店铺ID

                //折扣券优惠相关返回参数
                Map<String, Object> settleMap = MapUtil.newHashMap();
                BigDecimal marketingTotalPrice=new BigDecimal(0); //可优惠金额
                BigDecimal noMarketingTotalPrice=new BigDecimal(0); //不可优惠金额
                List<String> marketingGoodIds =  CollUtil.newArrayList(); //优惠商品
                List<String> noMarketingGoodIds =  CollUtil.newArrayList();  //无优惠商品
                BigDecimal coupon = BigDecimal.ZERO;

                MarketingDiscountCoupon marketingDiscountCoupon = null;
                List<String> marketingDiscountCouponGoodIds = CollUtil.newArrayList(); //优惠券对应的商品ID列表
                //礼品卡ID不为空，计算折扣券优惠金额
                if (StringUtils.isBlank(marketingStoreGiftCardMemberListId)) {
                    if (StrUtil.isNotBlank(orderJson)) {
                        //解析订单json
                        JSONObject orderJsonObject= JSON.parseObject(orderJson);
                        JSONArray storeGoods1=orderJsonObject.getJSONArray("storeGoods");
                        JSONObject jsonGoods=null;
                        for (Object s1:storeGoods1) {
                            JSONObject ss=(JSONObject) s1;
                            if(StrUtil.equals(ss.getString("id"),storeId)){
                                jsonGoods=ss;
                                break;
                            }
                        }

                        if (jsonGoods != null) {
                            String discountId = jsonGoods.getString("discountId");
                            //优惠券ID 不为空，则计算优惠金额
                            if (StrUtil.isNotBlank(discountId)) {
                                marketingDiscountCoupon = iMarketingDiscountCouponService.getById(discountId);
                                if (marketingDiscountCoupon != null && StrUtil.equals(marketingDiscountCoupon.getIsNomal(), "2")
                                        && StrUtil.equals(marketingDiscountCoupon.getStatus(), "1") && StrUtil.equals(storeId,marketingDiscountCoupon.getSysUserId())) {
                                    List<MarketingDisountGoodDTO> storeGood = marketingDiscountGoodService.findStoreGood(marketingDiscountCoupon.getMarketingDiscountId());
                                    marketingDiscountCouponGoodIds = storeGood.stream().map(MarketingDisountGoodDTO::getId).collect(Collectors.toList());
                                }
                            }
                        }
                    }
                }

                //总运费
                BigDecimal freight=new BigDecimal(0);
                //件数
                BigDecimal numberUnits=new BigDecimal(0);
                //价格
                BigDecimal totalPrice=new BigDecimal(0);

                List<Map<String,Object>> myStoreGoods=(List<Map<String,Object>>)s.get("myStoreGoods");

                List<String> goodsList=Lists.newArrayList();
                Map<String,Object> goodPrices=Maps.newHashMap();

                for (Map<String,Object> my:myStoreGoods) {
                    //计算总件数
                    numberUnits=numberUnits.add((BigDecimal) my.get("quantity"));
                    //计算总价格
                    totalPrice=totalPrice.add(((BigDecimal) my.get("price")).multiply((BigDecimal) my.get("quantity")));

                    //判断礼品卡商品和金额
                    if(my.get("marketingStoreGiftCardMemberListId")!=null){
                        goodGiftCardtotal=goodGiftCardtotal.add(((BigDecimal) my.get("price")).multiply((BigDecimal) my.get("quantity")));
                        if(StringUtils.isBlank(marketingStoreGiftCardMemberListId)){
                            marketingStoreGiftCardMemberListId=my.get("marketingStoreGiftCardMemberListId").toString();
                        }
                    }

                    if (StrUtil.isBlank(marketingStoreGiftCardMemberListId)) {
                        //判断哪些商品有优惠、哪些商品没优惠、并且计算优惠金额
                        if (marketingDiscountCoupon != null && StrUtil.equals(marketingDiscountCoupon.getIsNomal(),"2")
                                && StrUtil.equals(marketingDiscountCoupon.getStatus(), "1") && StrUtil.equals(storeId,marketingDiscountCoupon.getSysUserId())) {
                            if (marketingDiscountCouponGoodIds.contains(Convert.toStr(my.get("goodId")))) {
                                marketingGoodIds.add(Convert.toStr(my.get("goodId")));
                                //可优惠总金额
                                marketingTotalPrice = marketingTotalPrice.add(((BigDecimal) my.get("price")).multiply((BigDecimal) my.get("quantity")));
                                my.put("hasDiscount","1");
                            }else {
                                noMarketingGoodIds.add(Convert.toStr(my.get("goodId")));
                                noMarketingTotalPrice = noMarketingTotalPrice.add(((BigDecimal) my.get("price")).multiply((BigDecimal) my.get("quantity")));
                                my.put("hasDiscount","0");
                            }
                        }
                    }

                    //商品id对于的金额
                    if(goodPrices.get(my.get("goodId").toString())!=null){
                        goodPrices.put(my.get("goodId").toString(),((BigDecimal) goodPrices.get(my.get("goodId").toString())).add(((BigDecimal) my.get("price")).multiply((BigDecimal) my.get("quantity"))));
                    }else{
                        goodPrices.put(my.get("goodId").toString(),((BigDecimal) my.get("price")).multiply((BigDecimal) my.get("quantity")));

                    }
                    goodsList.add(my.get("goodId").toString());
                    if(memberShippingAddress!=null) {

                        //判断是否配送
                        boolean opinion = iStoreTemplateService.opinionCalculate(my, memberShippingAddress.getSysAreaId());

                        if (opinion) {
                            my.put("opinion", "1");
                        } else {
                            my.put("opinion", "0");
                        }
                    }else {
                        my.put("opinion", "1");
                    }
                }

                //获取过滤的优惠券列表,使用了礼品卡，就不能再使用优惠券了 fix by zhangshaolin
                List<Map<String,Object>> discounts=Lists.newArrayList();
                if (StringUtils.isBlank(marketingStoreGiftCardMemberListId)) {
                    //计算优惠券折扣多少钱
                    if (marketingDiscountCoupon != null && StrUtil.equals(marketingDiscountCoupon.getIsNomal(), "2")
                            && StrUtil.equals(marketingDiscountCoupon.getStatus(), "1") && StrUtil.equals(storeId,marketingDiscountCoupon.getSysUserId())) {
                        BigDecimal discountLimitAmount = marketingDiscountCoupon.getDiscountLimitAmount();
                        BigDecimal discountUseAmount = marketingDiscountCoupon.getDiscountUseAmount();
                        BigDecimal discountPercent = marketingDiscountCoupon.getDiscountPercent();
                        //可使用折扣余额
                        BigDecimal discountBalance = NumberUtil.sub(discountLimitAmount, discountUseAmount);
                        settleMap.put("marketingTotalPrice", marketingTotalPrice);
                        settleMap.put("noMarketingTotalPrice", noMarketingTotalPrice);
                        settleMap.put("marketingGoodIds", marketingGoodIds);
                        settleMap.put("noMarketingGoodIds", noMarketingGoodIds);

                        //判断订单金额不在上限金额的范围内
                        if (marketingTotalPrice.compareTo(BigDecimal.ZERO) > 0) {
                            if (discountBalance.compareTo(BigDecimal.ZERO) > 0) {
                                if (marketingTotalPrice.subtract(discountBalance).doubleValue() >= 0) {
                                    coupon = NumberUtil.mul(NumberUtil.sub(new BigDecimal("1"), NumberUtil.div(discountPercent.toString(), "10")), discountBalance);
                                    //控制优惠金额的优惠幅度
                                    if (marketingTotalPrice.subtract(coupon).doubleValue() < 0) {
                                        coupon = marketingTotalPrice;
                                    }
                                    settleMap.put("coupon", coupon);
                                    settleMap.put("discountUseAmount",discountBalance);
                                    settleMap.put("discountBalance",BigDecimal.ZERO);
                                } else {
                                    coupon = NumberUtil.mul(marketingTotalPrice, NumberUtil.sub(new BigDecimal("1"), NumberUtil.div(discountPercent.toString(), "10")));
                                    //控制优惠金额的优惠幅度
                                    if (marketingTotalPrice.subtract(coupon).doubleValue() < 0) {
                                        coupon = marketingTotalPrice;
                                    }
                                    settleMap.put("coupon", coupon);
                                    settleMap.put("discountUseAmount",marketingTotalPrice);
                                    settleMap.put("discountBalance",NumberUtil.sub(discountBalance,marketingTotalPrice));
                                }
                            }
                        }
                    }
                    //返回优惠券列表
                    Map<String,Object> paramMap=Maps.newHashMap();
                    paramMap.put("goodIds",StringUtils.join(goodsList,","));
                    paramMap.put("memberId",memberId);
                    paramMap.put("sysUserId",Convert.toStr(s.get("id")));
                    List<Map<String,Object>> marketingDiscountCouponMaps= iMarketingDiscountCouponService.findMarketingDiscountCouponByGoodIds(paramMap);
                    for (Map<String,Object> mdc:marketingDiscountCouponMaps) {
                        if (StrUtil.equals(Convert.toStr(mdc.get("isNomal")),"2")) {
                            discounts.add(mdc);
                            continue;
                        }
                        if(mdc.get("isThreshold").toString().equals("0")){
                            mdc.put("logoAddr",String.valueOf(mdc.get("logoAddr")));
                            discounts.add(mdc);
                        }else{
                            mdc.put("logoAddr",String.valueOf(mdc.get("logoAddr")));
                            List<String> goodIds=Arrays.asList(StringUtils.split(mdc.get("goodIds").toString(),","));
                            BigDecimal totalGoodsPrice=new BigDecimal(0);
                            for (String g:goodIds) {
                                totalGoodsPrice= totalGoodsPrice.add((BigDecimal) goodPrices.get(g));
                            }
                            BigDecimal completely=new BigDecimal(mdc.get("completely").toString());
                            log.info("completely:"+completely.doubleValue()+"====totalGoodsPrice:"+totalGoodsPrice.doubleValue());
                            if(totalGoodsPrice.doubleValue()>=completely.doubleValue()){
                                discounts.add(mdc);
                            }
                        }
                    }
                }

                //有地址计算运费
                if(memberShippingAddress!=null) {
                    freight=iStoreTemplateService.calculateFreight(myStoreGoods,memberShippingAddress.getSysAreaId());
                }
                allNumberUnits=allNumberUnits.add(numberUnits);
                allFreight=allFreight.add(freight);
                allTotalPrice=allTotalPrice.add(totalPrice);
                totalPrice=totalPrice.add(freight);

                //判断礼品卡的优惠
                if(StringUtils.isNotBlank(marketingStoreGiftCardMemberListId)){
                    MarketingStoreGiftCardMemberList marketingStoreGiftCardMemberList=iMarketingStoreGiftCardMemberListService.getById(marketingStoreGiftCardMemberListId);
                    if(marketingStoreGiftCardMemberList.getDenomination().subtract(goodGiftCardtotal).doubleValue()<0){
                        goodGiftCardtotal=marketingStoreGiftCardMemberList.getDenomination();
                    }
                    s.put("giftCarCounts",goodGiftCardtotal);
                    s.put("giftCarBalance",marketingStoreGiftCardMemberList.getDenomination());
                }else{
                    s.put("giftCarCounts",0);
                    s.put("giftCarBalance",0);
                }
                allTotalPrice=allTotalPrice.subtract(goodGiftCardtotal).subtract(ObjectUtil.defaultIfNull(Convert.toBigDecimal(settleMap.get("coupon")),BigDecimal.ZERO));
                s.put("discounts",discounts);
                s.put("settleMap",settleMap);
                s.put("totalPrice",totalPrice.subtract(goodGiftCardtotal).subtract(ObjectUtil.defaultIfNull(Convert.toBigDecimal(settleMap.get("coupon")),BigDecimal.ZERO)));
                s.put("numberUnits",numberUnits);
                s.put("freight",freight);
            }
        }

        Map<String,Object> goodsMap=Maps.newHashMap();

        //平台订单
        if(goods.size()>0){
            //总运费
            BigDecimal freight=new BigDecimal(0);
            //件数
            BigDecimal numberUnits=new BigDecimal(0);
            //价格
            BigDecimal totalPrice=new BigDecimal(0);


            BigDecimal welfarePayments=new BigDecimal(0);

            BigDecimal giveWelfarePayments=new BigDecimal(0);

            //会员直降总额
            BigDecimal vipLowerTotal=new BigDecimal(0);
           //会员等级折扣金额
            BigDecimal memberDiscountPrice=new BigDecimal(0);
            BigDecimal integralValue=iMarketingWelfarePaymentsSettingService.getIntegralValue();
            List<String> goodsList=Lists.newArrayList();
            Map<String,Object> goodPrices=Maps.newHashMap();
            String memberGradeContent = "";
            //拆分供应商
            for (Map<String,Object> g:goods) {
                //计算总件数
                numberUnits=numberUnits.add((BigDecimal) g.get("quantity"));
               if(g.containsKey("memberDiscountPrice")){
                   //会员等级折扣金额 (折扣金额*件数  累加)
                   memberDiscountPrice = memberDiscountPrice.add(((BigDecimal)g.get("memberDiscountPrice")).multiply((BigDecimal) g.get("quantity")) );
                if(StringUtils.isBlank("memberGradeContent")){
                    if(g.containsKey("memberGradeContent")){
                        memberGradeContent =g.get("memberGradeContent").toString();
                    }
                }
               }

                 //计算总价格
                totalPrice=totalPrice.add(((BigDecimal) g.get("price")).multiply((BigDecimal) g.get("quantity")));
                //商品id对于的金额
                if(goodPrices.get(g.get("goodId").toString())!=null){
                    goodPrices.put(g.get("goodId").toString(),((BigDecimal) goodPrices.get(g.get("goodId").toString())).add(((BigDecimal) g.get("price")).multiply((BigDecimal) g.get("quantity"))));
                }else{
                    goodPrices.put(g.get("goodId").toString(),((BigDecimal) g.get("price")).multiply((BigDecimal) g.get("quantity")));
                }
                goodsList.add(g.get("goodId").toString());
                //判断专区商品
                if(g.get("marketingPrefectureId")!=null) {
                    //获取专区商品
                    MarketingPrefectureGood marketingPrefectureGood = iMarketingPrefectureGoodService.getOne(new LambdaQueryWrapper<MarketingPrefectureGood>()
                            .eq(MarketingPrefectureGood::getMarketingPrefectureId,g.get("marketingPrefectureId").toString())
                            .eq(MarketingPrefectureGood::getGoodListId,g.get("goodId").toString()));

                    //获取专区规格
                    MarketingPrefectureGoodSpecification marketingPrefectureGoodSpecification = iMarketingPrefectureGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingPrefectureGoodSpecification>()
                            .eq(MarketingPrefectureGoodSpecification::getMarketingPrefectureGoodId,marketingPrefectureGood.getId())
                            .eq(MarketingPrefectureGoodSpecification::getGoodSpecificationId,g.get("goodSpecificationId").toString()));
                    //可抵用福利金的计划
                    BigDecimal welfareProportion=marketingPrefectureGoodSpecification.getWelfareProportion();
                    if(marketingPrefectureGoodSpecification.getIsWelfare().equals("1")){
                        welfareProportion=new BigDecimal(100);
                    }
                    BigDecimal welfareProportionPrice=null;

                    GoodSpecification goodSpecification=iGoodSpecificationService.getById(g.get("goodSpecificationId").toString());

                    if(marketingPrefectureGoodSpecification.getIsWelfare().equals("3")){
                        welfareProportionPrice=marketingPrefectureGoodSpecification.getPrefecturePrice().subtract(goodSpecification.getSupplyPrice()).multiply(welfareProportion.divide(new BigDecimal(100))).multiply((BigDecimal) g.get("quantity")).divide(integralValue,2,RoundingMode.DOWN);
                    }else{
                        welfareProportionPrice=marketingPrefectureGoodSpecification.getPrefecturePrice().multiply(welfareProportion.divide(new BigDecimal(100))).multiply((BigDecimal) g.get("quantity")).divide(integralValue,2,RoundingMode.DOWN);
                    }

                    //判断专区会员直降
                    MarketingPrefecture marketingPrefecture=iMarketingPrefectureService.getById(g.get("marketingPrefectureId").toString());


                    if(!marketingPrefecture.getStatus().equals("1")){
                        return Result.error("专区已停用");
                    }

                    //判断专区和礼包的关系判断是否团队成员
                    if(marketingPrefecture.getIsDesignation().equals("1")){
                        long giftCount=iMarketingGiftBagRecordService.count(new LambdaQueryWrapper<MarketingGiftBagRecord>().eq(MarketingGiftBagRecord::getMemberListId,memberId));
                        if(giftCount==0){
                            return Result.error("你没有权限购买，请先购买礼包");
                        }
                    }


                    MemberList memberList=iMemberListService.getById(memberId);

                    //支持会员免福利金
                    if(marketingPrefecture.getIsVipLower().equals("1")&&memberList.getMemberType().equals("1")){
                        vipLowerTotal=vipLowerTotal.add(welfareProportionPrice).multiply(integralValue);
                        welfareProportionPrice=new BigDecimal(0);
                    }

                    welfarePayments=welfarePayments.add(welfareProportionPrice);
                    //赠送福利金的计算
                    BigDecimal giveWelfareProportion=marketingPrefectureGoodSpecification.getGiveWelfareProportion();
                    giveWelfarePayments=giveWelfarePayments.add(marketingPrefectureGoodSpecification.getPrefecturePrice().multiply(giveWelfareProportion.divide(new BigDecimal(100))).multiply((BigDecimal) g.get("quantity"))).divide(integralValue,2,RoundingMode.DOWN);

                }

                if(memberShippingAddress!=null) {


                    //判断是否配送
                    boolean opinion = iProviderTemplateService.opinionCalculate(g, memberShippingAddress.getSysAreaId());


                    if (opinion) {
                        g.put("opinion", "1");
                    } else {
                        g.put("opinion", "0");
                    }
                }else {
                    g.put("opinion", "1");
                }
            }

            //抵扣福利金的总数与会员福利金的情况进行限制
            if(iMemberListService.getById(memberId).getWelfarePayments().subtract(welfarePayments).doubleValue()<0){
                welfarePayments=iMemberListService.getById(memberId).getWelfarePayments();
            }

            //获取优惠券
            Map<String,Object> paramMap=Maps.newHashMap();

            paramMap.put("goodIds",StringUtils.join(goodsList,","));
            paramMap.put("memberId",memberId);
            List<Map<String,Object>> marketingDiscountCouponMaps= iMarketingDiscountCouponService.findMarketingDiscountCouponByGoodIds(paramMap);

            //获取过滤的优惠券列表
            List<Map<String,Object>> discounts=Lists.newArrayList();
            for (Map<String,Object> mdc:marketingDiscountCouponMaps) {
                if (StrUtil.equals(Convert.toStr(mdc.get("isNomal")),"2")) {
                    discounts.add(mdc);
                    continue;
                }
                if(mdc.get("isThreshold").toString().equals("0")){
                    mdc.put("logoAddr",String.valueOf(mdc.get("logoAddr")));
                    discounts.add(mdc);
                }else{
                    List<String> goodIds=Arrays.asList(StringUtils.split(mdc.get("goodIds").toString(),","));
                    BigDecimal totalGoodsPrice=new BigDecimal(0);
                    for (String g:goodIds) {
                        totalGoodsPrice= totalGoodsPrice.add((BigDecimal) goodPrices.get(g));
                    }

                    BigDecimal completely=new BigDecimal(mdc.get("completely").toString());
                    log.info("completely:"+completely.doubleValue()+"====totalGoodsPrice:"+totalGoodsPrice.doubleValue());
                    if(totalGoodsPrice.doubleValue()>=completely.doubleValue()){
                        discounts.add(mdc);
                    }
                }
            }

            //有地址计算运费
            if(memberShippingAddress!=null) {
                freight=iProviderTemplateService.calculateFreight(goods,memberShippingAddress.getSysAreaId());
            }
            allNumberUnits=allNumberUnits.add(numberUnits);
            allFreight=allFreight.add(freight);
            totalPrice=totalPrice.subtract(welfarePayments.multiply(integralValue)).subtract(vipLowerTotal).subtract(memberDiscountPrice).setScale(2,RoundingMode.HALF_UP);
            allTotalPrice=allTotalPrice.add(totalPrice);
            totalPrice=totalPrice.add(freight);
            goodsMap.put("discounts",discounts);
            goodsMap.put("welfarePayments",welfarePayments);
            goodsMap.put("welfarePaymentsPrice",welfarePayments.multiply(integralValue).setScale(2,RoundingMode.HALF_UP));
            goodsMap.put("giveWelfarePayments",giveWelfarePayments);
            goodsMap.put("vipLowerTotal",vipLowerTotal);
            goodsMap.put("totalPrice",totalPrice);
            goodsMap.put("numberUnits",numberUnits);
            goodsMap.put("freight",freight);
            goodsMap.put("goods",goods);
            //会员等级折扣金额
            goodsMap.put("memberDiscountPrice",memberDiscountPrice);
            //会员等级信息
            goodsMap.put("memberGradeContent",memberGradeContent);

        }

        Map<String,Object> marketingFreeGoodsMap=Maps.newHashMap();
        //免单专区订单
        if(marketingFreeGoods.size()>0){
            //总运费
            BigDecimal freight=new BigDecimal(0);
            //件数
            BigDecimal numberUnits=new BigDecimal(0);
            //价格
            BigDecimal totalPrice=new BigDecimal(0);

            //拆分供应商
            for (Map<String,Object> op:marketingFreeGoods) {
                //计算总件数
                numberUnits=numberUnits.add((BigDecimal) op.get("quantity"));
                //计算总价格
                totalPrice=totalPrice.add(((BigDecimal) op.get("price")).multiply((BigDecimal) op.get("quantity")));


                if(memberShippingAddress!=null) {
                    //判断是否配送
                    boolean opinion = iProviderTemplateService.opinionCalculate(op, memberShippingAddress.getSysAreaId());

                    if (opinion) {
                        op.put("opinion", "1");
                    } else {
                        op.put("opinion", "0");
                    }
                }else {
                    op.put("opinion", "1");
                }
            }

            //有地址计算运费
            if(memberShippingAddress!=null) {
                freight=iProviderTemplateService.calculateFreight(marketingFreeGoods,memberShippingAddress.getSysAreaId());
            }
            allNumberUnits=allNumberUnits.add(numberUnits);

            allFreight=allFreight.add(freight);
            allTotalPrice=allTotalPrice.add(totalPrice);
            totalPrice=totalPrice.add(freight);
            marketingFreeGoodsMap.put("totalPrice",totalPrice);
            marketingFreeGoodsMap.put("numberUnits",numberUnits);
            marketingFreeGoodsMap.put("freight",freight);
            marketingFreeGoodsMap.put("marketingFreeGoods",marketingFreeGoods);
        }

        objectMap.put("allFreight",allFreight);
        objectMap.put("allNumberUnits",allNumberUnits);
        objectMap.put("allTotalPrice",allTotalPrice.add(allFreight));
        objectMap.put("storeGoods",storeGoods);
        objectMap.put("goods",goodsMap);
        objectMap.put("marketingFreeGoods",marketingFreeGoodsMap);
        result.setResult(objectMap);
        result.success("确认订单成功");
        return result;
    }






    /**
     * 提交订单并支付
     * @param request
     * @return
     */
    @RequestMapping("submitOrder")
    @ResponseBody
    public Result<Map<String,Object>> submitOrder(String ids,
                                                  String memberShippingAddressId,
                                                  String orderJson,
                                                  @RequestHeader(defaultValue = "") String sysUserId,
                                                  @RequestHeader(defaultValue = "") String longitude,
                                                  @RequestHeader(defaultValue = "") String latitude,
                                                  @RequestHeader(name = "softModel",required = false,defaultValue = "") String softModel,
                                                  @RequestAttribute(value = "memberId",required = false) String memberId,
                                                  HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //价格
        BigDecimal allTotalPrice=new BigDecimal(0);


        if(StringUtils.isBlank(ids)){
            result.error500("ids不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(memberShippingAddressId)){
            result.error500("收货地址id不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(orderJson)){
            result.error500("订单json不允许为空");
            return result;
        }



        //查询购物车商品
        List<MemberShoppingCart> memberShoppingCarts=Lists.newArrayList();


        log.info("确认订单，购物车id:{},orderJson: {}",ids,orderJson);

        Arrays.asList(StringUtils.split(ids,",")).parallelStream().forEach(id->{
            MemberShoppingCart memberShoppingCart= iMemberShoppingCartService.getById(id);
            if(memberShoppingCart!=null){
                memberShoppingCarts.add(memberShoppingCart);
            }
        });

        //商品归类处理
        Map<String,Object> stringObjectMap= iGoodListService.getCarGoodByMemberId(memberShoppingCarts,memberId);

        //店铺商品
        List<Map<String,Object>> storeGoods=(List<Map<String,Object>>) stringObjectMap.get("storeGoods");
        //平台商品
        List<Map<String,Object>> goods= (List<Map<String,Object>> )stringObjectMap.get("goods");
        //免单商品
        List<Map<String,Object>> marketingFreeGoods= ( List<Map<String,Object>>)stringObjectMap.get("marketingFreeGoods");
        //无效商品
        List<Map<String,Object>> disableGoods= (List<Map<String,Object>>)stringObjectMap.get("disableGoods");


        if(disableGoods.size()>0){
            objectMap.put("disableGoods",disableGoods);
            result.error500("您提交的订单商品有问题，请查验!!!");
            return result;
        }

        //查询收货地址
        MemberShippingAddress memberShippingAddress=iMemberShippingAddressService.getById(memberShippingAddressId);


        //将所有订单信息记录到支付日志

        Map<String,Object> payOrderLog=Maps.newHashMap();


        //进行订单处理生成待支付订单
        //店铺订单
        if(storeGoods.size()>0){
            List<String> storeGoodsList=Lists.newArrayList();
            for (Map<String,Object> s:storeGoods) {
                OrderStoreList orderStoreList= iOrderStoreListService.submitOrderStoreGoods(s,memberId,orderJson,memberShippingAddress,longitude,latitude);
                allTotalPrice=allTotalPrice.add(orderStoreList.getActualPayment());
                storeGoodsList.add(orderStoreList.getId());
            }
            payOrderLog.put("storeGoods",storeGoodsList);
        }


        //平台订单
        if(goods.size()>0){
            OrderList orderList= iOrderListService.submitOrderGoods( goods, memberId, orderJson,  memberShippingAddress, sysUserId,0,longitude,latitude);
            allTotalPrice=allTotalPrice.add(orderList.getActualPayment());
            payOrderLog.put("goods",orderList.getId());
        }

        //免单专区订单
        if(marketingFreeGoods.size()>0){
           OrderList orderList= iOrderListService.submitOrderGoods( marketingFreeGoods, memberId, orderJson,  memberShippingAddress, sysUserId,1,longitude,latitude);
            allTotalPrice=allTotalPrice.add(orderList.getActualPayment());
            payOrderLog.put("marketingFreeGoods",orderList.getId());
        }
        //支付
        objectMap.putAll(totalPayUtils.payOrder(allTotalPrice,payOrderLog,memberId,request,softModel));
        //删除购物车商品
        List<String> mIds=Lists.newArrayList();
        memberShoppingCarts.stream().forEach(m->{
            mIds.add(m.getId());
        });
        //删除购物车
        if(mIds.size()>0) {
            iMemberShoppingCartService.removeByIds(mIds);
        }

        log.info("订单支付信息汇总："+JSON.toJSONString(objectMap));

        result.setResult(objectMap);
        result.success("生成待支付订单成功");
        return result;
    }

    /**
     * 待支付订单计时器
     * @param orderId
     * @param isPlatform
     * @return
     */
    @RequestMapping("prepaidOrderTimer")
    @ResponseBody
    public Result<Map<String,Object>>  prepaidOrderTimer(String orderId,Integer isPlatform){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(orderId)){
            result.error500("orderId订单id不能为空！！！");
            return result;
        }
         //获取倒计时时间
        String  timer= iOrderListService.prepaidOrderTimer(orderId,isPlatform);
       if(StringUtils.isBlank(timer)){
           result.error500("未找到订单倒计时数据!");
           return result;
       }
        objectMap.put("timer",timer);
        result.setResult(objectMap);
        result.success("请求成功");
        return  result;

    }


    /**
     * 确认收货订单计时器
     * @param orderId
     * @param isPlatform
     * @return
     */
    @RequestMapping("confirmReceiptTimer")
    @ResponseBody
    public Result<Map<String,Object>>  confirmReceiptTimer(String orderId,Integer isPlatform){
        Result<Map<String,Object>> result=new Result<>();
        Map<String,Object> objectMap= Maps.newHashMap();
        //参数判断
        if(isPlatform==null){
            result.error500("isPlatform是否平台类型参数不能为空！！！");
            return result;
        }

        if(StringUtils.isBlank(orderId)){
            result.error500("orderId订单id不能为空！！！");
            return result;
        }
        //获取倒计时时间
        String  timer= iOrderListService.confirmReceiptTimer(orderId,isPlatform);
        if(StringUtils.isBlank(timer)){
            result.error500("未找到订单倒计时数据!");
            return result;
        }
        objectMap.put("timer",timer);
        result.setResult(objectMap);
        result.success("请求成功");
        return  result;
    }

}
