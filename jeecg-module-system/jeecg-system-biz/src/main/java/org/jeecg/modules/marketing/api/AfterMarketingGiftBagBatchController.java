package org.jeecg.modules.marketing.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.marketing.entity.MarketingGiftBagBatch;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.service.IMarketingGiftBagBatchService;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchService;
import org.jeecg.modules.member.entity.MemberGrade;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberGradeService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.pay.utils.NotifyUrlUtils;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 礼包需要的API接口
 */
@RequestMapping("after/marketingGiftBagBatch")
@Controller
@Slf4j
public class AfterMarketingGiftBagBatchController {

    @Autowired
    private IMarketingGiftBagBatchService iMarketingGiftBagBatchService;
    @Autowired
    private IMemberListService iMemberListService;
    @Autowired
    private IMemberGradeService iMemberGradeService;
    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;
    @Autowired
    private IMarketingGiftBagRecordBatchService iMarketingGiftBagRecordBatchService;
    @Autowired
    private ISysDictService iSysDictService;

    @Autowired
    private NotifyUrlUtils notifyUrlUtils;

    /**
     * 根据礼包id获取详情
     * @param id
     * @return
     */
    @RequestMapping("findMarketingGiftBagBatchInfo")
    @ResponseBody
    public Result<Map<String,Object>> findMarketingGiftBagBatchInfo(String id,
                                                                    HttpServletRequest request){
        Result<Map<String,Object>> result=new Result<>();
        String memberId = request.getAttribute("memberId").toString();
        //参数校验
        if(StringUtils.isBlank(id)){
            result.error500("id不能为空！！！");
            return result;
        }
        HashMap<String, Object> map = new HashMap<>();
        MarketingGiftBagBatch marketingGiftBagBatch = iMarketingGiftBagBatchService.getById(id);
        map.put("id",marketingGiftBagBatch.getId());
        map.put("giftName",marketingGiftBagBatch.getGiftName());
        map.put("coverPlan",marketingGiftBagBatch.getCoverPlan());
        map.put("posters",marketingGiftBagBatch.getPosters());
        map.put("giftDeals",marketingGiftBagBatch.getGiftDeals());
        map.put("price",marketingGiftBagBatch.getPrice());
        map.put("smallBuyCount",marketingGiftBagBatch.getSmallBuyCount());
        MemberList memberList = iMemberListService.getById(memberId);
        //判断是否等级限制
        if (marketingGiftBagBatch.getBuyLimit().contains("1")&&StringUtils.isNotBlank(marketingGiftBagBatch.getBuyVipMemberGradeId())){
            if (marketingGiftBagBatch.getBuyVipMemberGradeId().contains(memberList.getMemberGradeId())){
                map.put("isViewBuyVipMemberGradeName","0");
            }else {
                map.put("isViewBuyVipMemberGradeName","1");
                if (marketingGiftBagBatch.getBuyVipMemberGradeId().contains(",")){
                    List<String> memberGradeIds = Arrays.asList(StringUtils.split(marketingGiftBagBatch.getBuyVipMemberGradeId(), ","));
                    ArrayList<String> memberGradeNameList = new ArrayList<>();

                    memberGradeIds.forEach(mgs->{
                        MemberGrade memberGrade = iMemberGradeService.getById(mgs);
                        if (oConvertUtils.isNotEmpty(memberGrade)){
                            memberGradeNameList.add(memberGrade.getGradeName());
                        }
                    });
                    map.put("buyVipMemberGradeName","请先升级为("+memberGradeNameList+")会员");
                }else {
                    MemberGrade memberGrade = iMemberGradeService.getById(marketingGiftBagBatch.getBuyVipMemberGradeId());
                    if (oConvertUtils.isNotEmpty(memberGrade)){
                        map.put("buyVipMemberGradeName","请先升级为("+memberGrade.getGradeName()+")会员");
                    }
                }
            }
        }else {
            map.put("isViewBuyVipMemberGradeName","0");
        }
        result.setResult(map);
        result.success("获取礼包详情成功");
        return result;
    }
    @RequestMapping("affirmMarketingGiftBagBatch")
    @ResponseBody
    public Result<Map<String,Object>> affirmMarketingGiftBagBatch(String id,
                                                                  String quantity,
                                                                  @RequestParam(required = false) String memberShippingAddressId,
                                                                  HttpServletRequest request){
        Result<Map<String, Object>> result = new Result<>();
        HashMap<String, Object> map = new HashMap<>();
        String memberId = request.getAttribute("memberId").toString();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        MemberShippingAddress memberShippingAddress=null;
        if (StringUtils.isBlank(memberShippingAddressId)){
            LambdaQueryWrapper<MemberShippingAddress> memberShippingAddressLambdaQueryWrapper = new LambdaQueryWrapper<MemberShippingAddress>()
                    .eq(MemberShippingAddress::getDelFlag, "0")
                    .eq(MemberShippingAddress::getMemberListId, memberId)
                    .eq(MemberShippingAddress::getIsDefault, "1");
            if (iMemberShippingAddressService.count(memberShippingAddressLambdaQueryWrapper)>0){
                memberShippingAddress = iMemberShippingAddressService.list(memberShippingAddressLambdaQueryWrapper).get(0);
                stringObjectHashMap.put("id",memberShippingAddress.getId());
                stringObjectHashMap.put("linkman",memberShippingAddress.getLinkman());
                stringObjectHashMap.put("phone",memberShippingAddress.getPhone());
                stringObjectHashMap.put("isDefault",memberShippingAddress.getIsDefault());
                stringObjectHashMap.put("areaExplan",memberShippingAddress.getAreaExplan());
                stringObjectHashMap.put("areaAddress",memberShippingAddress.getAreaAddress());
                stringObjectHashMap.put("houseNumber",memberShippingAddress.getHouseNumber());

            }

        }else {
            memberShippingAddress = iMemberShippingAddressService.getById(memberShippingAddressId);
            stringObjectHashMap.put("id",memberShippingAddress.getId());
            stringObjectHashMap.put("linkman",memberShippingAddress.getLinkman());
            stringObjectHashMap.put("phone",memberShippingAddress.getPhone());
            stringObjectHashMap.put("isDefault",memberShippingAddress.getIsDefault());
            stringObjectHashMap.put("areaExplan",memberShippingAddress.getAreaExplan());
            stringObjectHashMap.put("areaAddress",memberShippingAddress.getAreaAddress());
            stringObjectHashMap.put("houseNumber",memberShippingAddress.getHouseNumber());

        }
        map.put("memberShippingAddress",stringObjectHashMap);
        HashMap<String, Object> objectHashMap = new HashMap<>();
        MarketingGiftBagBatch marketingGiftBagBatch = iMarketingGiftBagBatchService.getById(id);
        objectHashMap.put("mainPicture",marketingGiftBagBatch.getMainPicture());
        objectHashMap.put("giftName",marketingGiftBagBatch.getGiftName());
        objectHashMap.put("price",marketingGiftBagBatch.getPrice());
        objectHashMap.put("quantity",quantity);
        objectHashMap.put("shipFee",new BigDecimal(0));
        objectHashMap.put("subtotal",marketingGiftBagBatch.getPrice().multiply(new BigDecimal(quantity)));
        map.put("marketingGiftBagBatch",objectHashMap);
        map.put("id",id);
        result.setResult(map);
        result.success("返回采购礼包确认购买页");
        return result;
    }
    @RequestMapping("submitMarketingGiftBagBatch")
    @ResponseBody
    public Result<Map<String,Object>> submitMarketingGiftBagBatch(String id,
                                                                  String quantity,
                                                                  String message,
                                                                  String shippingAddressId,
                                                                  @RequestHeader(defaultValue = "") String sysUserId,
                                                                  @RequestHeader(defaultValue = "") String longitude,
                                                                  @RequestHeader(defaultValue = "") String latitude,
                                                                  @RequestHeader(defaultValue = "") String tMemberId,
                                                                  HttpServletRequest request){
        Result<Map<String, Object>> result = new Result<>();
        String memberId=request.getAttribute("memberId").toString();
        HashMap<String, Object> map = new HashMap<>();
        MarketingGiftBagBatch marketingGiftBagBatch = iMarketingGiftBagBatchService.getById(id);

        if (marketingGiftBagBatch.getRepertory().doubleValue()<=0){
            return result.error500("库存不足");
        }
        if (StringUtils.isBlank(shippingAddressId)){
            return result.error500("请选择收货地址");
        }
        MemberList memberList=iMemberListService.getById(memberId);
        MarketingGiftBagRecordBatch marketingGiftBagRecordBatch = new MarketingGiftBagRecordBatch();
        BigDecimal multiply = marketingGiftBagBatch.getPrice().multiply(new BigDecimal(quantity));

        //获取收货地址
        MemberShippingAddress memberShippingAddress = iMemberShippingAddressService.getById(shippingAddressId);

        marketingGiftBagRecordBatch
                .setDelFlag("0")
                .setGiftName(marketingGiftBagBatch.getGiftName())
                .setPrice(marketingGiftBagBatch.getPrice())
                .setSendTimes(marketingGiftBagBatch.getSendTimes())
                .setBuyLimit(marketingGiftBagBatch.getBuyLimit())
                .setBuyVipMemberGradeId(marketingGiftBagBatch.getBuyVipMemberGradeId())
                .setBuyCount(new BigDecimal(quantity))
                .setViewScope(marketingGiftBagBatch.getViewScope())
                .setStartTime(marketingGiftBagBatch.getStartTime())
                .setEndTime(marketingGiftBagBatch.getEndTime())
                .setMainPicture(marketingGiftBagBatch.getMainPicture())
                .setGiftDeals(marketingGiftBagBatch.getGiftDeals())
                .setCoverPlan(marketingGiftBagBatch.getCoverPlan())
                .setPosters(marketingGiftBagBatch.getPosters())
                .setGiftExplain(marketingGiftBagBatch.getGiftExplain())
                .setMarketingGiftBagBatchId(marketingGiftBagBatch.getId())
                .setGiftNo(OrderNoUtils.getOrderNo())
                .setShipFee(new BigDecimal(0))
                .setMemberListId(memberList.getId())
                .setCustomaryDues(multiply)
                .setActualPayment(multiply)
                .setMessage(message)
                .setPayStatus("0")
                .setDistributionChannel(sysUserId)
                .setLinkman(memberShippingAddress.getLinkman())
                .setPhone(memberShippingAddress.getPhone())
                .setAreaAddress(memberShippingAddress.getAreaAddress())
                .setAreaExplan(memberShippingAddress.getAreaExplan());

        if(StringUtils.isNotBlank(longitude)){
            marketingGiftBagRecordBatch.setLongitude(new BigDecimal(longitude));
        }
        if(StringUtils.isNotBlank(latitude)){
            marketingGiftBagRecordBatch.setLatitude(new BigDecimal(latitude));
        }
        iMarketingGiftBagRecordBatchService.save(marketingGiftBagRecordBatch);
        //获取常量信息
        String appid= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","AppID");

        String mchId= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","mchId");

        String partnerKey= iSysDictService.queryTableDictTextByKey("sys_dict_item","item_value","item_text","partnerKey");

        WxPayApiConfig wxPayApiConfig = WxPayApiConfig.builder().
                appId(appid).
                mchId(mchId).
                partnerKey(partnerKey).
                build();

        //设置回调地址
        String notifyUrl=notifyUrlUtils.getNotify("marketingGiftBagBatchUrl");

        String ip = IpKit.getRealIp(request);
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(marketingGiftBagRecordBatch.getId())
                .body("微信小程序支付")
                .attach("厦门靠勤网络科技有限公司厉害")
                .out_trade_no(marketingGiftBagRecordBatch.getId())
                .total_fee(marketingGiftBagRecordBatch.getActualPayment().multiply(new BigDecimal(100)).toBigInteger().toString())
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(memberList.getOpenid())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        log.info(xmlResult);
        Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);

        String returnCode = payResult.get("return_code");
        String returnMsg = payResult.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            result.error500(returnMsg);
            return result;
        }
        String resultCode = payResult.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            result.error500(returnMsg);
            return result;
        }
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = payResult.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayApiConfig.getAppId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        String jsonStr = JSON.toJSONString(packageParams);
        //支付日志
        marketingGiftBagRecordBatch.setPayParam(params.toString());
        //保存支付日志
        iMarketingGiftBagRecordBatchService.saveOrUpdate(marketingGiftBagRecordBatch);
        map.put("notifyUrl",notifyUrl+"?id="+marketingGiftBagRecordBatch.getId());
        map.put("jsonStr",jsonStr);

        result.setResult(map);
        result.success("生成待支付订单成功");
        return result;
    }


}
