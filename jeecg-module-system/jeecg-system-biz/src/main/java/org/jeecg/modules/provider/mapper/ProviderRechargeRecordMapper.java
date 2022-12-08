package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.modules.provider.dto.ProviderRechargeRecordDTO;
import org.jeecg.modules.provider.entity.ProviderRechargeRecord;
import org.jeecg.modules.provider.vo.ProviderRechargeRecordVO;

/**
 * @Description: 供应商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
public interface ProviderRechargeRecordMapper extends BaseMapper<ProviderRechargeRecord> {
    IPage<ProviderRechargeRecordDTO> getProviderRechargeRecord(Page<ProviderRechargeRecord> page, @Param("providerRechargeRecordVO") ProviderRechargeRecordVO providerRechargeRecordVO);

    IPage<ProviderRechargeRecordVO> queryPageList(Page<ProviderRechargeRecord> page,@Param("providerRechargeRecordDTO") ProviderRechargeRecordDTO providerRechargeRecordDTO);
}
