package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodList;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单商品
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface MarketingFreeGoodListMapper extends BaseMapper<MarketingFreeGoodList> {

    /**
     * 免单商品列表查询
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>>  selectMarketingFreeGoodList(Page<Map<String,Object>> page, @Param("paramMap") Map<String,String> paramMap);


    /**
     * 获取免单首页推荐商品
     *
     * @return
     */
    public List<Map<String,Object>> selectMarketingFreeGoodListByIsRecommend();

    /**
     * 根据免单活动类型id查询商品列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingFreeGoodListByMarketingFreeGoodTypeId(Page<Map<String,Object>> page,@Param("paramMap") Map<String,Object> paramMap);

    /**
     * 模糊查询商品列表
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingFreeGoodListBySearch(Page<Map<String,Object>> page, @Param("paramMap") Map<String,Object> paramMap);
}
