package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.store.dto.StorePaymentDto;
import org.jeecg.modules.store.entity.StorePayment;
import org.jeecg.modules.store.mapper.StorePaymentMapper;
import org.jeecg.modules.store.service.IStorePaymentService;
import org.jeecg.modules.store.vo.StorePaymentVo;
import org.springframework.stereotype.Service;

/**
 * @Description: 店铺消费记录
 * @Author: jeecg-boot
 * @Date:   2019-10-15
 * @Version: V1.0
 */
@Service
public class StorePaymentServiceImpl extends ServiceImpl<StorePaymentMapper, StorePayment> implements IStorePaymentService {

    @Override
    public IPage<StorePaymentDto> getStorePaymentVoList(Page<StorePaymentDto> page, StorePaymentVo storePaymentVo) {
        IPage<StorePaymentDto> list = baseMapper.getStorePaymentVoList(page, storePaymentVo);
        return list;
    }
}
