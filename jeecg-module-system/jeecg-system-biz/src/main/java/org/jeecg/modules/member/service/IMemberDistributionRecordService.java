package org.jeecg.modules.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MemberDistributionDTO;
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
public interface IMemberDistributionRecordService extends IService<MemberDistributionRecord> {

    List<MemberDistributionRecordVO> findMemberDistributionRecordVOById(String mId);

    /**
     * 查询余额记录下面的具体信息
     *
     * @param memberRechargeRecordId
     * @return
     */
    public List<Map<String,Object>> findMemberDistributionRecordByMrrId(String memberRechargeRecordId);

    List<MemberDistributionDTO> findById(String mId);
}
