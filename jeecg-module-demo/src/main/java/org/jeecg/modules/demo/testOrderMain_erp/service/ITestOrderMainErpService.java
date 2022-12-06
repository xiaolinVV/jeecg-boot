package org.jeecg.modules.demo.testOrderMain_erp.service;

import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderProductErp;
import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderMainErp;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 测试订单主表-ERP
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
public interface ITestOrderMainErpService extends IService<TestOrderMainErp> {

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
