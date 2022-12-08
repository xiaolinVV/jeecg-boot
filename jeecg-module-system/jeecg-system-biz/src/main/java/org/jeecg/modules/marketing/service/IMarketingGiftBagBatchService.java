package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingGiftBagBatchDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagBatch;
import org.jeecg.modules.marketing.vo.MarketingGiftBagBatchVO;

import java.util.Map;

/**
 * @Description: 采购礼包
 * @Author: jeecg-boot
 * @Date:   2020-08-29
 * @Version: V1.0
 */
public interface IMarketingGiftBagBatchService extends IService<MarketingGiftBagBatch> {

    IPage<MarketingGiftBagBatchVO> queryPageList(Page<MarketingGiftBagBatchVO> page, MarketingGiftBagBatchDTO marketingGiftBagBatchDTO);

    IPage<Map<String,Object>> findMarketingGiftBagBatch(Page<Map<String,Object>> page, MarketingGiftBagBatchDTO marketingGiftBagBatchDTO);

    void paySuccess(String memberId, String id);
}
