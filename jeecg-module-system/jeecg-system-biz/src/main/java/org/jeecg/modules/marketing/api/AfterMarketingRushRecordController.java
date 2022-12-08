package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.entity.*;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberShippingAddress;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberShippingAddressService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.jeecg.modules.provider.service.IProviderTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("after/marketingRushRecord")
@Log
public class AfterMarketingRushRecordController {

    @Autowired
    private IMarketingRushGoodService iMarketingRushGoodService;

    @Autowired
    private IMarketingRushTypeService iMarketingRushTypeService;

    @Autowired
    private IMarketingRushBaseSettingService iMarketingRushBaseSettingService;

    @Autowired
    private IMarketingRushGroupService iMarketingRushGroupService;

    @Autowired
    private IMarketingRushRecordService iMarketingRushRecordService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IMemberShippingAddressService iMemberShippingAddressService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IProviderTemplateService iProviderTemplateService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingFourthIntegralRecordService iMarketingFourthIntegralRecordService;


    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private IMarketingRushOrderService iMarketingRushOrderService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMarketingPrefectureService iMarketingPrefectureService;

    @Autowired
    private IMarketingPrefectureTypeService iMarketingPrefectureTypeService;



    /**
     *
     * 确认订单
     *
     *
     * @return
     */
    @RequestMapping("confirmOrder")
    @ResponseBody
    public Result<?> confirmOrder(String marketingRushGoodId,
                                  String specification,
                                  BigDecimal quantity,
                                  @RequestParam(name = "memberShippingAddressId" ,required = false,defaultValue = "") String memberShippingAddressId,
                                  @RequestAttribute(value = "memberId",required = false) String memberId){
        Map<String,Object> resultMap= Maps.newHashMap();
        //参数校验
        if(StringUtils.isBlank(marketingRushGoodId)){
            return Result.error("抢购商品id不能为空");
        }

        if(StringUtils.isBlank(specification)){
            return Result.error("商品规格不能为空");
        }
        if(quantity.intValue()<=0){
            return Result.error("商品数量不能小于0");
        }
        quantity=new BigDecimal(1);
        MarketingRushGood marketingRushGood=iMarketingRushGoodService.getById(marketingRushGoodId);
        MarketingRushType marketingRushType=iMarketingRushTypeService.getById(marketingRushGood.getMarketingRushTypeId());
        if(marketingRushType.getStatus().equals("0")){
            return Result.error("抢购分类停止");
        }
        MarketingRushBaseSetting marketingRushBaseSetting=iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getStatus,"1"));
        if(marketingRushBaseSetting==null){
            return Result.error("抢购设置不存在");
        }

        if(iMarketingRushOrderService.count(new LambdaQueryWrapper<MarketingRushOrder>().eq(MarketingRushOrder::getMemberListId,memberId))==0){
            resultMap.put("prefectureName", iMarketingPrefectureService.getById(marketingRushBaseSetting.getMarketingPrefectureId()).getPrefectureName());
            resultMap.put("marketingPrefectureId",marketingRushBaseSetting.getMarketingPrefectureId());
            resultMap.put("marketingPrefectureTypeId",marketingRushBaseSetting.getMarketingPrefectureTypeId());
            resultMap.put("typeName",iMarketingPrefectureTypeService.getById(marketingRushBaseSetting.getMarketingPrefectureTypeId()).getTypeName());
            return Result.ok(resultMap);
        }
        //会员单天单类型抢购次数
        Calendar calendar=Calendar.getInstance();
        long memberRushCount=iMarketingRushRecordService.count(new LambdaQueryWrapper<MarketingRushRecord>()
                .eq(MarketingRushRecord::getYear,calendar.get(Calendar.YEAR))
                .eq(MarketingRushRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingRushRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingRushRecord::getMemberListId,memberId)
                .eq(MarketingRushRecord::getMarketingRushTypeId,marketingRushType.getId()));
        if(memberRushCount>=marketingRushType.getPurchaseLimitation().intValue()){
            return Result.error("抢购次数超过单天上限，最大上限次数："+marketingRushType.getPurchaseLimitation());
        }

        try {
            Date startTime= DateUtils.parseDate(DateUtils.formatDate()+" "+marketingRushBaseSetting.getDayStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingRushBaseSetting.getDayEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>=startTime.getTime()&&new Date().getTime()<=endTime.getTime()) {
                BigDecimal freight=new BigDecimal(0);
                BigDecimal totalPrice=new BigDecimal(0);
                GoodList goodList=iGoodListService.getById(marketingRushGood.getGoodListId());
                resultMap.put("goodName",goodList.getGoodName());
                resultMap.put("mainPicture",goodList.getMainPicture());
                resultMap.put("id",goodList.getId());
                resultMap.put("marketPrice",goodList.getMarketPrice());
                resultMap.put("specification",specification);
                resultMap.put("label",marketingRushBaseSetting.getLabel());
                resultMap.put("price",marketingRushGood.getPrice());
                resultMap.put("quantity",quantity);
                MemberShippingAddress memberShippingAddress=null;

                if(StringUtils.isBlank(memberShippingAddressId)){
                    memberShippingAddress= iMemberShippingAddressService.getOne(new LambdaQueryWrapper<MemberShippingAddress>()
                            .eq(MemberShippingAddress::getMemberListId,memberId)
                            .eq(MemberShippingAddress::getIsDefault,"1")
                            .last("limit 1"));
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
                    resultMap.put("memberShippingAddressId",memberShippingAddress.getId());
                }
                resultMap.put("memberShippingAddress",memberShippingAddressMaps);

                //获取商品
                GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                        .eq(GoodSpecification::getGoodListId,goodList.getId())
                        .eq(GoodSpecification::getSpecification,specification)
                        .last("limit 1"));

                //有地址计算运费
                if(memberShippingAddress!=null) {
                    List<Map<String,Object>> goods= Lists.newArrayList();
                    Map<String,Object> g=Maps.newHashMap();
                    g.put("goodId",goodList.getId());
                    g.put("quantity",quantity);
                    g.put("price",marketingRushGood.getPrice());
                    g.put("goodSpecificationId",goodSpecification.getId());
                    goods.add(g);
                    freight=iProviderTemplateService.calculateFreight(goods,memberShippingAddress.getSysAreaId());
                }
                resultMap.put("freight",freight);
                resultMap.put("totalPrice",totalPrice.add(marketingRushGood.getPrice().multiply(quantity)).add(freight));
                resultMap.put("marketingRushGoodId",marketingRushGoodId);
                resultMap.put("memberListId",memberId);
            }else{
                return Result.error("抢购还没开始");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return Result.ok(resultMap);
    }


    /**
     * 抢购下单
     *
     * @param marketingRushGoodId
     * @param specification
     * @param quantity
     * @param memberShippingAddressId
     * @param message
     * @param captchaKey
     * @param captchaContent
     * @param memberId
     * @param request
     * @return
     */
    @RequestMapping("submitOrder")
    @ResponseBody
    @Transactional
    public Result<?> submitOrder(String marketingRushGoodId,
                                 String specification,
                                 BigDecimal  quantity,
                                 String memberShippingAddressId,
                                 String message,
                                 String captchaKey,
                                 String captchaContent,
                                 @RequestAttribute(value = "memberId",required = false) String memberId, HttpServletRequest request){
        //参数校验
        if(StringUtils.isBlank(marketingRushGoodId)){
            return Result.error("抢购商品id不能为空");
        }

        if(StringUtils.isBlank(specification)){
            return Result.error("商品规格不能为空");
        }
        if(quantity.intValue()<=0){
            return Result.error("商品数量不能小于0");
        }
        quantity=new BigDecimal(1);
        if(StringUtils.isBlank(captchaKey)){
            return Result.error("验证码的key不能为空");
        }

        if(StringUtils.isBlank(captchaContent)){
            return Result.error("图片验证码的值不能为空");
        }
        String ip= IpUtils.getIpAddr(request);
        log.info("请求的IP地址："+ip );
        if(redisUtil.get("submitOrder"+ip)==null){
            //缓存验证码并设置过期时间
            redisUtil.set("submitOrder"+ip,ip,3);
        }else{
            return Result.error("同一个ip请求过于频繁！！！请稍后再试！！！");
        }

        if(redisUtil.get("submitOrder"+memberId)==null){
            //缓存验证码并设置过期时间
            redisUtil.set("submitOrder"+memberId,memberId,3);
        }else{
            return Result.error("同一个会员请求过于频繁！！！请稍后再试！！！");
        }
        //获取验证码
        Object captchaObject= redisUtil.get(captchaKey);
        if(captchaObject==null||!captchaObject.toString().toLowerCase().equals(captchaContent.toLowerCase())){
            return Result.error("验证码不正确");
        }
        MarketingRushGood marketingRushGood=iMarketingRushGoodService.getById(marketingRushGoodId);
        MarketingRushType marketingRushType=iMarketingRushTypeService.getById(marketingRushGood.getMarketingRushTypeId());
        if(marketingRushType.getStatus().equals("0")){
            return Result.error("抢购分类停止");
        }
        //会员单天单类型抢购次数
        Calendar calendar=Calendar.getInstance();
        long memberRushCount=iMarketingRushRecordService.count(new LambdaQueryWrapper<MarketingRushRecord>()
                .eq(MarketingRushRecord::getYear,calendar.get(Calendar.YEAR))
                .eq(MarketingRushRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                .eq(MarketingRushRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                .eq(MarketingRushRecord::getMemberListId,memberId)
                .eq(MarketingRushRecord::getMarketingRushTypeId,marketingRushType.getId()));
        if(memberRushCount>=marketingRushType.getPurchaseLimitation().intValue()){
            return Result.error("抢购次数超过单天上限，最大上限次数："+marketingRushType.getPurchaseLimitation());
        }
        MarketingRushBaseSetting marketingRushBaseSetting=iMarketingRushBaseSettingService.getOne(new LambdaQueryWrapper<MarketingRushBaseSetting>()
                .eq(MarketingRushBaseSetting::getStatus,"1"));
        if(marketingRushBaseSetting==null){
            return Result.error("抢购设置不存在");
        }
        try {
            Date startTime= DateUtils.parseDate(DateUtils.formatDate()+" "+marketingRushBaseSetting.getDayStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingRushBaseSetting.getDayEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>=startTime.getTime()&&new Date().getTime()<=endTime.getTime()) {
                //判断会员的产品券是否足够
                MemberList memberList=iMemberListService.getById(memberId);
                if(memberList.getFourthIntegral().subtract(marketingRushGood.getPrice().multiply(quantity)).doubleValue()>=0){
                   //生成抢购记录
                    MarketingRushRecord marketingRushRecord=new MarketingRushRecord();
                    marketingRushRecord.setMarketingRushTypeId(marketingRushType.getId());
                    marketingRushRecord.setMemberListId(memberId);
                    marketingRushRecord.setSerialNumber(OrderNoUtils.getOrderNo());
                    marketingRushRecord.setMarketingRushGoodId(marketingRushGoodId);
                    GoodList goodList=iGoodListService.getById(marketingRushGood.getGoodListId());
                    //获取商品
                    GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                            .eq(GoodSpecification::getGoodListId,goodList.getId())
                            .eq(GoodSpecification::getSpecification,specification)
                            .last("limit 1"));
                    marketingRushRecord.setGoodNo(goodList.getGoodNo());
                    marketingRushRecord.setMainPicture(goodList.getMainPicture());
                    marketingRushRecord.setGoodName(goodList.getGoodName());
                    marketingRushRecord.setGoodSpecificationId(goodSpecification.getId());
                    marketingRushRecord.setSpecification(goodSpecification.getSpecification());
                    marketingRushRecord.setAmount(quantity);
                    marketingRushRecord.setPrice(marketingRushGood.getPrice().multiply(quantity));
                    marketingRushRecord.setStatus("0");
                    marketingRushRecord.setDistributionRewards("0");
                    marketingRushRecord.setMemberShippingAddressId(memberShippingAddressId);
                    marketingRushRecord.setMessage(message);
                    if(!iMarketingRushRecordService.save(marketingRushRecord)){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return Result.error("系统正在努力处理中，请稍作等待，继续尝试，谢谢！！！");
                    }
                    //扣除产品券
                    //减少会员第四积分
                    if(!iMarketingFourthIntegralRecordService.subtractFourthIntegral(memberId,marketingRushRecord.getPrice(),marketingRushRecord.getSerialNumber(),"5")){
                        //手动强制回滚事务，这里一定要第一时间处理
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return Result.error("系统正在努力处理中，请稍作等待，继续尝试，谢谢！！！");
                    }
                    long  upgradeRushCount=iMarketingRushRecordService.count(new LambdaQueryWrapper<MarketingRushRecord>()
                            .eq(MarketingRushRecord::getMemberListId,memberId));
                    if(upgradeRushCount==25) {
                        log.info("新用户触发团队升级");
                        iMemberDistributionLevelService.teamRushUpgrade(memberId);
                    }
                    //判断是否成功
                    boolean isSuccess=false;
                    List<String> successList=Arrays.asList(StringUtils.split(marketingRushBaseSetting.getRushController(),","));
                    for (String rush:successList) {
                        if(memberRushCount==Integer.parseInt(rush)){
                            isSuccess=true;
                            break;
                        }
                    }

                    if(isSuccess){
                        //成功
                        marketingRushRecord.setStatus("1");
                        marketingRushRecord.setDistributionRewards("1");
                        marketingRushRecord.setClassificationReward("1");
                        //形成分组数据
                        MarketingRushGroup marketingRushGroup=iMarketingRushGroupService.getOne(new LambdaQueryWrapper<MarketingRushGroup>()
                                .eq(MarketingRushGroup::getMemberListId,memberId)
                                .eq(MarketingRushGroup::getMarketingRushTypeId,marketingRushType.getId())
                                .eq(MarketingRushGroup::getConsignmentStatus,"0")
                                .orderByDesc(MarketingRushGroup::getCreateTime));
                        if(marketingRushGroup==null){
                            marketingRushGroup=new MarketingRushGroup();
                            marketingRushGroup.setMarketingRushTypeId(marketingRushType.getId());
                            marketingRushGroup.setTransformationThreshold(marketingRushType.getTransformationThreshold());
                            marketingRushGroup.setCanConsignment(marketingRushType.getCanConsignment());
                            marketingRushGroup.setMemberListId(memberId);
                            marketingRushGroup.setIfPurchase("0");
                            marketingRushGroup.setConsignmentStatus("0");
                            marketingRushGroup.setSerialNumber(OrderNoUtils.getOrderNo());
                            marketingRushGroup.setDistributionRewards("0");
                            if(!iMarketingRushGroupService.save(marketingRushGroup)){
                                //手动强制回滚事务，这里一定要第一时间处理
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            }
                        }
                        marketingRushRecord.setMarketingRushGroupId(marketingRushGroup.getId());
                        if(!iMarketingRushRecordService.saveOrUpdate(marketingRushRecord)){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        long marketingRushGroupCount=iMarketingRushRecordService.count(new LambdaQueryWrapper<MarketingRushRecord>().eq(MarketingRushRecord::getMarketingRushGroupId,marketingRushGroup.getId()));
                        if(marketingRushGroupCount>=marketingRushGroup.getTransformationThreshold().intValue()){
                            marketingRushGroup.setConsignmentStatus("1");
                            if(!iMarketingRushGroupService.saveOrUpdate(marketingRushGroup)){
                                //手动强制回滚事务，这里一定要第一时间处理
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            }
                        }
                    }else{
                        //失败
                        marketingRushRecord.setDistributionRewards("1");
                        if(!iMarketingFourthIntegralRecordService.addFourthIntegral(marketingRushRecord.getMemberListId(),marketingRushRecord.getPrice(),marketingRushRecord.getSerialNumber(),"6")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        if(!iMarketingRushRecordService.saveOrUpdate(marketingRushRecord)){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        //参与拼购奖励
                        if(!iMemberListService.addBlance(marketingRushRecord.getMemberListId(),marketingRushType.getGroupRewards().multiply(new BigDecimal(0.8)).setScale(2, RoundingMode.DOWN),marketingRushRecord.getSerialNumber(),"31")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        //加上积分
                        if(!iMemberWelfarePaymentsService.addWelfarePayments(marketingRushRecord.getMemberListId(),marketingRushType.getGroupRewards().multiply(new BigDecimal(0.2)).setScale(2, RoundingMode.DOWN), "37",marketingRushRecord.getSerialNumber(),"")){
                            //手动强制回滚事务，这里一定要第一时间处理
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }
                    return Result.ok(marketingRushRecord);
                }else{
                    return Result.error("会员产品券不足");
                }
            }else{
                return Result.error("抢购还没开始");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Result.ok();
    }

}
