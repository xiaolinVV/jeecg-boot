package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingPrefecture;

import java.util.List;
import java.util.Map;

/**
 * @Description: 平台专区
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
public interface IMarketingPrefectureService extends IService<MarketingPrefecture> {
    /**
     * 返回 平台专区 所有 启用 Id 名称 logo
     * @param paramMap
     * @return
     */
    List<Map<String,Object>> getMarketingPrefectureIdName(Map<String,Object> paramMap);

    /**
     * 专区首页数据
     *
     * @return
     */
    List<Map<String,Object>> findPrefectureIndex(String softModel);

    /**
     * 过期时间停用专区
     */
    public void   getMarketingPrefectureOvertime();

    List<Map<String,Object>> getFiltrationGoodIds(Map<String, Object> paramMap);

    List<Map<String,Object>> getMarketingPrefectureByRecommend();

}
