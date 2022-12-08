package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingGiftBagStoreBatch;
import org.jeecg.modules.store.entity.StoreManage;

/**
 * @Description: 礼包采购店铺映射
 * @Author: jeecg-boot
 * @Date:   2020-08-29
 * @Version: V1.0
 */
public interface MarketingGiftBagStoreBatchMapper extends BaseMapper<MarketingGiftBagStoreBatch> {

    IPage<StoreManage> findGiftBagStoreById(Page<StoreManage> page,@Param("id") String id);
}
