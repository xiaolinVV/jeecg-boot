package org.jeecg.modules.demo.testOrderMain_innerTable.service;

import org.jeecg.modules.demo.testOrderMain_innerTable.entity.TestOrderProductInnerTable;
import org.jeecg.modules.demo.testOrderMain_innerTable.entity.TestOrderMainInnerTable;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 测试订单主表-内嵌子表风格
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface ITestOrderMainInnerTableService extends IService<TestOrderMainInnerTable> {

	/**
	 * 添加一对多
	 *
	 * @param testOrderMainInnerTable
	 * @param testOrderProductInnerTableList
	 */
	public void saveMain(TestOrderMainInnerTable testOrderMainInnerTable,List<TestOrderProductInnerTable> testOrderProductInnerTableList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param testOrderMainInnerTable
	 * @param testOrderProductInnerTableList
	 */
	public void updateMain(TestOrderMainInnerTable testOrderMainInnerTable,List<TestOrderProductInnerTable> testOrderProductInnerTableList);
	
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
