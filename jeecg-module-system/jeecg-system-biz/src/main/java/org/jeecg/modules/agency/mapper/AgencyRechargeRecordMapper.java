package org.jeecg.modules.agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.dto.AgencyRechargeRecordDTO;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.vo.AgencyRechargeRecordVO;

/**
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
public interface AgencyRechargeRecordMapper extends BaseMapper<AgencyRechargeRecord> {
    IPage<AgencyRechargeRecordDTO> getAgencyRechargeRecordList(Page<AgencyRechargeRecord> page, @Param("agencyRechargeRecordVO")AgencyRechargeRecordVO agencyRechargeRecordVO);

    IPage<AgencyRechargeRecordVO> queryPageList(Page<AgencyRechargeRecord> page,@Param("agencyRechargeRecordDTO") AgencyRechargeRecordDTO agencyRechargeRecordDTO);

}
