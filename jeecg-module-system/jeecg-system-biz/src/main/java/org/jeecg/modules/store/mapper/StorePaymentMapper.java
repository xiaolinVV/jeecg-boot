package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.store.dto.StorePaymentDto;
import org.jeecg.modules.store.entity.StorePayment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.store.vo.StorePaymentVo;

/**
 * @Description: 店铺消费记录
 * @Author: jeecg-boot
 * @Date:   2019-10-15
 * @Version: V1.0
 */
public interface StorePaymentMapper extends BaseMapper<StorePayment> {
    IPage<StorePaymentDto> getStorePaymentVoList(Page<StorePaymentDto> page, @Param("storePaymentVo")StorePaymentVo storePaymentVo);
}
