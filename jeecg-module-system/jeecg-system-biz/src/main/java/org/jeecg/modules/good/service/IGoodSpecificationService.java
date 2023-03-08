package org.jeecg.modules.good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.good.dto.SpecificationsPicturesDTO;
import org.jeecg.modules.good.entity.GoodSpecification;
import org.jeecg.modules.good.vo.GoodListSpecificationVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2019-10-23
 * @Version: V1.0
 */
public interface IGoodSpecificationService extends IService<GoodSpecification> {
    List<GoodSpecification> getGoodSpecificationByGoodListId(String goodListId);


  /**
   * 查询价格最低的一条规格数据
   *
   * @param goodListId
   * @return
   */
  public GoodSpecification getSmallGoodSpecification(String goodListId);


  /**
   * 获取商品的总库存
   *
   * @param goodListId
   * @return
   */
  public BigDecimal getTotalRepertory(String goodListId);



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
   * @param goodListSpecificationVO
   */
  void updateGoodSpecificationOne(GoodListSpecificationVO goodListSpecificationVO);
  /**
   * 根据商品id查规格名, 规格图
   * @param goodListId
   * @return
   */
  List<SpecificationsPicturesDTO> selectByspecificationPicture(String goodListId);

  /**
   * 统计sku个数
   * @return
   */
  long  getGoodSkuCount();

    List<Map<String,Object>> getGoodSpecificationByGoodId(String id);

  /**
   * 选折商品列表
   * 1、免单活动新增商品选择商品接口获取商品规格信息
   *
   * @param goodListId
   * @return
   */
  public List<Map<String,Object>> chooseSpecificationByGoodId(String goodListId);
}
