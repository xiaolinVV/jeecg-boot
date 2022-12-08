package org.jeecg.modules.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.provider.dto.ProviderBankCardDTO;
import org.jeecg.modules.provider.entity.ProviderBankCard;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.provider.vo.ProviderBankCardVO;

import java.util.Map;

/**
 * @Description: 供应商银行卡
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
public interface IProviderBankCardService extends IService<ProviderBankCard> {

    Result<ProviderBankCard> updateOrSaveBankCard(ProviderBankCardVO providerBankCardVO);

    Result<ProviderBankCard> add(ProviderBankCard providerBankCard);

    Result<ProviderBankCard> edit(ProviderBankCard providerBankCard);

    Map<String,Object> returnBankCard();

    IPage<ProviderBankCardVO> queryPageList(Page<ProviderBankCardVO> page, ProviderBankCardDTO providerBankCardDTO);

}
