package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface MemberRechargeRecordMapper extends BaseMapper<MemberRechargeRecord> {

    /**
     * 查询佣金数量
     *
     * @param memberId
     * @return
     */
    public int findMemberRechargeRecordProtomerCount(@Param("memberId") String memberId);


    /**
     * 查询佣金列表
     *
     * @param objectMap
     * @return
     */
    public IPage<Map<String,Object>> findMemberRechargeRecordProtomerList(Page<Map<String,Object>> page, @Param("objectMap") Map<String,Object> objectMap);

    /**
     * 查询提现数量
     *
     * @param memberId
     * @return
     */
    public int findMemberRechargeRecordDepositCount(@Param("memberId") String memberId);

    /**
     * 查询佣金列表根据提现id
     * @param memberWithdrawDepositId
     * @return
     */
    public List<Map<String,Object>> findMemberRechargeRecordByMemberWithdrawDepositId(@Param("memberWithdrawDepositId") String memberWithdrawDepositId);

    /**
     * 查询佣金的总和
     *
     * @param objectMap
     * @return
     */
    public Double findMemberRechargeRecordSum(@Param("objectMap") Map<String,Object> objectMap);

    /**
     * 查询佣金提现的总和
     *
     * @param objectMap
     * @return
     */
    public Double findMemberRechargeRecordWithdrawSum(@Param("objectMap") Map<String,Object> objectMap);

    IPage<Map<String,Object>> findMemberRechargeRecordPage(Page<MemberRechargeRecord> page,@Param("map") HashMap<String, Object> map);

    IPage<MemberRechargeRecordVO> queryPageList(Page<MemberRechargeRecord> page,@Param("memberRechargeRecordDTO") MemberRechargeRecordDTO memberRechargeRecordDTO);
}
