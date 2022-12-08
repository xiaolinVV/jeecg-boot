package org.jeecg.modules.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.marketing.dto.MarketingMaterialListDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialList;
import org.jeecg.modules.marketing.vo.MarketingMaterialListVO;

import java.util.Map;

/**
 * @Description: 素材列表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
public interface IMarketingMaterialListService extends IService<MarketingMaterialList> {

    /**
     * 查询素材列表分页
     * @param page
     * @param marketingMaterialListVO
     * @return
     */
    IPage<MarketingMaterialListDTO> getMarketingMaterialListDTO(Page page, MarketingMaterialListVO marketingMaterialListVO);

    /**
     * 根据任何类型查询素材信息
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>>   findMarketingMaterial(Page<Map<String,Object>> page, Map<String,Object> paramMap);
    /**
     * 搜索框查询,排序素材列表
     * @param page
     * @param paramMap
     * @return
     */
    IPage<Map<String,Object>>  searchMarketingMaterial(Page<Map<String,Object>> page,Map<String,Object> paramMap);
    /**
     * 获取素材详情
     * @param id
     * @return
     */
    Map<String,Object>  getMarketingMaterialById(String id);
}
