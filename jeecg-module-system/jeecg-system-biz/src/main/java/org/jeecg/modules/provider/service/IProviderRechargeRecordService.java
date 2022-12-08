package org.jeecg.modules.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.provider.dto.ProviderRechargeRecordDTO;
import org.jeecg.modules.provider.entity.ProviderRechargeRecord;
import org.jeecg.modules.provider.vo.ProviderRechargeRecordVO;

/**
 * @Description: 供应商余额明细
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
public interface IProviderRechargeRecordService extends IService<ProviderRechargeRecord> {
    IPage<ProviderRechargeRecordDTO> getProviderRechargeRecord(Page<ProviderRechargeRecord> page, ProviderRechargeRecordVO providerRechargeRecordVO);

    /**
     * 系统自动提现调用
     */
    public void addStoreRechargeRecord();

    IPage<ProviderRechargeRecordVO> queryPageList(Page<ProviderRechargeRecord> page, ProviderRechargeRecordDTO providerRechargeRecordDTO);

}