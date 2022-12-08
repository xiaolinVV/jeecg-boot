package org.jeecg.modules.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.member.dto.MemberRechargeRecordDTO;
import org.jeecg.modules.member.entity.MemberRechargeRecord;
import org.jeecg.modules.member.mapper.MemberRechargeRecordMapper;
import org.jeecg.modules.member.service.IMemberRechargeRecordService;
import org.jeecg.modules.member.vo.MemberRechargeRecordVO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员余额明细
 * @Author: jeecg-boot
 * @Date:   2019-12-24
 * @Version: V1.0
 */
@Service
public class MemberRechargeRecordServiceImpl extends ServiceImpl<MemberRechargeRecordMapper, MemberRechargeRecord> implements IMemberRechargeRecordService {

    @Override
    public int findMemberRechargeRecordProtomerCount(String memberId) {
        return baseMapper.findMemberRechargeRecordProtomerCount(memberId);
    }

    @Override
    public IPage<Map<String, Object>> findMemberRechargeRecordProtomerList(Page<Map<String, Object>> page, Map<String, Object> objectMap) {
        return baseMapper.findMemberRechargeRecordProtomerList(page,objectMap);
    }

    @Override
    public int findMemberRechargeRecordDepositCount(String memberId) {
        return baseMapper.findMemberRechargeRecordDepositCount(memberId);
    }

    @Override
    public List<Map<String, Object>> findMemberRechargeRecordByMemberWithdrawDepositId(String memberWithdrawDepositId) {
        return baseMapper.findMemberRechargeRecordByMemberWithdrawDepositId(memberWithdrawDepositId);
    }

    @Override
    public Double findMemberRechargeRecordSum(Map<String, Object> objectMap) {
        return baseMapper.findMemberRechargeRecordSum(objectMap);
    }

    @Override
    public Double findMemberRechargeRecordWithdrawSum(Map<String, Object> objectMap) {
        return baseMapper.findMemberRechargeRecordWithdrawSum(objectMap);
    }

    @Override
    public IPage<Map<String, Object>> findMemberRechargeRecordPage(Page<MemberRechargeRecord> page, HashMap<String, Object> map) {
        return baseMapper.findMemberRechargeRecordPage(page,map);
    }

    @Override
    public IPage<MemberRechargeRecordVO> queryPageList(Page<MemberRechargeRecord> page, MemberRechargeRecordDTO memberRechargeRecordDTO) {
        return baseMapper.queryPageList(page,memberRechargeRecordDTO);
    }

}
