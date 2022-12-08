package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingFreeGoodSpecification;

import java.util.List;
import java.util.Map;

/**
 * @Description: 免单商品规格
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public interface IMarketingFreeGoodSpecificationService extends IService<MarketingFreeGoodSpecification> {

    /**
     * 根据免单商品id查询规格数据
     *
     * @param marketingFreeGoodListId
     * @return
     */
    public List<Map<String,Object>> selectMarketingFreeGoodSpecificationByMarketingFreeGoodListId(String marketingFreeGoodListId);




    /**
     * 获取活动价格最低的规格数据
     *
     * @param marketingFreeGoodListId
     * @return
     */
    public Map<String,Object> selectMarketingFreeGoodSpecificationBySmallprice(String marketingFreeGoodListId);

}
