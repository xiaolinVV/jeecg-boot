package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingGiftBagRecord;
import org.jeecg.modules.marketing.vo.MarketingGiftBagRecordVO;

import java.util.Map;

/**
 * @Description: 礼包记录
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface MarketingGiftBagRecordMapper extends BaseMapper<MarketingGiftBagRecord> {


    /**
     * 获取礼包列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> getMarketingGiftBagRecordList(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);


    /**
     * 根据礼包id获取信息
     *
     * @param id
     * @return
     */
    Map<String,Object> findMarketingGiftBagRecordById(@Param("id") String id);

    IPage<MarketingGiftBagRecordVO> findGiftBagRecord(Page<MarketingGiftBagRecordVO> page,@Param("marketingGiftBagRecordVO") MarketingGiftBagRecordVO marketingGiftBagRecordVO);
}
