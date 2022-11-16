package org.jeecg.modules.demo.testOrderMain_many.mapper;

import java.util.List;
import org.jeecg.modules.demo.testOrderMain_many.entity.TestOrderProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface TestOrderProductMapper extends BaseMapper<TestOrderProduct> {

  /**
   * 通过主表id删除子表数据
   *
   * @param mainId 主表id
   * @return boolean
   */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<TestOrderProduct>
   */
	public List<TestOrderProduct> selectByMainId(@Param("mainId") String mainId);
}
