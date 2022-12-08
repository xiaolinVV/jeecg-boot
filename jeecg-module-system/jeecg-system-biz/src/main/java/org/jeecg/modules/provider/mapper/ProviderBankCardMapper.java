package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.provider.dto.ProviderBankCardDTO;
import org.jeecg.modules.provider.entity.ProviderBankCard;
import org.jeecg.modules.provider.vo.ProviderBankCardVO;

/**
 * @Description: 供应商银行卡
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
public interface ProviderBankCardMapper extends BaseMapper<ProviderBankCard> {

    IPage<ProviderBankCardVO> queryPageList(Page<ProviderBankCardVO> page,@Param("providerBankCardDTO") ProviderBankCardDTO providerBankCardDTO);
}
