package org.jeecg.modules.store.service;

import org.jeecg.modules.store.entity.StoreFranchiser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @Description: 店铺经销商
 * @Author: jeecg-boot
 * @Date:   2022-10-26
 * @Version: V1.0
 */
public interface IStoreFranchiserService extends IService<StoreFranchiser> {

    /**
     * 查询会员对应的经销商
     *
     * @param memberId
     * @return
     */
    public StoreFranchiser findStoreFranchiser(String memberId,String storeManageId);

    /**
     * 加入经销商
     *
     * @param storeFranchiser
     * @param memberId
     */
    public void joinStoreFranchiser(StoreFranchiser storeFranchiser,String memberId);


    /**
     * 经销商分配奖励
     *
     * @param storeFranchiser
     * @param award
     */
    public void awardStoreFranchiser(StoreFranchiser storeFranchiser, BigDecimal award,String buyMemberId);
}
