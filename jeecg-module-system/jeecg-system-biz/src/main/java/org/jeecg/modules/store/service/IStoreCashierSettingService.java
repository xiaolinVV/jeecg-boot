package org.jeecg.modules.store.service;

import org.jeecg.modules.store.entity.StoreCashierSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 店铺收银台设置
 * @Author: jeecg-boot
 * @Date:   2022-04-25
 * @Version: V1.0
 */
public interface IStoreCashierSettingService extends IService<StoreCashierSetting> {

    /**
     * 获取收银台设置信息
     * @return
     */
    public StoreCashierSetting getStoreCashierSetting(String storeManageId);

}
