package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.store.entity.StoreCashierSetting;
import org.jeecg.modules.store.mapper.StoreCashierSettingMapper;
import org.jeecg.modules.store.service.IStoreCashierSettingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 店铺收银台设置
 * @Author: jeecg-boot
 * @Date:   2022-04-25
 * @Version: V1.0
 */
@Service
public class StoreCashierSettingServiceImpl extends ServiceImpl<StoreCashierSettingMapper, StoreCashierSetting> implements IStoreCashierSettingService {

    @Override
    public StoreCashierSetting getStoreCashierSetting(String storeManageId) {
        return this.getOne(new LambdaQueryWrapper<StoreCashierSetting>().eq(StoreCashierSetting::getIsCashier,"1").eq(StoreCashierSetting::getStoreManageId,storeManageId));
    }
}
