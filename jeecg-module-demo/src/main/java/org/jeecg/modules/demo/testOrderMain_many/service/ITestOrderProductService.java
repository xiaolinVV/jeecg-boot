package org.jeecg.modules.demo.testOrderMain_many.service;

import org.jeecg.modules.demo.testOrderMain_many.entity.TestOrderProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface ITestOrderProductService extends IService<TestOrderProduct> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<TestOrderProduct>
	 */
	public List<TestOrderProduct> selectByMainId(String mainId);
}
