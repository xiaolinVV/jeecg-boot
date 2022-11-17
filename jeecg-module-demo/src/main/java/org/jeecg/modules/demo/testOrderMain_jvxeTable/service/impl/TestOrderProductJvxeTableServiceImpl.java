package org.jeecg.modules.demo.testOrderMain_jvxeTable.service.impl;

import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderProductJvxeTable;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.mapper.TestOrderProductJvxeTableMapper;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.service.ITestOrderProductJvxeTableService;
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
public class TestOrderProductJvxeTableServiceImpl extends ServiceImpl<TestOrderProductJvxeTableMapper, TestOrderProductJvxeTable> implements ITestOrderProductJvxeTableService {
	
	@Autowired
	private TestOrderProductJvxeTableMapper testOrderProductJvxeTableMapper;
	
	@Override
	public List<TestOrderProductJvxeTable> selectByMainId(String mainId) {
		return testOrderProductJvxeTableMapper.selectByMainId(mainId);
	}
}
