package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.store.dto.StoreWelfarePaymentsGatheringDTO;
import org.jeecg.modules.store.entity.StoreWelfarePaymentsGathering;
import org.jeecg.modules.store.mapper.StoreWelfarePaymentsGatheringMapper;
import org.jeecg.modules.store.service.IStoreWelfarePaymentsGatheringService;
import org.jeecg.modules.store.vo.StoreWelfarePaymentsGatheringVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 福利金收款商家
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
@Service
public class StoreWelfarePaymentsGatheringServiceImpl extends ServiceImpl<StoreWelfarePaymentsGatheringMapper, StoreWelfarePaymentsGathering> implements IStoreWelfarePaymentsGatheringService {

    @Override
    public IPage<StoreWelfarePaymentsGatheringVO> queryPageList(Page<StoreWelfarePaymentsGathering> page, StoreWelfarePaymentsGatheringDTO storeWelfarePaymentsGatheringDTO) {
        return baseMapper.queryPageList(page,storeWelfarePaymentsGatheringDTO);
    }
}
