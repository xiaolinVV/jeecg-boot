package org.jeecg.modules.demo.testOrderMain_erp.service.impl;

import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderProductErp;
import org.jeecg.modules.demo.testOrderMain_erp.mapper.TestOrderProductErpMapper;
import org.jeecg.modules.demo.testOrderMain_erp.service.ITestOrderProductErpService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
@Service
public class TestOrderProductErpServiceImpl extends ServiceImpl<TestOrderProductErpMapper, TestOrderProductErp> implements ITestOrderProductErpService {
	
	@Autowired
	private TestOrderProductErpMapper testOrderProductErpMapper;
	
	@Override
	public List<TestOrderProductErp> selectByMainId(String mainId) {
		return testOrderProductErpMapper.selectByMainId(mainId);
	}
}
