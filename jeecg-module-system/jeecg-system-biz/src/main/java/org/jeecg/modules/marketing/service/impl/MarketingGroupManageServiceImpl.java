package org.jeecg.modules.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.entity.MarketingGroupBaseSetting;
import org.jeecg.modules.marketing.entity.MarketingGroupManage;
import org.jeecg.modules.marketing.entity.MarketingGroupRecord;
import org.jeecg.modules.marketing.mapper.MarketingGroupManageMapper;
import org.jeecg.modules.marketing.service.IMarketingGroupBaseSettingService;
import org.jeecg.modules.marketing.service.IMarketingGroupManageService;
import org.jeecg.modules.marketing.service.IMarketingGroupRecordService;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberWelfarePayments;
import org.jeecg.modules.member.service.IMemberListService;
import org.jeecg.modules.member.service.IMemberWelfarePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团管理
 * @Author: jeecg-boot
 * @Date:   2021-03-23
 * @Version: V1.0
 */
@Service
@Log
public class MarketingGroupManageServiceImpl extends ServiceImpl<MarketingGroupManageMapper, MarketingGroupManage> implements IMarketingGroupManageService {


    @Autowired
    private IMarketingGroupRecordService iMarketingGroupRecordService;

    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberWelfarePaymentsService iMemberWelfarePaymentsService;

    @Autowired
    private IMarketingGroupBaseSettingService iMarketingGroupBaseSettingService;


    @Override
    public void closeMarketingGroupManage() {
        this.list(new LambdaQueryWrapper<MarketingGroupManage>()
                .eq(MarketingGroupManage::getStatus,"0")
                .le(MarketingGroupManage::getEndTime,new Date())
                .last("limit 50")).forEach(mgm->{
                    this.closeMarketingGroupRecord(mgm);
        });
    }

    @Transactional
    public void closeMarketingGroupRecord(MarketingGroupManage marketingGroupManage){
        log.info("超时中奖拼团失败id："+marketingGroupManage.getId());
        marketingGroupManage.setStatus("2");//拼团失败
        marketingGroupManage.setTimeOutPeriod(new Date());//超时时间
        this.saveOrUpdate(marketingGroupManage);
        //退回拼团积分
        iMarketingGroupRecordService.list(new LambdaQueryWrapper<MarketingGroupRecord>()
                .eq(MarketingGroupRecord::getMarketingGroupManageId,marketingGroupManage.getId())
                .eq(MarketingGroupRecord::getStatus,"0")
                .eq(MarketingGroupRecord::getRewardStatus,"0")).forEach(mgr->{
                    //拼团失败退回积分
                    iMemberWelfarePaymentsService.addWelfarePayments(mgr.getMemberListId(),mgr.getTuxedoWelfarePayments(),"15",OrderNoUtils.getOrderNo(),"");
                });
    }

    @Override
    public List<Map<String, Object>> getMarketingGroupManageByGood() {
        return baseMapper.getMarketingGroupManageByGood();
    }

    @Override
    @Transactional
    public void successMarketingGroupManage(String marketingGroupManageId, String marketingGroupJson) {
        MarketingGroupManage marketingGroupManage=this.getById(marketingGroupManageId);
        marketingGroupManage.setStatus("1");
        marketingGroupManage.setSuccessTime(new Date());
        this.saveOrUpdate(marketingGroupManage);
        //规则信息
        MarketingGroupBaseSetting marketingGroupBaseSetting=iMarketingGroupBaseSettingService.getOne(new LambdaQueryWrapper<>());

        //解析json数据
        JSON.parseArray(marketingGroupJson).forEach(m->{
            JSONObject mgrJsonObject=(JSONObject)m;
            if(mgrJsonObject.get("winning").toString().equals("1")){
                //获取中奖记录
                MarketingGroupRecord marketingGroupRecord=iMarketingGroupRecordService.getById(mgrJsonObject.get("marketingGroupRecordId").toString());
                //设置中奖数据
                marketingGroupRecord.setRewardType("1");//中奖类型；0：积分；1：购买资格
                marketingGroupRecord.setRewardStatus("1");//中奖状态；0：未中奖；1：已中奖
                marketingGroupRecord.setRewardNumber(marketingGroupRecord.getQuantity());
                marketingGroupRecord.setDeadline(marketingGroupBaseSetting.getValidity());
                Calendar calendar=Calendar.getInstance();
                marketingGroupRecord.setBuyStartTime(calendar.getTime());
                calendar.add(Calendar.HOUR,marketingGroupRecord.getDeadline().intValue());
                marketingGroupRecord.setBuyEndTime(calendar.getTime());
                iMarketingGroupRecordService.saveOrUpdate(marketingGroupRecord);
            }
        });
          JSONArray jsonArray= JSON.parseArray(marketingGroupJson);
        //分配奖励的积分
        for(int i=0;i<jsonArray.size();i++){
            JSONObject mgrJsonObject=(JSONObject)jsonArray.get(i);
            //如果是中奖数据就跳过
            if(mgrJsonObject.get("winning").toString().equals("1")){
                continue;
            }
            MarketingGroupRecord mgr=iMarketingGroupRecordService.getById(mgrJsonObject.get("marketingGroupRecordId").toString());
            //如果是最后一条就赋值全部
            mgr.setRewardNumber(new BigDecimal(mgrJsonObject.get("rewardNumber").toString()));
            iMarketingGroupRecordService.saveOrUpdate(mgr);
            //加入用户积分
            MemberList memberList = iMemberListService.getById(mgr.getMemberListId());
            memberList.setWelfarePayments(memberList.getWelfarePayments().add(mgr.getRewardNumber()));
            iMemberListService.saveOrUpdate(memberList);
            //生成积分记录
            MemberWelfarePayments memberWelfarePayments = new MemberWelfarePayments();
            memberWelfarePayments.setSerialNumber(OrderNoUtils.getOrderNo());//流水号
            memberWelfarePayments.setBargainPayments(mgr.getRewardNumber());//交易福利金
            memberWelfarePayments.setWelfarePayments(memberList.getWelfarePayments());//账户福利金
            memberWelfarePayments.setWeType("1");//类型；0：支出；1：收入
            memberWelfarePayments.setWpExplain("拼团奖励[" + mgr.getId() + "]");//说明
            memberWelfarePayments.setGoAndCome("平台");//来源或者去向
            memberWelfarePayments.setBargainTime(new Date());//交易时间
            memberWelfarePayments.setOperator("系统");//操作人
            memberWelfarePayments.setMemberListId(memberList.getId());//会员列表id
            memberWelfarePayments.setIsPlatform("1");//0：店铺；1：平台；2：会员
            memberWelfarePayments.setTradeNo(mgr.getId());//交易订单号
            memberWelfarePayments.setIsFreeze("0");//是否冻结福利金，0:不是；1：是冻结；2：不可用
            memberWelfarePayments.setTradeType("10");//会员福利金交易类型，字典member_welfare_deal_type；0：订单赠送；1：礼包赠送；2：平台赠送；3：店铺赠送；4：好友赠送；5：赠送好友；6：订单交易；7：福利金付款；8：进店奖励；9：中奖拼团
            memberWelfarePayments.setTradeStatus("5");//交易状态：0：未支付；1：进行中；2：待结算：3：待打款；4：待退款；5：交易完成；6：已退款；7：交易关闭；数据字典：trade_status
            iMemberWelfarePaymentsService.save(memberWelfarePayments);
        }
    }
}
