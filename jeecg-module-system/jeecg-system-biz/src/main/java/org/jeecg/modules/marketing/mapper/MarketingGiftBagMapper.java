package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.dto.MarketingGiftBagDTO;
import org.jeecg.modules.marketing.entity.MarketingGiftBag;
import org.jeecg.modules.marketing.vo.MarketingGiftBagVO;

import java.util.Map;

/**
 * @Description: 礼包管理
 * @Author: jeecg-boot
 * @Date:   2019-12-09
 * @Version: V1.0
 */
public interface MarketingGiftBagMapper extends BaseMapper<MarketingGiftBag> {


    /**
     * 获取礼包列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String, Object>> getMarketingGiftBagList(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);


    /**
     * 根据礼包id获取信息
     *
     * @param id
     * @return
     */
    Map<String,Object> findMarketingGiftBagById(@Param("id") String id);

    IPage<MarketingGiftBagVO> findMarketingGifiBagPageList(Page<MarketingGiftBagVO> page,@Param("marketingGiftBagVO") MarketingGiftBagVO marketingGiftBagVO);

    void deleteAndDelExplain(@Param("id") String id,@Param("delExplain") String delExplain);

    IPage<Map<String,Object>> isPrepositionList(Page<Map<String,Object>> page,@Param("marketingGiftBagDTO") MarketingGiftBagDTO marketingGiftBagDTO);
}
