package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.agency.entity.AgencyAccountCapital;
import org.jeecg.modules.marketing.entity.MarketingEnterMoney;
import org.jeecg.modules.member.dto.MemberAccountCapitalDTO;
import org.jeecg.modules.member.entity.MemberAccountCapital;
import org.jeecg.modules.member.mapper.MemberAccountCapitalMapper;
import org.jeecg.modules.member.service.IMemberAccountCapitalService;
import org.jeecg.modules.member.vo.MemberAccountCapitalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 会员资金明细
 * @Author: jeecg-boot
 * @Date:   2019-12-17
 * @Version: V1.0
 */
@Service
public class MemberAccountCapitalServiceImpl extends ServiceImpl<MemberAccountCapitalMapper, MemberAccountCapital> implements IMemberAccountCapitalService {
    @Autowired(required = false)
    private  MemberAccountCapitalMapper memberAccountCapitalMapper;
    @Override
    public IPage<Map<String, Object>> findMemberAccountCapitalByMemberId(Page<Map<String, Object>> page, Map<String, Object> paramMap) {
        return baseMapper.findMemberAccountCapitalByMemberId(page,paramMap);
    }


    @Override
    public IPage<MemberAccountCapitalDTO> getMemberAccountCapitalList(Page<MemberAccountCapital> page, MemberAccountCapitalVO memberAccountCapitalVO){
        return memberAccountCapitalMapper.getMemberAccountCapitalList(page,memberAccountCapitalVO);
    }

    @Override
    public IPage<Map<String, Object>> findAccountCapitalByMemberId(Page<Map<String, Object>> page, HashMap<String, Object> map) {
        return baseMapper.findAccountCapitalByMemberId(page,map);
    }

    @Override
    public IPage<MemberAccountCapitalVO> getEnterMoney(Page<AgencyAccountCapital> page, MarketingEnterMoney marketingEnterMoney) {
        return baseMapper.getEnterMoney(page,marketingEnterMoney);
    }

    ;
}
