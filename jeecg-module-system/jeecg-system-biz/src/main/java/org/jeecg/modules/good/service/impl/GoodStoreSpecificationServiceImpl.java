package org.jeecg.modules.good.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.mapper.GoodStoreSpecificationMapper;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.good.vo.GoodStoreListSpecificationVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 店铺商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
@Service
@Slf4j
public class GoodStoreSpecificationServiceImpl extends ServiceImpl<GoodStoreSpecificationMapper, GoodStoreSpecification> implements IGoodStoreSpecificationService {

    @Override
    public GoodStoreSpecification getSmallGoodSpecification(String goodListId) {
        return this.getOne(new LambdaQueryWrapper<GoodStoreSpecification>().eq(GoodStoreSpecification::getGoodStoreListId,goodListId).orderByAsc(GoodStoreSpecification::getPrice).last("limit 1"));
    }

    @Override
    public BigDecimal getTotalRepertory(String goodListId) {
        return new BigDecimal(String.valueOf(this.getMap(new QueryWrapper<GoodStoreSpecification>().select("sum(repertory) as repertory").lambda().eq(GoodStoreSpecification::getGoodStoreListId,goodListId)).get("repertory")));
    }

    @Override
    public List<GoodStoreSpecification> getGoodStoreSpecificationByGoodListId(String goodStoreListId){
        return baseMapper.getGoodStoreSpecificationByGoodListId(goodStoreListId);
    }

    @Override
    public List<String> selectByGoodId(String goodId) {
        return baseMapper.selectByGoodId(goodId);
    }

    @Override
    public void delpecification(String goodId, List<String> specification) {
        baseMapper.delpecification(goodId,specification);
    }

    @Override
    public void updateGoodSpecificationOne(GoodStoreListSpecificationVO goodListSpecificationVO) {
        baseMapper.updateGoodSpecificationOne(goodListSpecificationVO);
    };
    /**
     * 根据商品id查规格名, 规格图
     * @param goodStoreId
     * @return
     */
    public List<SpecificationsPicturesDTO> selectByspecificationPicture(String goodStoreId){
            return baseMapper.selectByspecificationPicture(goodStoreId);
    };
    /**
     * 统计店铺sku个数
     * @return
     */
    public long getGoodStoreSkuCount(){
        return baseMapper.getGoodStoreSkuCount();
    };
}
