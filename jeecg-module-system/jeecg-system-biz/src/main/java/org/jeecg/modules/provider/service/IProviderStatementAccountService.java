package org.jeecg.modules.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.provider.dto.ProviderStatementAccountDTO;
import org.jeecg.modules.provider.entity.ProviderStatementAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.provider.vo.ProviderStatementAccountVO;

/**
 * @Description: 供应商对账单
 * @Author: jeecg-boot
 * @Date:   2020-01-04
 * @Version: V1.0
 */
public interface IProviderStatementAccountService extends IService<ProviderStatementAccount> {
    /**
     * 监听器调用
     * 生成每日的对账单数据
     */
    void addStoreStatementAccount();

    IPage<ProviderStatementAccountVO> queryPageList(Page<ProviderStatementAccount> page, ProviderStatementAccountDTO providerStatementAccountDTO);
}
