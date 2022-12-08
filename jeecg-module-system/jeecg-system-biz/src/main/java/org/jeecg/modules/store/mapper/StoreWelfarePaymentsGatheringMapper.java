package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StoreWelfarePaymentsGatheringDTO;
import org.jeecg.modules.store.entity.StoreWelfarePaymentsGathering;
import org.jeecg.modules.store.vo.StoreWelfarePaymentsGatheringVO;

/**
 * @Description: 福利金收款商家
 * @Author: jeecg-boot
 * @Date:   2020-06-18
 * @Version: V1.0
 */
public interface StoreWelfarePaymentsGatheringMapper extends BaseMapper<StoreWelfarePaymentsGathering> {

    IPage<StoreWelfarePaymentsGatheringVO> queryPageList(Page<StoreWelfarePaymentsGathering> page,@Param("storeWelfarePaymentsGatheringDTO") StoreWelfarePaymentsGatheringDTO storeWelfarePaymentsGatheringDTO);
}
