package org.jeecg.modules.demo.testOrderMain_many.service.impl;

import org.jeecg.modules.demo.testOrderMain_many.entity.TestOrderProduct;
import org.jeecg.modules.demo.testOrderMain_many.mapper.TestOrderProductMapper;
import org.jeecg.modules.demo.testOrderMain_many.service.ITestOrderProductService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 订单产品明细
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Service
public class TestOrderProductServiceImpl extends ServiceImpl<TestOrderProductMapper, TestOrderProduct> implements ITestOrderProductService {
	
	@Autowired
	private TestOrderProductMapper testOrderProductMapper;
	
	@Override
	public List<TestOrderProduct> selectByMainId(String mainId) {
		return testOrderProductMapper.selectByMainId(mainId);
	}
}
