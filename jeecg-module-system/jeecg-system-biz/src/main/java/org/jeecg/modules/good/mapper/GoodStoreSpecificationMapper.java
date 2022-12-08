package org.jeecg.modules.good.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
public interface GoodStoreSpecificationMapper extends BaseMapper<GoodStoreSpecification> {
    List<GoodStoreSpecification> getGoodStoreSpecificationByGoodListId(@Param("goodStoreListId")String goodStoreListId);
    /**
     * 根据商品id删除对应规格
     * @param goodId
     * @param delFlag
     */
    void updateDelFlagByGoodId(@Param("goodListId")String goodId, @Param("delFlag")String delFlag);

    /**
     * 根据商品id查规格
     * @param goodStoreListId
     * @return
     */
    List<String> selectByGoodId(@Param("goodStoreListId")String goodStoreListId);
    /**
     * 根据商品id查和规格删除不在范围内规格
     * @param goodId
     * @Param specification 规格
     * @return
     */
    void delpecification(@Param("goodStoreListId")String goodId,@Param("specifications")List<String>specifications);

    void updateGoodSpecificationOne(@Param("goodStoreListSpecificationVO") GoodStoreListSpecificationVO goodStoreListSpecificationVO);
    /**
     * 根据商品id查规格名, 规格图
     * @param goodStoreId
     * @return
     */
    List<SpecificationsPicturesDTO> selectByspecificationPicture(@Param("goodStoreId")String goodStoreId);

    /**
     * 统计店铺sku个数
     * @return
     */
   long getGoodStoreSkuCount();
}
