package org.jeecg.modules.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.provider.dto.ProvideSettleAccountsDTO;
import org.jeecg.modules.provider.entity.ProvideSettleAccounts;
import org.jeecg.modules.provider.vo.ProvideSettleAccountsVO;

/**
 * @Description: 供应商待付款
 * @Author: jeecg-boot
 * @Date:   2019-12-23
 * @Version: V1.0
 */
public interface ProvideSettleAccountsMapper extends BaseMapper<ProvideSettleAccounts> {
    /**
     * 贷款结算列表
     * @param page
     * @param provideSettleAccountsVO
     * @return
     */
    IPage<ProvideSettleAccountsDTO> getProvideSettleAccountsList(Page<ProvideSettleAccounts> page, @Param("provideSettleAccountsVO") ProvideSettleAccountsVO provideSettleAccountsVO);


}
