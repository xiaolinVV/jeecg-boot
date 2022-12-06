package org.jeecg.modules.demo.testOrderMain_innerTable.service.impl;

import org.jeecg.modules.demo.testOrderMain_innerTable.entity.TestOrderProductInnerTable;
import org.jeecg.modules.demo.testOrderMain_innerTable.mapper.TestOrderProductInnerTableMapper;
import org.jeecg.modules.demo.testOrderMain_innerTable.service.ITestOrderProductInnerTableService;
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
public class TestOrderProductInnerTableServiceImpl extends ServiceImpl<TestOrderProductInnerTableMapper, TestOrderProductInnerTable> implements ITestOrderProductInnerTableService {
	
	@Autowired
	private TestOrderProductInnerTableMapper testOrderProductInnerTableMapper;
	
	@Override
	public List<TestOrderProductInnerTable> selectByMainId(String mainId) {
		return testOrderProductInnerTableMapper.selectByMainId(mainId);
	}
}
