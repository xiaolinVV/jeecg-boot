package org.jeecg.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.store.dto.StoreAddressDTO;
import org.jeecg.modules.store.entity.StoreAddress;
import org.jeecg.modules.store.mapper.StoreAddressMapper;
import org.jeecg.modules.store.service.IStoreAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 地址库
 * @Author: jeecg-boot
 * @Date:   2019-10-21
 * @Version: V1.0
 */
@Service
public class StoreAddressServiceImpl extends ServiceImpl<StoreAddressMapper, StoreAddress> implements IStoreAddressService {
    @Autowired(required = false)
    private StoreAddressMapper storeAddressMapper;
    @Override
    public boolean updateIsDeliverById(StoreAddress storeAddress) {
        QueryWrapper<StoreAddress> queryWrapper = new QueryWrapper<>();
        List<StoreAddress> addressList = storeAddressMapper.selectList(queryWrapper);
        for (StoreAddress address : addressList) {
            if ("1".equals(address.getIsDeliver())){
                String id = address.getStoreManageId();
                storeAddressMapper.updateIsDeliverById(id);
            }
        }
        return true;
    }

    @Override
    public boolean updateIsReturnById(StoreAddress storeAddress) {
        QueryWrapper<StoreAddress> queryWrapper = new QueryWrapper<>();
        List<StoreAddress> addressList = storeAddressMapper.selectList(queryWrapper);
        for (StoreAddress address : addressList) {
            if ("1".equals(address.getIsReturn())){
                String id = address.getStoreManageId();
                storeAddressMapper.updateIsReturnById(id);
            }
        }
        return true;
    }
    @Override
    public List<StoreAddressDTO> getlistStoreAddress(Map<String,String> paramMap){
        return storeAddressMapper.getlistStoreAddress(paramMap);
    };
}
