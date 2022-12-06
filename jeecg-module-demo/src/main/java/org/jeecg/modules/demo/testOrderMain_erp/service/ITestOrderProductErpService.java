package org.jeecg.modules.demo.testOrderMain_erp.service;

import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderProductErp;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
public interface ITestOrderProductErpService extends IService<TestOrderProductErp> {

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId
   * @return List<TestOrderProductErp>
   */
	public List<TestOrderProductErp> selectByMainId(String mainId);
}
