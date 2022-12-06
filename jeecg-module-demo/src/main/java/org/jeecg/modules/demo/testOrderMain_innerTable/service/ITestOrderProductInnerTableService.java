package org.jeecg.modules.demo.testOrderMain_innerTable.service;

import org.jeecg.modules.demo.testOrderMain_innerTable.entity.TestOrderProductInnerTable;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface ITestOrderProductInnerTableService extends IService<TestOrderProductInnerTable> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<TestOrderProductInnerTable>
	 */
	public List<TestOrderProductInnerTable> selectByMainId(String mainId);
}
