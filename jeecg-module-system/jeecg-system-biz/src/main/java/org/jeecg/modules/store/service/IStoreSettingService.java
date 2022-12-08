package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.entity.StoreSetting;

/**
 * @Description: 店铺设置
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface IStoreSettingService extends IService<StoreSetting> {

    Result<StoreSetting> add(StoreSetting storeSetting);

    Result<StoreSetting> edit(StoreSetting storeSetting);

    Result<StoreSetting> setStoreSetting(StoreSetting storeSetting);

    Result<StoreSetting> findStoreSetting();

}
