package org.jeecg.modules.agency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.agency.dto.AgencyStatementAccountDTO;
import org.jeecg.modules.agency.entity.AgencyStatementAccount;
import org.jeecg.modules.agency.vo.AgencyStatementAccountVO;

/**
 * @Description: 代理对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-22
 * @Version: V1.0
 */
public interface IAgencyStatementAccountService extends IService<AgencyStatementAccount> {
    /**
     * 监听器调用
     * 生成每日的对账单数据
     */
     void addStoreStatementAccount();

    IPage<AgencyStatementAccountVO> queryPageList(Page<AgencyStatementAccount> page, AgencyStatementAccountDTO agencyStatementAccountDTO);
}
