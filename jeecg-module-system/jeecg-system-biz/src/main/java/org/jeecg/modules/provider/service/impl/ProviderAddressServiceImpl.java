package org.jeecg.modules.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.provider.dto.ProviderAddressDTO;
import org.jeecg.modules.provider.entity.ProviderAddress;
import org.jeecg.modules.provider.mapper.ProviderAddressMapper;
import org.jeecg.modules.provider.service.IProviderAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商地址库
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
@Service
public class ProviderAddressServiceImpl extends ServiceImpl<ProviderAddressMapper, ProviderAddress> implements IProviderAddressService {

    @Autowired(required = false)
    private ProviderAddressMapper providerAddressMapper;
  public  List<ProviderAddressDTO> getlistProviderAddress(Map<String,String> paramMap){
        return providerAddressMapper.getlistProviderAddress(paramMap);
    };
    @Override
    public boolean updateIsDeliverById(ProviderAddress providerAddress) {
        QueryWrapper<ProviderAddress> queryWrapper = new QueryWrapper<>();
        List<ProviderAddress> addressList = providerAddressMapper.selectList(queryWrapper);
        for (ProviderAddress address : addressList) {
            if ("1".equals(address.getIsDeliver())){
                String id = address.getSysUserId();
                providerAddressMapper.updateIsDeliverById(id);
            }
        }
        return true;
    }

    @Override
    public boolean updateIsReturnById(ProviderAddress providerAddress ) {
        QueryWrapper<ProviderAddress> queryWrapper = new QueryWrapper<>();
        List<ProviderAddress> addressList = providerAddressMapper.selectList(queryWrapper);
        for (ProviderAddress address : addressList) {
            if ("1".equals(address.getIsReturn())){
                String id = address.getSysUserId();
                providerAddressMapper.updateIsReturnById(id);
            }
        }
        return true;
    }
}
