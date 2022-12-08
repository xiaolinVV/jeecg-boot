package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingLeagueGoodListDTO;
import org.jeecg.modules.marketing.entity.MarketingLeagueGoodList;

import java.util.Map;

/**
 * @Description: 加盟专区-商品列表
 * @Author: jeecg-boot
 * @Date:   2021-12-23
 * @Version: V1.0
 */
public interface IMarketingLeagueGoodListService extends IService<MarketingLeagueGoodList> {


    /**
     * 查询加盟专区商品列表
     *
     * @param page
     * @param marketingLeagueGoodListDTO
     * @return
     */
    public IPage<Map<String,Object>> queryPageList(Page<Map<String,Object>> page, MarketingLeagueGoodListDTO marketingLeagueGoodListDTO);



    /*
     * 根据类型查询商品列表
     *
     * */
    public IPage<Map<String, Object>> getMarketingLeagueGoodListByTypeId(Page<Map<String,Object>> page,String typeId);


}
