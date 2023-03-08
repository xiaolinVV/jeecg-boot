package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.OrderNoUtils;
import org.jeecg.modules.marketing.dto.MarketingEnterMoneyDTO;
import org.jeecg.modules.marketing.entity.MarketingEnterMoney;
import org.jeecg.modules.marketing.mapper.MarketingEnterMoneyMapper;
import org.jeecg.modules.marketing.service.IMarketingEnterMoneyService;
import org.jeecg.modules.marketing.vo.MarketingEnterMoneyVO;
import org.jeecg.modules.member.entity.MemberDesignation;
import org.jeecg.modules.member.entity.MemberDesignationGroup;
import org.jeecg.modules.member.entity.MemberDesignationMemberList;
import org.jeecg.modules.member.service.IMemberDesignationGroupService;
import org.jeecg.modules.member.service.IMemberDesignationMemberListService;
import org.jeecg.modules.member.service.IMemberDesignationService;
import org.jeecg.modules.member.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @Description: 称号出账资金
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class MarketingEnterMoneyServiceImpl extends ServiceImpl<MarketingEnterMoneyMapper, MarketingEnterMoney> implements IMarketingEnterMoneyService {
    @Autowired
    private IMemberListService iMemberListService;

    @Autowired
    private IMemberDesignationService iMemberDesignationService;

    @Autowired
    private IMemberDesignationGroupService iMemberDesignationGroupService;

    @Autowired
    private IMemberDesignationMemberListService iMemberDesignationMemberListService ;

    @Override
    public IPage<MarketingEnterMoneyVO> queryPageList(Page<MarketingEnterMoney> page, MarketingEnterMoneyDTO marketingEnterMoneyDTO) {
        return baseMapper.queryPageList(page,marketingEnterMoneyDTO);
    }

    @Override
    public void marketingGiftBagShareProfit() {
        //查出称号团队
        if (iMemberDesignationGroupService.count(new LambdaQueryWrapper<>())>0){

            List<MemberDesignationGroup> memberDesignationGroupList = iMemberDesignationGroupService.list(new LambdaQueryWrapper<>());
            memberDesignationGroupList.forEach(mdgl->{

                List<MemberDesignation> memberDesignationList = iMemberDesignationService.list(new LambdaQueryWrapper<MemberDesignation>()
                        .eq(MemberDesignation::getMemberDesignationGroupId,mdgl.getId())
                        .eq(MemberDesignation::getStatus, "1")
                        .gt(MemberDesignation::getSort,"0")
                        .orderByAsc(MemberDesignation::getSort)
                );
                memberDesignationList.forEach(mdl->{
                    if (mdl.getBalance().doubleValue()>0){
                        List<MemberDesignationMemberList> memberDesignationMemberLists=iMemberDesignationMemberListService.list(new LambdaQueryWrapper<MemberDesignationMemberList>().eq(MemberDesignationMemberList::getMemberDesignationId,mdl.getId()));
                        if (memberDesignationMemberLists.size()>0){
                            BigDecimal memberCount=new BigDecimal(0);

                            for (MemberDesignationMemberList memberDesignationCount:memberDesignationMemberLists) {
                                memberCount=memberCount.add(memberDesignationCount.getBuyCount());
                            }

                            BigDecimal divide = mdl.getBalance().divide(memberCount,2, RoundingMode.DOWN);

                            MarketingEnterMoney marketingEnterMoney = new MarketingEnterMoney()
                                    .setMemberDesignationId(mdl.getId())
                                    .setTradeType("0")
                                    .setEnterMoney(mdl.getBalance())
                                    .setBalance(new BigDecimal(0))
                                    .setTradeTime(new Date())
                                    .setOrderNo(OrderNoUtils.getOrderNo());

                            this.save(marketingEnterMoney);
                            log.info(" 称号分红 !  出账记录:" + marketingEnterMoney.getOrderNo()+",   分配金额:");
                            mdl.setEnterMoney(mdl.getEnterMoney().add(marketingEnterMoney.getEnterMoney()));
                            mdl.setBalance(new BigDecimal(0));
                            iMemberDesignationService.saveOrUpdate(mdl);
                            memberDesignationMemberLists.forEach(mdld->{
                                log.info(divide.multiply(mdld.getBuyCount())+":"+mdld.getMemberListId());
                                int i=mdld.getBuyCount().intValue();
                                while (i>0) {
                                    iMemberListService.addBlance(mdld.getMemberListId(), divide, marketingEnterMoney.getOrderNo(), "8");
                                    i--;
                                }
                            });
                        }
                    }

                });
            });

        }

    }
}
