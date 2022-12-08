package org.jeecg.modules.good.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.mapper.GoodStoreSpecificationMapper;
import org.jeecg.modules.good.service.IGoodStoreSpecificationService;
import org.jeecg.modules.good.vo.GoodStoreListSpecificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    @Autowired(required = false)
    private GoodStoreSpecificationMapper goodStoreSpecificationMapper;
    @Override
    public List<GoodStoreSpecification> getGoodStoreSpecificationByGoodListId(String goodStoreListId){
        return goodStoreSpecificationMapper.getGoodStoreSpecificationByGoodListId(goodStoreListId);
    }

    @Override
    public void updateDelFlagByGoodId(String goodId, String delFlag) {
        goodStoreSpecificationMapper.updateDelFlagByGoodId(goodId,delFlag);
    }

    @Override
    public List<String> selectByGoodId(String goodId) {
        return goodStoreSpecificationMapper.selectByGoodId(goodId);
    }

    @Override
    public void delpecification(String goodId, List<String> specification) {
        goodStoreSpecificationMapper.delpecification(goodId,specification);
    }

    @Override
    public void updateGoodSpecificationOne(GoodStoreListSpecificationVO goodListSpecificationVO) {
        goodStoreSpecificationMapper.updateGoodSpecificationOne(goodListSpecificationVO);
    };
    /**
     * 根据商品id查规格名, 规格图
     * @param goodStoreId
     * @return
     */
    public List<SpecificationsPicturesDTO> selectByspecificationPicture(String goodStoreId){
            return goodStoreSpecificationMapper.selectByspecificationPicture(goodStoreId);
    };
    /**
     * 统计店铺sku个数
     * @return
     */
    public long getGoodStoreSkuCount(){
        return goodStoreSpecificationMapper.getGoodStoreSkuCount();
    };
}
