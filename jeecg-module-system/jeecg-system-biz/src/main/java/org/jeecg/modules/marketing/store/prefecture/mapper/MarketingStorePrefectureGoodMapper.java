package org.jeecg.modules.marketing.store.prefecture.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.store.prefecture.entity.MarketingStorePrefectureGood;

import java.util.Map;

/**
 * @Description: 店铺专区商品
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface MarketingStorePrefectureGoodMapper extends BaseMapper<MarketingStorePrefectureGood> {

    /**
     * 后端列表查询
     *
     * @param page
     * @param wrapper
     * @param goodName
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page,@Param(Constants.WRAPPER) QueryWrapper wrapper,@Param("goodName") String goodName);


    /**
     * 专区商品列表
     *
     * @param page
     * @return
     */
    public IPage<Map<String,Object>> getMarketingStorePrefectureGoodList(Page<Map<String,Object>> page,@Param("marketingStorePrefectureGoodListId") String marketingStorePrefectureGoodListId);


}
