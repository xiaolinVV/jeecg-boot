package org.jeecg.modules.agency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.agency.dto.AgencySettleAccountsDTO;
import org.jeecg.modules.agency.entity.AgencySettleAccounts;
import org.jeecg.modules.agency.vo.AgencySettleAccountsVO;

/**
 * @Description: 代理提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-30
 * @Version: V1.0
 */
public interface AgencySettleAccountsMapper extends BaseMapper<AgencySettleAccounts> {

    /**
     * 贷款结算列表
     * @param page
     * @param agencySettleAccountsVO
     * @return
     */
    IPage<AgencySettleAccountsDTO> getAgencySettleAccountsList(Page<AgencySettleAccounts> page,@Param("agencySettleAccountsVO") AgencySettleAccountsVO agencySettleAccountsVO);

}
