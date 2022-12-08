package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
public interface ProviderAddressMapper extends BaseMapper<ProviderAddress> {

 List<ProviderAddressDTO> getlistProviderAddress(@Param("paramMap")Map<String,String> paramMap);

    void updateIsDeliverById(String id);

    void updateIsReturnById(String id);
}
