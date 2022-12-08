package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;

import java.util.List;
import java.util.Map;

/**
 * @Description: 平台专区
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
public interface MarketingPrefectureMapper extends BaseMapper<MarketingPrefecture> {
    /**
     * 返回 平台专区 所有 启用 Id 名称 logo
      * @param paramMap
     * @return
     */
    List<Map<String,Object>> getMarketingPrefectureIdName(@Param("paramMap")Map<String,Object> paramMap);

    /**
     * 专区首页数据
     *
     * @return
     */
    List<Map<String,Object>> findPrefectureIndex(@Param("softModel") String softModel);

    /**
     * 过滤专区商品id
     * @param paramMap
     * @return
     */
    List<Map<String,Object>>  getFiltrationGoodId(@Param("paramMap")  Map<String,Object> paramMap);

    List<Map<String,Object>> getFiltrationGoodIds(@Param("paramMap") Map<String, Object> paramMap);

    List<Map<String,Object>> getMarketingPrefectureByRecommend();

}
