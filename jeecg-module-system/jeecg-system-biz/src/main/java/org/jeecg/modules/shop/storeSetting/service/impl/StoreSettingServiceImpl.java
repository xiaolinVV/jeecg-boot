package org.jeecg.modules.shop.storeSetting.service.impl;

import org.jeecg.modules.shop.storeSetting.entity.StoreSetting;
import org.jeecg.modules.shop.storeSetting.mapper.StoreSettingMapper;
import org.jeecg.modules.shop.storeSetting.service.IStoreSettingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



/**
 * @Description: 店铺设置
 * @Author: jeecg-boot
 * @Date:   2022-09-29
 * @Version: V1.0
 */
@Service("storeSettingService")
public class StoreSettingServiceImpl extends ServiceImpl<StoreSettingMapper, StoreSetting> implements IStoreSettingService  {

}
