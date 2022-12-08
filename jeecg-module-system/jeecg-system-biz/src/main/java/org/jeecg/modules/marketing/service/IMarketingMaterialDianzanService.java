package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingMaterialDianzan;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.vo.MarketingMaterialDianzanVO;

import java.util.Map;

/**
 * @Description: 素材点赞
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
public interface IMarketingMaterialDianzanService extends IService<MarketingMaterialDianzan> {

    /**
     * 查询点赞分页数据
     * @param page
     * @param marketingMaterialDianzanVO
     * @return
     */
    IPage<Map<String,Object>> getMarketingMaterialDianzanMap(Page<Map<String,Object>> page, @Param("marketingMaterialDianzanVO") MarketingMaterialDianzanVO marketingMaterialDianzanVO);


}
