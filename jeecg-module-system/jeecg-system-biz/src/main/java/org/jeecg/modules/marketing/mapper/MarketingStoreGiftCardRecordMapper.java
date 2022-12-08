package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingStoreGiftCardRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 店铺礼品卡使用记录
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
public interface MarketingStoreGiftCardRecordMapper extends BaseMapper<MarketingStoreGiftCardRecord> {

    IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, @Param(Constants.WRAPPER) QueryWrapper<MarketingStoreGiftCardRecord> queryWrapper,@Param("requestMap") Map<String, Object> requestMap);

    IPage<Map<String,Object>> findMarketingStoreGiftCardRecordList(Page<Map<String,Object>> page,@Param("map") HashMap<String, Object> map);

    Map<String,Object> findMarketingStoreGiftCardRecordParticulars(@Param("id") String id);
}
