package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.entity.MarketingGroupGoodType;

import java.util.List;
import java.util.Map;

/**
 * @Description: 拼团基础设置
 * @Author: jeecg-boot
 * @Date:   2021-03-20
 * @Version: V1.0
 */
public interface IMarketingGroupGoodTypeService extends IService<MarketingGroupGoodType> {


    /**
     *拼团活动商品类型列表查询
     *
     * 张靠勤   2021-3-29
     *
     * @return
     */
    public List<Map<String,Object>> getAllMarketingGroupGoodType();

}
