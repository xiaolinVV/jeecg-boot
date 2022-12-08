package org.jeecg.modules.agency.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.agency.dto.AgencyRechargeRecordDTO;
import org.jeecg.modules.agency.entity.AgencyRechargeRecord;
import org.jeecg.modules.agency.vo.AgencyRechargeRecordVO;

/**
 * @Description: 代理列表
 * @Author: jeecg-boot
 * @Date:   2019-12-21
 * @Version: V1.0
 */
public interface IAgencyRechargeRecordService extends IService<AgencyRechargeRecord> {
    IPage<AgencyRechargeRecordDTO> getAgencyRechargeRecordList(Page<AgencyRechargeRecord> page, AgencyRechargeRecordVO agencyRechargeRecordVO);

    Result<AgencyRechargeRecordDTO> cashOut(JSONObject jsonObject);

    IPage<AgencyRechargeRecordVO> queryPageList(Page<AgencyRechargeRecord> page, AgencyRechargeRecordDTO agencyRechargeRecordDTO);

}
