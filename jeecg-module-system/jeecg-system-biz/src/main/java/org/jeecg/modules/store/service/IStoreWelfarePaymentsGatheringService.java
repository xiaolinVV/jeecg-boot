package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.dto.StoreWelfarePaymentsGatheringDTO;
import org.jeecg.modules.store.entity.StoreWelfarePaymentsGathering;
import org.jeecg.modules.store.vo.StoreWelfarePaymentsGatheringVO;

/**
 * @Description: 福利金收款商家
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
public interface IStoreWelfarePaymentsGatheringService extends IService<StoreWelfarePaymentsGathering> {

    IPage<StoreWelfarePaymentsGatheringVO> queryPageList(Page<StoreWelfarePaymentsGathering> page, StoreWelfarePaymentsGatheringDTO storeWelfarePaymentsGatheringDTO);
}
