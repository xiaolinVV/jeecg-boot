package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManage;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManageList;
import org.jeecg.modules.marketing.entity.MarketingGroupIntegralManageRecord;
import org.jeecg.modules.marketing.service.*;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("after/marketingGroupIntegralManage")
@Log
public class AfterMarketingGroupIntegralManageController {


    @Autowired
    private IMarketingGroupIntegralManageService iMarketingGroupIntegralManageService;

    @Autowired
    private IMarketingGroupIntegralBaseSettingService iMarketingGroupIntegralBaseSettingService;

    @Autowired
    private IMarketingGroupIntegralManageListService iMarketingGroupIntegralManageListService;

    @Autowired
    private IMarketingGroupIntegralManageRecordService iMarketingGroupIntegralManageRecordService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingFourthIntegralRecordService iMarketingFourthIntegralRecordService;

    @Autowired
    @Lazy
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Autowired
    private RedisUtil redisUtil;




    /**
     * 拼购详情
     * @param marketingGroupIntegralManageId
     * @return
     */
    @RequestMapping("getMarketingGroupIntegralManageById")
    @ResponseBody
    public Result<?> getMarketingGroupIntegralManageById(String marketingGroupIntegralManageId,
                                                         @RequestAttribute(value = "memberId",required = false) String memberId){
        if(StringUtils.isBlank(marketingGroupIntegralManageId)){
            return Result.error("拼购管理id不能为空");
        }
        MarketingGroupIntegralBaseSetting marketingGroupIntegralBaseSetting=iMarketingGroupIntegralBaseSettingService.getOne(new LambdaQueryWrapper<MarketingGroupIntegralBaseSetting>()
                .eq(MarketingGroupIntegralBaseSetting::getStatus,"1"));
        Map<String,Object> resultMap= Maps.newHashMap();
        MarketingGroupIntegralManage marketingGroupIntegralManage=iMarketingGroupIntegralManageService.getById(marketingGroupIntegralManageId);
        resultMap.put("anotherName",marketingGroupIntegralManage.getAnotherName());
        resultMap.put("payment",marketingGroupIntegralManage.getPayment());
        resultMap.put("paymentAmount",marketingGroupIntegralManage.getPaymentAmount());
        resultMap.put("surfacePlot",marketingGroupIntegralManage.getSurfacePlot());
        resultMap.put("mainPicture",marketingGroupIntegralManage.getMainPicture());
        resultMap.put("id",marketingGroupIntegralManage.getId());
        resultMap.put("numberClusters",marketingGroupIntegralManage.getNumberClusters());
        resultMap.put("startTime",marketingGroupIntegralBaseSetting.getStartTime());
        resultMap.put("endTime",marketingGroupIntegralBaseSetting.getEndTime());
        //判断是否有拼购列表数据
        long marketingGroupIntegralManageListCount=iMarketingGroupIntegralManageListService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageList>().eq(MarketingGroupIntegralManageList::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId).eq(MarketingGroupIntegralManageList::getStatus,"0"));
        MarketingGroupIntegralManageList marketingGroupIntegralManageList=null;
        if(marketingGroupIntegralManageListCount==0){
            marketingGroupIntegralManageList=new MarketingGroupIntegralManageList()
                    .setAnotherName(marketingGroupIntegralManage.getAnotherName())
                    .setSurfacePlot(marketingGroupIntegralManage.getSurfacePlot())
                    .setMainPicture(marketingGroupIntegralManage.getMainPicture())
                    .setPayment(marketingGroupIntegralManage.getPayment())
                    .setPaymentAmount(marketingGroupIntegralManage.getPaymentAmount())
                    .setNumberClusters(marketingGroupIntegralManage.getNumberClusters())
                    .setMarketingGroupIntegralManageId(marketingGroupIntegralManageId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setStartTime(new Date());
            iMarketingGroupIntegralManageListService.save(marketingGroupIntegralManageList);
        }else{
            marketingGroupIntegralManageList=iMarketingGroupIntegralManageListService.getOne(new LambdaQueryWrapper<MarketingGroupIntegralManageList>().eq(MarketingGroupIntegralManageList::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId).eq(MarketingGroupIntegralManageList::getStatus,"0").last("limit 1"));
        }
        //查询拼购记录信息
        resultMap.put("marketingGroupIntegralManageRecordList",iMarketingGroupIntegralManageRecordService.getByMarketingGroupIntegralManageListId(marketingGroupIntegralManageList.getId()));
        resultMap.put("marketingGroupIntegralManageRecordCount",iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>().eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageListId,marketingGroupIntegralManageList.getId())));
        //判断时间段
        try {
            Date startTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingGroupIntegralBaseSetting.getStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingGroupIntegralBaseSetting.getEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(System.currentTimeMillis()>=startTime.getTime()&&System.currentTimeMillis()<=endTime.getTime()){
                Calendar calendar=Calendar.getInstance();
                //判断会员可以用次数
                long count=iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                        .eq(MarketingGroupIntegralManageRecord::getYear,calendar.get(Calendar.YEAR))
                        .eq(MarketingGroupIntegralManageRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                        .eq(MarketingGroupIntegralManageRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                        .eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId)
                        .eq(MarketingGroupIntegralManageRecord::getMemberListId,memberId));
                if(count<marketingGroupIntegralBaseSetting.getTimes().intValue()){
                    resultMap.put("status","1");
                    resultMap.put("surplusTimes",marketingGroupIntegralBaseSetting.getTimes().intValue()-count);
                    //判断会员的产品券是否足够
                    MemberList memberList=iMemberListService.getById(memberId);
                    resultMap.put("fourthIntegral",memberList.getFourthIntegral());
                }else{
                    resultMap.put("status","2");
                }
            }else{
                resultMap.put("status","0");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Result.ok(resultMap);
    }


    /**
     * 参与拼团
     * @param marketingGroupIntegralManageId
     * @param memberId
     * @return
     */
    @RequestMapping("spellGroup")
    @ResponseBody
    @Transactional
    public Result<?> spellGroup(String marketingGroupIntegralManageId, String captchaKey, String captchaContent,
                                @RequestAttribute(value = "memberId",required = false) String memberId, HttpServletRequest request){
        //参数判断
        if(StringUtils.isBlank(marketingGroupIntegralManageId)){
            return Result.error("拼购管理id不能为空");
        }

        if(StringUtils.isBlank(captchaKey)){
            return Result.error("验证码的key不能为空");
        }

        if(StringUtils.isBlank(captchaContent)){
            return Result.error("图片验证码的值不能为空");
        }

        String ip= IpUtils.getIpAddr(request);
        log.info("请求的IP地址："+ip );
        if(redisUtil.get("spellGroup"+ip)==null){
            //缓存验证码并设置过期时间
            redisUtil.set("spellGroup"+ip,ip,3);
        }else{
            return Result.error("同一个ip请求过于频繁！！！请稍后再试！！！");
        }

        if(redisUtil.get("spellGroup"+memberId)==null){
            //缓存验证码并设置过期时间
            redisUtil.set("spellGroup"+memberId,memberId,3);
        }else{
            return Result.error("同一个会员请求过于频繁！！！请稍后再试！！！");
        }
        //获取验证码
        Object captchaObject= redisUtil.get(captchaKey);
        if(captchaObject==null||!captchaObject.toString().toLowerCase().equals(captchaContent.toLowerCase())){
            return Result.error("验证码不正确");
        }

        //判断是否有在拼团成功没结算的数据
        if(iMarketingGroupIntegralManageRecordService.getDistributionRewards(memberId)!=0){
            log.info("会员数据还在结算，请等待，会员id："+memberId);
            return Result.error("系统正在努力处理中，请稍作等待，继续尝试，谢谢！！！");
        }

        MarketingGroupIntegralBaseSetting marketingGroupIntegralBaseSetting=iMarketingGroupIntegralBaseSettingService.getOne(new LambdaQueryWrapper<MarketingGroupIntegralBaseSetting>()
                .eq(MarketingGroupIntegralBaseSetting::getStatus,"1"));

        //判断是否有拼购列表数据
        MarketingGroupIntegralManage marketingGroupIntegralManage=iMarketingGroupIntegralManageService.getById(marketingGroupIntegralManageId);
        long marketingGroupIntegralManageListCount=iMarketingGroupIntegralManageListService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageList>()
                .eq(MarketingGroupIntegralManageList::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId)
                .eq(MarketingGroupIntegralManageList::getStatus,"0"));
        MarketingGroupIntegralManageList marketingGroupIntegralManageList=null;
        if(marketingGroupIntegralManageListCount==0){
            marketingGroupIntegralManageList=new MarketingGroupIntegralManageList()
                    .setAnotherName(marketingGroupIntegralManage.getAnotherName())
                    .setSurfacePlot(marketingGroupIntegralManage.getSurfacePlot())
                    .setMainPicture(marketingGroupIntegralManage.getMainPicture())
                    .setPayment(marketingGroupIntegralManage.getPayment())
                    .setPaymentAmount(marketingGroupIntegralManage.getPaymentAmount())
                    .setNumberClusters(marketingGroupIntegralManage.getNumberClusters())
                    .setMarketingGroupIntegralManageId(marketingGroupIntegralManageId)
                    .setSerialNumber(OrderNoUtils.getOrderNo())
                    .setStatus("0")
                    .setStartTime(new Date());
            iMarketingGroupIntegralManageListService.save(marketingGroupIntegralManageList);
        }else{
            marketingGroupIntegralManageList=iMarketingGroupIntegralManageListService.getOne(new LambdaQueryWrapper<MarketingGroupIntegralManageList>()
                    .eq(MarketingGroupIntegralManageList::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId)
                    .eq(MarketingGroupIntegralManageList::getStatus,"0")
                    .last("limit 1"));
        }

        if(marketingGroupIntegralManageList.getNumberClusters().intValue()<iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>().eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageListId,marketingGroupIntegralManageList.getId()))){
            log.info("拼团超过30人，会员id："+memberId);
            if(marketingGroupIntegralManageList.getStatus().equals("0")){
                marketingGroupIntegralManageList.setStatus("1");
                marketingGroupIntegralManageList.setEndTime(new Date());
                iMarketingGroupIntegralManageListService.saveOrUpdate(marketingGroupIntegralManageList);
            }
        }
        Calendar calendar=Calendar.getInstance();
        //判断时间段
        try {
            Date startTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingGroupIntegralBaseSetting.getStartTime(),"yyyy-MM-dd HH:mm:ss");
            Date endTime=DateUtils.parseDate(DateUtils.formatDate()+" "+marketingGroupIntegralBaseSetting.getEndTime(),"yyyy-MM-dd HH:mm:ss");
            if(System.currentTimeMillis()>=startTime.getTime()&&System.currentTimeMillis()<=endTime.getTime()){

                //判断会员可以用次数
                long count=iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                        .eq(MarketingGroupIntegralManageRecord::getYear,calendar.get(Calendar.YEAR))
                        .eq(MarketingGroupIntegralManageRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                        .eq(MarketingGroupIntegralManageRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                        .eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId)
                        .eq(MarketingGroupIntegralManageRecord::getMemberListId,memberId));
                if(count<marketingGroupIntegralBaseSetting.getTimes().intValue()){
                    //判断会员的产品券是否足够
                    MemberList memberList=iMemberListService.getById(memberId);
                    if(marketingGroupIntegralManageList.getPayment().equals("0")){
                        if(memberList.getFourthIntegral().subtract(marketingGroupIntegralManageList.getPaymentAmount()).doubleValue()>=0){
                            //生成拼购记录
                            MarketingGroupIntegralManageRecord marketingGroupIntegralManageRecord=new MarketingGroupIntegralManageRecord()
                                    .setAnotherName(marketingGroupIntegralManageList.getAnotherName())
                                    .setMainPicture(marketingGroupIntegralManageList.getMainPicture())
                                    .setPayment(marketingGroupIntegralManageList.getPayment())
                                    .setPaymentAmount(marketingGroupIntegralManageList.getPaymentAmount())
                                    .setSerialNumber(OrderNoUtils.getOrderNo())
                                    .setParticipationTime(new Date())
                                    .setPurchaseQuantity(new BigDecimal(1))
                                    .setMemberListId(memberId)
                                    .setMarketingGroupIntegralManageId(marketingGroupIntegralManage.getId())
                                    .setMarketingGroupIntegralManageListId(marketingGroupIntegralManageList.getId());
                            iMarketingGroupIntegralManageRecordService.save(marketingGroupIntegralManageRecord);
                            //减少会员第四积分
                            if(!iMarketingFourthIntegralRecordService.subtractFourthIntegral(memberId,marketingGroupIntegralManageList.getPaymentAmount(),marketingGroupIntegralManageRecord.getSerialNumber(),"2")){
                                //手动强制回滚事务，这里一定要第一时间处理
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return Result.error("系统正在努力处理中，请稍作等待，继续尝试，谢谢！！！");
                            }
                            long marketingGroupIntegralManageRecordCount=iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>().eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageListId,marketingGroupIntegralManageList.getId()));
                            marketingGroupIntegralManageRecord.setRewardNumerical(new BigDecimal(marketingGroupIntegralManageRecordCount));
                            if(marketingGroupIntegralManageRecordCount==1){
                                marketingGroupIntegralManageRecord.setIdentity("1");
                            }
                            iMarketingGroupIntegralManageRecordService.saveOrUpdate(marketingGroupIntegralManageRecord);
                            //判断是否升级
                            long manageRecordCount=iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>().eq(MarketingGroupIntegralManageRecord::getMemberListId,memberId));
                            if(manageRecordCount==40) {
                                log.info("新用户触发团队升级");
                                iMemberDistributionLevelService.teamUpgrade(memberId);
                            }
                            //判断用户是否中奖
                            long winningStateCount=iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                                    .eq(MarketingGroupIntegralManageRecord::getYear,calendar.get(Calendar.YEAR))
                                    .eq(MarketingGroupIntegralManageRecord::getMonth,calendar.get(Calendar.MONTH)+1)
                                    .eq(MarketingGroupIntegralManageRecord::getDay,calendar.get(Calendar.DAY_OF_MONTH))
                                    .eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId)
                                    .eq(MarketingGroupIntegralManageRecord::getMemberListId,memberId)
                                    .eq(MarketingGroupIntegralManageRecord::getWinningState,"1"));

                            calendar.add(Calendar.MONTH,-1);
                            long monthCount=iMarketingGroupIntegralManageRecordService.count(new LambdaQueryWrapper<MarketingGroupIntegralManageRecord>()
                                    .ge(MarketingGroupIntegralManageRecord::getCreateTime,calendar.getTime())
                                    .eq(MarketingGroupIntegralManageRecord::getMarketingGroupIntegralManageId,marketingGroupIntegralManageId)
                                    .eq(MarketingGroupIntegralManageRecord::getMemberListId,memberId)
                                    .eq(MarketingGroupIntegralManageRecord::getWinningState,"1"));

                            if(winningStateCount<2&&monthCount<50){
                                marketingGroupIntegralManageRecord.setWinningState("1");
                                log.info("中奖记录id：" + marketingGroupIntegralManageRecord.getId());
                                if (!iMarketingGroupIntegralManageRecordService.saveOrUpdate(marketingGroupIntegralManageRecord)) {
                                    //手动强制回滚事务，这里一定要第一时间处理
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    return Result.error("系统正在努力处理中，请稍作等待，继续尝试，谢谢！！！");
                                }
                            }
                            //判断是否成团
                            if(marketingGroupIntegralManageList.getStatus().equals("0")&&marketingGroupIntegralManageList.getNumberClusters().intValue()<=marketingGroupIntegralManageRecordCount){
                                marketingGroupIntegralManageList.setStatus("1");
                                marketingGroupIntegralManageList.setEndTime(new Date());
                                iMarketingGroupIntegralManageListService.saveOrUpdate(marketingGroupIntegralManageList);
                            }
                            return Result.ok(marketingGroupIntegralManageRecord);
                        }else{
                            return Result.error("会员产品券不足");
                        }
                    }
                }else{
                    return Result.error("会员无可用次数");
                }
            }else{
                return Result.error("非活动时间段！！！");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Result.error("拼购失败！！！");
    }
 }
