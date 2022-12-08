package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingBusinessGiftList;

import java.util.Map;

/**
 * @Description: 创业礼包列表
 * @Author: jeecg-boot
 * @Date:   2021-07-30
 * @Version: V1.0
 */
public interface MarketingBusinessGiftListMapper extends BaseMapper<MarketingBusinessGiftList> {

    IPage<Map<String,Object>> findMarketingBusinessGiftList(Page<Map<String,Object>> page);

    IPage<Map<String,Object>> getIsMarketingBusinessGiftList(Page<Map<String,Object>> page,@Param("memberId") String memberId);
}
