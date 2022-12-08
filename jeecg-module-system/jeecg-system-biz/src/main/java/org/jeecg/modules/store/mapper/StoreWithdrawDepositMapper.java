package org.jeecg.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface StoreWithdrawDepositMapper extends BaseMapper<StoreWithdrawDeposit> {
    IPage<StoreWithdrawDepositDTO> getStoreWithdrawDeposit(Page<StoreWithdrawDeposit> page, @Param("storeWithdrawDepositVO") StoreWithdrawDepositVO storeWithdrawDepositVO);

    /**
     * 商家端提现明细
     * @param page
     * @param storeWithdrawDepositVO
     * @return
     */
    IPage<Map<String,Object >> getStoreWithdrawDepositMap(Page<StoreWithdrawDeposit> page, @Param("storeWithdrawDepositVO") StoreWithdrawDepositVO storeWithdrawDepositVO);

}
