package org.jeecg.modules.marketing.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.good.entity.GoodList;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.marketing.entity.MarketingRushGood;
import org.jeecg.modules.marketing.entity.MarketingRushGroup;
import org.jeecg.modules.marketing.entity.MarketingRushRecord;
import org.jeecg.modules.marketing.entity.MarketingRushType;
import org.jeecg.modules.marketing.service.IMarketingRushGoodService;
import org.jeecg.modules.marketing.service.IMarketingRushGroupService;
import org.jeecg.modules.marketing.service.IMarketingRushRecordService;
import org.jeecg.modules.marketing.service.IMarketingRushTypeService;
import org.jeecg.modules.member.entity.MemberThirdIntegral;
import org.jeecg.modules.member.service.IMemberDistributionLevelService;
import org.jeecg.modules.member.service.IMemberThirdIntegralService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;


@Slf4j
@Component
public class TestJob implements Job {

    @Autowired
    private IMemberThirdIntegralService iMemberThirdIntegralService;

    @Autowired
    private IMarketingRushGoodService iMarketingRushGoodService;

    @Autowired
    private IMarketingRushTypeService iMarketingRushTypeService;

    @Autowired
    private IMarketingRushGroupService iMarketingRushGroupService;

    @Autowired
    private IMarketingRushRecordService iMarketingRushRecordService;

    @Autowired
    private IGoodListService iGoodListService;

    @Autowired
    private IGoodSpecificationService iGoodSpecificationService;

    @Autowired
    private IMemberDistributionLevelService iMemberDistributionLevelService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("转第三积分");
        while (true) {
            if(!kjhkjhkljh()){
                break;
            }
        }
    }


    @Transactional
    public boolean kjhkjhkljh(){
        MemberThirdIntegral memberThirdIntegral = iMemberThirdIntegralService.getOne(new LambdaQueryWrapper<MemberThirdIntegral>().gt(MemberThirdIntegral::getIntegral, 0).orderByDesc(MemberThirdIntegral::getCreateTime).last("limit 1"));
        if (memberThirdIntegral == null) {
            return false;
        }
        String marketingRushGoodId = "";
        String specification = "";
        BigDecimal quantity = new BigDecimal(1);
        String memberShippingAddressId = "";
        String message = "";
        BigDecimal price=new BigDecimal(0);
        //99
        if (memberThirdIntegral.getMarketingGroupIntegralManageId().equals("78844a3f743566f977b97d73c1918dfa")) {
            marketingRushGoodId = "57e07e04f23154755b42c7c290645db9";
            specification = "无";
            quantity = new BigDecimal(1);
            memberShippingAddressId = "";
            message = "";
            price=new BigDecimal(99);

        }
        //299
        if (memberThirdIntegral.getMarketingGroupIntegralManageId().equals("31c343d362aec583003e9384c79b6216")) {
            marketingRushGoodId = "8b981494b7401409f63c65688f0efe82";
            specification = "无";
            quantity = new BigDecimal(1);
            memberShippingAddressId = "";
            message = "";
            price=new BigDecimal(299);
        }

        //499
        if (memberThirdIntegral.getMarketingGroupIntegralManageId().equals("25277cc6a22aa54f58fd3a6a969a14a6")) {
            marketingRushGoodId = "9dd6c188b609cd76bca57e57b188d1a8";
            specification = "无";
            quantity = new BigDecimal(1);
            memberShippingAddressId = "";
            message = "";
            price=new BigDecimal(499);
        }
        if (StringUtils.isNotBlank(marketingRushGoodId)) {
            MarketingRushGood marketingRushGood = iMarketingRushGoodService.getById(marketingRushGoodId);
            if(marketingRushGood.getPrice().doubleValue()!=price.doubleValue()){
                return false;
            }
            MarketingRushType marketingRushType = iMarketingRushTypeService.getById(marketingRushGood.getMarketingRushTypeId());
            //生成抢购记录
            MarketingRushRecord marketingRushRecord = new MarketingRushRecord();
            marketingRushRecord.setMarketingRushTypeId(marketingRushType.getId());
            marketingRushRecord.setMemberListId(memberThirdIntegral.getMemberListId());
            marketingRushRecord.setSerialNumber(OrderNoUtils.getOrderNo());
            marketingRushRecord.setMarketingRushGoodId(marketingRushGoodId);
            GoodList goodList = iGoodListService.getById(marketingRushGood.getGoodListId());
            //获取商品
            GoodSpecification goodSpecification = iGoodSpecificationService.getOne(new LambdaQueryWrapper<GoodSpecification>()
                    .eq(GoodSpecification::getGoodListId, goodList.getId())
                    .eq(GoodSpecification::getSpecification, specification)
                    .last("limit 1"));
            marketingRushRecord.setGoodNo(goodList.getGoodNo());
            marketingRushRecord.setMainPicture(goodList.getMainPicture());
            marketingRushRecord.setGoodName(goodList.getGoodName());
            marketingRushRecord.setGoodSpecificationId(goodSpecification.getId());
            marketingRushRecord.setSpecification(goodSpecification.getSpecification());
            marketingRushRecord.setAmount(quantity);
            marketingRushRecord.setPrice(marketingRushGood.getPrice().multiply(quantity));
            marketingRushRecord.setStatus("1");
            marketingRushRecord.setDistributionRewards("1");
            marketingRushRecord.setClassificationReward("1");
            marketingRushRecord.setMemberShippingAddressId(memberShippingAddressId);
            marketingRushRecord.setMessage(message);
            if(!iMarketingRushRecordService.save(marketingRushRecord)){
                //手动强制回滚事务，这里一定要第一时间处理
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

            //形成分组数据
            MarketingRushGroup marketingRushGroup = iMarketingRushGroupService.getOne(new LambdaQueryWrapper<MarketingRushGroup>()
                    .eq(MarketingRushGroup::getMemberListId, memberThirdIntegral.getMemberListId())
                    .eq(MarketingRushGroup::getMarketingRushTypeId, marketingRushType.getId())
                    .eq(MarketingRushGroup::getConsignmentStatus, "0")
                    .orderByDesc(MarketingRushGroup::getCreateTime));
            if (marketingRushGroup == null) {
                marketingRushGroup = new MarketingRushGroup();
                marketingRushGroup.setMarketingRushTypeId(marketingRushType.getId());
                marketingRushGroup.setTransformationThreshold(marketingRushType.getTransformationThreshold());
                marketingRushGroup.setCanConsignment(marketingRushType.getCanConsignment());
                marketingRushGroup.setMemberListId(memberThirdIntegral.getMemberListId());
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
            long marketingRushGroupCount = iMarketingRushRecordService.count(new LambdaQueryWrapper<MarketingRushRecord>().eq(MarketingRushRecord::getMarketingRushGroupId, marketingRushGroup.getId()));
            if (marketingRushGroupCount >= marketingRushGroup.getTransformationThreshold().intValue()) {
                marketingRushGroup.setConsignmentStatus("1");
                if(!iMarketingRushGroupService.saveOrUpdate(marketingRushGroup)){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            if (memberThirdIntegral.getIntegral().doubleValue() >= marketingRushRecord.getPrice().doubleValue()) {
                if(!iMemberThirdIntegralService.subtractThirdIntegral(memberThirdIntegral.getMarketingGroupIntegralManageId(), memberThirdIntegral.getMemberListId(), marketingRushRecord.getPrice(), marketingRushRecord.getSerialNumber(), "5")){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                if(!iMemberThirdIntegralService.subtractThirdIntegral(memberThirdIntegral.getMarketingGroupIntegralManageId(), memberThirdIntegral.getMemberListId(), memberThirdIntegral.getIntegral(), marketingRushRecord.getSerialNumber(), "5")){
                    //手动强制回滚事务，这里一定要第一时间处理
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            long upgradeRushCount = iMarketingRushRecordService.count(new LambdaQueryWrapper<MarketingRushRecord>()
                    .eq(MarketingRushRecord::getMemberListId, memberThirdIntegral.getMemberListId()));
            if (upgradeRushCount == 25) {
                log.info("新用户触发团队升级");
                iMemberDistributionLevelService.teamRushUpgrade(memberThirdIntegral.getMemberListId());
            }
        }else{
            log.info("marketingRushGoodId  数据为空");
            return false;
        }
        return true;
    }
}
