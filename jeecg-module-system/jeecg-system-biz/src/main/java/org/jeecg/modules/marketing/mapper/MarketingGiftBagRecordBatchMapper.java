package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingGiftBagRecordBatchDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecordBatch;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordBatchVO;

/**
 * @Description: 采购礼包记录
 * @Author: jeecg-boot
 * @Date:   2020-09-01
 * @Version: V1.0
 */
public interface MarketingGiftBagRecordBatchMapper extends BaseMapper<MarketingGiftBagRecordBatch> {

    IPage<MarketingGiftBagRecordBatchVO> queryPageList(Page<MarketingGiftBagRecordBatchVO> page,@Param("marketingGiftBagRecordBatchDTO") MarketingGiftBagRecordBatchDTO marketingGiftBagRecordBatchDTO);
}
