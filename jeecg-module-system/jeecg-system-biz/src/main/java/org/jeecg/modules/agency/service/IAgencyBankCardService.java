package org.jeecg.modules.agency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.agency.dto.AgencyBankCardDTO;
import org.jeecg.modules.agency.entity.AgencyBankCard;
import org.jeecg.modules.agency.vo.AgencyBankCardVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 代理银行卡
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
public interface IAgencyBankCardService extends IService<AgencyBankCard> {

    Result<AgencyBankCard> add(AgencyBankCard agencyBankCard);

    Result<AgencyBankCard> edit(AgencyBankCard agencyBankCard);

    Result<AgencyBankCard> agencyBankCardAudit(AgencyBankCardVO agencyBankCardVO);

    Map<String,Object> findAgencyBankCard();

    List<AgencyBankCardDTO> findBankCardById(String sysUserId);

    IPage<AgencyBankCardVO> queryPageList(Page<AgencyBankCardVO> page, AgencyBankCardDTO agencyBankCardDTO);

}
