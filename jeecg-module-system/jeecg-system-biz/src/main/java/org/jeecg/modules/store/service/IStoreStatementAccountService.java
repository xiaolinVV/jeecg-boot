package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.store.dto.StoreStatementAccountDTO;
import org.jeecg.modules.store.entity.StoreStatementAccount;
import org.jeecg.modules.store.vo.StoreStatementAccountVO;

/**
 * @Description: 店铺对账单
 * @Author: jeecg-boot
 * @Date:   2019-12-11
 * @Version: V1.0
 */
public interface IStoreStatementAccountService extends IService<StoreStatementAccount> {

    /**
     * 定时器添加对账单数据
     */
   void addStoreStatementAccount();

    IPage<StoreStatementAccountVO> queryPageList(Page<StoreStatementAccount> page, StoreStatementAccountDTO storeStatementAccountDTO);
}
