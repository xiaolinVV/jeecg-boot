package org.jeecg.modules.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.good.service.IGoodListService;
import org.jeecg.modules.marketing.dto.MarketingMaterialGoodDTO;
import org.jeecg.modules.marketing.dto.MarketingMaterialListDTO;
import org.jeecg.modules.marketing.entity.MarketingMaterialList;
import org.jeecg.modules.marketing.mapper.MarketingMaterialListMapper;
import org.jeecg.modules.marketing.service.IMarketingMaterialGoodService;
import org.jeecg.modules.marketing.service.IMarketingMaterialListService;
import org.jeecg.modules.marketing.vo.MarketingMaterialListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 素材列表
 * @Author: jeecg-boot
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Service
public class MarketingMaterialListServiceImpl extends ServiceImpl<MarketingMaterialListMapper, MarketingMaterialList> implements IMarketingMaterialListService {

    @Autowired
    private IGoodListService goodListService;
    @Autowired
    private IMarketingMaterialGoodService marketingMaterialGoodService;
    @Override
   public IPage<MarketingMaterialListDTO> getMarketingMaterialListDTO(Page page, MarketingMaterialListVO marketingMaterialListVO){
       IPage<MarketingMaterialListDTO> pageList  = baseMapper.getMarketingMaterialListDTO(page,marketingMaterialListVO);
       pageList.getRecords().forEach(pl->{
           List<MarketingMaterialGoodDTO> marketingMaterialGoodDTOList =  marketingMaterialGoodService.getMarketingMaterialGoodDTO(pl.getId());
           //添加素材商品数据
           pl.setMarketingMaterialGoodDTOList(marketingMaterialGoodDTOList);
       });
       return pageList;
   };


    /**
     * 根据任何类型查询素材信息
     * @param page
     * @param paramMap
     * @return
     */
    @Override
 public  IPage<Map<String,Object>>   findMarketingMaterial(Page<Map<String,Object>> page, Map<String,Object> paramMap){
     return   baseMapper.findMarketingMaterial(page,paramMap);
 };


    /**
     * 搜索框查询,排序素材列表
     * @param page
     * @param paramMap
     * @return
     */
    @Override
  public  IPage<Map<String,Object>>  searchMarketingMaterial(Page<Map<String,Object>> page,Map<String,Object> paramMap){
      return  baseMapper.searchMarketingMaterial(page,paramMap);
  };

    /**
     * 获取素材详情
     * @param id
     * @return
     */
    @Override
   public Map<String,Object>  getMarketingMaterialById(String id){
      return baseMapper.getMarketingMaterialById(id);



   }

}
