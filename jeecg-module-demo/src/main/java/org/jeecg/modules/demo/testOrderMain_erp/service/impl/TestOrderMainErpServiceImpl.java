package org.jeecg.modules.demo.testOrderMain_erp.service.impl;

import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderMainErp;
import org.jeecg.modules.demo.testOrderMain_erp.entity.TestOrderProductErp;
import org.jeecg.modules.demo.testOrderMain_erp.mapper.TestOrderProductErpMapper;
import org.jeecg.modules.demo.testOrderMain_erp.mapper.TestOrderMainErpMapper;
import org.jeecg.modules.demo.testOrderMain_erp.service.ITestOrderMainErpService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 测试订单主表-ERP
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
@Service
public class TestOrderMainErpServiceImpl extends ServiceImpl<TestOrderMainErpMapper, TestOrderMainErp> implements ITestOrderMainErpService {

	@Autowired
	private TestOrderMainErpMapper testOrderMainErpMapper;
	@Autowired
	private TestOrderProductErpMapper testOrderProductErpMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		testOrderProductErpMapper.deleteByMainId(id);
		testOrderMainErpMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			testOrderProductErpMapper.deleteByMainId(id.toString());
			testOrderMainErpMapper.deleteById(id);
		}
	}
	
}
