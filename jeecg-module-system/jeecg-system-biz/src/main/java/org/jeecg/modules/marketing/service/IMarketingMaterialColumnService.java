package org.jeecg.modules.marketing.service;

import org.jeecg.modules.marketing.entity.MarketingMaterialColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 素材库栏目
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
public interface IMarketingMaterialColumnService extends IService<MarketingMaterialColumn> {
    /**
     * 查询素材库栏目名称
     * @return
     */
    List<Map<String,Object>> getMarketingMaterialColumnName();
}
