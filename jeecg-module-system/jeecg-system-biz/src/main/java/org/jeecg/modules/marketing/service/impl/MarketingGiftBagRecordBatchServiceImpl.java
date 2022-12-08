package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.marketing.dto.MarketingGiftBagRecordBatchDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.mapper.MarketingGiftBagRecordBatchMapper;
import org.jeecg.modules.marketing.service.IMarketingGiftBagRecordBatchService;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordBatchVO;
import org.springframework.stereotype.Service;

/**
 * @Description: 采购礼包记录
 * @Author: jeecg-boot
 * @Date:   2020-09-01
 * @Version: V1.0
 */
@Service
public class MarketingGiftBagRecordBatchServiceImpl extends ServiceImpl<MarketingGiftBagRecordBatchMapper, MarketingGiftBagRecordBatch> implements IMarketingGiftBagRecordBatchService {

    @Override
    public IPage<MarketingGiftBagRecordBatchVO> queryPageList(Page<MarketingGiftBagRecordBatchVO> page, MarketingGiftBagRecordBatchDTO marketingGiftBagRecordBatchDTO) {
        return baseMapper.queryPageList(page,marketingGiftBagRecordBatchDTO);
    }
}
