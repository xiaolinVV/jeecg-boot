package org.jeecg.modules.demo.testOrderMain_jvxeTable.service.impl;

import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderMainJvxeTable;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.entity.TestOrderProductJvxeTable;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.mapper.TestOrderProductJvxeTableMapper;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.mapper.TestOrderMainJvxeTableMapper;
import org.jeecg.modules.demo.testOrderMain_jvxeTable.service.ITestOrderMainJvxeTableService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 测试订单主表,jvxe风格
 * @Author: jeecg-boot
 * @Date:   2022-11-17
 * @Version: V1.0
 */
@Service
public class TestOrderMainJvxeTableServiceImpl extends ServiceImpl<TestOrderMainJvxeTableMapper, TestOrderMainJvxeTable> implements ITestOrderMainJvxeTableService {

	@Autowired
	private TestOrderMainJvxeTableMapper testOrderMainJvxeTableMapper;
	@Autowired
	private TestOrderProductJvxeTableMapper testOrderProductJvxeTableMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(TestOrderMainJvxeTable testOrderMainJvxeTable, List<TestOrderProductJvxeTable> testOrderProductJvxeTableList) {
		testOrderMainJvxeTableMapper.insert(testOrderMainJvxeTable);
		if(testOrderProductJvxeTableList!=null && testOrderProductJvxeTableList.size()>0) {
			for(TestOrderProductJvxeTable entity:testOrderProductJvxeTableList) {
				//外键设置
				entity.setOrderFkId(testOrderMainJvxeTable.getId());
				testOrderProductJvxeTableMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(TestOrderMainJvxeTable testOrderMainJvxeTable,List<TestOrderProductJvxeTable> testOrderProductJvxeTableList) {
		testOrderMainJvxeTableMapper.updateById(testOrderMainJvxeTable);
		
		//1.先删除子表数据
		testOrderProductJvxeTableMapper.deleteByMainId(testOrderMainJvxeTable.getId());
		
		//2.子表数据重新插入
		if(testOrderProductJvxeTableList!=null && testOrderProductJvxeTableList.size()>0) {
			for(TestOrderProductJvxeTable entity:testOrderProductJvxeTableList) {
				//外键设置
				entity.setOrderFkId(testOrderMainJvxeTable.getId());
				testOrderProductJvxeTableMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		testOrderProductJvxeTableMapper.deleteByMainId(id);
		testOrderMainJvxeTableMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			testOrderProductJvxeTableMapper.deleteByMainId(id.toString());
			testOrderMainJvxeTableMapper.deleteById(id);
		}
	}
	
}
