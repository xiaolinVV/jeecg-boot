package org.jeecg.modules.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.member.entity.MemberDistributionRecord;
import org.jeecg.modules.member.vo.MemberDistributionRecordVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 会员分销明细
 * @Author: jeecg-boot
 * @Date:   2020-01-07
 * @Version: V1.0
 */
public interface MemberDistributionRecordMapper extends BaseMapper<MemberDistributionRecord> {

    List<MemberDistributionRecordVO> findMemberDistributionRecordVOById(@Param("mId") String mId);

    /**
     * 查询余额记录下面的具体信息
     *
     * @param memberRechargeRecordId
     * @return
     */
    public List<Map<String,Object>> findMemberDistributionRecordByMrrId(@Param("memberRechargeRecordId") String memberRechargeRecordId);
}
