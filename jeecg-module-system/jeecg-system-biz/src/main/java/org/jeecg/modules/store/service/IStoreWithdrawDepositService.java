package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.dto.StoreWithdrawDepositDTO;
import org.jeecg.modules.store.entity.StoreWithdrawDeposit;
import org.jeecg.modules.store.vo.StoreWithdrawDepositVO;

import java.util.Map;

/**
 * @Description: 店铺提现审批
 * @Author: jeecg-boot
 * @Date:   2019-12-23
 * @Version: V1.0
 */
public interface IStoreWithdrawDepositService extends IService<StoreWithdrawDeposit> {
    IPage<StoreWithdrawDepositDTO> getStoreWithdrawDeposit(Page<StoreWithdrawDeposit> page, StoreWithdrawDepositVO storeWithdrawDepositVO);

    Result<StoreWithdrawDeposit> audit(StoreWithdrawDepositVO storeWithdrawDepositVO);

    Result<StoreWithdrawDeposit> remit(StoreWithdrawDepositVO storeWithdrawDepositVO);
    /**
     * 商家端提现明细
     * @param page
     * @param storeWithdrawDepositVO
     * @return
     */
    IPage<Map<String,Object >> getStoreWithdrawDepositMap(Page<StoreWithdrawDeposit> page, StoreWithdrawDepositVO storeWithdrawDepositVO);

}
