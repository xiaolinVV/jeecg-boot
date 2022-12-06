package org.jeecg.modules.demo.testOrderMain_many.service;

import org.jeecg.modules.demo.testOrderMain_many.entity.TestOrderProduct;
import org.jeecg.modules.demo.testOrderMain_many.entity.TestOrderMain;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 测试订单主表，一对多经典风格
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface ITestOrderMainService extends IService<TestOrderMain> {

	/**
	 * 添加一对多
	 *
	 * @param testOrderMain
	 * @param testOrderProductList
	 */
	public void saveMain(TestOrderMain testOrderMain,List<TestOrderProduct> testOrderProductList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param testOrderMain
	 * @param testOrderProductList
	 */
	public void updateMain(TestOrderMain testOrderMain,List<TestOrderProduct> testOrderProductList);
	
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
