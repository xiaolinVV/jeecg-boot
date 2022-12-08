package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodList;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
public interface IMarketingGroupGoodListService extends IService<MarketingGroupGoodList> {



    /**
     * 中奖拼团商品列表查询
     *
     * 张靠勤   2021-3-30
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingGroupGoodList(Page<Map<String,Object>> page, Map<String,String> paramMap);



    /**
     * 查询中奖拼团推荐商品列表
     * 张靠勤   2021-3-31
     * @return
     */
    public List<Map<String,Object>> selectMarketingGroupGoodListByIsRecommend();


    /**
     *
     * 根据拼团类型id查询拼团商品列表
     *
     * 张靠勤   2021-4-1
     *
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingGroupGoodListByMarketingGroupGoodTypeId(Page<Map<String,Object>> page,Map<String,Object> paramMap);



    /**
     * 模糊查询拼团商品列表
     *
     * 张靠勤   2021-4-1
     *
     * @param page
     * @param paramMap
     * @return
     */
    public IPage<Map<String,Object>> selectMarketingGroupGoodListBySearch(Page<Map<String,Object>> page,Map<String,Object> paramMap);

    /**
     * 停止超时商品
     *
     * 张靠勤  2021-4-8
     */
    public void stopMarketingGroupGoodList();


}
