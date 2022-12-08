package org.jeecg.modules.provider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.provider.dto.ProviderAddressDTO;
import org.jeecg.modules.provider.entity.ProviderAddress;

import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商地址库
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface IProviderAddressService extends IService<ProviderAddress> {
    List<ProviderAddressDTO> getlistProviderAddress(Map<String,String> paramMap);

    boolean updateIsDeliverById(ProviderAddress providerAddress);


    boolean updateIsReturnById( ProviderAddress providerAddress);

}
