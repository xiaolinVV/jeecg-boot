package org.jeecg.modules.demo.testOrderMain_jvxeTable.service;

import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderProductJvxeTable;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderMainJvxeTable;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 测试订单主表,jvxe风格
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
public interface ITestOrderMainJvxeTableService extends IService<TestOrderMainJvxeTable> {

	/**
	 * 添加一对多
	 *
	 * @param testOrderMainJvxeTable
	 * @param testOrderProductJvxeTableList
	 */
	public void saveMain(TestOrderMainJvxeTable testOrderMainJvxeTable,List<TestOrderProductJvxeTable> testOrderProductJvxeTableList) ;
	
	/**
	 * 修改一对多
	 *
   * @param testOrderMainJvxeTable
   * @param testOrderProductJvxeTableList
	 */
	public void updateMain(TestOrderMainJvxeTable testOrderMainJvxeTable,List<TestOrderProductJvxeTable> testOrderProductJvxeTableList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
