package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface StoreAddressMapper extends BaseMapper<StoreAddress> {

    boolean updateIsDeliverById (String id);

    boolean updateIsReturnById (String id);

    /**
     * 查询店铺发货退货地址list
     * @param paramMap
     * @return
     */
    List<StoreAddressDTO> getlistStoreAddress(@Param("paramMap") Map<String,String> paramMap);
}
