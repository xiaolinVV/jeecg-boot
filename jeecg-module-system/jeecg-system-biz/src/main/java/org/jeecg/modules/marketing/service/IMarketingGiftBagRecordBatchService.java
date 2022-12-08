package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingGiftBagRecordBatchDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordBatchVO;

/**
 * @Description: 采购礼包记录
 * @Author: jeecg-boot
 * @Date:   2020-09-01
 * @Version: V1.0
 */
public interface IMarketingGiftBagRecordBatchService extends IService<MarketingGiftBagRecordBatch> {

    IPage<MarketingGiftBagRecordBatchVO> queryPageList(Page<MarketingGiftBagRecordBatchVO> page, MarketingGiftBagRecordBatchDTO marketingGiftBagRecordBatchDTO);
}
