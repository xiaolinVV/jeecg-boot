package org.jeecg.modules.demo.testOrderMain_jvxeTable.service;

import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderProductJvxeTable;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
public interface ITestOrderProductJvxeTableService extends IService<TestOrderProductJvxeTable> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<TestOrderProductJvxeTable>
	 */
	public List<TestOrderProductJvxeTable> selectByMainId(String mainId);
}
