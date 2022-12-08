package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.modules.store.dto.StoreAddressDTO;
import org.jeecg.modules.store.entity.StoreAddress;

import java.util.List;
import java.util.Map;

/**
 * @Description: 地址库
 * @Author: jeecg-boot
 * @Date:   2019-10-21
 * @Version: V1.0
 */
public interface IStoreAddressService extends IService<StoreAddress> {


    boolean updateIsDeliverById(StoreAddress storeAddress);


    boolean updateIsReturnById(StoreAddress storeAddress);
    /**
     * 查询店铺发货退货地址list
     * @param paramMap
     * @return
     */
    List<StoreAddressDTO> getlistStoreAddress(Map<String,String> paramMap);
}
