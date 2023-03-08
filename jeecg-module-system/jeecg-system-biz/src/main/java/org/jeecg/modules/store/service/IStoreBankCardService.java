package org.jeecg.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.store.dto.StoreBankCardDTO;
import org.jeecg.modules.store.entity.StoreBankCard;

import java.util.List;
import java.util.Map;

/**
 * @Description: 店铺银行卡
 * @Author: jeecg-boot
 * @Date:   2019-10-16
 * @Version: V1.0
 */
public interface IStoreBankCardService extends IService<StoreBankCard> {

    List<StoreBankCardDTO> findBankCardById(String id);

    Result<Map<String,Object>> findStoreBankCard();

    IPage<StoreBankCard> queryPageList(Page<StoreBankCard> page, StoreBankCardDTO storeBankCardDTO);

    /**
     * 获取相关类型的店铺银行卡信息
     *
     * @param storeManageId
     * @param carType
     * @return
     */
    public StoreBankCard getStoreBankCardByStoreManageId(String storeManageId,String carType);

    /**
     * 创建汇付天下分账信息
     *
     * @param storeBankCard
     * @return
     */
    public Result<?> createMemberPrivate(StoreBankCard storeBankCard);

    /**
     * 修改汇付天下分账信息
     *
     * @param storeBankCard
     * @return
     */
    public Result<?> updateSettleAccountPrivate(StoreBankCard storeBankCard);

}
