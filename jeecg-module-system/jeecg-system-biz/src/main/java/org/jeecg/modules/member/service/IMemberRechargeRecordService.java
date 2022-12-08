package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.member.dto.MemberRechargeRecordDTO;
import org.jeecg.modules.member.entity.MemberRechargeRecord;
import org.jeecg.modules.member.vo.MemberRechargeRecordVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 会员余额明细
 * @Author: jeecg-boot
 * @Date:   2019-12-24
 * @Version: V1.0
 */
public interface IMemberRechargeRecordService extends IService<MemberRechargeRecord> {


    /**
     * 查询佣金数量
     *
     * @param memberId
     * @return
     */
    public int findMemberRechargeRecordProtomerCount(String memberId);

    /**
     * 查询佣金列表
     *
     * @param objectMap
     * @return
     */
    public IPage<Map<String,Object>> findMemberRechargeRecordProtomerList(Page<Map<String,Object>> page,Map<String,Object> objectMap);

    /**
     * 查询提现数量
     *
     * @param memberId
     * @return
     */
    public int findMemberRechargeRecordDepositCount(String memberId);

    /**
     * 查询佣金列表根据提现id
     * @param memberWithdrawDepositId
     * @return
     */
    public List<Map<String,Object>> findMemberRechargeRecordByMemberWithdrawDepositId(String memberWithdrawDepositId);

    /**
     * 查询佣金的总和
     *
     * @param objectMap
     * @return
     */
    public Double findMemberRechargeRecordSum( Map<String,Object> objectMap);

    /**
     * 查询佣金提现的总和
     *
     * @param objectMap
     * @return
     */
    public Double findMemberRechargeRecordWithdrawSum(Map<String,Object> objectMap);

    IPage<Map<String, Object>> findMemberRechargeRecordPage(Page<MemberRechargeRecord> page, HashMap<String, Object> map);

    IPage<MemberRechargeRecordVO> queryPageList(Page<MemberRechargeRecord> page, MemberRechargeRecordDTO memberRechargeRecordDTO);
}
