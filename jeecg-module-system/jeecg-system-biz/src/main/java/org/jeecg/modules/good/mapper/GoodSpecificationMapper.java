package org.jeecg.modules.good.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.vo.GoodListSpecificationVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-23
 * @Version: V1.0
 */
public interface GoodSpecificationMapper extends BaseMapper<GoodSpecification> {
    List<GoodSpecification> getGoodSpecificationByGoodListId(@Param("goodListId")String goodListId);

  /**
   * 根据商品id删除对应规格
   * @param goodId
   * @param delFlag
   */
    void updateDelFlagByGoodId(@Param("goodListId")String goodId, @Param("delFlag")String delFlag);

  /**
   * 根据商品id查规格
   * @param goodId
   * @return
   */
  List<String> selectByGoodId(@Param("goodListId")String goodId);
  /**
   * 根据商品id查和规格删除不在范围内规格
   * @param goodId
   * @Param specification 规格
   * @return
   */
  void delpecification(@Param("goodListId")String goodId,@Param("specifications")List<String>specifications);

  void updateGoodSpecificationOne(@Param("goodListSpecificationVO")GoodListSpecificationVO goodListSpecificationVO);
  /**
   * 根据商品id查规格名, 规格图
   * @param goodId
   * @return
   */
  List<SpecificationsPicturesDTO> selectByspecificationPicture(@Param("goodListId")String goodId);

  /**
   * 统计sku个数
   * @return
   */
  long  getGoodSkuCount();

    List<Map<String,Object>> getGoodSpecificationByGoodId(@Param("id") String id);


  /**
   * 选折商品列表
   * 1、免单活动新增商品选择商品接口获取商品规格信息
   *
   * @param goodListId
   * @return
   */
    public List<Map<String,Object>> chooseSpecificationByGoodId(@Param("goodListId") String goodListId);
}
