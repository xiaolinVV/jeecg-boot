package org.jeecg.modules.alliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.alliance.dto.AllianceRechargeRecordDTO;
import org.jeecg.modules.alliance.entity.AllianceRechargeRecord;
import org.jeecg.modules.alliance.vo.AllianceRechargeRecordVO;

/**
 * @Description: 加盟商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
public interface AllianceRechargeRecordMapper extends BaseMapper<AllianceRechargeRecord> {

    IPage<AllianceRechargeRecordVO> queryPageList(Page<AllianceRechargeRecord> page,@Param("allianceRechargeRecordDTO") AllianceRechargeRecordDTO allianceRechargeRecordDTO);
}
