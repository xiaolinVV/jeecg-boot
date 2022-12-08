package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.member.entity.MemberBankCard;
import org.jeecg.modules.member.entity.MemberList;
import org.jeecg.modules.member.entity.MemberRechargeRecord;
import org.jeecg.modules.member.entity.MemberWithdrawDeposit;
import org.jeecg.modules.member.vo.MemberWithdrawDepositVO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 会员提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
public interface IMemberWithdrawDepositService extends IService<MemberWithdrawDeposit> {
    IPage<MemberWithdrawDepositVO> getMemberWithdrawDeposit(Page<MemberWithdrawDeposit> page, MemberWithdrawDepositVO memberWithdrawDepositVO);

    /**
     * 根据会员id进行提现
     *
     * @param memberId
     */
    public String addWithdrawDeposit(String memberId,BigDecimal money);

    /**
     * 分页查询提现记录
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>> findMemberWithdrawDepositByMemberId(Page<Map<String,Object>> page,Map<String,Object> paramMap);

    /**
     * 查询提现详情
     * @param id
     * @return
     */
    public Map<String,Object> findMemberWithdrawDepositById(String id);

    Result<MemberWithdrawDeposit> audit(MemberWithdrawDepositVO memberWithdrawDepositVO);

    Result<String> remit(MemberWithdrawDepositVO memberWithdrawDepositVO);

    boolean withdrawalCard(BigDecimal money, MemberList member, MemberBankCard bankCard);

    IPage<Map<String,Object>> findMemberWithdrawDepositPageByMemberId(Page<Map<String,Object>> page, Map<String, Object> paramObjectMap);

    Map<String,Object> findMemberWithdrawDepositParticulars(String id);

    /**
     * 微信提现后(数据更改)
     * @param memberList
     * @param memberRechargeRecord
     * @param memberWithdrawDeposit
     */
     void weChatWithdrawal(MemberList memberList, MemberRechargeRecord memberRechargeRecord, MemberWithdrawDeposit memberWithdrawDeposit);
    /**
     * 微信(银行卡)提现后(数据更改)
     * @param memberList
     * @param memberRechargeRecord
     * @param memberWithdrawDeposit
     */
     void  postWXPayToBC(MemberList memberList,MemberRechargeRecord memberRechargeRecord,MemberWithdrawDeposit memberWithdrawDeposit);

    boolean memberWithdrawDepositAdd(BigDecimal money, MemberList member, MemberBankCard bankCard);
}
