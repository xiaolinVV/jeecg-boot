package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordVO;

import java.util.Map;

/**
 * @Description: 礼包记录
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface IMarketingGiftBagRecordService extends IService<MarketingGiftBagRecord> {

    /**
     * 获取礼包列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> getMarketingGiftBagRecordList(Page<Map<String,Object>> page, Map<String,Object> paramMap);


    /**
     * 根据礼包id获取信息
     *
     * @param id
     * @return
     */
    Map<String,Object> findMarketingGiftBagRecordById( String id);

    IPage<MarketingGiftBagRecordVO> findGiftBagRecord(Page<MarketingGiftBagRecordVO> page, MarketingGiftBagRecordVO marketingGiftBagRecordVO);
}
