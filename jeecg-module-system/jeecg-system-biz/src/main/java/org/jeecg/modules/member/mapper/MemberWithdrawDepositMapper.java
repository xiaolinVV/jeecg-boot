package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.dto.MemberWithdrawDepositDTO;
import org.jeecg.modules.member.entity.MemberWithdrawDeposit;
import org.jeecg.modules.member.vo.MemberWithdrawDepositVO;

import java.util.Map;

/**
 * @Description: 会员提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
public interface MemberWithdrawDepositMapper extends BaseMapper<MemberWithdrawDeposit> {
    IPage<MemberWithdrawDepositVO> getMemberWithdrawDeposit(Page<MemberWithdrawDeposit> page, @Param("memberWithdrawDepositVO")MemberWithdrawDepositVO memberWithdrawDepositVO);

    /**
     * 分页查询提现记录
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> findMemberWithdrawDepositByMemberId(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);


    /**
     * 查询提现详情
     * @param id
     * @return
     */
    public Map<String,Object> findMemberWithdrawDepositById(@Param("id") String id);

    IPage<Map<String,Object>> findMemberWithdrawDepositPageByMemberId(Page<Map<String,Object>> page,@Param("paramObjectMap") Map<String, Object> paramObjectMap);

    Map<String,Object> findMemberWithdrawDepositParticulars(@Param("id") String id);
}
