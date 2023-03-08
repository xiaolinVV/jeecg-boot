package org.jeecg.modules.marketing.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodList;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodSpecification;
import org.jeecg.modules.marketing.entity.MarketingGroupManage;
import org.jeecg.modules.marketing.entity.MarketingGroupRecord;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodListService;
import org.jeecg.modules.marketing.service.IMarketingGroupGoodSpecificationService;
import org.jeecg.modules.marketing.service.IMarketingGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingGroupRecordService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberWelfarePayments;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 拼团管理接口
 */
@Controller
@RequestMapping("after/marketingGroupManage")
public class AfterMarketingGroupManageController {

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMarketingGroupGoodListService iMarketingGroupGoodListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMarketingGroupManageService iMarketingGroupManageService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IMarketingGroupGoodSpecificationService iMarketingGroupGoodSpecificationService;

    @Autowired
    private IMarketingGroupRecordService iMarketingGroupRecordService;


    /**
     * 支付购买中奖拼团接口
     *
     * 张靠勤  2021-4-2
     *
     * @param password
     * @param marketingGroupGoodListId
     * @param specification
     * @param quantity
     * @param memberId
     * @return
     */
    @RequestMapping("paySuccessMarketingGroup")
    @ResponseBody
    @Transactional
    public Result<?> paySuccessMarketingGroup(String password,String marketingGroupGoodListId,String specification,BigDecimal quantity,
                                              @RequestAttribute(value = "memberId",required = false) String memberId){
        Result<Map<String,Object>> result=new Result<>();

        Map<String,Object> resultMap= Maps.newHashMap();

        MemberList memberList=iMemberListService.getById(memberId);
        if(StringUtils.isBlank(marketingGroupGoodListId)){
            return result.error500("拼团商品id不能为空");
        }
        //对于用户交易密码进行验证
        if(StringUtils.isBlank(password)){
            return result.error500("用户交易密码不能为空");
        }
        if(StringUtils.isBlank(memberList.getTransactionPassword())){
            return result.error500("交易密码没有设置");
        }
        if(!PasswordUtil.decrypt(memberList.getTransactionPassword(),password,PasswordUtil.Salt).equals(password)){
            return result.error500("交易密码输入错误");
        }

        MarketingGroupGoodList marketingGroupGoodList=iMarketingGroupGoodListService.getById(marketingGroupGoodListId);
        if(memberList.getWelfarePayments().subtract(marketingGroupGoodList.getTuxedoWelfarePayments().multiply(quantity)).doubleValue()<0){
            return result.error500("用户积分不足");
        }


        //判断是否生成拼团管理
        MarketingGroupManage marketingGroupManageOne=iMarketingGroupManageService.getOne(new LambdaQueryWrapper<MarketingGroupManage>()
                .eq(MarketingGroupManage::getMarketingGroupGoodListId,marketingGroupGoodListId)
                .eq(MarketingGroupManage::getStatus,"0")
                .ge(MarketingGroupManage::getEndTime, DateUtils.formatDateTime())
                .le(MarketingGroupManage::getStartTime,DateUtils.formatDateTime()));

        GoodList goodList=iGoodListService.getGoodListById(marketingGroupGoodList.getGoodListId());
        GoodSpecification goodSpecification=iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                .eq(GoodSpecification::getSpecification,specification)
                .eq(GoodSpecification::getGoodListId,marketingGroupGoodList.getGoodListId()));
        MarketingGroupGoodSpecification marketingGroupGoodSpecification=iMarketingGroupGoodSpecificationService.getOne(new LambdaQueryWrapper<MarketingGroupGoodSpecification>()
                .eq(MarketingGroupGoodSpecification::getMarketingGroupGoodListId,marketingGroupGoodListId)
                .eq(MarketingGroupGoodSpecification::getGoodSpecificationId,goodSpecification.getId()));

        if(marketingGroupGoodSpecification==null){
            return result.error500("拼团规格数据不存在");
        }
        //库存控制
        if(goodSpecification.getRepertory().subtract(quantity).intValue()<0){
            return result.error500("规格库存不足");
        }

        goodSpecification.setRepertory(goodSpecification.getRepertory().subtract(quantity));//扣除规格库存
        iGoodSpecificationService.saveOrUpdate(goodSpecification);
        boolean identity=false;

        //生成拼团管理
        if(marketingGroupManageOne==null){
            MarketingGroupManage marketingGroupManage=new MarketingGroupManage();
            marketingGroupManage.setGroupNo(OrderNoUtils.getOrderNo());//团号
            marketingGroupManage.setGoodNo(goodList.getGoodNo());//商品编号
            marketingGroupManage.setMainPicture(goodList.getMainPicture());//商品主图相对地址（以json的形式存储多张）
            marketingGroupManage.setGoodName(goodList.getGoodName());//商品名称
            marketingGroupManage.setMarketingGroupGoodListId(marketingGroupGoodListId);//拼团商品id
            marketingGroupManage.setMarketPrice(new BigDecimal(goodList.getMarketPrice()));//市场价
            marketingGroupManage.setSmallPrice(goodSpecification.getPrice());//销售价
            marketingGroupManage.setActivityPrice(marketingGroupGoodSpecification.getGroupPrice());//拼团价格
            marketingGroupManage.setStartTime(marketingGroupGoodList.getStartTime());//活动开始时间
            marketingGroupManage.setEndTime(marketingGroupGoodList.getEndTime());//活动结束时间
            marketingGroupManage.setNumberTuxedo(marketingGroupGoodList.getNumberTuxedo());//参团人数
            marketingGroupManage.setBuildTime(new Date());//建团时间
            marketingGroupManage.setReturnProportion(marketingGroupGoodList.getReturnProportion());//返还比例
            marketingGroupManage.setTuxedoWelfarePayments(marketingGroupGoodList.getTuxedoWelfarePayments());//参团积分
            iMarketingGroupManageService.save(marketingGroupManage);
            marketingGroupManageOne=marketingGroupManage;
            identity=true;
        }
        //生成拼团记录
        MarketingGroupRecord marketingGroupRecord=new MarketingGroupRecord();
        marketingGroupRecord.setMarketingGroupManageId(marketingGroupManageOne.getId());//拼团管理id
        marketingGroupRecord.setGroupRecordNo(OrderNoUtils.getOrderNo());//参团编号
        marketingGroupRecord.setTuxedoTime(new Date());//参团时间
        marketingGroupRecord.setMarketingGroupGoodSpecificationId(marketingGroupGoodSpecification.getId());//拼团商品规格
        marketingGroupRecord.setSpecification(goodSpecification.getSpecification());//规格名称，按照顺序逗号隔开
        marketingGroupRecord.setMarketPrice(new BigDecimal(goodList.getMarketPrice()));//市场价
        marketingGroupRecord.setSmallPrice(goodSpecification.getPrice());//销售价
        marketingGroupRecord.setActivityPrice(marketingGroupGoodSpecification.getGroupPrice());//活动价
        marketingGroupRecord.setMemberListId(memberId);//会员列表id
        marketingGroupRecord.setQuantity(quantity);//商品数量
        marketingGroupRecord.setGoodNo(goodList.getGoodNo());//商品编号
        marketingGroupRecord.setGoodName(goodList.getGoodName());//商品名称
        marketingGroupRecord.setMainPicture(goodList.getMainPicture());//商品主图相对地址（以json的形式存储多张）
        marketingGroupRecord.setTuxedoWelfarePayments(marketingGroupGoodList.getTuxedoWelfarePayments().multiply(quantity));//参团积分
        marketingGroupRecord.setMarketingGroupGoodListId(marketingGroupGoodListId);//拼团商品id
        if(identity){
            marketingGroupRecord.setIdentityStatus("1");//拼团身份；0：参与人；1：发起人
        }else{
            marketingGroupManageOne.setNumberClusters(marketingGroupManageOne.getNumberClusters().add(new BigDecimal(1)));
            iMarketingGroupManageService.saveOrUpdate(marketingGroupManageOne);
        }
        iMarketingGroupRecordService.save(marketingGroupRecord);

        resultMap.put("marketingGroupRecordId",marketingGroupRecord.getId());

        //扣除用户积分
        memberList.setWelfarePayments(memberList.getWelfarePayments().subtract(marketingGroupGoodList.getTuxedoWelfarePayments().multiply(quantity)));
        iMemberListService.saveOrUpdate(memberList);
        //生成积分记录
        MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
        memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
        memberWelfarePayments.setBargainPayments(marketingGroupGoodList.getTuxedoWelfarePayments().multiply(quantity));//交易福利金
        memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
        memberWelfarePayments.setWeType("0");//类型；0：支出；1：收入
        memberWelfarePayments.setWpExplain("拼团扣除[" + marketingGroupRecord.getId() + "]");//说明
        memberWelfarePayments.setGoAndCome("平台");//来源或者去向
        memberWelfarePayments.setBargainTime(new Date());//交易时间
        memberWelfarePayments.setOperator("系统");//操作人
        memberWelfarePayments.setMemberListId(memberId);//会员列表id
        memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
        memberWelfarePayments.setTradeNo(marketingGroupRecord.getId());//交易订单号
        memberWelfarePayments.setIsFreeze("0");//是否冻结福利金，0:不是；1：是冻结；2：不可用
        memberWelfarePayments.setTradeType("9");//会员福利金交易类型，字典member_welfare_deal_type；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：福利金付款；8：进店奖励；9：中奖拼团
        memberWelfarePayments.setTradeStatus("5");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
        iMemberWelfarePaymentsService.save(memberWelfarePayments);
        //判断是否拼团成功
        long marketingGroupRecorCount=iMarketingGroupRecordService.count(new LambdaQueryWrapper<MarketingGroupRecord>().eq(MarketingGroupRecord::getMarketingGroupManageId,marketingGroupManageOne.getId()));
        if(marketingGroupRecorCount==marketingGroupManageOne.getNumberTuxedo().intValue()){
            marketingGroupManageOne.setStatus("1");//拼团成功
            marketingGroupManageOne.setSuccessTime(new Date());//设置成团时间
            iMarketingGroupManageService.saveOrUpdate(marketingGroupManageOne);
            //分配奖励
            iMarketingGroupRecordService.randomMarketingGroupRecord(marketingGroupManageOne.getId());
        }

        result.setResult(resultMap);
        return result.success("中奖拼团支付成功！！！");
    }
}

