package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MemberDistributionDTO;
import org.jeecg.modules.marketing.mapper.MarketingStoreDistributionSettingMapper;
import org.jeecg.modules.member.entity.MemberDistributionRecord;
import org.jeecg.modules.member.mapper.MemberDistributionRecordMapper;
import org.jeecg.modules.member.service.IMemberDistributionRecordService;
import org.jeecg.modules.member.vo.MemberDistributionRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员分销明细
 * @Author: jeecg-boot
 * @Date:   2020-01-07
 * @Version: V1.0
 */
@Service
public class MemberDistributionRecordServiceImpl extends ServiceImpl<MemberDistributionRecordMapper, MemberDistributionRecord> implements IMemberDistributionRecordService {
    @Autowired(required = false)
    private MarketingStoreDistributionSettingMapper marketingStoreDistributionSettingMapper;
    @Override
    public List<MemberDistributionRecordVO> findMemberDistributionRecordVOById(String mId) {
        return baseMapper.findMemberDistributionRecordVOById(mId);
    }

    @Override
    public List<Map<String, Object>> findMemberDistributionRecordByMrrId(String memberRechargeRecordId) {
        return baseMapper.findMemberDistributionRecordByMrrId(memberRechargeRecordId);
    }

    @Override
    public List<MemberDistributionDTO> findById(String mId) {
        return marketingStoreDistributionSettingMapper.findById(mId);
    }
}
