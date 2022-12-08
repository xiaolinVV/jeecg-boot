package org.jeecg.modules.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.marketing.entity.MarketingMaterialBrowse;
import org.jeecg.modules.marketing.vo.MarketingMaterialBrowseVO;

import java.util.Map;

/**
 * @Description: 素材浏览记录
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
public interface MarketingMaterialBrowseMapper extends BaseMapper<MarketingMaterialBrowse> {


    /**
     * 查询素材浏览记录分页数据
     * @param page
     * @param marketingMaterialBrowseVO
     * @return
     */
    IPage<Map<String,Object>> getMarketingMaterialBrowseMap(Page<Map<String,Object>> page, @Param("marketingMaterialBrowseVO") MarketingMaterialBrowseVO marketingMaterialBrowseVO);

}
