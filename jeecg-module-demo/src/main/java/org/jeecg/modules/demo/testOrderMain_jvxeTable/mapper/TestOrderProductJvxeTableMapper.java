package org.jeecg.modules.demo.testOrderMain_jvxeTable.mapper;

import java.util.List;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderProductJvxeTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
public interface TestOrderProductJvxeTableMapper extends BaseMapper<TestOrderProductJvxeTable> {

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
   * @return List<TestOrderProductJvxeTable>
   */
	public List<TestOrderProductJvxeTable> selectByMainId(@Param("mainId") String mainId);
}
