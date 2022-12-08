package org.jeecg.modules.alliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.alliance.dto.AllianceRechargeRecordDTO;
import org.jeecg.modules.alliance.entity.AllianceRechargeRecord;
import org.jeecg.modules.alliance.mapper.AllianceRechargeRecordMapper;
import org.jeecg.modules.alliance.service.IAllianceRechargeRecordService;
import org.jeecg.modules.alliance.vo.AllianceRechargeRecordVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 加盟商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-05-17
 * @Version: V1.0
 */
@Service
public class AllianceRechargeRecordServiceImpl extends ServiceImpl<AllianceRechargeRecordMapper, AllianceRechargeRecord> implements IAllianceRechargeRecordService {

    @Override
    public IPage<AllianceRechargeRecordVO> queryPageList(Page<AllianceRechargeRecord> page, AllianceRechargeRecordDTO allianceRechargeRecordDTO) {
        return baseMapper.queryPageList(page,allianceRechargeRecordDTO);
    }
}
