package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.GoodStoreSpecification;
import org.jeecg.modules.good.vo.GoodStoreListSpecificationVO;

import java.util.List;

/**
 * @Description: 店铺商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
public interface IGoodStoreSpecificationService extends IService<GoodStoreSpecification> {
    List<GoodStoreSpecification> getGoodStoreSpecificationByGoodListId(String goodStoreListId);
    /**
     * 根据商品id删除规格
     * @param goodId
     * @param delFlag
     */
    void updateDelFlagByGoodId(String goodId, String delFlag);

    /**
     * 根据商品id查询规格
     * @param goodId
     * @return
     */
    List<String> selectByGoodId(String goodId);
    /**
     * 根据商品id查和规格删除不在范围内规格
     * @param goodId
     * @Param specification 规格
     * @return
     */
    void delpecification(String goodId,List<String>specification);

    /**
     * 传入Vo对象 修改规格
     * @param goodStoreListSpecificationVO
     */
    void updateGoodSpecificationOne(GoodStoreListSpecificationVO goodStoreListSpecificationVO);
    /**
     * 根据商品id查规格名, 规格图
     * @param goodStoreId
     * @return
     */
    List<SpecificationsPicturesDTO> selectByspecificationPicture(String goodStoreId);
    /**
     * 统计店铺sku个数
     * @return
     */
    long getGoodStoreSkuCount();
}
