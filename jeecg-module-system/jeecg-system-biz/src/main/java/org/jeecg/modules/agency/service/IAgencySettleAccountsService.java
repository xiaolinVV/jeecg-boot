package org.jeecg.modules.agency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.agency.dto.AgencySettleAccountsDTO;
import org.jeecg.modules.agency.entity.AgencySettleAccounts;
import org.jeecg.modules.agency.vo.AgencySettleAccountsVO;

/**
 * @Description: 代理提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-30
 * @Version: V1.0
 */
public interface IAgencySettleAccountsService extends IService<AgencySettleAccounts> {
    /**
     * 提现审核结算列表
     * @param page
     * @param agencySettleAccountsVO
     * @return
     */
    IPage<AgencySettleAccountsDTO> getAgencySettleAccountsList(Page<AgencySettleAccounts> page, AgencySettleAccountsVO agencySettleAccountsVO);

    Result<AgencySettleAccounts> audit(AgencySettleAccountsVO agencySettleAccountsVO);

    Result<AgencySettleAccounts> remit(AgencySettleAccountsVO agencySettleAccountsVO);
}
