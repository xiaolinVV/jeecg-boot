package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.dto.StorePaymentDto;
import org.jeecg.modules.store.entity.StorePayment;
import org.jeecg.modules.store.vo.StorePaymentVo;

/**
 * @Description: 店铺消费记录
 * @Author: jeecg-boot
 * @Date:   2019-10-15
 * @Version: V1.0
 */
public interface IStorePaymentService extends IService<StorePayment> {
    IPage<StorePaymentDto>getStorePaymentVoList(Page<StorePaymentDto>page, StorePaymentVo storePaymentVo);
}
