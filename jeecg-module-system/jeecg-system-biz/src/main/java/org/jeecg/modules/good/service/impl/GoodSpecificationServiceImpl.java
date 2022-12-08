package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.mapper.GoodSpecificationMapper;
import org.jeecg.modules.good.service.IGoodSpecificationService;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.good.vo.GoodListSpecificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-23
 * @Version: V1.0
 */
@Service
@Slf4j
public class GoodSpecificationServiceImpl extends ServiceImpl<GoodSpecificationMapper, GoodSpecification> implements IGoodSpecificationService {

    @Autowired
    private IGoodStoreSpecificationService iGoodStoreSpecificationService;

    @Override
    public List<GoodSpecification> getGoodSpecificationByGoodListId(String goodListId){
        return baseMapper.getGoodSpecificationByGoodListId(goodListId);
    }

    @Override
    public void updateDelFlagByGoodId(String goodId, String delFlag) {
        baseMapper.updateDelFlagByGoodId(goodId,delFlag);
    }

    @Override
    public List<String> selectByGoodId(String goodId) {
        return baseMapper.selectByGoodId(goodId);
    }

    @Override
    public void delpecification(String goodId, List<String> specifications) {
        baseMapper.delpecification(goodId,specifications);
    }
    @Override
    public void updateGoodSpecificationOne(GoodListSpecificationVO goodListSpecificationVO){
        baseMapper.updateGoodSpecificationOne(goodListSpecificationVO);
    };
    /**
     * 根据商品id查规格名, 规格图
     * @param goodListId
     * @return
     */
  public  List<SpecificationsPicturesDTO> selectByspecificationPicture(String goodListId){
      return baseMapper.selectByspecificationPicture(goodListId);
  };
    /**
     * 统计sku个数(店铺平台sku个数)
     * @return
     */
  public  long  getGoodSkuCount(){
      long GoodCount = baseMapper.getGoodSkuCount();
      long GoodStoreCount =  iGoodStoreSpecificationService.getGoodStoreSkuCount();
      GoodCount  = GoodCount+GoodStoreCount;
      return GoodCount;
  }

    @Override
    public List<Map<String, Object>> getGoodSpecificationByGoodId(String id) {
        return baseMapper.getGoodSpecificationByGoodId(id);
    }

    @Override
    public List<Map<String, Object>> chooseSpecificationByGoodId(String goodListId) {
        return baseMapper.chooseSpecificationByGoodId(goodListId);
    }

    ;
}
