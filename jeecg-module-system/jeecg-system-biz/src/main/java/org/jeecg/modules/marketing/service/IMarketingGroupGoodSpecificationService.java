package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodSpecification;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-21
 * @Version: V1.0
 */
public interface IMarketingGroupGoodSpecificationService extends IService<MarketingGroupGoodSpecification> {



    /**
     * 根据免单商品id查询规格数据
     * 张靠勤   2021-3-30
     * @param marketingGroupGoodListId
     * @return
     */
    public List<Map<String,Object>> selectMarketingGroupGoodSpecificationByMarketingGroupGoodListId(String marketingGroupGoodListId);

}
