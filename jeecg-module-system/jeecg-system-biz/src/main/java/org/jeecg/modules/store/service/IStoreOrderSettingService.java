package org.jeecg.modules.store.service;

import org.jeecg.modules.order.entity.OrderStoreList;
import org.jeecg.modules.store.entity.StoreOrderSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 店铺订单设置
 * @Author: jeecg-boot
 * @Date:   2022-08-23
 * @Version: V1.0
 */
public interface IStoreOrderSettingService extends IService<StoreOrderSetting> {


    public StoreOrderSetting getStoreOrderSetting(String storeManageId);


    /**
     * 送积分
     *
     * @param orderStoreList
     */
    public void success(OrderStoreList orderStoreList);

}
